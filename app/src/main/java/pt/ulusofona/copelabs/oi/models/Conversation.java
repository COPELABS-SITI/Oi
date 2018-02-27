package pt.ulusofona.copelabs.oi.models;


/**
 * Conversation class is part of Oi! application. It provides support to manage information about
 * Conversations. The object conversation is definite as a combination of two contacts.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */

public class Conversation {
    /**
     * Contact information of localContact. LocalContact is the contact of the actual user.
     */
    private Contact mLocalContact;
    /**
     * Contact information of the contact which one the localContact
     * wants to establish the conversation.
     */
    private Contact mContact;

    /**
     * Conversation ID, is the combination of localContactId + contactId.
     */
    private String mID;

    /**
     * ConversationId back is the combination of contactId + localContactId.
     */
    private String mIDBack;

    /**
     * Conversation constructor.
     *
     * @param localContact LocalContact information.
     * @param contact      Contact information to be connected.
     */
    public Conversation(Contact localContact, Contact contact) {
        mLocalContact = localContact;
        mContact = contact;
        /*try {
            mID=Utils.hashMD5(contact.getID() + localContact.getID());
            mIDBack=Utils.hashMD5(contact.getID() + localContact.getID());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/
        mID = localContact.getID() + contact.getID();
        mIDBack = contact.getID() + localContact.getID();
    }

    /**
     * Get LocalContact information.
     *
     * @return LocalContact.
     */
    public Contact getLocalContact() {
        return mLocalContact;
    }

    /**
     * Get Contact information.
     *
     * @return Contact information.
     */
    public Contact getContact() {
        return mContact;
    }

    /**
     * Get conversation ID.
     *
     * @return String conversation ID.
     */
    public String getID() {
        return mID;
    }

    /**
     * Get Conversation ID back
     *
     * @return String conversation ID back.
     */
    public String getIDBack() {
        return mIDBack;
    }

}
