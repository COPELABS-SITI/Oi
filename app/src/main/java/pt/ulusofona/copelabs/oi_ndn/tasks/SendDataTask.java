package pt.ulusofona.copelabs.oi_ndn.tasks;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import net.named_data.jndn.Data;
import net.named_data.jndn.Face;

import java.io.IOException;

/**
 * This class is a AsyncTack which is used to send data though the face.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public class SendDataTask extends AsyncTask<Void, Void, Integer> {

    /**
     * String used for debug.
     */
    private static final String TAG = SendDataTask.class.getSimpleName();
    /**
     * NDN face object.
     */
    private Face mFace;
    /**
     * NDN data object.
     */
    private Data mData;
    /**
     * String id of the message that is going to be sent.
     */
    private String idMessage;
    /**
     * Integer which values determines if an error occur during the process.
     */
    private int mRetVal = 0;
    /**
     * DataSentInterface interface.
     */
    private DataSentInterface mInterface;

    /**
     * SendDataTask constructor.
     *
     * @param face              NDN face object.
     * @param data              NDN data object.
     * @param dataSentInterface DataSentInterface interface.
     * @param id                String id of the message to be sent.
     */
    public SendDataTask(Face face, Data data, DataSentInterface dataSentInterface, String id) {
        mFace = face;
        mData = data;
        mInterface = dataSentInterface;
        idMessage = id;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        Log.d(TAG, "Responding with Data [" + Base64.encodeToString(mData.getContent().getImmutableArray(), Base64.NO_PADDING) + "]");

        try {
            mFace.putData(mData);
        } catch (IOException e) {
            e.printStackTrace();
            //Error occurred.
            mRetVal = -1;
        }

        return mRetVal;

    }

    /**
     * After execute the task.
     *
     * @param result Integer which value indicates if the task was done successfully.
     */
    @Override
    protected void onPostExecute(Integer result) {
        if (mRetVal == -1) {
            Log.d(TAG, "Data sent fail");
            mInterface.dataSent(false, idMessage);
            //new SendDataTask(mFace,mData,mInterface,idMessage).execute();
        } else {
            Log.d(TAG, "Data sent");
            mInterface.dataSent(true, idMessage);
        }
    }

    /**
     * Send dat interface. This interface indicates when a data packet is sent.
     */
    public interface DataSentInterface {
        /**
         * @param isSent Boolean value, if it is true the data was sent, otherwise was not.
         * @param id     String id of the message.
         */
        void dataSent(boolean isSent, String id);
    }
}
