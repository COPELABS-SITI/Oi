package pt.ulusofona.copelabs.oi.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import pt.ulusofona.copelabs.oi.R;
import pt.ulusofona.copelabs.oi.models.Contact;

import java.util.ArrayList;


/**
 * This class extends to ArrayAdapter and it receives the data collection whit contacts information
 * in order to bind with the cell views.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 6/9/17 3:05 PM
 */

public class ContactsArrayAdapter extends ArrayAdapter {


    /**
     * This variable saves the name of this class and is used for debug proposal.
     */
    private String TAG = ContactsArrayAdapter.class.getSimpleName();

    /**
     * ArrayList of Contacts data collection.
     */
    private ArrayList<Contact> mContacts;

    /**
     * Context of the application.
     */
    private Activity mContext;

    /**
     * Constructor of ContactsArrayAdapter.
     *
     * @param context  Context of the application.
     * @param contacts ArrayList of data collection.
     */
    public ContactsArrayAdapter(Activity context, ArrayList<Contact> contacts) {
        super(context, R.layout.contact, contacts);
        mContacts = contacts;
        mContext = context;

        // TODO Auto-generated constructor stub
    }

    /**
     * This function is used to set the view of the cell in the listView. In this case is used
     * the holder pattern.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.contact, null);
            holder = new ViewHolder();
            holder.text = convertView.findViewById(R.id.contact_name);
            convertView.setTag(holder);
        } else {

        }

        holder = (ViewHolder) convertView.getTag();
        holder.text.setText(mContacts.get(position).getName());

        return (convertView);

    }

    /**
     * View holder pattern used to hold the data that conforms the information is the cell views.
     */
    private class ViewHolder {
        /**
         * TextView that shows of the contact's user name.
         */
        TextView text;
    }
}
