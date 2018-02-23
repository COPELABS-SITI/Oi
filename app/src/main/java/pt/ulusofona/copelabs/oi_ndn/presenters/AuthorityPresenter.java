package pt.ulusofona.copelabs.oi_ndn.presenters;

import android.content.Context;

import pt.ulusofona.copelabs.oi_ndn.helpers.DataManager;
import pt.ulusofona.copelabs.oi_ndn.helpers.DataManagerListenerManager;
import pt.ulusofona.copelabs.oi_ndn.helpers.NameModule;
import pt.ulusofona.copelabs.oi_ndn.interfaces.AuthorityContract;
import pt.ulusofona.copelabs.oi_ndn.models.Message;

import java.security.NoSuchAlgorithmException;

/**
 * This class is part of Oi application.
 * It receives the interactions from the AuthorityActivity class and perform the actions based on
 * those interactions.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */

public class AuthorityPresenter implements DataManager.DataManagerEmergencyInterface, AuthorityContract.Presenter {

    /**
     * View interface implemented by the activity.
     */
    private AuthorityContract.View mView;
    /**
     * Context of the application.
     */
    private Context mContext;
    /**
     * DataManager object.
     */
    private DataManager mDataMngr;
    /**
     * String with the prefix that are going to be expressed.
     */
    private String mPrefix;

    /**
     * Constructor of AuthorityPresenter class.
     *
     * @param view    View interface.
     * @param context Context of the application.
     */
    public AuthorityPresenter(AuthorityContract.View view, Context context) {
        mView = view;
        mContext = context;
        mDataMngr = DataManager.getInstance(context);
    }

    @Override
    public void dataInComing(Message message) {
        mView.showMessage(message);
        mView.upDateListView();
    }

    /**
     * This method is used to express a interest using DataManager object
     * once the activity starts. Also, this method set the name of the authority selected.
     *
     * @param authority Authority selected.
     */
    @Override
    public void start(int authority) {
        String title = null;
        switch (authority) {
            case NameModule.FIREFIGHTER_ID:
                title = mContext.getString(pt.ulusofona.copelabs.oi_ndn.R.string.firefighter);
                mPrefix = NameModule.getEmergencyPrefix(authority);
                break;
            case NameModule.POLICE_ID:
                title = mContext.getString(pt.ulusofona.copelabs.oi_ndn.R.string.police);
                mPrefix = NameModule.getEmergencyPrefix(authority);
                break;
            case NameModule.SPECIAL_SERVICE_ID:
                title = mContext.getString(pt.ulusofona.copelabs.oi_ndn.R.string.special_services);
                mPrefix = NameModule.getEmergencyPrefix(authority);
                break;
            case NameModule.RESCUE_TEAM_ID:
                title = mContext.getString(pt.ulusofona.copelabs.oi_ndn.R.string.rescue_team);
                mPrefix = NameModule.getEmergencyPrefix(authority);
                break;
        }
        mView.showAuthoritySelected(title);
        try {
            mDataMngr.expressLLI(mPrefix);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs the registration of the presenter in order to receive the messages from DataManager
     * module.
     */
    @Override
    public void registerAsListener() {
        DataManagerListenerManager.registerEmergencyListeners(this);
    }

    /**
     * Performs the unregistration of the presenter from the DataManager interface.
     */
    @Override
    public void unRegisterAsListener() {
        DataManagerListenerManager.unRegisterEmergencyListeners(this);
    }

    /**
     * Performs the configuration of the message.
     */
    @Override
    public void changeMessageConfiguration() {
        mView.showConfigurationDialog();
    }

    /**
     * This function is performed once the user selects the option of receive push data. From here
     * is used DataManager in order to execute this task.
     */
    @Override
    public void registerPushPrefix() {
        mDataMngr.registerPushPrefix();
    }

}
