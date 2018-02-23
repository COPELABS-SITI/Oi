package pt.ulusofona.copelabs.oi_ndn.interfaces;

import pt.ulusofona.copelabs.oi_ndn.models.Message;

/**
 * This class is part of Oi application.
 * It provides the interaction between the AuthorityActivity class and the
 * AuthorityPresenter class.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0,  02/14/18
 */

public interface AuthorityContract {

    /**
     * Interface implemented by AuthorityActivity class.
     */
    interface View {

        /**
         * Shows the dialog where users can decide if want to receive push data or not.
         */
        void showConfigurationDialog();

        /**
         * Shows the authority that was selected from the AuthoritySelectionActivity activity.
         *
         * @param authority Authority selected.
         */
        void showAuthoritySelected(String authority);

        /**
         * When a message is received, this function is called whit the proposal of add the new message
         * to the Array message.
         *
         * @param message Message received.
         */
        void showMessage(Message message);

        /**
         * Updates the listView once a new message is added to the Array message.
         */
        void upDateListView();
    }

    /**
     * Interface implemented by AuthorityPresenter class.
     */
    interface Presenter {

        /**
         * This method is used to express a interest using DataManager object
         * once the activity starts. Also, this method set the name of the authority selected.
         * @param authority Authority selected.
         */
        void start(int authority);

        /**
         * Performs the registration of the presenter in order to receive the messages from DataManager
         * module.
         */
        void registerAsListener();

        /**
         * Performs the unregistration of the presenter from the DataManager interface.
         */
        void unRegisterAsListener();

        /**
         * Performs the configuration of the message.
         */
        void changeMessageConfiguration();

        /**
         * This function is performed once the user selects the option of receive push data. From here
         * is used DataManager in order to execute this task.
         */
        void registerPushPrefix();
    }
}
