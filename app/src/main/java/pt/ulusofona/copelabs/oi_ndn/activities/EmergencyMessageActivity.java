package pt.ulusofona.copelabs.oi_ndn.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import pt.ulusofona.copelabs.oi_ndn.helpers.NameModule;
import pt.ulusofona.copelabs.oi_ndn.interfaces.EmergencyMessageContract;
import pt.ulusofona.copelabs.oi_ndn.presenters.EmergencyMessagePresenter;

/**
 * This class is part of Oi! application.
 * This class is a AppCompactActivity class which allows users create emergency messages.
 * In order to do that, it is possible to select an authority an write a message.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18 3:05 PM
 */
public class EmergencyMessageActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener, EmergencyMessageContract.View {

    /**
     * EditText that contain the content of the message.
     */
    private EditText mMessageText;
    /**
     * Checkbox for Firefighter authority.
     */

    private CheckBox mCheckBoxFirefighter;

    /**
     * Checkbox for Police authority.
     */
    private CheckBox mCheckBoxPolice;

    /**
     * Checkbox for Special Services authority.
     */
    private CheckBox mCheckBoxSpecialService;

    /**
     * Checkbox for People Nearby.
     */
    private CheckBox mCheckBoxPeopleNearby;

    /**
     * Checkbox for RescueTeam authority.
     */
    private CheckBox mCheckBoxRescueTeam;

    /**
     * TextView of no option selected.
     */
    private TextView mErrorNoOptionSelected;

    /**
     * TextView of empty message.
     */
    private TextView mErrorEmptyMessage;

    /**
     * Presenter of the EmergencyMessageActivity.
     */
    private EmergencyMessagePresenter mPresenter;

    /**
     * In this method initial setup of the view is preformed and
     * the presenter is initialized.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(pt.ulusofona.copelabs.oi_ndn.R.layout.activity_emergency_message);

        mErrorEmptyMessage = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.textViewErrorEmtyMessage);
        mErrorNoOptionSelected = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.textViewErrorOptionSelected);

        mMessageText = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.editTextUserName);

        mMessageText.setSelection(mMessageText.getText().length());

        mCheckBoxFirefighter = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.checkBoxFirefighter);
        mCheckBoxPolice = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.checkBoxPolice);
        mCheckBoxSpecialService = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.checkBoxSpecialService);
        mCheckBoxPeopleNearby = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.checkBoxPeopleNearby);
        mCheckBoxRescueTeam = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.checkBoxRescueTeam);

        mCheckBoxFirefighter.setOnCheckedChangeListener(this);
        mCheckBoxPolice.setOnCheckedChangeListener(this);
        mCheckBoxSpecialService.setOnCheckedChangeListener(this);
        mCheckBoxPeopleNearby.setOnCheckedChangeListener(this);
        mCheckBoxRescueTeam.setOnCheckedChangeListener(this);

        mPresenter = new EmergencyMessagePresenter(this, this, this);

    }

    /**
     * Creates the menu of the activity.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(pt.ulusofona.copelabs.oi_ndn.R.menu.menu_emergency_message_activity, menu);
        return true;
    }

    /**
     * Receives the actions when menu options are pressed.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == pt.ulusofona.copelabs.oi_ndn.R.id.messageConfiguration) {
            mPresenter.sendMessage(mMessageText.getText().toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Set the checkboxes once Firefighter checkbox is checked.
     */
    @Override
    public void onFireFighterSelected() {
        mCheckBoxPolice.setChecked(false);
        mCheckBoxSpecialService.setChecked(false);
        mCheckBoxPeopleNearby.setChecked(false);
        mCheckBoxRescueTeam.setChecked(false);
    }

    /**
     * Set the checkboxes once Police checkbox is checked.
     */
    @Override
    public void onPoliceSelected() {
        mCheckBoxFirefighter.setChecked(false);
        mCheckBoxSpecialService.setChecked(false);
        mCheckBoxPeopleNearby.setChecked(false);
        mCheckBoxRescueTeam.setChecked(false);
    }

    /**
     * Set the checkboxes once Special Service checkbox is checked.
     */
    @Override
    public void onEspecialServiceSelected() {
        mCheckBoxFirefighter.setChecked(false);
        mCheckBoxPolice.setChecked(false);
        mCheckBoxPeopleNearby.setChecked(false);
        mCheckBoxRescueTeam.setChecked(false);
    }

    /**
     * Set the checkboxes once People Nearby checkbox is checked.
     */
    @Override
    public void onPeopleNearbySelected() {
        mCheckBoxFirefighter.setChecked(false);
        mCheckBoxPolice.setChecked(false);
        mCheckBoxSpecialService.setChecked(false);
        mCheckBoxRescueTeam.setChecked(false);
    }

    /**
     * Set the checkboxes once Rescue Team checkbox is checked.
     */
    @Override
    public void onRescueTeamSelected() {
        mCheckBoxFirefighter.setChecked(false);
        mCheckBoxPolice.setChecked(false);
        mCheckBoxSpecialService.setChecked(false);
        mCheckBoxPeopleNearby.setChecked(false);
    }

    /**
     * Shows error message once the content of the message is empty.
     */
    @Override
    public void showErrorEmptyMessage() {
        mErrorEmptyMessage.setVisibility(View.VISIBLE);

    }

    /**
     * Shows error message once there is not option selected.
     */
    @Override
    public void showErrorMessageOptionNotSelected() {
        mErrorNoOptionSelected.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the error message of content message empty.
     */
    @Override
    public void hideErrorEmptyMessage() {
        mErrorEmptyMessage.setVisibility(View.INVISIBLE);
    }

    /**
     * Hides the error message of no option selected.
     */
    @Override
    public void hideErrorMessageOptionSelected() {
        mErrorNoOptionSelected.setVisibility(View.INVISIBLE);
    }

    /**
     * Finishes the activity once the message is sent.
     */
    @Override
    public void afterMessageSent() {
        finish();
    }

    /**
     * This method receives the action when a checkbox is checked.
     *
     * @param buttonView
     * @param b
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean b) {
        switch (buttonView.getId()) {
            case pt.ulusofona.copelabs.oi_ndn.R.id.checkBoxFirefighter:
                mPresenter.optionSelected(NameModule.FIREFIGHTER_ID);
                break;

            case pt.ulusofona.copelabs.oi_ndn.R.id.checkBoxPolice:
                mPresenter.optionSelected(NameModule.POLICE_ID);
                break;

            case pt.ulusofona.copelabs.oi_ndn.R.id.checkBoxSpecialService:
                mPresenter.optionSelected(NameModule.SPECIAL_SERVICE_ID);
                break;

            case pt.ulusofona.copelabs.oi_ndn.R.id.checkBoxPeopleNearby:
                mPresenter.optionSelected(NameModule.PEOPLE_NEARBY_ID);
                break;

            case pt.ulusofona.copelabs.oi_ndn.R.id.checkBoxRescueTeam:
                mPresenter.optionSelected(NameModule.RESCUE_TEAM_ID);
                break;

        }
    }
}
