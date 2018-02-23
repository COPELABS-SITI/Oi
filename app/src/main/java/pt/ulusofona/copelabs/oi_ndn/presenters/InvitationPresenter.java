package pt.ulusofona.copelabs.oi_ndn.presenters;

import android.content.Context;
import android.view.View;

import java.security.NoSuchAlgorithmException;

import pt.ulusofona.copelabs.oi_ndn.helpers.DataManager;
import pt.ulusofona.copelabs.oi_ndn.helpers.OiDataBaseManager;
import pt.ulusofona.copelabs.oi_ndn.interfaces.InvitationContract;
import pt.ulusofona.copelabs.oi_ndn.models.Contact;
import pt.ulusofona.copelabs.oi_ndn.models.Conversation;
import pt.ulusofona.copelabs.oi_ndn.utils.Utils;

 /**
  * This class is part of Oi application.
  * It receives the interactions from the InvitationDialog fragment class and perform the actions based on
  * those interactions.
  *
  * @author Omar Aponte (COPELABS/ULHT)
  * @version 1.0
  *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
  */

public class InvitationPresenter implements InvitationContract.Presenter {

     /**
      * Variable used for debug.
      */
    private String TAG = InvitationPresenter.class.getSimpleName();
     /**
      * Context of the application.
      */
    private Context mContext;
     /**
      * View implemented by the fragment.
      */
    private InvitationContract.View mView;
     /**
      * Contact who sends the invitation.
      */
    private Contact mContactRequester;
     /**
      * Contact local information.
      */
    private Contact mLocalContact;
     /**
      * DataManager used to express interest.
      */
    private DataManager mDataManager;
     /**
      * DataBaseManager used to save
      */
    private OiDataBaseManager mOiDataBaseManager;

     /**
      * Constructor of InvitationPresenter class.
      * @param context Context of the application.
      * @param view View which implements the interface.
      * @param contactRequester Contact who sends the invitation.
      * @param localContact Local contact information.
      */

    public InvitationPresenter(Context context, InvitationContract.View view, Contact contactRequester, Contact localContact) {
        mContext = context;
        mView = view;
        mView.showContactRequester(contactRequester.getName() + " " + mContext.getResources().getString(pt.ulusofona.copelabs.oi_ndn.R.string.new_invitation));
        mContactRequester = contactRequester;
        mLocalContact = localContact;

        mDataManager = DataManager.getInstance(context);
        mOiDataBaseManager = OiDataBaseManager.getInstance(context);
    }

     /**
      * Receives the actions when user accept or reject an invitation.
      * @param view View element pressed by the user.
      */
    @Override
    public void actionUser(View view) {
        switch (view.getId()) {
            case pt.ulusofona.copelabs.oi_ndn.R.id.buttonAccept:
                Conversation conversation_ = new Conversation(mLocalContact, mContactRequester);

                try {
                    mOiDataBaseManager.saveNewChat(conversation_);
                    mDataManager.expressInterest(Utils.hashMD5(mContactRequester.getID() + mLocalContact.getID()) + "/0");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                mView.invitationAccept(mContactRequester);
                mView.cancelDialog();
                break;
            case pt.ulusofona.copelabs.oi_ndn.R.id.buttonReject:
                mView.cancelDialog();
                break;
        }

    }
}
