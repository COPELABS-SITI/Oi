package pt.ulusofona.copelabs.oi.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import pt.ulusofona.copelabs.oi.adapters.ContactsArrayAdapter;
import pt.ulusofona.copelabs.oi.interfaces.ContactsContract;

import pt.ulusofona.copelabs.oi.models.Contact;
import pt.ulusofona.copelabs.oi.models.Conversation;
import pt.ulusofona.copelabs.oi.presenters.ContactsPresenter;

import java.util.ArrayList;

/**
 * This class is a DialogFragment class which shows the contacts save in the device.
 * From here is possible to create a new conversation every time that an user selects a
 * contact from the contact list.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */

public class NewChatDialogFragment extends DialogFragment implements
        AdapterView.OnItemClickListener, ContactsContract.View {

    /**
     * Variable used for debug.
     */
    private String TAG = getClass().getSimpleName();
    /**
     * ContactsFragmentInterface interface implemented by the activity.
     */
    private ContactsFragmentInterface mInterface;
    /**
     * ListView which contains the data of the contacts.
     */
    private ListView mListView;
    /**
     * Presenter
     */
    private ContactsPresenter mPresenter;
    /**
     * Context of the activity.
     */
    private Context mContext;

    /**
     * Create a new instance of EmergencyDialogFragment, providing "num"
     * as an argument.
     */
    public static NewChatDialogFragment newInstance(int num) {

        NewChatDialogFragment f = new NewChatDialogFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }

    /**
     * In this method the presenter is initialized. And the contacts information is load.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ContactsPresenter(this, mContext, this.getActivity(), getLoaderManager());
        mPresenter.loadContacts();
    }

    /**
     * This method is used to setup the view of the fragment.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(pt.ulusofona.copelabs.oi.R.layout.fragment_new_chat, container, false);

        mListView = v.findViewById(pt.ulusofona.copelabs.oi.R.id.listView_contacts);
        mListView.setOnItemClickListener(this);

        Button btnFinish = v.findViewById(pt.ulusofona.copelabs.oi.R.id.button3);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        try {
            mInterface = (ContactsFragmentInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    /**
     * This method receives tha action whe the user selects a contact from the contact list.
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Contact contact = (Contact) mListView.getItemAtPosition(i);
        mPresenter.onContactSelected(contact);
    }

    /**
     * Shows the information of the contacts saved in the device.
     *
     * @param arrayList Contacts collection data.
     */
    @Override
    public void showContacts(ArrayList<Contact> arrayList) {
        ContactsArrayAdapter mContactsArrayAdapter = new ContactsArrayAdapter(this.getActivity(), arrayList);
        mListView.setAdapter(mContactsArrayAdapter);
    }

    /**
     * Cancels the dialog.
     */
    @Override
    public void afterContactSelected() {
        getDialog().cancel();
    }

    /**
     * Shows an error message when the local user information is empty.
     */
    @Override
    public void errorUserConfiguration() {
        Toast.makeText(getActivity(), "Configure your user information", Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows the contact selected from the listView.
     *
     * @param contact Contact selected.
     */
    @Override
    public void showContactSelected(Contact contact) {
        mInterface.newConversation(contact);
    }

    /**
     * Starts ConversationActivity when the conversation already exists.
     *
     * @param conversation Conversation selected.
     */
    @Override
    public void chatAlreadyExists(Conversation conversation) {
        mInterface.startChat(conversation);
    }

    /**
     * This interface allows to receive new conversations that are created when a user select
     * a contact or start an ConversationActivity when the conversation already exists.
     */
    public interface ContactsFragmentInterface {
        /**
         * This method notifies when a new conversation is created.
         *
         * @param contact Contact selected by the user.
         */
        void newConversation(Contact contact);

        /**
         * Start a ConversationActivity when the conversation already exists.
         *
         * @param conversation Conversation selected.
         */
        void startChat(Conversation conversation);
    }

}
