package pt.ulusofona.copelabs.oi.interfaces;

import pt.ulusofona.copelabs.oi.models.Contact;
import pt.ulusofona.copelabs.oi.models.Conversation;
import pt.ulusofona.copelabs.oi.models.Message;

import java.util.ArrayList;

/**
 * This class is part of Oi application.
 * It provides the interaction between the EndUserActivity class and the
 * EndUserPresenter class.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0,  02/14/18
 */

public interface EndUserContract {
    /**
     * Interface implemented by EndUserActivity class.
     */
    interface View{
        /**
         * Shows the user configuration activity.
         */
        void showUserConfiguration();

        /**
         * Shows the MessageConfigDialog fragment class.
         */
        void showMessageConfiguration();

        /**
         * Shows the activity from where is possible create a emergency message.
         */
        void showEmergencyMessage();

        /**
         * Shows when a new chat invitation arrives.
         * @param contact Contact of the requester.
         */
        void showInvitation(Contact contact);

        /**
         * Show view with the contact list saved iin the device.
         */
        void showContact();

        /**
         * Shows view of push data message arrived.
         * @param message Message push mode object.
         */
        void showPushDatArrives(Message message);

        /**
         * Shows a new conversation added to the view.
         * @param conversation Conversation object.
         */
        void showNewConversation (Conversation conversation);

        /**
         * Show all the conversation registered in the device.
         * @param arrayList Array list with all conversation objects.
         */
        void showConversation(ArrayList<Conversation> arrayList);
    }

    /**
     * This interface is implemented by EndUserPresenter class.
     */
    interface Presenter{
        /**
         * This method perform the initial setup, where is loaded all the conversation registered
         * in the device and missing interest are expressed.
         */
        void start();

        /**
         * This method receives the menu option from the view and performs actions related to each
         * item.
         * @param itemId ItemID of the menu item pressed.
         */
        void OnMenuItemPressed(int itemId);

        /**
         * This method notify when a new conversation is added. The information of the contact is
         * used to create prefixes and save the information of the new conversation in database.
         * @param contact Contact object of the new conversation.
         */
        void newConversation(Contact contact);

        /**
         * This method notify when the message configuration is changed.
         * @param option Integer of option selected.
         */
        void messageConfiguration(int option);

        /**
         * Registers the presenter as a listener of DataManager class.
         */
        void registerAsListener();

        /**
         * Unregisters the presenter as a listener of DataManager class.
         */
        void unRegisterAsListener();

        /**
         * Loads contacts saved in the device.
         */
        void loadContacts();

        void startLocation();

    }
}
