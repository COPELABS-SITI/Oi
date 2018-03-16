package pt.ulusofona.copelabs.oi.helpers;

import android.content.Context;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import pt.ulusofona.copelabs.oi.models.Contact;
import pt.ulusofona.copelabs.oi.models.Message;
import pt.ulusofona.copelabs.oi.tasks.ExpressInterestTask;
import pt.ulusofona.copelabs.oi.tasks.JndnProcessEventTask;
import pt.ulusofona.copelabs.oi.tasks.RegisterPrefixForPushedDataTask;
import pt.ulusofona.copelabs.oi.tasks.RegisterPrefixTask;
import pt.ulusofona.copelabs.oi.tasks.SendDataTask;
import pt.ulusofona.copelabs.oi.utils.Utils;

import net.named_data.jndn.Data;
import net.named_data.jndn.Face;
import net.named_data.jndn.Interest;
import net.named_data.jndn.InterestFilter;
import net.named_data.jndn.Name;
import net.named_data.jndn.OnData;
import net.named_data.jndn.OnInterestCallback;
import net.named_data.jndn.OnPushedDataCallback;
import net.named_data.jndn.OnRegisterFailed;
import net.named_data.jndn.OnRegisterSuccess;
import net.named_data.jndn.OnTimeout;
import net.named_data.jndn.util.Blob;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is part of Oi application.
 * This class contains all methods to send NDN interest and data packets.
 * Every time that a component of the application wants to use NDN infrastructure
 * must used this class.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public class DataManager implements OnRegisterSuccess, OnRegisterFailed, OnInterestCallback, OnData
        , OnTimeout, OnPushedDataCallback, SendDataTask.DataSentInterface {

    /**
     * Variable used fo debug.
     */
    private static final String TAG = DataManager.class.getSimpleName();
    /**
     * Integer used to define the LifeTime of a typical interest NDN packet.
     */
    public static double INTEREST_LIFETIME = 300000;
    /**
     * Integer used to define the LifeTime of a Long Live Interest.
     */
    public static double LLI_LIFETIME = 500000;
    /**
     * Integer used to define the time to check events in the face.
     */
    private static int PROCESS_INTERVAL = 1000;
    /**
     * Variable used to save the instance of the DataManage object.
     */
    private static DataManager mInstance = null;
    /**
     * Value used to enable the synchronization of data packets.
     */
    public boolean SYNC = true;
    /**
     * Face object, used to connect with NDN-Opp.
     */
    private Face mFace = new Face("127.0.0.1");
    /**
     * OiDataBaseManager object used to save data in the internal database.
     */
    private OiDataBaseManager mDBMngr;
    /**
     * Context of the application.
     */
    private Context mContext;
    /**
     * HashMap used to save the interest that arrives to the application.
     */
    private HashMap<String, String> mInterests = new HashMap<>();

    /**
     * List of contacts saved in the device.
     */
    private ArrayList<Contact> mContacts;
    /**
     * Information about local contact.
     */
    private Contact mLocalContact;
    private Handler mHandler = new Handler();
    private Runnable mJndnProcessor = new Runnable() {
        @Override
        public void run() {
            new JndnProcessEventTask(mFace).execute();
            mHandler.postDelayed(mJndnProcessor, PROCESS_INTERVAL);
        }
    };

    /**
     * Data Manager constructor. In this method are performed the registration of the prefixes
     * related to the application.
     *
     * @param context Context of the application.
     */
    private DataManager(Context context) {
        mDBMngr = OiDataBaseManager.getInstance(context);
        mDBMngr.openDB(true);
        mContext = context;
        new RegisterPrefixTask(mFace, NameModule.PREFIX, this, this, mContext).execute();
        //new RegisterPrefixTask(mFace, NameModule.PREFIX, this, this, mContext).execute();
        mHandler.postDelayed(mJndnProcessor, PROCESS_INTERVAL);
    }

    public static DataManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataManager(context);
        }
        return mInstance;
    }

    public void setLocalContact(Contact contact) {
        mLocalContact = contact;
    }

    public void setContactList(ArrayList<Contact> contacts) {
        mContacts = contacts;
    }

    /**
     * This method is used to register prefixes of push data packets.
     */
    public void registerPushPrefix() {
        new RegisterPrefixForPushedDataTask(mFace, NameModule.EMERGENCY_PUSH, this, this, this).execute();
    }

    /**
     * This function is used to express LLI for All the authorities entities registered
     * in name module.
     */
    public void expressAllEmergencyLLI() {
        try {
            try {
            expressLLI(NameModule.EMERGENCY_PREFIX + NameModule.POLICE_NAME);
                Thread.sleep(500);
            expressLLI(NameModule.EMERGENCY_PREFIX + NameModule.FIREFIGHTER_NAME);
                Thread.sleep(500);
            expressLLI(NameModule.EMERGENCY_PREFIX + NameModule.SPECIAL_SERVICE_NAME);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            expressLLI(NameModule.EMERGENCY_PREFIX + NameModule.RESCUE_TEAM_NAME);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to send data mark as push data.
     *
     * @param message Message object to be sent.
     */
    public void sendPushData(Message message) {
        Name dName = null;
        //dName = new Name(NameModule.EMERGENCY_PREFIX + "/" + Utils.hashMD5(message.getContent()));
        dName = new Name(  NameModule.EMERGENCY_PUSH +"/"+ message.getContent());
        Data data = new Data(dName);
        data.setPushed(true);
        Blob blob = new Blob(message.getEmergencyMessageJSONObject().toString());
        Log.v(TAG, "Blob : " + message.getContent() + " > " + Base64.encodeToString(blob.getImmutableArray(), Base64.NO_PADDING));
        data.setContent(blob);
        new SendDataTask(mFace, data, this, message.getID()).execute();
    }

    public void sendPushData(String message) {
        Name dName = new Name(NameModule.PREFIX + "/" + "opp");
        Data data = new Data(dName);
        data.setPushed(true);
        Blob blob = new Blob();
        Log.v(TAG, "Blob : " + message + " > " + Base64.encodeToString(blob.getImmutableArray(), Base64.NO_PADDING));
        data.setContent(blob);
        new SendDataTask(mFace, data, this, "ui").execute();
    }

    /**
     * This method is used to send the data packet of a conversation.
     *
     * @param message  Message object to be sent.
     * @param sequence Sequence of the message to be sent.
     */
    public void sendData(Message message, int sequence) {
        Name dName = null;
        Log.d(TAG, "send data name: " + NameModule.PREFIX + "/" + message.getFrom().toString() + message.getTo().toString() + "/" + sequence);
        try {
            dName = new Name(NameModule.PREFIX + "/" + Utils.hashMD5(message.getFrom().toString() + message.getTo().toString()) + "/" + sequence);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Data data = new Data(dName);
        data.setPushed(false);
        message.setID(data.getName().toString());
        Blob blob = new Blob(message.getShortContent().toString());
        Log.d(TAG, "Data name " + data.getName().toString());
        Log.d(TAG, "Data content " + data.getContent().toString());
        Log.v(TAG, "Blob : " + message + " > " + Base64.encodeToString(blob.getImmutableArray(), Base64.NO_PADDING));
        data.setContent(blob);
        Log.d(TAG, "Data size: " + data.getContent().size());
        mDBMngr.saveNewMessage(message);
        if (SYNC) {
            if (mInterests.containsKey(data.getName().toString()))
                new SendDataTask(mFace, data, this, message.getID()).execute();
        }
    }

    /**
     * Used to send message in emergency mode.
     *
     * @param message Message object.
     */
    public void sendEmergencyData(Message message) {
        Name dName = new Name(message.getID());
        Data data = new Data(dName);
        data.setPushed(false);
        Blob blob = new Blob(message.getEmergencyMessageJSONObject().toString());
        Log.d(TAG, "Data name " + data.getName().toString());
        Log.d(TAG, "Data content " + data.getContent().toString());
        Log.v(TAG, "Blob : " + message + " > " + Base64.encodeToString(blob.getImmutableArray(), Base64.NO_PADDING));
        data.setContent(blob);
        new SendDataTask(mFace, data, this, message.getID()).execute();
    }

    /**
     * This method is used to send data when a interest arrives to the application.
     *
     * @param message Message object.
     */
    public void sendDataRequested(Message message) {
        Name dName = new Name(message.getID());
        Data data = new Data(dName);
        data.setPushed(false);
        Blob blob = new Blob(message.getShortContent().toString());
        Log.d(TAG, "Data name " + data.getName().toString());
        Log.d(TAG, "Data content " + data.getContent().toString());
        Log.v(TAG, "Blob : " + message + " > " + Base64.encodeToString(blob.getImmutableArray(), Base64.NO_PADDING));
        data.setContent(blob);
        new SendDataTask(mFace, data, this, message.getID()).execute();

    }

    /**
     * This method is used to express a Long Live Interest.
     *
     * @param prefix String prefix to be expressed.
     * @throws NoSuchAlgorithmException
     */
    public void expressLLI(String prefix) throws NoSuchAlgorithmException {
        Log.d(TAG, "express_Interest");
        Name name = new Name(prefix);
        Interest interest = new Interest(name, LLI_LIFETIME);

        interest.setLongLived(true);
        Log.d(TAG, "Expressing interest: " + interest.getName().toString());
        new ExpressInterestTask(mFace, interest, this, this).execute();
    }

    /**
     * This method is used to expressed NDN interest packets.
     *
     * @param prefix String whit the name of the interest to be sent.
     * @throws NoSuchAlgorithmException
     */
    public void expressInterest(String prefix) throws NoSuchAlgorithmException {
        Log.d(TAG, "expressInterest");
        Name name = new Name(NameModule.PREFIX + "/" + prefix);

        Interest interest = new Interest(name, INTEREST_LIFETIME);
        interest.setLongLived(false);

        Log.d(TAG, "Expressing interest: " + interest.getName().toString());
        mInterests.put(Utils.hashMD5(NameModule.PREFIX + "/" + prefix), NameModule.PREFIX + "/" + prefix);

        mDBMngr.updateChat(Utils.getConversationID(NameModule.PREFIX + "/" + prefix), NameModule.PREFIX + "/" + prefix);
        new ExpressInterestTask(mFace, interest, this, this).execute();
    }

    /**
     * This method is used to send interest packets after receives a data packet. This method
     * sends the interest of the next data packet.
     *
     * @param prefix String with the name of the interest to be sent.
     * @throws NoSuchAlgorithmException
     */
    public void sendLLI2(String prefix) throws NoSuchAlgorithmException {
        Log.d(TAG, "sendLLI2");
        Name name = new Name(prefix);

        Interest interest = new Interest(name, INTEREST_LIFETIME);
        interest.setLongLived(false);

        Log.d(TAG, "Expressing interest: " + interest.getName().toString());
        mInterests.put(Utils.hashMD5(prefix), prefix);
        mDBMngr.updateChat(Utils.getConversationID(prefix), prefix);
        new ExpressInterestTask(mFace, interest, this, this).execute();
    }

    /**
     * This method is called when a data packet arrives to the device
     *
     * @param interest Interest related to the data requested.
     * @param data     Data Packet data received.
     */
    @Override
    public void onData(Interest interest, Data data) {
        Log.d(TAG, "DATA receive");
        Log.d(TAG, "Received Data : " + data.getName().toString() + " > " + data.getContent().toString());


        if (SYNC) {
            if (data.getName().toString().contains("emergency")) {
                Log.d(TAG, "data emergency");
                DataManagerListenerManager.notifyDataEmergencyInComing(Utils.parseJsonToEmergencyMessage(data.getContent().toString()));

            } else {
                int num = Integer.parseInt(Utils.divide(data.getName().toString()));
                num++;
                if (mDBMngr.hasPrefix(Utils.reducePrefix(data.getName().toString()) + "/" + num)) {
                    Log.d(TAG, "Exists");
                } else {
                    try {
                        sendLLI2(Utils.reducePrefix(data.getName().toString()) + "/" + num);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
                mDBMngr.saveNewMessage(parseJsonToMessage(data.getContent().toString()));
                DataManagerListenerManager.notifyData(parseJsonToMessage(data.getContent().toString()));
                Utils.notifyNewMessage(mContext);
            }
        }
    }

    /**
     * This method is called when a interest arrives to the device.
     *
     * @param name           Name of the interest.
     * @param interest       Interest object arrived.
     * @param face           Face where the interest arrived.
     * @param l              Number identifies of the interest.
     * @param interestFilter InterestFilter.
     */
    @Override
    public void onInterest(Name name, Interest interest, Face face, long l, InterestFilter interestFilter) {
        Log.d(TAG, "onInterest");
        Log.v(TAG, "Received Interest : " + interest.getName() + " [" + interest.getInterestLifetimeMilliseconds() + "]");

        if (Utils.divide(interest.getName().toString()).equals("0")) {

            for (Contact contact : mContacts) {
                Log.d(TAG, "Contact name: " + contact.getName() + " contact id: " +contact.getID());

                try {
                    Log.d(TAG, "hash: " + Utils.hashMD5(contact.getID() + mLocalContact.getID()) + " name from interes: " + Utils.getConversationID(interest.getName().toString()));
                    if (Utils.hashMD5(mLocalContact.getID() + contact.getID()).equals(Utils.getConversationID(interest.getName().toString()))) {
                        if (!mDBMngr.hasConversation(mLocalContact.getID() + contact.getID())) {
                            DataManagerListenerManager.notifyInvitationListeners(contact);
                        }
                    }
                    } catch(NoSuchAlgorithmException e){
                        e.printStackTrace();
                    }

            }

        }
        if (SYNC) {
            mInterests.put(interest.getName().toString(), interest.getName().toString());

            Message message = mDBMngr.getMessage(interest.getName().toString());
            if (message != null)
                sendDataRequested(message);
        }

    }

    /**
     * This method receives a Data object when this data is marked as Push data.
     *
     * @param data Data object.
     */
    @Override
    public void onPushedData(Data data) {
        Log.d(TAG, "Push Data Received : " + data.getName().toString());
        Message message = Utils.parseJsonToEmergencyMessage(data.getContent().toString());
        Log.d(TAG, "Push Data Received : " + message.getContent());

    }

    /**
     * This method receives the information when a prefix is registered successfully.
     *
     * @param name Name of the prefix.
     * @param l    Identifier of the prefix registered.
     */
    @Override
    public void onRegisterSuccess(Name name, long l) {
        Log.d(TAG, "Registration Success : " + name.toString());
        Log.d(TAG, "Register ok");
    }

    /**
     * This method is call when the registration of a prefix failed.
     *
     * @param name Name of the prefix.
     */
    @Override
    public void onRegisterFailed(Name name) {
        Log.d(TAG, "Registration Failed : " + name.toString());
        Log.d(TAG, "Starting a new registration task");
        new RegisterPrefixForPushedDataTask(mFace, name.toString(), this, this, this).execute();
    }

    @Override
    public void dataSent(boolean isSent, String id) {
        Log.d(TAG, "Data sent");
        if (isSent)
            mDBMngr.updateMessageSent(id, Message.MESSAGE_IS_SENT);
        else
            mDBMngr.updateMessageSent(id, Message.MESSAGE_NO_SENT);

        DataManagerListenerManager.notifyDataSent(isSent, id);
    }

    /**
     * This method is called when a interest time expires, and is used to express again the
     * same interest if there is not a message whit that identifier.
     *
     * @param interest Interest object expired.
     */
    @Override
    public void onTimeout(Interest interest) {
        Log.d(TAG, "onTimeOut: " + interest.getName().toString());
        if (!mDBMngr.hasMessage(interest.getName().toString()))
            new ExpressInterestTask(mFace, interest, this, this).execute();
    }

    /**
     * This method transform a Json string to a message object. It is used when a new
     * data arrives to the devices.
     *
     * @param string String whit a structure like JSON object.
     * @return Message object.
     */

    public Message parseJsonToMessage(String string) {
        Message result = null;
        try {
            JSONObject jsonObject = new JSONObject(string);

            Message message = new Message(
                    jsonObject.getString("content"),
                    jsonObject.getString("time")
            );
            message.setConversationId(jsonObject.getString("c_i"));
            message.setID(jsonObject.getString("id"));
            message.setIsMine(Message.MESSAGE_NOT_MINE);
            result = message;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Interface used to notify when a new message arrived DataManager, when a message
     * was sent and when a message was received.
     */
    public interface DataManagerInterface {
        /**
         * Notifies when a new message arrive to DataManager.
         *
         * @param message message object received.
         */
        void dataInComing(Message message);

        /**
         * Notifies when a message was sent.
         *
         * @param isSent Boolean value true is was sent, otherwise, false.
         * @param id     string with the id of the message sent.
         */
        void dataSent(boolean isSent, String id);

        /**
         * Notifies when a message was delivered.
         * @param isReceived Boolean value tue if the mwssage was received, otherwise, false.
         * @param id         String id of the message received.
         */
        void dataReceived(boolean isReceived, String id);
    }

    /**
     * Interface used to notify when a push data packet arrives to the application.
     */
    public interface PushData {
        /**
         * Notifies a new push data packet.
         *
         * @param message Message object arrived.
         */
        void pushDataArrives(Message message);
    }

    /**
     * Interface used to notify when a Emergency message arrives to the application.
     */
    public interface DataManagerEmergencyInterface {
        /**
         * Notifies when a EmergencyMassage arrives to the application.
         *
         * @param message Message object arrived.
         */
        void dataInComing(Message message);
    }

    /**
     * Interface used to notify when a new invitation arrives to the application.
     */
    public interface Invitations {
        /**
         * Notifies a new invitation.
         *
         * @param contact Contact from where comes the invitation.
         */
        void InvitationInConing(Contact contact);
    }
}
