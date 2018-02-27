package pt.ulusofona.copelabs.oi.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import pt.ulusofona.copelabs.oi.models.Contact;

/**
 * This class contains the information that is saved in the shared preferences of the application.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public abstract class Preferences {
    /**
     * Value when the push dat option is set.
     */
    public static final int MESSAGE_PUSH_ENABLE = 1;

    //Variables of local contact preferences.
    /**
     * Value when the push data option is no set.
     */
    public static final int MESSAGE_PUSH_DISABLE = 0;
    /**
     * Default value of the user configuration.
     */
    public static final int DEFAULT_VALUE_USER = 2;
    /**
     * Value of End User mode.
     */
    public static final int USER_END_USER = 0;
    /**
     * Value of the Authority mode.
     */
    public static final int USER_AUTHORITY = 1;
    /**
     * Variable used for debug.
     */
    private static final String TAG = Preferences.class.getSimpleName();

    //Variables of user configuration mode.

    /**
     * Default value of user name.
     */
    private static final String DEFAULT_VALUE_USER_NAME = null;
    /**
     * Default value of phone number of the user.
     */
    private static final String DEFAULT_VALUE_USER_PHONE = null;
    /**
     * Name of the preferences which saves the value of name user.
     */
    private static final String USER_NAME_PREFERENCES = "User_Name";
    /**
     * Name of the preferences which saves the value of phone number.
     */
    private static final String USER_PHONE_PREFERENCES = "Phone_Number";
    /**
     * Identifier of the preferences which saves the value of the local contact (End User).
     */
    private static final String PREFERENCES_ID = "localcontact";

    //Variables of message setting preferences.
    /**
     * Identifier of the preferences which save the value of the message configuration.
     */
    private static final String PREFERENCES_MESSAGE = "message";
    /**
     * Default value of message configuration.
     */
    public static final int DEFAULT_VALUE_MESSAGE_CONFIGURATION = 0;
    /**
     * Identifier of the message configuration preference.
     */
    private static final String MESSAGE_CONFIGURATION_PREFERENCE = "message_configuration";
    /**
     * Identifier of the preference which saves the information about the type of user.
     */
    private static final String PREFERENCE_USER = "user";
    /**
     * Identifier of the user mode preference.
     */
    private static final String PREFERENCE_USER_MODE = "usermode";

    /**
     * Get the information of the local contact.
     *
     * @param activity activity variable of the application.
     * @return Contact object whit hte information of the local contact.
     */
    public static Contact getLocalContact(Activity activity) {

        SharedPreferences sharedPref = activity.getSharedPreferences(PREFERENCES_ID, Context.MODE_PRIVATE);
        return new Contact(sharedPref.getString(USER_PHONE_PREFERENCES, DEFAULT_VALUE_USER_NAME),
                sharedPref.getString(USER_NAME_PREFERENCES, DEFAULT_VALUE_USER_PHONE));
    }

    /**
     * Save the information of the local contact.
     *
     * @param context Context of the application.
     * @param contact Contact whit information of the local contact.
     */
    public static void saveLocalContact(Activity context, Contact contact) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_NAME_PREFERENCES, contact.getName());
        editor.putString(USER_PHONE_PREFERENCES, contact.getID());
        editor.apply();

    }

    /**
     * Get message configuration related to receive or not push data messages. This action is
     * perform from the AuthorityActivity class.
     *
     * @param activity Activity of the application.
     * @return integer which specifies if the push data option is active or not.
     */
    public static int getMessageConfig(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFERENCES_MESSAGE, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(MESSAGE_CONFIGURATION_PREFERENCE, DEFAULT_VALUE_MESSAGE_CONFIGURATION);
    }

    /**
     * set messague configuration related to receive or not push data messages. This action is
     * perform from the AuthorityActivity class.
     *
     * @param activity      Activity of the application.
     * @param configuration integer which specifies if the push data option is active or not.
     */
    public static void setMessageConfiguration(Activity activity, int configuration) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFERENCES_MESSAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(MESSAGE_CONFIGURATION_PREFERENCE, configuration);
        editor.apply();
    }

    /**
     * This function get the information about the kind of user that is using the application.
     * End user or authority.
     *
     * @param activity Activity of the application.
     * @return integer which specifies if the user is a End user or an Authority.
     */
    public static int getUserConfiguration(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFERENCE_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PREFERENCE_USER_MODE, DEFAULT_VALUE_USER);
    }

    /**
     * This function set the information about the kind of user that is using the application.
     * End user or authority.
     *
     * @param activity          Activity of the application.
     * @param userConfiguration integer which specifies if the user is a End user or an Authority.
     */
    public static void setUserConfiguration(Activity activity, int userConfiguration) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFERENCE_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREFERENCE_USER_MODE, userConfiguration);
        editor.apply();
    }

}
