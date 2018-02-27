package pt.ulusofona.copelabs.oi.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Message class is part of Oi! application. It provides support to manage information about Message.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 6/9/17
 */

public class Message {

    /**
     * Message sent by User.
     */
    public static final int MESSAGE_IS_MINE = 0;

    /**
     * Message does not send by the user.
     */
    public static final int MESSAGE_NOT_MINE = 1;

    /**
     * Message was sent.
     */
    public static final int MESSAGE_NO_SENT = 0;

    /**
     * Message was not sent.
     */
    public static final int MESSAGE_IS_SENT = 1;

    /**
     * User Name.
     */
    private String mUser;

    /**
     * Message content.
     */
    private String mContent;

    /**
     * Message interest.
     */
    private String mInterest;

    /**
     * Message creation date.
     */
    private String mDate;

    /**
     * Message ID.
     */
    private String mID;

    /**
     * Message create by the end user.(True).
     */
    private int mIsMine;

    /**
     * Message is sent.
     */
    private int isSent;

    /**
     * Message is delivered.
     */
    private boolean mIsDelivered;

    /**
     * Receiver id.
     */
    private String mTo;

    /**
     * Sender id.
     */
    private String mFrom;

    /**
     * Data format.
     */
    private SimpleDateFormat periodFormat = new SimpleDateFormat("HH:mm");

    /**
     * Message creation time.
     */
    private String mCreationTime;

    /**
     * Conversation id of the message.
     */
    private String mConversationId;

    /**
     * Constructor of Message class.
     *
     * @param mUser     User name
     * @param mMessage  Content of the message
     * @param mInterest Interest of the message
     */
    public Message(String mUser, String mMessage, String mInterest, String ID) {
        this.mUser = mUser;
        this.mContent = mMessage;
        this.mInterest = mInterest;
        this.mID = ID;
        isSent = 1;
    }

    /**
     * Constructor of Message class.
     *
     * @param from    Sender id.
     * @param to      Receiver id.
     * @param content Message content.
     */
    public Message(String from, String to, String content) {
        mFrom = from;
        mTo = to;
        mContent = content;
        mCreationTime = periodFormat.format(System.currentTimeMillis());
    }

    /**
     * Constructor of Message class.
     *
     * @param content Content of the message.
     * @param time    Time creatio of the message.
     */
    public Message(String content, String time) {
        mContent = content;
        mCreationTime = time;
    }

    /**
     * Constructor of Message class.
     */
    public Message() {
    }

    /**
     * Set time of creation by default.
     */
    public void setCreationTime() {
        mCreationTime = periodFormat.format(System.currentTimeMillis());
    }

    /**
     * Set user name.
     *
     * @param user User name.
     */
    public void setUser(String user) {
        mUser = user;
    }

    /**
     * Get creation time.
     *
     * @return Creation Time of the message.
     */
    public String getCreationTime() {
        return mCreationTime;
    }

    /**
     * Set creation time.
     *
     * @param creationTime String time of message creation.
     */
    public void setCreationTime(String creationTime) {
        mCreationTime = creationTime;
    }

    /**
     * Get sender ID.
     *
     * @return Sender ID of the message.
     */
    public String getFrom() {
        return mFrom;
    }

    /**
     * Set message sender ID.
     *
     * @param from Sender ID of the message.
     */
    public void setFrom(String from) {
        mFrom = from;
    }

    /**
     * Get receiver ID.
     *
     * @return Receiver ID of the message.
     */
    public String getTo() {
        return mTo;
    }

    /**
     * Get isSent status. 0 if the message was sent otherwise 1.
     *
     * @return Status of the message.
     */
    public int getIsSent() {
        return isSent;
    }

    /**
     * Set isSent status. 1 is the message was sent otherwise 0.
     *
     * @param condition Status of the message.
     */
    public void setIsSent(int condition) {
        isSent = condition;
    }

    /**
     * Get conversation ID.
     *
     * @return Conversation ID.
     */
    public String getConversationId() {
        return mConversationId;
    }

    /**
     * Set conversation id.
     *
     * @param conversationId Conversation Id.
     */
    public void setConversationId(String conversationId) {
        mConversationId = conversationId;
    }

    /**
     * Set delivered status message. True if the message was delivered otherwise false.
     *
     * @param isDelivered Boolean status.
     */
    public void setIsDelivered(boolean isDelivered) {
        mIsDelivered = isDelivered;
    }

    /**
     * Is delivered.
     *
     * @return Delivered status. If true message was delivered, otherwise it was not delivered.
     */
    public boolean isDelivered() {
        return mIsDelivered;
    }

    /**
     * Get Message ID.
     *
     * @return
     */
    public String getID() {
        return mID;
    }

    /**
     * Set message id.
     *
     * @param id Message id.
     */
    public void setID(String id) {
        mID = id;
    }

    /**
     * Get user name
     *
     * @return User name string
     */
    public String getmUser() {
        return mUser;
    }

    /**
     * Get Message content
     *
     * @return message content string
     */
    public String getContent() {
        return mContent;
    }

    /**
     * Set message content.
     *
     * @param content
     */
    public void setContent(String content) {
        mContent = content;
    }

    /**
     * Get interest of the message
     *
     * @return interest of the message string
     */
    public String getmInterest() {
        return mInterest;
    }

    /**
     * Get Date when was created the message
     *
     * @return date of the message string
     */
    public String getmDate() {
        return mDate;
    }

    /**
     * Get is mine status.
     *
     * @return mine status. If the message was created by the local user
     * this value has to be 0 otherwise 1.
     */
    public int getIsMine() {
        return mIsMine;
    }

    /**
     * Set the status isMine of the message. If the message was created by the local user
     * this value has to be 0 otherwise 1.
     *
     * @param isMine is mine status.
     */
    public void setIsMine(int isMine) {
        mIsMine = isMine;
    }

    /**
     * This method transforms a message object to a JSON object.
     *
     * @return JSON object whit message information.
     */
    public JSONObject getShortContent() {
        final JSONObject jObject = new JSONObject();  // JSON object to store toast_message
        try {
            jObject.put("content", mContent);
            jObject.put("time", mCreationTime);
            jObject.put("id", mID);
            jObject.put("c_i", mConversationId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jObject;
    }

    /**
     * This method transforms a message object to a JSON object.
     *
     * @return JSON object whit message information.
     */
    public JSONObject getEmergencyMessageJSONObject() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("content", mContent);
            jsonObject.put("time", mCreationTime);
            jsonObject.put("sender", mUser);
            jsonObject.put("id", mID);
            jsonObject.put("from", mFrom);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
