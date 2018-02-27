package pt.ulusofona.copelabs.oi.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class is a SQLite helper which manages the creation of the table in the data base and
 * provide information that is used in the OIDataBaseManager class in order to execute action into
 * the database.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public class OiSQLiteHelper extends SQLiteOpenHelper {

    /**
     * Name of the table contacts
     */
    public static final String TABLE_CONTACTS = "oiContacts";
    /**
     * Name of the table Message
     */
    public static final String TABLE_MESSAGE = "messages";
    /**
     * name of hte table Chat
     */
    public static final String TABLE_CHAT = "chats";
    // CONTACTS TABLE
    public static final String COLUMN_CONTACTS_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LASTNAME = "lastname";
    //MESSAGE TABLE
    public static final String COLUMN_MESSAGE_ID = "_id";
    public static final String COLUMN_SENDER_ID = "senderId";
    public static final String COLUMN_RECEIVER_ID = "receivrId";
    public static final String COLUMN_IS_MINE = "ismine";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_CONVERSATION_ID = "conversationid";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_STATUS_SENT = "sent";
    //CONVERSATION TABLE
    public static final String COLUMN_CONTACT_ID = "contactid";
    public static final String COLUMN_CONTACT_NAME = "contactname";
    public static final String COLUMN_CONVERSATION_ID_BACK = "idback";
    public static final String COLUMN_LAST_INTEREST = "lastinterest";
    /**
     * variable used for debug.
     */
    private static final String TAG = OiSQLiteHelper.class.getSimpleName();
    /**
     * DataBase name
     */
    private static final String DATABASE_NAME = "oi.db";
    /**
     * DataBase version
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * SQLite script to create the contact table
     */
    private static final String CREATE_CONTACTS_TABLE = "create table "
            + TABLE_CONTACTS
            + "("
            + COLUMN_CONTACTS_ID + "text no null, "
            + COLUMN_NAME + "text, "
            + COLUMN_LASTNAME + "text "
            + ");";

    /**
     * SQLite script to create the message table
     */
    private static final String CREATE_MESSAGE_TABLE = "create table "
            + TABLE_MESSAGE
            + "("
            + COLUMN_MESSAGE_ID + " text no null unique, "
            + COLUMN_CONTENT + " text, "
            + COLUMN_CONVERSATION_ID + " text, "
            + COLUMN_IS_MINE + " integer, "
            + COLUMN_TIME + " text, "
            + COLUMN_STATUS_SENT + " integer "
            + ");";

    /**
     * SQLite script to create the chat table
     */
    private static final String CREATE_CHAT_TABLE = "create table "
            + TABLE_CHAT
            + "("
            + COLUMN_CONVERSATION_ID + " text no null, "
            + COLUMN_CONTACT_ID + " text, "
            + COLUMN_CONTACT_NAME + " text, "
            + COLUMN_CONVERSATION_ID_BACK + " text, "
            + COLUMN_LAST_INTEREST + " text "
            + ");";

    /**
     * OiSQLiteHelper constructor.
     *
     * @param context Context of the application.
     */
    public OiSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Creates tables into the database.
     *
     * @param db data base object.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_MESSAGE_TABLE);
        db.execSQL(CREATE_CHAT_TABLE);
        Log.d(TAG, "DataBase was created");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        onCreate(db);
    }
}
