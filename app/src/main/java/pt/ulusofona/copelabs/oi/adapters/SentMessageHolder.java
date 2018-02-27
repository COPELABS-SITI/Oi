package pt.ulusofona.copelabs.oi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import pt.ulusofona.copelabs.oi.R;
import pt.ulusofona.copelabs.oi.models.Message;


/**
 * This class is a RecyclerView.ViewHolder used to hold the data information of sent messages.
 * This class is used in MessageListAdapter class.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public class SentMessageHolder extends RecyclerView.ViewHolder {
    /**
     * Variable use to show the content of the message.
     */
    private TextView mContent;
    /**
     * Used to show the status of the message.
     */
    private ImageView mStatusImg;
    /**
     * Used to show the time when the message was created.
     */
    private TextView mTime;

    /**
     * View holder constructor.
     *
     * @param itemView item og the view.
     */
    public SentMessageHolder(View itemView) {
        super(itemView);
        mContent = itemView.findViewById(R.id.textView);
        mStatusImg = itemView.findViewById(R.id.imageView2);
        mTime = itemView.findViewById(R.id.text_message_time);
    }

    /**
     * Binds the holder with the information of the message.
     *
     * @param message Sent message object.
     */
    public void bind(Message message) {
        mContent.setText(message.getContent());
        mTime.setText(message.getCreationTime());

        // Select the status image.
        if (message.getIsSent() == Message.MESSAGE_IS_SENT) {
            mStatusImg.setBackgroundResource(R.drawable.ic_check);
        } else {
            mStatusImg.setBackgroundResource(R.drawable.ic_sending);

        }
        if (message.isDelivered()) {
            mStatusImg.setBackgroundResource(R.drawable.ic_check_all);
        }
    }
}
