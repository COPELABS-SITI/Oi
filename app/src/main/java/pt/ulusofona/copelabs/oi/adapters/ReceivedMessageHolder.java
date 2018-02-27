package pt.ulusofona.copelabs.oi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import pt.ulusofona.copelabs.oi.models.Message;

/**
 * This class is a RecyclerView.ViewHolder used to hold the data information of received messages.
 * This class is used in MessageListAdapter class.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */

public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
    /**
     * Used to show the content of the message.
     */
    private TextView mContent;
    /**
     * Used to show the time creation of the message.
     */
    private TextView mTime;

    /**
     * Constructor of the ReceivedMessageHolder class.
     *
     * @param itemView item of the view.
     */
    public ReceivedMessageHolder(View itemView) {
        super(itemView);
        mContent = (TextView) itemView.findViewById(pt.ulusofona.copelabs.oi.R.id.textView);
        mTime = itemView.findViewById(pt.ulusofona.copelabs.oi.R.id.text_message_time);
    }

    /**
     * Binds the holder with the information of the message.
     *
     * @param message Received message object.
     */
    public void bind(Message message) {
        mContent.setText(message.getContent());
        mTime.setText(message.getCreationTime());
    }
}
