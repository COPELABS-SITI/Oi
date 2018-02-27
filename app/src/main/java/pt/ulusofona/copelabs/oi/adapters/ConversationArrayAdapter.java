package pt.ulusofona.copelabs.oi.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import pt.ulusofona.copelabs.oi.R;
import pt.ulusofona.copelabs.oi.models.Conversation;

import java.util.ArrayList;


/**
 * This class extends to ArrayAdapter and it receives the data collection whit conversation
 * information in order to bind with the cell views.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 6/9/17 3:05 PM
 */

public class ConversationArrayAdapter extends ArrayAdapter {

    /**
     * This variable saves the name of this class and is used for debug proposal.
     */
    private static final String TAG = ConversationArrayAdapter.class.getSimpleName();

    /**
     * ArrayList of conversations data collection.
     */
    private ArrayList<Conversation> mConversations;

    /**
     * Context of the application.
     */
    private Activity mContext;

    /**
     * Constructor of ConversationArrayAdapter
     *
     * @param context       Context of the application
     * @param conversations ArrayList of data collection
     */
    public ConversationArrayAdapter(Activity context, ArrayList<Conversation> conversations) {
        super(context, R.layout.conversation_2, conversations);
        mConversations = conversations;
        mContext = context;
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
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            if (position == 0)
                convertView = inflater.inflate(R.layout.conversation_1, null);
            else
                convertView = inflater.inflate(R.layout.conversation_2, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = convertView.findViewById(R.id.conversation_name);
            convertView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.text.setText(mConversations.get(position).getContact().getName());

        return (convertView);
    }

    /**
     * View holder pattern used to hold the data that conforms the information is the cell views.
     */
    private static class ViewHolder {
        /**
          * TextView that shows of the contact's user name of who the conversation is with.
         */
        TextView text;
    }


}
