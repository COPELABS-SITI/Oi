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

import java.util.ArrayList;

import pt.ulusofona.copelabs.oi_ndn.adapters.MessageListAdapter;
import pt.ulusofona.copelabs.oi_ndn.interfaces.ConversationContract;
import pt.ulusofona.copelabs.oi_ndn.models.Message;
import pt.ulusofona.copelabs.oi_ndn.presenters.ConversationPresenter;

/**
 * This class is a AppCompactActivity class which shows the messages exchanged in the conversation.
 * From here , users can create new messages an send it.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public class ConversationActivity extends AppCompatActivity implements
        ConversationContract.View,
        View.OnClickListener {
    /**
     * Variable used for debug.
     */
    private static final String TAG = ConversationActivity.class.getSimpleName();
    /**
     * EditText used to collect the content of the message.
     */
    private EditText mContentEditText;

    private ArrayList<Message> mConversations;
    /**
     * Adapter used to display the messages received and sent.
     */
    private MessageListAdapter mMessageAdapter;
    /**
     * RecyclerView used to contain the adapter of the message.
     */
    private RecyclerView mMessageRecycler;
    /**
     * TextView displays the name of the contact which the conversation is established.
     */
    private TextView mContactTextView;
    /**
     * View interface implemented by this activity.
     */
    private ConversationContract.Presenter mPresenter;

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        mPresenter.registerAsListener();
        mPresenter.preLoad();
    }

    /**
     * This method performs the initial set up of the view.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(pt.ulusofona.copelabs.oi_ndn.R.layout.activity_conversation);

        Log.d(TAG, "onCreate");

        mContactTextView = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.textView);

        mMessageRecycler = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.recyclerView);

        mPresenter = new ConversationPresenter(getIntent().getStringExtra("LOCAL_CONTACT_NAME"),
                getIntent().getStringExtra("LOCAL_CONTACT_ID"),
                getIntent().getStringExtra("CONTACT_NAME"),
                getIntent().getStringExtra("CONTACT_ID"),
                this,
                this);

        ImageButton btn = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.button);
        mContentEditText = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.editTextUserName);
        btn.setOnClickListener(this);

    }

    @Override
    protected void onPause() {
        mPresenter.unregisterAsListener();
        super.onPause();
    }

    public void updateRecyclerView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMessageAdapter.notifyDataSetChanged();
                mMessageRecycler.smoothScrollToPosition(mConversations.size() - 1);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPresenter.itemMenuSelect(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * shows the name of the contact with the conversation is established.
     *
     * @param contactName Name of the contact.
     */
    @Override
    public void showContactName(String contactName) {
        mContactTextView.setText(contactName);
    }

    /**
     * This is used to set any message in the edit text field used to create message content.
     *
     * @param content Content on the message.
     */
    @Override
    public void setMessageEditText(String content) {

    }

    /**
     * Shows the messages when the activity is started.
     *
     * @param messages
     */
    @Override
    public void loadMessages(final ArrayList<Message> messages) {
        mMessageAdapter = new MessageListAdapter(this, messages);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
        mMessageRecycler.smoothScrollToPosition(messages.size());
    }

    /**
     * Any time a new message arrives in the data manager or  the messages change theirs status, this
     * method is used to notify those cases.
     *
     * @param messages
     */
    @Override
    public void showMessageSentStatus(final ArrayList<Message> messages) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMessageAdapter.notifyDataSetChanged();
                mMessageRecycler.smoothScrollToPosition(messages.size() - 1);
            }
        });
    }

    /**
     * This method performs an action when the activity closes.
     */
    @Override
    public void exitAction() {
        finish();
    }


    @Override
    public void onClick(View view) {
        mPresenter.sendMessage(mContentEditText.getText().toString());
    }
}
