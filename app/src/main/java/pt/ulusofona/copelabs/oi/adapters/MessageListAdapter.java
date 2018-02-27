package pt.ulusofona.copelabs.oi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.ulusofona.copelabs.oi.models.Message;

import java.util.List;


/**
 * This class is a RecyclerView.Adapter used to adapt the view of the RecyclerView implemented in
 * the ConversationActivity class. This adapter manage the data of messages received and sent.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public class MessageListAdapter extends RecyclerView.Adapter {
    /**
     * Condition of sent message.
     */
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    /**
     * Condition of Received message.
     */
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    /**
     * List of message data collection.
     */
    private List<Message> mMessageList;

    /**
     * MessageListAdapter constructor.
     *
     * @param context     Context of the application.
     * @param messageList Data collection of messages.
     */
    public MessageListAdapter(Context context, List<Message> messageList) {
        mMessageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mMessageList.get(position);

        if (message.getIsMine() == Message.MESSAGE_IS_MINE) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(pt.ulusofona.copelabs.oi.R.layout.message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(pt.ulusofona.copelabs.oi.R.layout.message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = mMessageList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
