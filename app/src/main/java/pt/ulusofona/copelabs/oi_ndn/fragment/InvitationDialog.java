package pt.ulusofona.copelabs.oi_ndn.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pt.ulusofona.copelabs.oi_ndn.helpers.Preferences;
import pt.ulusofona.copelabs.oi_ndn.interfaces.InvitationContract;
import pt.ulusofona.copelabs.oi_ndn.models.Contact;
import pt.ulusofona.copelabs.oi_ndn.presenters.InvitationPresenter;

/**
 * This class is a DialogFragment class which shows the invitation to establish a new conversation
 * this a contact requester.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */

public class InvitationDialog extends DialogFragment implements
        View.OnClickListener, InvitationContract.View {

    /**
     * Context of the application.
     */
    private Context mContext;

    /**
     * TextView used to display the name of the contact requester.
     */
    private TextView mContactName;

    /**
     * Presenter used to manages the interaction from te view.
     */
    private InvitationPresenter mPresenter;

    /**
     * Value use to define the argument related to the name of the contact requester.
     */
    private static final String ARGUMENT_CONTACT_NAME= "contactName";

    /**
     * Value use to define the argument related to the id of the contact requester.
     */
    private static final String ARGUMENT_CONTACT_ID = "contactId";

    private InvitationDialogInterface mInterface;

    /**
     * Create a new instance of MessageConfigDialogFragment, providing "num"
     * as an argument.
     */
    public static InvitationDialog newInstance(int num, String contactName, String contactId) {

        InvitationDialog f = new InvitationDialog();
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putString(ARGUMENT_CONTACT_NAME,contactName);
        args.putString(ARGUMENT_CONTACT_ID,contactId);
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
        View v = inflater.inflate(pt.ulusofona.copelabs.oi_ndn.R.layout.fragment_invitation, container, false);
        mContactName = v.findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.textViewContactName);
        Button btnAccept = v.findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.buttonAccept);
        btnAccept.setOnClickListener(this);
        Button btnReject = v.findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.buttonReject);
        btnReject.setOnClickListener(this);
        Contact contact = new Contact(getArguments().getString(ARGUMENT_CONTACT_ID),getArguments().getString(ARGUMENT_CONTACT_NAME));
        mPresenter = new InvitationPresenter(mContext,this,contact, Preferences.getLocalContact(getActivity()));
        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mContext = context;
            mInterface = (InvitationDialogInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    /**
     * Receives the action from the user.
     * @param view View pressed by the user.
     */
    @Override
    public void onClick(View view) {
        mPresenter.actionUser(view);
    }

    /**
     * Shows the contact requester.
     * @param name String name of the contact.
     */
    @Override
    public void showContactRequester(String name) {
        mContactName.setText(name);
    }

    /**
     * Shows a contact when the invitation is accepted.
     */
    @Override
    public void invitationAccept(Contact contact) {
        mInterface.OnInvitationAccepted(contact);
    }

    /**
     * Cancels the invitation dialog.
     */

    @Override
    public void cancelDialog() {
        getDialog().cancel();
    }

    /**
     * This interface notify when the option is set.
     */
    public interface InvitationDialogInterface {
        /**
         * Notifies when the user set the option. This method is called form the button function.
         *
         * @param contact option selected.
         */
        void OnInvitationAccepted(Contact contact);
    }

}
