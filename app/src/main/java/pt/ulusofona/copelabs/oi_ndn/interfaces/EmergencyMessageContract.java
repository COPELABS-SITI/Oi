package pt.ulusofona.copelabs.oi_ndn.interfaces;

/**
 * This class is part of Oi application.
 * It provides the interaction between MessageEmergencyActivity class and
 * EmergencyMessagePresenter class.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0,  02/14/18 3:05 PM
 */

public interface EmergencyMessageContract {

    /**
     * Interface Implemented by EmergencyMessageActivity class.
     */
    interface View {
        /**
         * Set the checkboxes once Firefighter checkbox is checked.
         */
        void onFireFighterSelected();

        /**
         * Set the checkboxes once Police checkbox is checked.
         */
        void onPoliceSelected();

        /**
         * Set the checkboxes once Special Service checkbox is checked.
         */
        void onEspecialServiceSelected();

        /**
         * Set the checkboxes once People Nearby checkbox is checked.
         */
        void onPeopleNearbySelected();

        /**
         * Set the checkboxes once Rescue Team checkbox is checked.
         */
        void onRescueTeamSelected();

        /**
         * Shows error message once the content of the message is empty.
         */
        void showErrorEmptyMessage();

        /**
         * Shows error message once there is not option selected.
         */
        void showErrorMessageOptionNotSelected();

        /**
         * Hides the error message of content message empty.
         */
        void hideErrorEmptyMessage();

        /**
         * Hides the error message of no option selected.
         */
        void hideErrorMessageOptionSelected();

        /**
         * Finishes the activity once the message is sent.
         */
        void afterMessageSent();

    }

    /**
     * Interface implemented by EmergencyMessagePresenter class.
     */
    interface Presenter {

        /**
         * Error messages are hidden and the DataManager instance is gotten.
         */
        void start();

        /**
         * This method receives the content of the message and creates a message object which is
         * sent to DataManager whit the intention of sent it.
         *
         * @param contentMessage String contains the content of the message.
         */
        void sendMessage(String contentMessage);

        /**
         * This method receives the action when a checkbox is checked.
         * Base on the selection is created a user prefix using the NameModule.
         *
         * @param option Integer that represents the option selected.
         */
        void optionSelected(int option);
    }
}
