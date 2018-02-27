package pt.ulusofona.copelabs.oi.presenters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import pt.ulusofona.copelabs.oi.helpers.DataManager;
import pt.ulusofona.copelabs.oi.helpers.NameModule;
import pt.ulusofona.copelabs.oi.helpers.Preferences;
import pt.ulusofona.copelabs.oi.interfaces.EmergencyMessageContract;
import pt.ulusofona.copelabs.oi.models.Message;

/**
 * This class is part of Oi application.
 * It receives the interactions from the EmergencyMessageActivity class and performs the actions
 * based on those interactions.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0,02/14/18
 */

public class EmergencyMessagePresenter implements EmergencyMessageContract.Presenter {

    /**
     * Variable used for Debug proposal.
     */
    private static final String TAG = EmergencyMessagePresenter.class.getSimpleName();
    /**
     * Default value of no selection.
     */
    private static final int NO_SELECTION = 0;
    /**
     * View interface implemented by the activity.
     */
    private EmergencyMessageContract.View mView;
    /**
     * Context of the activity.
     */
    private Context mContext;
    /**
     * This variable contains the initial values when no option is selected.
     */
    private int mOptionSelected = NO_SELECTION;

    /**
     * DataManager Object.
     */
    private DataManager mDataManager;

    /**
     * Activity variable used to take the information from the shared preferences.
     */
    private Activity mActivity;

    /**
     * Constructor of EmergencyMessagePresenter class.
     *
     * @param view     View interface implemented by the activity.
     * @param context  Context of the activity.
     * @param activity Activity variable.
     */
    public EmergencyMessagePresenter(EmergencyMessageContract.View view, Context context, Activity activity) {
        mView = view;
        mContext = context;
        mActivity = activity;
        start();
    }


    /**
     * Error messages are hidden and the DataManager instance is gotten.
     */
    @Override
    public void start() {
        mView.hideErrorEmptyMessage();
        mView.hideErrorMessageOptionSelected();
        mDataManager = DataManager.getInstance(mContext);
    }

    /**
     * This method receives the content of the message and creates a message object which is
     * sent to DataManager whit the intention of sent it.
     *
     * @param contentMessage String contains the content of the message.
     */
    @Override
    public void sendMessage(String contentMessage) {
        Log.d("selection", mOptionSelected + "");
        if (mOptionSelected != NO_SELECTION) {
            if (contentMessage.isEmpty()) {
                mView.showErrorEmptyMessage();
            } else {

                Message message = new Message();
                message.setContent(contentMessage);
                //The message's ID is the prefix created by the NameModule
                message.setID(NameModule.getEmergencyPrefix(mOptionSelected));
                Log.d(TAG, Preferences.getLocalContact(mActivity).getName());
                message.setUser(Preferences.getLocalContact(mActivity).getName());
                message.setFrom(Preferences.getLocalContact(mActivity).getID());
                message.setCreationTime();

                if(mOptionSelected==NameModule.PEOPLE_NEARBY_ID)
                    mDataManager.sendPushData(message);
                else
                    mDataManager.sendEmergencyData(message);


                mView.afterMessageSent();
            }
        } else {
            mView.showErrorMessageOptionNotSelected();
        }
    }

    /**
     * This method receives the action when a checkbox is checked.
     * Base on the selection is created a user prefix using the NameModule.
     *
     * @param option Integer that represents the option selected.
     */
    @Override
    public void optionSelected(int option) {
        switch (option) {
            case NameModule.FIREFIGHTER_ID:
                if (mOptionSelected != NameModule.FIREFIGHTER_ID) {
                    mView.hideErrorMessageOptionSelected();
                    mView.onFireFighterSelected();
                    mOptionSelected = NameModule.FIREFIGHTER_ID;
                } else {
                    mOptionSelected = NO_SELECTION;
                }
                break;

            case NameModule.POLICE_ID:
                if (mOptionSelected != NameModule.POLICE_ID) {
                    mView.hideErrorMessageOptionSelected();
                    mView.onPoliceSelected();
                    mOptionSelected = NameModule.POLICE_ID;
                } else {
                    mOptionSelected = NO_SELECTION;
                }
                break;

            case NameModule.SPECIAL_SERVICE_ID:
                if (mOptionSelected != NameModule.SPECIAL_SERVICE_ID) {
                    mView.hideErrorMessageOptionSelected();
                    mView.onEspecialServiceSelected();
                    mOptionSelected = NameModule.SPECIAL_SERVICE_ID;
                } else {
                    mOptionSelected = NO_SELECTION;
                }
                break;

            case NameModule.PEOPLE_NEARBY_ID:
                if (mOptionSelected != NameModule.PEOPLE_NEARBY_ID) {
                    mView.hideErrorMessageOptionSelected();
                    mView.onPeopleNearbySelected();
                    mOptionSelected = NameModule.PEOPLE_NEARBY_ID;
                } else {
                    mOptionSelected = NO_SELECTION;
                }
                break;

            case NameModule.RESCUE_TEAM_ID:
                if (mOptionSelected != NameModule.RESCUE_TEAM_ID) {
                    mView.hideErrorMessageOptionSelected();
                    mView.onRescueTeamSelected();
                    mOptionSelected = NameModule.RESCUE_TEAM_ID;
                } else {
                    mOptionSelected = NO_SELECTION;
                }

                break;

        }
    }
}
