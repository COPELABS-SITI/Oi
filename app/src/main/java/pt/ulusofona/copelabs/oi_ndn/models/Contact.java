package pt.ulusofona.copelabs.oi_ndn.models;

/**
 * Contact class is part of Oi! application. It provides support to manage information about Contacts.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */

public class Contact {

    /**
     * String contact ID, in this application this information is the phone number of the contact.
     */
    private String mID;

    /**
     * String Name of the contact.
     */
    private String mName;

    /**
     * Contact Constructor.
     *
     * @param ID   Contact identifier.
     * @param name Contact name.
     */
    public Contact(String ID, String name) {
        mID = ID;
        mName = name;
    }

    /**
     * Get the name of the contact.
     *
     * @return String contact name.
     */
    public String getName() {
        return mName;
    }

    /**
     * Get the  identifier of the contact.
     *
     * @return String contact identifier.
     */
    public String getID() {
        return mID;
    }

}
