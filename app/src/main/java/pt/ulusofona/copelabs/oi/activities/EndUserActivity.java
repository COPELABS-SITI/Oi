package pt.ulusofona.copelabs.oi.activities;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import pt.ulusofona.copelabs.oi.adapters.ConversationArrayAdapter;
import pt.ulusofona.copelabs.oi.fragment.EmergencyMessageDialog;
import pt.ulusofona.copelabs.oi.fragment.InvitationDialog;
import pt.ulusofona.copelabs.oi.fragment.MessageConfigDialogFragment;
import pt.ulusofona.copelabs.oi.fragment.NewChatDialogFragment;
import pt.ulusofona.copelabs.oi.interfaces.EndUserContract;
import pt.ulusofona.copelabs.oi.models.Contact;
import pt.ulusofona.copelabs.oi.models.Conversation;
import pt.ulusofona.copelabs.oi.models.Message;
import pt.ulusofona.copelabs.oi.presenters.EndUserPresenter;

import java.util.ArrayList;

/**
 * This class is a AppCompactActivity class which shows the conversation registered in the device.
 * Also from here is possible to change the user configuration, message configuration and start
 * new conversations. This class implement EndUserContract.View interface in order to communicate
 * with EndUserPresenter class.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public class EndUserActivity extends AppCompatActivity implements
        NewChatDialogFragment.ContactsFragmentInterface,
        AdapterView.OnItemClickListener,
        MessageConfigDialogFragment.MessageConfigInterface,
        EndUserContract.View,ActivityCompat.OnRequestPermissionsResultCallback, InvitationDialog.InvitationDialogInterface{
    /**
     * Variable used for debug.
     */
    private static final String TAG = EndUserActivity.class.getSimpleName();
    /**
     * This array contains all the conversation objects.
     */
    private ArrayList<Conversation> mConversations;
    /**
     * View adapter for the cells of the listView.
     */
    private ConversationArrayAdapter mConversationArrayAdapter;
    /**
     * ListView used to display the conversations active in the application.
     */
    private ListView mListView;
    /**
     * Presenter of the EndUserActivity class.
     */
    private EndUserPresenter mPresenter;
    /**
     * Variable used to identify the permission of the contacts.
     */
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    /**
     * Variable used to identify the permission of location.
     */
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 2;

    /**
     * Registers the presenter as a listener of DataManager class.
     */
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.registerAsListener();
    }

    /**
     * This method performs the view setup and the presenter is initialized.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(pt.ulusofona.copelabs.oi.R.layout.activity_end_user);
        mListView = findViewById(pt.ulusofona.copelabs.oi.R.id.chat_list);
        mPresenter = new EndUserPresenter(this, this, this);
    }

    /**
     * When a item from the listView is pressed, this method receives the notification
     * and a conversation activity is opened.
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i != 0) {
            Conversation conversation = (Conversation) mListView.getItemAtPosition(i);
            Intent intent = new Intent(EndUserActivity.this, ConversationActivity.class);
            intent.putExtra("LOCAL_CONTACT_ID", conversation.getLocalContact().getID());
            intent.putExtra("LOCAL_CONTACT_NAME", conversation.getLocalContact().getName());
            intent.putExtra("CONTACT_NAME", conversation.getContact().getName());
            intent.putExtra("CONTACT_ID", conversation.getContact().getID());
            startActivity(intent);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(pt.ulusofona.copelabs.oi.R.menu.menu_main, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    /**
     * This method receives the action when a menu item is pressed.
     * @param item MenuItem pressed.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mPresenter.OnMenuItemPressed(id);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void newConversation(Contact contact) {
        Log.d(TAG, "Contact name: " + contact.getName() + " contact_id: " + contact.getID());
        mPresenter.newConversation(contact);
    }

    @Override
    public void startChat(Conversation conversation) {
        Intent intent = new Intent(EndUserActivity.this, ConversationActivity.class);
        intent.putExtra("LOCAL_CONTACT_ID", conversation.getLocalContact().getID());
        intent.putExtra("LOCAL_CONTACT_NAME", conversation.getLocalContact().getName());
        intent.putExtra("CONTACT_NAME", conversation.getContact().getName());
        intent.putExtra("CONTACT_ID", conversation.getContact().getID());
        startActivity(intent);
    }

    /**
     * Unregisters the presenter as a listener of DataManager class.
     */
    @Override
    protected void onPause() {
        mPresenter.unRegisterAsListener();
        super.onPause();
    }


    @Override
    public void OnMessageSet(int option) {
        mPresenter.messageConfiguration(option);
    }

    /**
     * Shows the user configuration activity.
     */
    @Override
    public void showUserConfiguration() {
        Intent intent = new Intent(this, UserConfigurationActivity.class);
        intent.putExtra(UserConfigurationActivity.FROM_ACTIVITY,
                UserConfigurationActivity.FROM_END_USER_ACTIVITY);
        startActivity(intent);
    }

    /**
     * Shows the MessageConfigDialog fragment class.
     */
    @Override
    public void showMessageConfiguration() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        android.app.Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
            ft.remove(prev);

        ft.addToBackStack(null);
        DialogFragment newFragment = MessageConfigDialogFragment.newInstance(0);
        newFragment.show(ft, "dialog");
    }

    /**
     * Shows the activity from where is possible create a emergency message.
     */
    @Override
    public void showEmergencyMessage() {
        Intent intent = new Intent(this, EmergencyMessageActivity.class);
        startActivity(intent);
    }

    /**
     * Shows when a new chat invitation arrives.
     * @param contact Contact of the requester.
     */
    @Override
    public void showInvitation(Contact contact) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        android.app.Fragment prev = getFragmentManager().findFragmentByTag("newchat");
        if (prev != null)
            ft.remove(prev);
        ft.addToBackStack(null);
        InvitationDialog newFragment = InvitationDialog.newInstance(0, contact.getName(),contact.getID());
        newFragment.show(ft, "dialog");
    }

    /**
     * Show view with the contact list saved iin the device.
     */
    @Override
    public void showContact() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        android.app.Fragment prev = getFragmentManager().findFragmentByTag("newchat");
        if (prev != null)
            ft.remove(prev);
        ft.addToBackStack(null);
        DialogFragment newFragment = NewChatDialogFragment.newInstance(0);
        newFragment.show(ft, "dialog");
    }

    /**
     * Shows view of push data message arrived.
     * @param message Message push mode object.
     */
    @Override
    public void showPushDatArrives(Message message) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        android.app.Fragment prev = getFragmentManager().findFragmentByTag("newchat");
        if (prev != null)
            ft.remove(prev);
        ft.addToBackStack(null);
        EmergencyMessageDialog newFragment = EmergencyMessageDialog.newInstance(0,
                message.getmUser(),
                message.getFrom(),
                message.getContent(),
                message.getmDate());
        newFragment.show(ft, "dialog");
    }

    /**
     * Shows a new conversation added to the view.
     * @param conversation Conversation object.
     */
    @Override
    public void showNewConversation(Conversation conversation) {
        mConversations.add(conversation);
        mConversationArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Show all the conversation registered in the device.
     * @param arrayList Array list with all conversation objects.
     */
    @Override
    public void showConversation(ArrayList<Conversation> arrayList) {
        mConversations = arrayList;
        mConversationArrayAdapter = new ConversationArrayAdapter(this, mConversations);
        mListView.setAdapter(mConversationArrayAdapter);
        LayoutInflater layoutInflater = getLayoutInflater();
        ViewGroup myHeader = (ViewGroup) layoutInflater.inflate(pt.ulusofona.copelabs.oi.R.layout.header_list_view, mListView, false);
        mListView.addHeaderView(myHeader);
        mListView.setOnItemClickListener(this);
    }
    /**
     * This function is executed once permission are checked.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"onRequest");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPresenter.loadContacts();
                } else {
                    Toast.makeText(this, getResources().getString(pt.ulusofona.copelabs.oi.R.string.contact_permission_denied), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();

    }

    @Override
    public void OnInvitationAccepted(Contact contact) {
        mPresenter.newConversation(contact);
    }
}
