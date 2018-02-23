package pt.ulusofona.copelabs.oi_ndn.interfaces;

/**
 * This class is part of Oi application.
 * It provides the interaction between the AuthoritySelectionActivity class and the
 * AuthoritySelectionPresenter class.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 6/9/17 3:05 PM
 */

public interface AuthoritySelectionContract {

    /**
     * Interface implemented by AuthoritySelectionActivity class.
     */
    interface View {

        /**
         * Starts a new activity with the information about the authority selected.
         *
         * @param authority Integer which represented the authority selected.
         */
        void startAuthorityActivity(int authority);
    }

    /**
     * Interface implemented by AuthoritySelectionPresenter class.
     */
    interface Presenter {

        /**
         * This method receives the option of the authority selected and start a new activity
         * with that information.
         *
         * @param option Integer value which represents the authority selected.
         */
        void onAuthoritySelected(int option);
    }
}
