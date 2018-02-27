package pt.ulusofona.copelabs.oi.helpers;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import pt.ulusofona.copelabs.oi.models.Contact;
import pt.ulusofona.copelabs.oi.utils.Utils;

import java.util.ArrayList;

/**
 * This class is a Loader class, which implements LoaderManager interface in order to retrieve
 * information about the contacts that are saved in the device.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */

public class ContactsLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * String used for debug.
     */
    private static final String TAG = ContactsLoader.class.getSimpleName();
    /**
     * Context of the activity.
     */
    private Context mContext;
    /**
     * ArrayList contacts data collection.
     */
    private ArrayList<Contact> mContacts = new ArrayList<>();
    /**
     * ContactLoaderInterface implemented by the requester.
     */
    private ContactLoaderInterface mInterface;

    /**
     * ContactsLoader constructor.
     *
     * @param context                Context of the application.
     * @param contactLoaderInterface Interface implemented by the requester.
     * @param loaderManager          LoadManager from the activity.
     */
    public ContactsLoader(Context context, ContactLoaderInterface contactLoaderInterface, LoaderManager loaderManager) {
        mContext = context;
        mInterface = contactLoaderInterface;
        loaderManager.initLoader(ContactsQuery.QUERY_ID, null, this);
    }

    /**
     * Starts a new request.
     *
     * @param id
     * @param bundle
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (id == ContactsQuery.QUERY_ID) {
            Uri contentUri;
            contentUri = ContactsQuery.CONTENT_URI;
            return new CursorLoader(mContext, contentUri, ContactsQuery.PROJECTION, ContactsQuery.SELECTION, null, ContactsQuery.SORT_ORDER);
        }
        return null;
    }

    /**
     * After the loader finish its task this method shows the cursor whit the information.
     * From here is called the ContactLoaderInterface in order to notify the new array data
     * collection.
     *
     * @param loader Loader.
     * @param cursor Cursor whit data requested.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ContentResolver cr = mContext.getContentResolver();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        mContacts.add(new Contact(phoneNo, name));
                    }
                    pCur.close();
                }
            }
            cursor.close();
            mInterface.onContactLoaded(mContacts);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * This interface shows contact loaded from the device.
     */
    public interface ContactLoaderInterface {
        /**
         * This method shows the data collection of contacts saved in the device.
         *
         * @param arrayList ArrayList of contact data collection.
         */
        void onContactLoaded(ArrayList<Contact> arrayList);
    }

    /**
     * This interface is used to create a projection of the data that is requested to the loader.
     */
    public interface ContactsQuery {

        // An identifier for the loader
        int QUERY_ID = 1;

        // A content URI for the Contacts table
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

        // The selection clause for the CursorLoader query. The search criteria defined here
        // restrict results to contacts that have a display name and are linked to visible groups.
        // Notice that the search on the string provided by the user is implemented by appending
        // the search string to CONTENT_FILTER_URI.
        @SuppressLint("InlinedApi")
        String SELECTION =
                (Utils.hasHoneycomb() ? ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME) +
                        "<>''" + " AND " + ContactsContract.Contacts.IN_VISIBLE_GROUP + "=1";

        // The desired sort order for the returned Cursor. In Android 3.0 and later, the primary
        // sort key allows for localization. In earlier versions. use the display name as the sort
        // key.
        @SuppressLint("InlinedApi")
        String SORT_ORDER =
                Utils.hasHoneycomb() ? ContactsContract.Contacts.SORT_KEY_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;

        // The projection for the CursorLoader query. This is a list of columns that the Contacts
        // Provider should return in the Cursor.
        @SuppressLint("InlinedApi")
        String[] PROJECTION = {

                // The contact's row id
                ContactsContract.Contacts._ID,

                // A pointer to the contact that is guaranteed to be more permanent than _ID. Given
                // a contact's current _ID value and LOOKUP_KEY, the Contacts Provider can generate
                // a "permanent" contact URI.
                ContactsContract.Contacts.LOOKUP_KEY,

                // In platform version 3.0 and later, the Contacts table contains
                // DISPLAY_NAME_PRIMARY, which either contains the contact's displayable name or
                // some other useful identifier such as an email address. This column isn't
                // available in earlier versions of Android, so you must use Contacts.DISPLAY_NAME
                // instead.
                Utils.hasHoneycomb() ? ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME,

                // In Android 3.0 and later, the thumbnail image is pointed to by
                // PHOTO_THUMBNAIL_URI. In earlier versions, there is no direct pointer; instead,
                // you generate the pointer from the contact's ID value and constants defined in
                // android.provider.ContactsContract.Contacts.
                Utils.hasHoneycomb() ? ContactsContract.Contacts.PHOTO_THUMBNAIL_URI : ContactsContract.Contacts._ID,

                ContactsContract.Contacts.HAS_PHONE_NUMBER,

                // The sort order column for the returned Cursor, used by the AlphabetIndexer
                SORT_ORDER,
        };

    }
}
