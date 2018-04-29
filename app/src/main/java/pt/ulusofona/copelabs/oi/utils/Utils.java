package pt.ulusofona.copelabs.oi.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import pt.ulusofona.copelabs.oi.models.Message;

import net.named_data.jndn.Name;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndn.security.identity.IdentityManager;
import net.named_data.jndn.security.identity.MemoryIdentityStorage;
import net.named_data.jndn.security.identity.MemoryPrivateKeyStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * This class contains function that are used during the execution of the application. Those
 * are auxiliaries functions.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 6/9/17
 */

public abstract class Utils {


    /**
     * Uses static final constants to detect if the device's platform version is Gingerbread or
     * later.
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * Uses static final constants to detect if the device's platform version is Honeycomb or
     * later.
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Uses static final constants to detect if the device's platform version is Honeycomb MR1 or
     * later.
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * Uses static final constants to detect if the device's platform version is ICS or
     * later.
     */
    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * Uses the hashMD5 to create the id of the conversations.
     *
     * @param text Text to be modified.
     * @return MD5 String.
     * @throws NoSuchAlgorithmException
     */
    public static String hashMD5(String text) throws NoSuchAlgorithmException {
        String hash = "";
        try {
            MessageDigest m = java.security.MessageDigest.getInstance("MD5");
            m.update(text.getBytes(), 0, text.length());
            hash = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    /**
     * This function resolve a string and takes the 5 element of that string based on the divider
     * "/".
     *
     * @param text Text that contains the divider "/".
     * @return The section number 5 of the string.
     */
    public static String divide(String text) {
        String string = text;
        String[] parts = string.split("/");
        //return parts[5];
        return parts[4];
    }

    /**
     * This function resolve a string and takes the 5 element of that string based on the divider
     * "/".
     *
     * @param text Text that contains the divider "/".
     * @return The section number 5 of the string.
     */
    public static String getLastSequence(String text) {
        String string = text;
        String[] parts = string.split("/");
        //return parts[5];
        return parts[4];
    }

    /**
     * This functions takes a string and delete the 5th element.
     *
     * @param text Text that contains the divider "/".
     * @return String without the 5th element.
     */
    public static String reducePrefix(String text) {
        String string = text;
        String[] parts = string.split("/");
        //return parts[0] + "/" + parts[1] + "/" + parts[2] + "/" + parts[3] + "/" + parts[4]; // 004
        return parts[0] + "/" + parts[1] + "/" + parts[2] + "/" + parts[3] ; // 004
    }

    /**
     * This function resolve a string and takes the 4th element of that string based on the divider
     * "/".
     *
     * @param text Text that contains the divider "/".
     * @return The section number 4th of the string.
     */
    public static String getConversationID(String text) {
        String string = text;
        String[] parts = string.split("/");
        //return parts[4];
        return parts[3];
    }

    /**
     * Shows a notification, this function is used when a new message is received.
     *
     * @param context Context of the application.
     */
    public static void notifyNewMessage(Context context) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(pt.ulusofona.copelabs.oi.R.drawable.ic_chat)
                        .setContentTitle("Oi")
                        .setContentText("New message");
        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    /**
     * Setup an in-memory KeyChain with a default identity.
     *
     * @return keyChain object
     * @throws net.named_data.jndn.security.SecurityException
     */
    public static KeyChain buildTestKeyChain() throws net.named_data.jndn.security.SecurityException {
        MemoryIdentityStorage identityStorage = new MemoryIdentityStorage();
        MemoryPrivateKeyStorage privateKeyStorage = new MemoryPrivateKeyStorage();
        IdentityManager identityManager = new IdentityManager(identityStorage, privateKeyStorage);
        KeyChain keyChain = new KeyChain(identityManager);
        try {
            keyChain.getDefaultCertificateName();
        } catch (net.named_data.jndn.security.SecurityException e) {
            keyChain.createIdentity(new Name("/test/identity"));
            keyChain.getIdentityManager().setDefaultIdentity(new Name("/test/identity"));
        }
        return keyChain;
    }

    /**
     * This function transform a JSON object to a Message object.
     *
     * @param string JSON String
     * @return Message
     */
    public static Message parseJsonToEmergencyMessage(String string) {
        Message result = null;
        try {
            JSONObject jsonObject = new JSONObject(string);

            Message message = new Message();
            message.setID(jsonObject.getString("id"));
            message.setContent(jsonObject.getString("content"));
            message.setCreationTime(jsonObject.getString("time"));
            message.setFrom(jsonObject.getString("from"));
            message.setUser(jsonObject.getString("sender"));
            message.setLatitude(jsonObject.getString("la"));
            message.setLongitud(jsonObject.getString("lo"));
            result = message;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This function is used to verify if a package is installed in the device.
     * @param packagename Name of the package.
     * @param packageManager Package manager.
     * @return Boolean value where true value means the package is installed, otherwise false.
     */
    public static boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
