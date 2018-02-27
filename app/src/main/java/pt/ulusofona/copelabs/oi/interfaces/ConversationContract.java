package pt.ulusofona.copelabs.oi.interfaces;

import android.view.MenuItem;

import java.util.ArrayList;

import pt.ulusofona.copelabs.oi.models.Message;

/**
 * This class is part of Oi application.
 * It provides the interaction between the ConversationActivity class and the
 * ConversationPresenter class.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0,  02/14/18
 */
public interface ConversationContract {

    /**
     * Interface implemented by ConversationActivity class.
     */
    interface View{

        /**
         * shows the name of the contact with the conversation is established.
         * @param contactName Name of the contact.
         */
        void showContactName(String contactName);

        /**
         * This is used to set any message in the edit text field used to create message content.
         * @param content Content on the message.
         */
        void setMessageEditText(String content);

        /**
         * Shows the messages when the activity is started.
         * @param messages
         */
        void loadMessages(ArrayList<Message> messages);

        /**
         * Any time a new message arrives in the data manager or  the messages change theirs status, this
         * method is used to notify those cases.
         * @param messages
         */
        void showMessageSentStatus(ArrayList<Message> messages);



        /**
         * This method performs an action when the activity closes.
         */
        void exitAction();

    }

    /**
     * This interface is implemented by ConversationPresenter class.
     */
    interface Presenter{

        /**
         * This method is used to receive the information about the contacts involved in the
         * conversation. Whit that information is created the object Conversation.
         * @param localContactName Local contact name.
         * @param localContactId Local contact Id.
         * @param contactName Contact name.
         * @param contactId Contact Id.
         */
        void start(String localContactName, String localContactId, String contactName, String contactId);

        /**
         * This method is used to load the messages already saved in the conversation.
         */
        void preLoad();

        /**
         * This method is used to sent a new message.
         * @param messageContent Content of th message.
         */
        void sendMessage(String messageContent);

        void setBlank();

        /**
         * This method is used to receive the item pressed in the menu.
         * @param item
         */
        void itemMenuSelect(MenuItem item);

        /**
         * Registers the presenter as listener.
         */

        void registerAsListener();

        /**
         * Unregisters the presenter as listener.
         */
        void unregisterAsListener();

    }
}
