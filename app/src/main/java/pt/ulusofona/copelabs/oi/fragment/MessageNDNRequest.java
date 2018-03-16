package pt.ulusofona.copelabs.oi.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import pt.ulusofona.copelabs.oi.R;
import pt.ulusofona.copelabs.oi.helpers.Preferences;

/**
 * This class is a DialogFragment class which shows the option of install NDN-Opp in the device.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */

public class MessageNDNRequest extends DialogFragment implements View.OnClickListener {

    private MessageNDNRequestInterface mInterface;

    /**
     * Create a new instance of MessageNDNRequest, providing "num"
     * as an argument.
     */
    public static MessageNDNRequest newInstance(int num) {

        MessageNDNRequest f = new MessageNDNRequest();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * In this method the view is setup.
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
        View v = inflater.inflate(R.layout.fragment_message_ndnrequest, container, false);
        Button button = v.findViewById(R.id.button);
        button.setOnClickListener(this);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mInterface = (MessageNDNRequestInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    /**
     * When user presses the "Accept" button, this method is call and the activity where is
     * implemented the function, is notified.
     * @param view
     */
    @Override
    public void onClick(View view) {
        mInterface.OnRequestAccepted();
        getDialog().cancel();
    }

    /**
     * This interface notify when the button is pressed.
     */
    public interface MessageNDNRequestInterface {
        /**
         * Notifies when the user press the button. This method is called form the button function.
         */
        void OnRequestAccepted();
    }

}
