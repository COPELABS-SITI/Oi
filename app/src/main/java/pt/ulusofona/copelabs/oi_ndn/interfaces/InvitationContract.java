package pt.ulusofona.copelabs.oi_ndn.interfaces;

import pt.ulusofona.copelabs.oi_ndn.models.Contact;

/**
 * This class is part of Oi application.
 * It provides the interaction between the InvitationDialog class and the
 * InvitationPresenter class.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0,  02/14/18
 */

public interface InvitationContract {
    /**
     * Interface implemented by InvitationDialog class.
     */
    interface View{
        /**
         * Shows the contact requester.
         * @param name String name of the contact.
         */
        void showContactRequester(String name);

        /**
         * Shows a contact when the invitation is accepted.
         */
        void invitationAccept(Contact contact);

        /**
         * Cancels the invitation dialog.
         */
        void cancelDialog();
    }

    /**
     * Interface implemented by InvitationPresenter class.
     */
    interface Presenter{
        /**
         * Receives the actions when user accept or reject an invitation.
         * @param view View element pressed by the user.
         */
        void actionUser(android.view.View view);
    }
}
