package pt.ulusofona.copelabs.oi.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import pt.ulusofona.copelabs.oi.utils.Utils;

import net.named_data.jndn.Face;
import net.named_data.jndn.Interest;
import net.named_data.jndn.Name;
import net.named_data.jndn.OnInterestCallback;
import net.named_data.jndn.OnRegisterFailed;
import net.named_data.jndn.OnRegisterSuccess;
import net.named_data.jndn.encoding.WireFormat;
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
public class RegisterPrefixTask extends AsyncTask<Void, Void, Integer> {


    /**
     * variable used for debug.
     */
    private static final String TAG = RegisterPrefixTask.class.getSimpleName();
    /**
     * NDN face object.
     */
    private Face mFace;
    /**
     * String prefix to be registered.
     */
    private String mPrefix;
    /**
     * OnRegisterSuccess interface.
     */
    private OnRegisterSuccess mOnRegistrationSuccess;

    /**
     * OnInterestCallBack interface.
     */
    private OnInterestCallback mOnInterestCallback;

    /**
     * Integer which values determines if an error occur during the process.
     */
    private int mRetVal = 0;

    /**
     * Context of the application.
     */
    private Context mContext;

    /**
     * Dialog used to notify users about that a background process is being performed.
     */
    private ProgressDialog dialog;

    /**
     * RegisterPrefixTask constructor.
     *
     * @param face   NDN Face object.
     * @param prefix String prefix to be registered.
     * @param ors    OnRegisterSuccess interface.
     * @param oic    OnInterestCallBack interface.
     * @param context Context of the application.
     */
    public RegisterPrefixTask(Face face, String prefix, OnRegisterSuccess ors, OnInterestCallback oic, Context context) {
        mFace = face;
        mPrefix = prefix;
        mOnRegistrationSuccess = ors;
        mOnInterestCallback = oic;
        mContext=context;
    }

    /**
     * Shows the progress dialog.
     */
    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(mContext);
        dialog.setMessage(mContext.getResources().getString(pt.ulusofona.copelabs.oi.R.string.progress_message));
        dialog.setIndeterminate(true);
        dialog.show();
        super.onPreExecute();
    }

    /**
     * Performs the registration of the prefixes.
     * @param params
     * @return Integer value, where 0 means that the operation was succeed, otherwise the value will be -1;
     */
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
            mFace.registerPrefix(new Name(mPrefix), mOnInterestCallback, new OnRegisterFailed() {
                @Override
                public void onRegisterFailed(Name prefix) {
                    Log.e(TAG, "Registration failed : " + prefix);
                }
            }, mOnRegistrationSuccess);
            Log.v(TAG, "Register prefix issued ...");
        } catch (SecurityException e) {
            //Error occurred.
            e.printStackTrace();
        } catch (IOException e) {
            //Error occurred.
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
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (mRetVal == -1) {
            Log.d(TAG, "Error Register Prefix Task");
        } else {
            Log.d(TAG, "Register Prefix Task ended (onPostExecute)");
        }
    }

}
