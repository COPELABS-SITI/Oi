package pt.ulusofona.copelabs.oi_ndn.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import pt.ulusofona.copelabs.oi_ndn.R;
import pt.ulusofona.copelabs.oi_ndn.models.Message;

import java.util.ArrayList;

/**
 * This class extends to ArrayAdapter and it receives the data collection whit message
 * information in order to bind with the cell views.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 6/9/17 3:05 PM
 */

public class EmergencyMessageArrayAdapter extends ArrayAdapter {

    /**
     * This variable saves the name of this class and is used for debug proposal.
     */
    private String TAG = ContactsArrayAdapter.class.getSimpleName();
    /**
     * ArrayList of messages data collection.
     */
    private ArrayList<Message> mMessages;
    /**
     * Context of the application.
     */
    private Activity mContext;

    /**
     * Constructor of EmergencyMessage ArrayAdapter
     *
     * @param context  Context of the application.
     * @param messages ArrayList of messages data collection.
     */
    public EmergencyMessageArrayAdapter(Activity context, ArrayList<Message> messages) {
        super(context, R.layout.contact, messages);
        mMessages = messages;
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

        EmergencyMessageArrayAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.emergency_message_view, null);
            holder = new EmergencyMessageArrayAdapter.ViewHolder();
            holder.textViewContent = convertView.findViewById(R.id.messageContent);
            holder.textViewTime = convertView.findViewById(R.id.textViewTime);
            holder.textViewName = convertView.findViewById(R.id.textViewName);
            holder.textViewPhone = convertView.findViewById(R.id.textViewPhone);
            convertView.setTag(holder);
        } else {

        }

        holder = (EmergencyMessageArrayAdapter.ViewHolder) convertView.getTag();
        holder.textViewContent.setText(mMessages.get(position).getContent());
        holder.textViewTime.setText(mMessages.get(position).getCreationTime());
        holder.textViewName.setText(mMessages.get(position).getmUser());
        holder.textViewPhone.setText(mMessages.get(position).getFrom());

        return (convertView);

    }

    /**
     * View holder pattern used to hold the data that conforms the information is the cell views.
     */
    private class ViewHolder {
        /**
         * TextView that shows the content of the message.
         */
        TextView textViewContent;

        /**
         * TextView that shows the time when was created the message.
         */
        TextView textViewTime;

        /**
         * TextView that shows the name of who created the message.
         */
        TextView textViewName;

        /**
         * TextView that shows the phone number of who created the message.
         */
        TextView textViewPhone;
    }
}
