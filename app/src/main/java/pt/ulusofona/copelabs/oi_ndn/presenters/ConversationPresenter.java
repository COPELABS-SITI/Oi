package pt.ulusofona.copelabs.oi_ndn.presenters;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import pt.ulusofona.copelabs.oi_ndn.helpers.DataManager;
import pt.ulusofona.copelabs.oi_ndn.helpers.DataManagerListenerManager;
import pt.ulusofona.copelabs.oi_ndn.helpers.OiDataBaseManager;
import pt.ulusofona.copelabs.oi_ndn.interfaces.ConversationContract;
import pt.ulusofona.copelabs.oi_ndn.models.Contact;
import pt.ulusofona.copelabs.oi_ndn.models.Conversation;
import pt.ulusofona.copelabs.oi_ndn.models.Message;

/**
 * This class is part of Oi application.
 * It receives the interactions from the ConversationActivity class and perform the actions based on
 * those interactions.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */

public class ConversationPresenter implements ConversationContract.Presenter,
        DataManager.DataManagerInterface {
    /**
     * Variable used for debug.
     */
    private static final String TAG = ConversationPresenter.class.getSimpleName();
    /**
     * Used to set blank text in the message text field..
     */
    private static final String SET_BLANK_TEXT = "";
    /**
     * Data base manager.
     */
    private OiDataBaseManager mDBManager;

    /**
     * Data manager.
     */
    private DataManager mDataManager;
    /**
     * This is the conversation shows in the activity.
     */
    private Conversation mConversation;
    /**
     * This array contains all the messages exchanged in the conversation.
     */
    private ArrayList<Message> mMessages;
    /**
     * Context of the application.
     */
    private Context mContext;

    /**
     * View interface implemented by the activity.
     */
    private ConversationContract.View mView;

    /**
     * Sequence of the message.
     */
    private int mSequence;

    /**
     * ConversationPresenter constructor.
     *
     * @param localContactName Local contact name.
     * @param localContactId   Local contact id.
     * @param contactName      Contact name.
     * @param contactId        Contact id.
     * @param context          Context of the application.
     * @param view             View implemented by the activity.
     */
    public ConversationPresenter(String localContactName,
                                 String localContactId,
                                 String contactName,
                                 String contactId,
                                 Context context,
                                 ConversationContract.View view) {
        mContext = context;
        mView = view;

        start(localContactName, localContactId, contactName, contactId);
    }


    /**
     * This method is used to receive the information about the contacts involved in the
     * conversation. Whit that information is created the object Conversation.
     *
     * @param localContactName Local contact name.
     * @param localContactId   Local contact Id.
     * @param contactName      Contact name.
     * @param contactId        Contact Id.
     */
    @Override
    public void start(String localContactName, String localContactId, String contactName, String contactId) {
        mConversation = new Conversation(new Contact(localContactId, localContactName),
                new Contact(contactId, contactName));
        mView.showContactName(contactName);
    }

    /**
     * This method is used to load the messages already saved in the conversation.
     */
    @Override
    public void preLoad() {
        mDataManager = DataManager.getInstance(mContext);
        mDBManager = OiDataBaseManager.getInstance(mContext);
        mSequence = mDBManager.getNextSequence(mConversation.getID());
        mMessages = mDBManager.getAllMessage(mConversation);
        mView.loadMessages(mMessages);
    }

    /**
     * This method is used to sent a new message.
     *
     * @param messageContent Content of th message.
     */
    @Override
    public void sendMessage(String messageContent) {
        if (!messageContent.equals(SET_BLANK_TEXT)) {
            Message message = new Message(mConversation.getLocalContact().getID(),
                    mConversation.getContact().getID(),
                    messageContent);
            message.setIsMine(Message.MESSAGE_IS_MINE);
            message.setIsSent(Message.MESSAGE_NO_SENT);
            message.setConversationId(mConversation.getID());

            mDataManager.sendData(message, mSequence);
            mSequence++;

            mView.setMessageEditText(SET_BLANK_TEXT);

            mMessages.add(message);
            mView.showMessageSentStatus(mMessages);
        }
    }

    @Override
    public void setBlank() {

    }

    /**
     * This method is used to receive the item pressed in the menu.
     *
     * @param item
     */
    @Override
    public void itemMenuSelect(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mView.exitAction();
        }
    }

    /**
     * Registers the presenter as listener.
     */

    @Override
    public void registerAsListener() {
        DataManagerListenerManager.registerListener(this);
    }

    /**
     * Unregisters the presenter as listener.
     */
    @Override
    public void unregisterAsListener() {
        DataManagerListenerManager.unRegisterListener(this);
    }

    @Override
    public void dataInComing(Message message) {
        Log.d(TAG, "data incoming conversation id: " + message.getConversationId() + " conversation Id of actual: " + mConversation.getID());
        if (message.getConversationId().equals(mConversation.getIDBack())) {
            mMessages.add(message);
            mView.showMessageSentStatus(mMessages);
        }
    }

    @Override
    public void dataSent(boolean isSent, String id) {
        Log.d(TAG, "Data Status message id: " + id + " was sent: " + isSent);
        if (isSent) {
            for (Message a : mMessages) {
                if (a.getID().equals(id)) {
                    a.setIsSent(Message.MESSAGE_IS_SENT);
                }
            }
        } else {
            for (Message a : mMessages) {
                if (a.getID().equals(id)) {
                    a.setIsSent(Message.MESSAGE_NO_SENT);
                }
            }
        }
        mView.showMessageSentStatus(mMessages);
    }

    @Override
    public void dataReceived(boolean isReceived, String id) {
        for (Message a : mMessages) {
            if (a.getID().equals(id)) {
                a.setIsDelivered(true);
            }
        }
        mView.showMessageSentStatus(mMessages);
    }
}
