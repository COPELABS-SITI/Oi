package pt.ulusofona.copelabs.oi.presenters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import pt.ulusofona.copelabs.oi.fragment.MessageConfigDialogFragment;
import pt.ulusofona.copelabs.oi.helpers.ContactsLoader;
import pt.ulusofona.copelabs.oi.helpers.DataManager;
import pt.ulusofona.copelabs.oi.helpers.DataManagerListenerManager;
import pt.ulusofona.copelabs.oi.helpers.OiDataBaseManager;
import pt.ulusofona.copelabs.oi.helpers.Preferences;
import pt.ulusofona.copelabs.oi.interfaces.EndUserContract;
import pt.ulusofona.copelabs.oi.models.Contact;
import pt.ulusofona.copelabs.oi.models.Conversation;
import pt.ulusofona.copelabs.oi.models.Message;
import pt.ulusofona.copelabs.oi.utils.Utils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * This class is part of Oi application.
 * It receives the interactions from the EndUserActivity class and perform the actions based on
 * those interactions.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */

public class EndUserPresenter implements EndUserContract.Presenter, DataManager.PushData, DataManager.Invitations, ContactsLoader.ContactLoaderInterface {
    /**
     * Variable used for debug
     */
    private static final String TAG = EndUserPresenter.class.getSimpleName();
    /**
     * Variable used to identify the permission of the contacts.
     */
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    /**
     * Context of the application.
     */
    private Context mContext;
    /**
     * View interface implemented by a activity or oder view component.
     */
    private EndUserContract.View mView;

    /**
     * DataManager variable.
     */
    private DataManager mDataMngr;
    /**
     * OiDataBaseManager variable.
     */
    private OiDataBaseManager mDBMngr;

    /**
     * Activity of the application.
     */
    private Activity mActivity;

    /**
     * Constructor of EndUserPresenter.
     * @param context Context of the application.
     * @param view View object which implements the view interface.
     * @param activity Activity of the application.
     */
    public EndUserPresenter(Context context, EndUserContract.View view, Activity activity) {
        mContext = context;
        mView = view;
        mActivity = activity;
        start();
    }

    /**
     * This method perform the initial setup, where is loaded all the conversation registered
     * in the device and missing interest are expressed.
     */
    @Override
    public void start() {
        if (checkContactsPermission())
            loadContacts();

        mDataMngr = DataManager.getInstance(mContext);
        mDataMngr.SYNC = true;

        if (Preferences.getMessageConfig(mActivity) != Preferences.DEFAULT_VALUE_MESSAGE_CONFIGURATION) {
            mDataMngr.registerPushPrefix();
        }
        mDataMngr.expressAllEmergencyLLI();

        mDBMngr = OiDataBaseManager.getInstance(mContext);
        mDBMngr.openDB(true);

        ArrayList<Conversation> mConversations;
        mConversations = mDBMngr.getAllConversations(Preferences.getLocalContact(mActivity));
        mView.showConversation(mConversations);


        if (mConversations.size() > 0) {
            for (Conversation conversation : mConversations) {
                String lastPrefix = mDBMngr.getlastPrefix(conversation.getID());
                if (lastPrefix != null) {

                    int dataNumber = Integer.parseInt(Utils.getLastSequence(lastPrefix));

                    if (mDBMngr.hasMessage(lastPrefix))
                        dataNumber = dataNumber + 1;

                    try {
                        mDataMngr.expressInterest(Utils.hashMD5(conversation.getContact().getID() + Preferences.getLocalContact(mActivity).getID()) + "/" + dataNumber);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * This method receives the menu option from the view and performs actions related to each
     * item.
     * @param itemId ItemID of the menu item pressed.
     */
    @Override
    public void OnMenuItemPressed(int itemId) {
        switch (itemId) {
            case pt.ulusofona.copelabs.oi.R.id.user_configuration:
                mView.showUserConfiguration();
                break;
            case pt.ulusofona.copelabs.oi.R.id.emergency:
                mView.showEmergencyMessage();
                break;
            case pt.ulusofona.copelabs.oi.R.id.newChat:
                if (checkContactsPermission())
                    mView.showContact();
                break;
            case pt.ulusofona.copelabs.oi.R.id.messageConfiguration:
                mView.showMessageConfiguration();
                break;
        }
    }

    /**
     * This method notify when a new conversation is added. The information of the contact is
     * used to create prefixes and save the information of the new conversation in database.
     * @param contact Contact object of the new conversation.
     */
    @Override
    public void newConversation(Contact contact) {
        Conversation conversation = new Conversation(Preferences.getLocalContact(mActivity), contact);
        mView.showNewConversation(conversation);
    }

    /**
     * This method notify when the message configuration is changed.
     * @param option Integer of option selected.
     */
    @Override
    public void messageConfiguration(int option) {
        if (option != MessageConfigDialogFragment.NO_SELECTION) {
            mDataMngr.registerPushPrefix();
            registerAsListener();
        }
    }

    /**
     * Registers the presenter as a listener of DataManager class.
     */
    @Override
    public void registerAsListener() {
        DataManagerListenerManager.registerPushDataListener(this);
        DataManagerListenerManager.registerInvitationListener(this);
    }
    /**
     * Unregisters the presenter as a listener of DataManager class.
     */
    @Override
    public void unRegisterAsListener() {
        DataManagerListenerManager.unRegisterPushDataListener(this);
        DataManagerListenerManager.unRegisterInvitationListener(this);
    }

    @Override
    public void loadContacts() {
        new ContactsLoader(mActivity,this, mActivity.getLoaderManager());
    }

    /**
     * Function used to check the Contacts permission.
     * @return Boolean, where true value mean the the permission is granted, otherwise, false.
     */
    private boolean checkContactsPermission() {
        boolean result = true;
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_CONTACTS);
        if (permissionCheck == -1) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            result = false;
        }
        return result;
    }

    /**
     *
     * @param message
     */
    @Override
    public void pushDataArrives(Message message) {
        mView.showPushDatArrives(message);
    }

    /**
     *
     * @param contact
     */
    @Override
    public void InvitationInConing(Contact contact) {
        mView.showInvitation(contact);
    }

    /**
     *
     * @param arrayList ArrayList of contact data collection.
     */
    @Override
    public void onContactLoaded(ArrayList<Contact> arrayList) {

        mDataMngr.setLocalContact(Preferences.getLocalContact(mActivity));
        mDataMngr.setContactList(arrayList);
    }
}
