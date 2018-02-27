package pt.ulusofona.copelabs.oi_ndn.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import pt.ulusofona.copelabs.oi_ndn.adapters.MessageListAdapter;
import pt.ulusofona.copelabs.oi_ndn.helpers.DataManager;
import pt.ulusofona.copelabs.oi_ndn.helpers.DataManagerListenerManager;
import pt.ulusofona.copelabs.oi_ndn.helpers.OiDataBaseManager;
import pt.ulusofona.copelabs.oi_ndn.models.Contact;
import pt.ulusofona.copelabs.oi_ndn.models.Conversation;
import pt.ulusofona.copelabs.oi_ndn.models.Message;

import java.util.ArrayList;

public class ConversationActivity extends AppCompatActivity implements DataManager.DataManagerInterface {

    private static final String TAG = ConversationActivity.class.getSimpleName();
    private EditText mContentEditText;
    private ArrayList<Message> mConversations;
    private MessageListAdapter mMessageAdapter;
    private DataManager mDataMngr;
    private RecyclerView  mMessageRecycler;
    private int mSequence;
    private OiDataBaseManager mDBMngr;
    private static final String SET_BLANK_TEXT ="";
    private Conversation mConversation;

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
        DataManagerListenerManager.registerListener(this);

        mDataMngr = DataManager.getInstance(this);
        mDBMngr = OiDataBaseManager.getInstance(this);
        mSequence =mDBMngr.getNextSequence(mConversation.getID());
        mConversations=mDBMngr.getAllMessage(mConversation);
        mMessageAdapter = new MessageListAdapter(this, mConversations);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
        mMessageRecycler.smoothScrollToPosition(mConversations.size());

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(pt.ulusofona.copelabs.oi_ndn.R.layout.activity_conversation);

        Log.d(TAG, "onCreate");
        String localContactName = getIntent().getStringExtra("LOCAL_CONTACT_NAME");
        String localContactId= getIntent().getStringExtra("LOCAL_CONTACT_ID");
        String contactName = getIntent().getStringExtra("CONTACT_NAME");
        String contactId = getIntent().getStringExtra("CONTACT_ID");

        mConversation= new Conversation(new Contact(localContactId,localContactName),new Contact(contactId,contactName));

        TextView txtContactName= findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.textView);
        txtContactName.setText(contactName);

        mMessageRecycler = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.recyclerView);

        ImageButton btn = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.button);
        mContentEditText =  findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.editTextUserName);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!mContentEditText.getText().equals(SET_BLANK_TEXT)) {
                    Message message = new Message(mConversation.getLocalContact().getID(),
                            mConversation.getContact().getID(),
                            mContentEditText.getText().toString());
                    message.setIsMine(Message.MESSAGE_IS_MINE);
                    message.setIsSent(Message.MESSAGE_NO_SENT);
                    message.setConversationId(mConversation.getID());

                    mDataMngr.sendData(message, mSequence);
                    mSequence++;

                    mContentEditText.setText(SET_BLANK_TEXT);

                    mConversations.add(message);

                    updateRecyclerView();
                }
            }
        });

    }

    @Override
    protected void onPause(){
        DataManagerListenerManager.unRegisterListener(this);
        super.onPause();
    }

    public void updateRecyclerView(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMessageAdapter.notifyDataSetChanged();
                mMessageRecycler.smoothScrollToPosition(mConversations.size()-1);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if ( id == android.R.id.home ) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dataInComing(Message message) {
        Log.d(TAG,"data incoming conversation id: " + message.getConversationId() +" conversation Id of actual: " + mConversation.getID());
        if(message.getConversationId().equals(mConversation.getIDBack())) {
            mConversations.add(message);
            updateRecyclerView();
        }
    }

    @Override
    public void dataSent(boolean isSent, String id) {
        Log.d(TAG,"Data Status message id: " +id + " was sent: "+isSent);
        if(isSent){
            for(Message a: mConversations){
                if(a.getID().equals(id)){
                    a.setIsSent(Message.MESSAGE_IS_SENT);
                }
            }
        }else{
            for(Message a: mConversations) {
                if (a.getID().equals(id)) {
                    a.setIsSent(Message.MESSAGE_NO_SENT);
                }
            }
        }
        updateRecyclerView();
    }

    @Override
    public void dataReceived(boolean isReceived, String id) {
        for(Message a: mConversations) {
            if (a.getID().equals(id)) {
                a.setIsDelivered(true);
            }
        }
        updateRecyclerView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
