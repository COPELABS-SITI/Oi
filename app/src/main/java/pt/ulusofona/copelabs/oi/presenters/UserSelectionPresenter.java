package pt.ulusofona.copelabs.oi.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import pt.ulusofona.copelabs.oi.interfaces.UserSelectionContract;
import pt.ulusofona.copelabs.oi.helpers.Preferences;
import pt.ulusofona.copelabs.oi.utils.Utils;

/**
 * This class is part of Oi application.
 * It receives the interactions from the UserSelectionActivity class and performs the actions based on
 * those interactions.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public class UserSelectionPresenter implements UserSelectionContract.Presenter {

    /**
     * Integer used to define if the next activity will be EndUserActivity class.
     */
    public final int START_END_USER_ACTIVITY = 0;
    /**
     * Integer used to define if the next activity will be AuthorityActivity class.
     */
    public final int START_AUTHORITY_ACTIVITY = 1;
    /**
     * View interface implemented by the activity.
     */
    private UserSelectionContract.View mView;
    /**
     * Activity from where is initialized the presenter.
     */
    private Activity mActivity;

    /**
     * Context of the application.
     */
    private Context mContext;

    /**
     * UserSelectionPresenter constructor.
     *
     * @param view     View interface implemented by the activity.
     * @param activity Activity from where is initialized the presenter.
     */
    public UserSelectionPresenter(UserSelectionContract.View view, Activity activity, Context context) {
        mView = view;
        mActivity = activity;
        mContext=context;
        start();


    }


    /**
     * When the presenter is initialized this method is called in order to check the preference
     * information and based on that, take the decision of which will be the next activity
     * to be initialized.
     */
    @Override
    public void start() {
        switch (Preferences.getUserConfiguration(mActivity)) {
            case Preferences.USER_END_USER:
                mView.startEndUserActivity();
                break;
            case Preferences.USER_AUTHORITY:
                mView.startAuthorityActivity();
                break;
            case Preferences.DEFAULT_VALUE_USER:
                mView.startDefaultActivity();
                checkNDNOpp();
                break;
        }
    }

    /**
     * When the button is pressed the option selected is received in this method in order to
     * decide which will be next activity to be initialized.
     *
     * @param option Integer represents the user option selected.
     */
    @Override
    public void startActivitySelected(int option) {
        PackageManager pm = mContext.getPackageManager();
        if(Utils.isPackageInstalled("pt.ulusofona.copelabs.ndn", pm)) {
            if (option == START_END_USER_ACTIVITY) {
                Preferences.setUserConfiguration(mActivity, Preferences.USER_END_USER);
                if (Preferences.getLocalContact(mActivity).getID() == null)
                    mView.startUserConfigurationActivity();
                else
                    mView.startEndUserActivity();
            } else
                mView.startAuthorityActivity();
        }else
            mView.startNDN();
    }

    /**
     * This function is used to check if NDN-Opp application is installed.
     */
    public void checkNDNOpp(){
        PackageManager pm = mContext.getPackageManager();
        boolean isInstalled = Utils.isPackageInstalled("pt.ulusofona.copelabs.ndn", pm);
        Log.d("Userselection", "application installed: " + isInstalled);
        if(isInstalled)
            mView.showInitialMessage();
        else
            mView.startNDN();
    }
}
