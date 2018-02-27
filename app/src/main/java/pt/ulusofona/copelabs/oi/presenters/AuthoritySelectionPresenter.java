package pt.ulusofona.copelabs.oi.presenters;

import pt.ulusofona.copelabs.oi.interfaces.AuthoritySelectionContract;

/**
 * This class is part of Oi application.
 * It receives the interactions from the AuthoritySelectionActivity class and performs the actions
 * based on those interactions.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0,02/14/18
 */

public class AuthoritySelectionPresenter implements AuthoritySelectionContract.Presenter {

    /**
     * Variable of the view interface.
     */
    private AuthoritySelectionContract.View mView;

    /**
     * Constructor of AuthoritySelectionPresenter class.
     *
     * @param view View interface.
     */
    public AuthoritySelectionPresenter(AuthoritySelectionContract.View view) {
        mView = view;
    }


    /**
     * This method receives the option of the authority selected and start a new activity
     * with that information.
     *
     * @param option Integer value which represents the authority selected.
     */
    @Override
    public void onAuthoritySelected(int option) {
        mView.startAuthorityActivity(option);
    }
}
