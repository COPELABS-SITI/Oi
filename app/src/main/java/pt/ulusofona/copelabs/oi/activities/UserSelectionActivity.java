package pt.ulusofona.copelabs.oi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import pt.ulusofona.copelabs.oi.R;
import pt.ulusofona.copelabs.oi.interfaces.UserSelectionContract;
import pt.ulusofona.copelabs.oi.presenters.UserSelectionPresenter;

/**
 * This class is a AppCompactActivity class which allows to select the kind of user will be using
 * the application. In this case End User or Authority.
 * Base on that decision is two different activities are going to be initialized.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public class UserSelectionActivity extends AppCompatActivity implements View.OnClickListener, UserSelectionContract.View {

    /**
     * Variable used for debug.
     */
    private static final String TAG = UserSelectionActivity.class.getSimpleName();

    /**
     * UserSelection presenter.
     */
    private UserSelectionPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new UserSelectionPresenter(this, this);
    }

    /**
     * Receives the action when a button is pressed.
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_endUser:
                mPresenter.startActivitySelected(mPresenter.START_END_USER_ACTIVITY);
                break;
            case R.id.button_authority:
                mPresenter.startActivitySelected(mPresenter.START_AUTHORITY_ACTIVITY);
                break;
        }
    }

    /**
     * Displays initial message with relevant information.
     */
    @Override
    public void showInitialMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.initial_message)
                .setTitle(R.string.dialog_title);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Starts the EndUserActivity class.
     */
    @Override
    public void startEndUserActivity() {
        Intent intent = new Intent(UserSelectionActivity.this, EndUserActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Starts the AuthorityActivity class.
     */
    @Override
    public void startAuthorityActivity() {
        Intent intent = new Intent(UserSelectionActivity.this, AuthoritySelectionActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Set the view of the UserSelectionActivity class.
     */
    @Override
    public void startDefaultActivity() {
        setContentView(R.layout.activity_user_selection);
        Button btnEndUser = findViewById(R.id.button_endUser);
        btnEndUser.setOnClickListener(this);
        Button btnAuthority = findViewById(R.id.button_authority);
        btnAuthority.setOnClickListener(this);
    }

    /**
     * Starts the UserConfigurationActivity class.
     */
    @Override
    public void startUserConfigurationActivity() {
        Intent intent = new Intent(UserSelectionActivity.this, UserConfigurationActivity.class);
        intent.putExtra(UserConfigurationActivity.FROM_ACTIVITY, UserConfigurationActivity.FROM_USER_SELECTION_ACTIVITY);
        startActivity(intent);
        finish();
    }
}
