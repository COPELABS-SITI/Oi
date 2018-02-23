package pt.ulusofona.copelabs.oi_ndn.presenters;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;

import pt.ulusofona.copelabs.oi_ndn.interfaces.ContactsContract;
import pt.ulusofona.copelabs.oi_ndn.helpers.ContactsLoader;
import pt.ulusofona.copelabs.oi_ndn.helpers.DataManager;
import pt.ulusofona.copelabs.oi_ndn.helpers.OiDataBaseManager;
import pt.ulusofona.copelabs.oi_ndn.helpers.Preferences;
import pt.ulusofona.copelabs.oi_ndn.models.Contact;
import pt.ulusofona.copelabs.oi_ndn.models.Conversation;
import pt.ulusofona.copelabs.oi_ndn.utils.Utils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


/**
 * This class is part of Oi application.
 * It receives the interactions from the NewChatDialogFragment class and performs the actions
 * based on those interactions.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */

public class ContactsPresenter implements ContactsContract.Presenter, ContactsLoader.ContactLoaderInterface {

    /**
     *  View interface implemented by the ctivity.
     */
    private ContactsContract.View mView;

    /**
     * Context of the activity.
     */
    private Context mContext;

    /**
     * Activity variable.
     */
    private Activity mActivity;

    /**
     * Loader Manager used to request the information about contacts saved in the device.
     */
    private LoaderManager mLoadManager;

    /**
     * OiDataBaseManager object.
     */
    private OiDataBaseManager mDBManager;

    /**
     * DataManager object.
     */
    private DataManager mDataManager;

    /**
     * ContactsPresenter constructor.
     * @param view View interface implemented by the activity.
     * @param context Context of the activity.
     * @param activity Activity.
     * @param loaderManager LoadManager from the activity.
     */
    public ContactsPresenter (ContactsContract.View view, Context context, Activity activity, LoaderManager loaderManager){
        mView=view;
        mContext=context;
        mActivity=activity;
        mLoadManager=loaderManager;
        start();
    }

    /**
     * Instance of DataManager and OiDtaBaseManager are requested.
     */
    @Override
    public void start() {
        mDBManager =OiDataBaseManager.getInstance(mContext);
        mDataManager = DataManager.getInstance(mContext);
    }

    /**
     * Loads the contacts which are saved in the device.
     */
    @Override
    public void loadContacts() {
        new ContactsLoader(mActivity,this, mLoadManager);
    }

    /**
     * When a contact is selected from the view that information comes to this method in order to
     * decide if a new conversation is going to be created or if it already exists, starts a new
     * ConversationActivity.
     * @param contact Contact selected.
     */
    @Override
    public void onContactSelected(Contact contact) {
        Contact localContact = Preferences.getLocalContact(mActivity);

        if(localContact.getID()==null)
            mView.errorUserConfiguration();
        else {
            Conversation conversation_ = new Conversation(localContact, contact);
            if(mDBManager.hasConversation(conversation_.getID())) {
                mView.chatAlreadyExists(conversation_);
                mView.afterContactSelected();
            }else {
                mView.showContactSelected(contact);
                try {
                    mDBManager.saveNewChat(conversation_);
                    mDataManager.expressInterest(Utils.hashMD5(contact.getID() + localContact.getID())+"/0");
                    //mDataManager.expressInterest(conversation_.getIDBack()+"/0");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * This method shows the data collection of contacts saved in the device.
     *
     * @param arrayList ArrayList of contact data collection.
     */
    @Override
    public void onContactLoaded(ArrayList<Contact> arrayList) {
        mView.showContacts(arrayList);
    }
}
