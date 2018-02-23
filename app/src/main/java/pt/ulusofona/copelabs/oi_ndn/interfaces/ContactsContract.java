package pt.ulusofona.copelabs.oi_ndn.interfaces;

import pt.ulusofona.copelabs.oi_ndn.models.Contact;
import pt.ulusofona.copelabs.oi_ndn.models.Conversation;

import java.util.ArrayList;

/**
 * This class is part of Oi application.
 * It provides the interaction between NewChatDialogFragment class and the
 * ContactsPresenter class.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0,  02/14/18 3:05 PM
 */

public interface ContactsContract {

    /**
     * Interface implemented by NewChatDialogFragment class.
     */
    interface View {

        /**
         * Shows the information of the contacts saved in the device.
         *
         * @param arrayList Contacts collection data.
         */
        void showContacts(ArrayList<Contact> arrayList);

        /**
         * Cancels the dialog.
         */
        void afterContactSelected();

        /**
         * Shows an error message when the local user information is empty.
         */
        void errorUserConfiguration();

        /**
         * Shows the contact selected from the listView.
         *
         * @param contact Contact selected.
         */
        void showContactSelected(Contact contact);

        /**
         * Starts ConversationActivity when the conversation already exists.
         *
         * @param conversation Conversation selected.
         */
        void chatAlreadyExists(Conversation conversation);
    }

    /**
     * Interface implemented by ContactsPresenter.
     */
    interface Presenter {
        /**
         * Instance of DataManager and OiDtaBaseManager are requested.
         */
        void start();

        /**
         * Loads the contacts which are saved in the device.
         */
        void loadContacts();

        /**
         * When a contact is selected from the view that infomration comes to this method in order to
         * decide if a new conversation is going to be created or if it already exists, starts a new
         * ConversationActivity.
         *
         * @param contact Contact selected.
         */
        void onContactSelected(Contact contact);
    }

}
