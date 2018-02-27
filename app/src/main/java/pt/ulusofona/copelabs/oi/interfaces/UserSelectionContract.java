package pt.ulusofona.copelabs.oi.interfaces;

/**
 * This java interface class is part of Oi application.
 * It provides a connection between the UserSelectionPresenter class and the UserSelectionActivity.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */

public interface UserSelectionContract {

    /**
     * Interface implemented by UserSelectionActivity.
     */
    interface View {
        /**
         * Shows the initial message where users can read that in this version of Oi application
         * is possible just select one kind of user. End user or Authority user.
         */
        void showInitialMessage();

        /**
         * Starts the activity EndUserActivity.
         */
        void startEndUserActivity();

        /**
         * Starts the activity AuthoritySelectionActivity.
         */
        void startAuthorityActivity();

        /**
         * Shows the UserSelectionActivity.
         */
        void startDefaultActivity();

        /**
         * Starts the EndUserConfigurationActivity.
         */
        void startUserConfigurationActivity();
    }

    /**
     * Interface implemented by UserSelectionPresenter
     */
    interface Presenter {

        /**
         * When the presenter is initialized this method is called in order to check the preference
         * information and based on that, take the decision of which will be the next activity
         * to be initialized.
         */
        void start();

        /**
         * When the button is pressed the option selected is received in this method in order to
         * decide which will be next activity to be initialized.
         *
         * @param option Integer represents the user option selected.
         */
        void startActivitySelected(int option);
    }
}
