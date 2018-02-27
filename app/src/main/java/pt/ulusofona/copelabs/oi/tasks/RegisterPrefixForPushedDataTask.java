package pt.ulusofona.copelabs.oi.tasks;

import android.os.AsyncTask;
import android.util.Log;


import pt.ulusofona.copelabs.oi.utils.Utils;

import net.named_data.jndn.Face;
import net.named_data.jndn.Name;
import net.named_data.jndn.OnPushedDataCallback;
import net.named_data.jndn.OnRegisterFailed;
import net.named_data.jndn.OnRegisterSuccess;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndn.security.SecurityException;

import java.io.IOException;

/**
 * This class is a AsyncTack which is used to register a prefix into the Face.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public class RegisterPrefixForPushedDataTask extends AsyncTask<Void, Void, Integer> {

    /**
     * Variable used for debug.
     */
    private static final String TAG = RegisterPrefixForPushedDataTask.class.getSimpleName();

    /**
     * NDN Face object.
     */
    private Face mFace;
    /**
     * Strng whit the prefix that is going to be registered.
     */
    private String mPrefix;

    /**
     * Interface of OnPushedData.
     */
    private OnPushedDataCallback mOnPushedData;

    /**
     * Interface of OnRegistrationSuccess.
     */
    private OnRegisterSuccess mOnRegistrationSuccess;

    /**
     * Interface of OnRegistrationFailed.
     */
    private OnRegisterFailed mOnRegistrationFailed;

    /**
     * Integer which values determines if an error occur during the process.
     */
    private int mRetVal = 0;

    /**
     * RegisterPrefixForPushedDataTask constructor.
     * @param face NDN Face object.
     * @param prefix String prefix to be registered.
     * @param opdc Interface of OnPushedData.
     * @param ors Interface of OnRegistrationSuccess.
     * @param orf Interface of OnRegistrationFailed.
     */
    public RegisterPrefixForPushedDataTask(Face face, String prefix, OnPushedDataCallback opdc, OnRegisterSuccess ors, OnRegisterFailed orf) {
        mFace = face;
        mPrefix = prefix;
        mOnPushedData = opdc;
        mOnRegistrationSuccess = ors;
        mOnRegistrationFailed=orf;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        Log.d(TAG, "Register Prefix Task (doInBackground)");

        // Create keychain
        KeyChain keyChain;
        try {
            keyChain = Utils.buildTestKeyChain();
        } catch (SecurityException e) {
            e.printStackTrace();
            //Error occurred.
            mRetVal = -1;
            return mRetVal;
        }

        // Register keychain with the face
        keyChain.setFace(mFace);
        try {
            mFace.setCommandSigningInfo(keyChain, keyChain.getDefaultCertificateName());
        } catch (SecurityException e) {
            e.printStackTrace();
            //Error occurred.
            mRetVal = -1;
            return mRetVal;
        }

        try {
            Log.v(TAG, "Register prefix ...");
            mFace.registerPrefix(new Name(mPrefix), mOnPushedData, mOnRegistrationSuccess, mOnRegistrationFailed);
            Log.v(TAG, "Register prefix issued ...");
        } catch (SecurityException e) {
            //Error occurred.
            mRetVal = -1;
            e.printStackTrace();
        } catch (IOException e) {
            //Error occurred.
            mRetVal = -1;
            e.printStackTrace();
        }

        return mRetVal;

    }

    /**
     * After execute the task.
     *
     * @param result Integer which value indicates if the task was done successfully.
     */
    @Override
    protected void onPostExecute(final Integer result) {
        if (mRetVal == -1) {
            Log.d(TAG, "Error Register Prefix Task");

        } else {
            Log.d(TAG, "Register Prefix Task ended (onPostExecute)");
        }
    }
}
