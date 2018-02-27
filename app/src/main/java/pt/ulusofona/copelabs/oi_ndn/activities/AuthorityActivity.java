package pt.ulusofona.copelabs.oi_ndn.activities;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import pt.ulusofona.copelabs.oi_ndn.adapters.EmergencyMessageArrayAdapter;
import pt.ulusofona.copelabs.oi_ndn.interfaces.AuthorityContract;
import pt.ulusofona.copelabs.oi_ndn.fragment.MessageConfigDialogFragment;
import pt.ulusofona.copelabs.oi_ndn.models.Message;
import pt.ulusofona.copelabs.oi_ndn.presenters.AuthorityPresenter;

import java.util.ArrayList;

/**
 * This class is a AppCompactActivity class which shows the messages that come from the DataManager
 * module. From this activity users can select the option of receive information from "people nearby",
 * that means register a prefix for push communication.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public class AuthorityActivity extends AppCompatActivity implements
        MessageConfigDialogFragment.MessageConfigInterface, AuthorityContract.View {

    /**
     * This variable saves the name of this class and is used for debug proposal.
     */
    private static final String TAG = AuthorityActivity.class.getSimpleName();

    /**
     * This Array contains every message received.
     */
    private ArrayList<Message> mMessages = new ArrayList<>();

    /**
     * This variable represents the ArrayAdapter used to display the messages.
     */
    private EmergencyMessageArrayAdapter mEmergencyMessageArrayAdapter;

    /**
     * TextView that shows the type of the authority selected.
     */
    private TextView mTitleTxt;

    /**
     * AuthorityActivity's presenter.
     */
    private AuthorityPresenter mPresenter;


    /**
     * Performs the registration of the presenter in order to receive the messages from DataManager
     * module.
     */
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.registerAsListener();
        Log.e(TAG, "onResume");
    }

    /**
     * Performs the unregistration of the presenter from the DataManager interface.
     */
    @Override
    public void onPause() {
        mPresenter.unRegisterAsListener();
        super.onPause();
    }

    /**
     * In this method is performed the initial configuration of the activity. The visual components
     * are presented and the presenter is initialized.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(pt.ulusofona.copelabs.oi_ndn.R.layout.activity_authority);

        mEmergencyMessageArrayAdapter = new EmergencyMessageArrayAdapter(this, mMessages);

        ListView mListView = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.chat_list);
        mListView.setAdapter(mEmergencyMessageArrayAdapter);
        LayoutInflater layoutInflater = getLayoutInflater();
        ViewGroup headerListView = (ViewGroup) layoutInflater.inflate(pt.ulusofona.copelabs.oi_ndn.R.layout.header_emergency_list_view, mListView, false);
        mTitleTxt = headerListView.findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.textView);
        mListView.addHeaderView(headerListView);
        mPresenter = new AuthorityPresenter(this, this);
        mPresenter.start(getIntent().getIntExtra("AUTHORITY", 0));
    }

    /**
     * This function creates the menu of the activity.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(pt.ulusofona.copelabs.oi_ndn.R.menu.menu_authority_activity, menu);
        return true;
    }

    /**
     * This function receives the action from the menu items.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == pt.ulusofona.copelabs.oi_ndn.R.id.messageConfiguration) {
            mPresenter.changeMessageConfiguration();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();

    }

    /**
     * This function is performed once the user selects the option of receive push data.
     *
     * @param option Option selected for the user.
     */
    @Override
    public void OnMessageSet(int option) {
        if (option != MessageConfigDialogFragment.NO_SELECTION)
            mPresenter.registerPushPrefix();
    }

    /**
     * Shows the dialog where users can decide if want to receive push data or not.
     */
    @Override
    public void showConfigurationDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        android.app.Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
            ft.remove(prev);

        ft.addToBackStack(null);
        DialogFragment newFragment = MessageConfigDialogFragment.newInstance(0);
        newFragment.show(ft, "dialog");
    }

    /**
     * Shows the authority that was selected from the AuthoritySelectionActivity activity.
     *
     * @param authority Authority selected.
     */
    @Override
    public void showAuthoritySelected(String authority) {
        mTitleTxt.setText(authority);
    }

    /**
     * When a message is received, this function is called whit the proposal of add the new message
     * to the Array message.
     *
     * @param message Message received.
     */
    @Override
    public void showMessage(Message message) {
        mMessages.add(message);
    }

    /**
     * Updates the listView once a new message is added to the Array message.
     */
    @Override
    public void upDateListView() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEmergencyMessageArrayAdapter.notifyDataSetChanged();
            }
        });

    }
}
