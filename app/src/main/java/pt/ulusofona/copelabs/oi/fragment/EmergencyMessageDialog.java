package pt.ulusofona.copelabs.oi.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import pt.ulusofona.copelabs.oi.R;

/**
 * This class is a DialogFragment class which shows the option to select if the user wants  to
 * receive message from people nearby. This means to activate Push Data mechanism.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */

public class EmergencyMessageDialog extends DialogFragment {

    private Context mContext;

    private static final String ARGUMENT_CONTACT_NAME= "contactName";

    private static final String ARGUMENT_CONTACT_PHONE_NUMBER= "phoneNumber";

    private static final String ARGUMENT_MESSAGE_CONTENT = "messageContent";

    private static final String ARGUMENT_DATE = "date";


    /**
     * Create a new instance of MessageConfigDialogFragment, providing "num"
     * as an argument.
     */
    public static EmergencyMessageDialog newInstance(int num, String contactName, String phoneNumber, String contentMessage, String date) {

        EmergencyMessageDialog f = new EmergencyMessageDialog();
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putString(ARGUMENT_CONTACT_NAME,contactName);
        args.putString(ARGUMENT_CONTACT_PHONE_NUMBER,phoneNumber);
        args.putString(ARGUMENT_MESSAGE_CONTENT,contentMessage);
        args.putString(ARGUMENT_DATE,date);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * In this method the view is setup. In this case we use SharePreferences in order to save the
     * status of the option selected.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.fragment_push_data, container, false);
        TextView txtContactName = v.findViewById(R.id.textViewContactName);
        TextView txtPhoneNumber = v.findViewById(R.id.textViewPhoneNumber);
        TextView txtMessageContent = v.findViewById(R.id.textViewMessageContent);
        TextView txtDate = v.findViewById(R.id.textViewDate);

        txtContactName.setText(getArguments().getString(ARGUMENT_CONTACT_NAME));
        txtPhoneNumber.setText(getArguments().getString(ARGUMENT_CONTACT_PHONE_NUMBER));
        txtMessageContent.setText(getArguments().getString(ARGUMENT_MESSAGE_CONTENT));
        txtDate.setText(getArguments().getString(ARGUMENT_DATE));

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mContext = context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
