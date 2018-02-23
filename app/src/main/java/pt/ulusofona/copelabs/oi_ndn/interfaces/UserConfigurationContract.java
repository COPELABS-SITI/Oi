package pt.ulusofona.copelabs.oi_ndn.interfaces;

/**
 * This java interface class is part of Oi! application.
 * It provides a connection between the UserConfigurationPresenter class and the UserConfigurationActivity.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public interface UserConfigurationContract {

    interface View {

        /**
         * Displays the information of the user.
         *
         * @param name        String user name.
         * @param phoneNumber String phone number.
         */
        void showContactInformation(String name, String phoneNumber);

        /**
         * Shows the error message when the editText is empty.
         */
        void showUserNameErrorMessage();

        /**
         * Shows the error message when the editText is empty.
         */
        void showPhoneNumberErrorMessage();

        /**
         * Hides the error message when the editText is no empty.
         */
        void hideUserNameErrorMessage();

        /**
         * Hides the error message generated by User's Phone Number field empty.
         */
        void hidePhoneNumberErrorMessage();

        /**
         * Starts the EndUserActivity class.
         */
        void startEndUserActivity();

        /**
         * Disables the save button when there is a field empty.
         */
        void disableSaveButton();

        /**
         * Finishes the actual activity.
         */
        void finishActivity();

        /**
         * Enables the save button when the user introduce information in the empty field.
         */
        void enableSaveButton();

    }

    interface Presenter {

        /**
         * Start the inital configuration of the view.
         * Send contact information to the view and hide
         * error messages.
         */
        void start();

        /**
         * Uses the parameters sent by the view and save the information about de user in preferences.
         * if user's name or phone number field are empty an error message is shown.
         *
         * @param name        String user name.
         * @param phoneNumber String phone number.
         */
        void onFormFilled(String name, String phoneNumber);

        /**
         * Hides the error message generated by user name field empty. This action occur when hte user
         * introduces values in the user name field.
         *
         * @param length Integer length of the text in the editText.
         */
        void userNameEditTxtChanged(int length);

        /**
         * Hides the error message generated by user's phone number field empty. This action occur when
         * the user introduces values in the phone number field.
         *
         * @param length Integer length of the text in the editText.
         */
        void phoneNumberEditTxtChanged(int length);
    }
}