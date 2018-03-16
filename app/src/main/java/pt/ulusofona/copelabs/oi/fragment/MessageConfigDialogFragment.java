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
 * This class is a DialogFragment class which shows the option to select if the user wants  to
 * receive message from people nearby. This means to activate Push Data mechanism.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */

public class MessageConfigDialogFragment extends DialogFragment implements CompoundButton.OnCheckedChangeListener {

    public static final int NO_SELECTION = 0;
    private CheckBox mCheckBoxPeopleNearby;
    private int mOptionSelected = NO_SELECTION;
    private Context mContext;
    private MessageConfigInterface mInterface;

    /**
     * Create a new instance of MessageConfigDialogFragment, providing "num"
     * as an argument.
     */
    public static MessageConfigDialogFragment newInstance(int num) {

        MessageConfigDialogFragment f = new MessageConfigDialogFragment();
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
        View v = inflater.inflate(R.layout.fragment_message_configuration, container, false);

        mCheckBoxPeopleNearby = v.findViewById(R.id.checkBoxPeopleNearby);

        //Check preferences.
        if (Preferences.getMessageConfig(getActivity()) == 0) {
            mCheckBoxPeopleNearby.setChecked(false);
        } else {
            mCheckBoxPeopleNearby.setChecked(true);
        }

        mCheckBoxPeopleNearby.setOnCheckedChangeListener(this);

        //Set preferences after user selection.
        Button button = v.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Preferences.setMessageConfiguration(getActivity(), mOptionSelected);
                mInterface.OnMessageSet(mOptionSelected);
                getDialog().cancel();
            }
        });

        return v;
    }

    /**
     * This method receives the actions when user selects options in the view.
     *
     * @param buttonView buttonView in the view.
     * @param isChecked  Boolean state of the view.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        boolean checked = buttonView.isChecked();
        switch (buttonView.getId()) {

            case R.id.checkBoxPeopleNearby:
                if (checked) {
                    mOptionSelected = Preferences.MESSAGE_PUSH_ENABLE;
                } else
                    mOptionSelected = Preferences.MESSAGE_PUSH_DISABLE;
                break;

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mContext = context;
            mInterface = (MessageConfigInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    /**
     * This interface notify when the option is set.
     */
    public interface MessageConfigInterface {
        /**
         * Notifies when the user set the option. This method is called form the button function.
         *
         * @param option option selected.
         */
        void OnMessageSet(int option);
    }

}
