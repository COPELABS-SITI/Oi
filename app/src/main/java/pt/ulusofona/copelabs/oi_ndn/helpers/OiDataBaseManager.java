package pt.ulusofona.copelabs.oi_ndn.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import pt.ulusofona.copelabs.oi_ndn.models.Contact;
import pt.ulusofona.copelabs.oi_ndn.models.Conversation;
import pt.ulusofona.copelabs.oi_ndn.models.Message;
import pt.ulusofona.copelabs.oi_ndn.utils.Utils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * This class is a SQLite manager class which manages the inserton, consults and updates of the
 * tables presents in the Oi! application. This class uses OiSQLiteHelper class in order
 * to execute all the action into the data base.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public class OiDataBaseManager {

    /**
     * Variable used for debug.
     */
    private static final String TAG = OiDataBaseManager.class.getSimpleName();

    /**
     * Instance of OIDataBaseManager.
     */
    private static OiDataBaseManager mInstance = null;

    /**
     * SQLIte data base variable.
     */
    private SQLiteDatabase mDb;

    /**
     * SQLite helper variable.
     */
    private OiSQLiteHelper mDbHelper;

    /**
     * Boolean used to know when the data base is open or not.
     */
    private boolean mIsDbOpen;

    /**
     * Context of the application.
     */
    private Context mContext;
    /**
     * This variable contains the fields of the Contacts table.
     */
    private String[] allColumnsContacts = {
            OiSQLiteHelper.COLUMN_CONTACTS_ID,
            OiSQLiteHelper.COLUMN_NAME,
            OiSQLiteHelper.COLUMN_LASTNAME
    };
    /**
     * This variable contains the fields of the message table.
     */
    private String[] messagesColumns = {
            OiSQLiteHelper.COLUMN_MESSAGE_ID,
            OiSQLiteHelper.COLUMN_CONTENT,
            OiSQLiteHelper.COLUMN_CONVERSATION_ID,
            OiSQLiteHelper.COLUMN_IS_MINE,
            OiSQLiteHelper.COLUMN_TIME,
            OiSQLiteHelper.COLUMN_STATUS_SENT
    };
    /**
     * This variable contains the fields of the Conversation table.
     */
    private String[] chatColumns = {
            OiSQLiteHelper.COLUMN_CONVERSATION_ID,
            OiSQLiteHelper.COLUMN_CONTACT_ID,
            OiSQLiteHelper.COLUMN_CONTACT_NAME,
            OiSQLiteHelper.COLUMN_CONVERSATION_ID_BACK,
            OiSQLiteHelper.COLUMN_LAST_INTEREST
    };

    /**
     * OiDataBaseManager constructor.
     *
     * @param context Context of the application.
     */
    private OiDataBaseManager(Context context) {
        mDbHelper = new OiSQLiteHelper(context);
        mContext = context;
    }

    /**
     * This method is used to get an instance of the OIDataBaseManager class.
     *
     * @param context Context of the application.
     * @return A instance of the class is going to be returned.
     */
    public static OiDataBaseManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new OiDataBaseManager(context);
        }
        return mInstance;
    }

    /**
     * Opens the data base if it is already open it will be close.
     *
     * @param writable
     */
    public void openDB(boolean writable) {
        if (!mIsDbOpen) {
            if (writable)
                mDb = mDbHelper.getWritableDatabase();
            else
                mDb = mDbHelper.getReadableDatabase();
        }
    }

    /**
     * Close the data base.
     */
    public void closeDB() {
        mDbHelper.close();
        mInstance = null;
        mIsDbOpen = false;
    }

    /**
     * This method transforms the data from a Cursor to a message object.
     *
     * @param cursor Cursor from the data base consult.
     * @return Message object.
     */
    private Message cursorToMessage(Cursor cursor) {
        Message message = new Message();
        message.setID(cursor.getString(0));
        message.setContent(cursor.getString(1));
        message.setIsMine(cursor.getInt(3));
        message.setCreationTime(cursor.getString(4));
        message.setIsSent(cursor.getInt(5));
        message.setConversationId(cursor.getString(2));
        return message;
    }

    /**
     * This method transforms the data from a Cursor to a conversation object.
     *
     * @param cursor Cursor from the data base consult.
     * @return Conversation object.
     */
    private Conversation cursorToContact(Cursor cursor, Contact localContact) {
        Contact contact = new Contact(cursor.getString(1), cursor.getString(2));
        return new Conversation(localContact, contact);
    }

    /**
     * Save a message information to de data base.
     *
     * @param message Message object.
     * @return long whit the information about the row which was added.
     */
    public synchronized long saveNewMessage(Message message) {
        ContentValues values = new ContentValues();
        values.put(OiSQLiteHelper.COLUMN_MESSAGE_ID, message.getID());
        values.put(OiSQLiteHelper.COLUMN_CONTENT, message.getContent());
        values.put(OiSQLiteHelper.COLUMN_CONVERSATION_ID, message.getConversationId());
        values.put(OiSQLiteHelper.COLUMN_IS_MINE, message.getIsMine());
        values.put(OiSQLiteHelper.COLUMN_TIME, message.getCreationTime());
        values.put(OiSQLiteHelper.COLUMN_STATUS_SENT, message.getIsSent());
        Log.d(TAG, "save message: " + message.getContent());
        return mDb.insert(OiSQLiteHelper.TABLE_MESSAGE, null, values);
    }


    /**
     * Upate message information into the data base.
     *
     * @param messageId  String with message id.
     * @param sentStatus Status of the message to be updated.
     */
    public synchronized void updateMessageSent(String messageId, int sentStatus) {
        ContentValues values = new ContentValues();
        values.put(OiSQLiteHelper.COLUMN_STATUS_SENT, sentStatus);
        mDb.update(OiSQLiteHelper.TABLE_MESSAGE, values, OiSQLiteHelper.COLUMN_MESSAGE_ID + "='" + messageId + "'", null);
    }

    /**
     * This method is used to retrieve all the messages saved in the data base.
     *
     * @param conversation Conversation identifier.
     * @return Array list with all messages data collection.
     */
    public ArrayList<Message> getAllMessage(Conversation conversation) {
        Log.d(TAG, "ConversationId: " + conversation.getID());
        ArrayList<Message> messageList = new ArrayList<>();
        Cursor cursor = mDb.query(OiSQLiteHelper.TABLE_MESSAGE, messagesColumns,
                OiSQLiteHelper.COLUMN_CONVERSATION_ID + "='" + conversation.getID()
                        + "'" + " OR " + OiSQLiteHelper.COLUMN_CONVERSATION_ID
                        + "='" + conversation.getIDBack() + "'",
                null, null, null, OiSQLiteHelper.COLUMN_TIME);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = cursorToMessage(cursor);
            Log.d(TAG, message.getID());
            messageList.add(message);
            cursor.moveToNext();
        }
        cursor.close();
        return messageList;
    }

    /**
     * Update a conversation with the information of the last prefix sent.
     *
     * @param conversationID String conversation identifier.
     * @param prefix         String prefix to be saved.
     */
    public void updateChat(String conversationID, String prefix) {
        Log.d(TAG, "update chat, conversatio: " + conversationID + " prefix: " + prefix);
        ContentValues values = new ContentValues();
        values.put(OiSQLiteHelper.COLUMN_LAST_INTEREST, prefix);
        mDb.update(OiSQLiteHelper.TABLE_CHAT, values, OiSQLiteHelper.COLUMN_CONVERSATION_ID_BACK + "='" + conversationID + "'", null);

    }

    /**
     * Save a  new conversation information to de data base.
     *
     * @param conversation Conversation object.
     * @return long whit the information about the row which was added.
     */
    public synchronized long saveNewChat(Conversation conversation) throws NoSuchAlgorithmException {
        ContentValues values = new ContentValues();
        values.put(OiSQLiteHelper.COLUMN_CONVERSATION_ID, conversation.getID());
        values.put(OiSQLiteHelper.COLUMN_CONTACT_ID, conversation.getContact().getID());
        values.put(OiSQLiteHelper.COLUMN_CONTACT_NAME, conversation.getContact().getName());
        values.put(OiSQLiteHelper.COLUMN_CONVERSATION_ID_BACK, Utils.hashMD5(conversation.getIDBack()));
        values.put(OiSQLiteHelper.COLUMN_LAST_INTEREST, "");
        return mDb.insert(OiSQLiteHelper.TABLE_CHAT, null, values);

    }

    /**
     * Used to get information form a specific message.
     *
     * @param messageId String message identifier.
     * @return Message object is returned.
     */
    public Message getMessage(String messageId) {
        Message message = null;
        Cursor cursor = mDb.query(OiSQLiteHelper.TABLE_MESSAGE, messagesColumns,
                OiSQLiteHelper.COLUMN_MESSAGE_ID + "='" + messageId + "'",
                null, null, null, null);
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            message = cursorToMessage(cursor);
            Log.d(TAG, message.getID());
            cursor.moveToNext();

        }

        cursor.close();
        return message;
    }

    /**
     * This method is used to retrieve all conversations saved in the data base.
     *
     * @param localContact Contact object with the information of the local contact.
     * @return Array list with all Conversation data collection.
     */
    public ArrayList<Conversation> getAllConversations(Contact localContact) {
        ArrayList<Conversation> conversationList = new ArrayList<>();
        Cursor cursor = mDb.query(OiSQLiteHelper.TABLE_CHAT, chatColumns, null,
                null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Conversation conversation = cursorToContact(cursor, localContact);

            conversationList.add(conversation);
            cursor.moveToNext();
        }

        cursor.close();
        return conversationList;
    }

    /**
     * This method is used to retrieve the next sequence of the message to be sent.
     *
     * @param conversationId String conversation identifier.
     * @return Integer value of the next sequence.
     */
    public int getNextSequence(String conversationId) {
        int sequence = 0;
        Cursor cursor = mDb.query(OiSQLiteHelper.TABLE_MESSAGE, messagesColumns,
                OiSQLiteHelper.COLUMN_CONVERSATION_ID + "='" + conversationId + "'",
                null, null, null, null);
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            sequence = Integer.parseInt(Utils.getLastSequence(cursor.getString(0)));
            sequence++;

        }
        Log.d(TAG, sequence + "");
        cursor.close();
        return sequence;
    }

    public int getNextSequenceInterest(String conversationId) {
        int sequence = 0;
        Cursor cursor = mDb.query(OiSQLiteHelper.TABLE_MESSAGE, messagesColumns,
                OiSQLiteHelper.COLUMN_CONVERSATION_ID + "='" + conversationId + "'",
                null, null, null, null);
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            sequence = Integer.parseInt(Utils.getLastSequence(cursor.getString(0)));
            sequence = sequence + 2;

        }
        Log.d(TAG, sequence + "");
        cursor.close();
        return sequence;
    }

    /**
     * This method is used to know the last interest expressed in a conversation.
     * @param conversationId  Conversation identifier.
     * @return String with the information about the last prefix.
     */
    public String getlastPrefix(String conversationId) {
        String prefix = null;
        Cursor cursor = mDb.query(OiSQLiteHelper.TABLE_CHAT, chatColumns,
                OiSQLiteHelper.COLUMN_CONVERSATION_ID + "='" + conversationId + "'",
                null, null, null, null);
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            prefix = cursor.getString(4);

        }
        Log.d(TAG, "Last prefix save: " + prefix + "");
        cursor.close();
        return prefix;
    }

    /**
     * This method is used to confirm that a specific message already exits.
     *
     * @param messageID String message identifier.
     * @return Boolean value is returned.
     */
    public boolean hasMessage(String messageID) {
        Log.d(TAG, "requesting message id: " + messageID);
        boolean exists;
        Cursor cursor = mDb.query(OiSQLiteHelper.TABLE_MESSAGE, messagesColumns,
                OiSQLiteHelper.COLUMN_MESSAGE_ID + "='" + messageID + "'",
                null, null, null, null);
        Log.d(TAG, "cursor size: " + cursor.getCount());
        if (cursor.getCount() > 0)
            exists = true;
        else
            exists = false;

        return exists;
    }

    /**
     * This method is used to confirm that a specific conversation already exits.
     *
     * @param conversationID String conversation identifier.
     * @return Boolean value is returned.
     */
    public boolean hasConversation(String conversationID) {
        Log.d(TAG, "requesting message id: " + conversationID);
        boolean exists;
        Cursor cursor = mDb.query(OiSQLiteHelper.TABLE_CHAT, chatColumns,
                OiSQLiteHelper.COLUMN_CONVERSATION_ID + "='" + conversationID + "'",
                null, null, null, null);
        Log.d(TAG, "cursor size: " + cursor.getCount());
        if (cursor.getCount() > 0)
            exists = true;
        else
            exists = false;

        return exists;
    }

    /**
     * This method is used to confirm that a specific prefix already exits in the conversation table.
     *
     * @param prefix String prefix value.
     * @return Boolean value is returned.
     */
    public boolean hasPrefix(String prefix) {
        Log.d(TAG, "requesting message id: " + prefix);
        boolean exists;
        Cursor cursor = mDb.query(OiSQLiteHelper.TABLE_CHAT, chatColumns,
                OiSQLiteHelper.COLUMN_LAST_INTEREST + "='" + prefix + "'",
                null, null, null, null);
        Log.d(TAG, "cursor size: " + cursor.getCount());
        if (cursor.getCount() > 0)
            exists = true;
        else
            exists = false;

        return exists;
    }
}
