package pt.ulusofona.copelabs.oi.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import pt.ulusofona.copelabs.oi.R;
import pt.ulusofona.copelabs.oi.interfaces.UserConfigurationContract;
import pt.ulusofona.copelabs.oi.presenters.UserConfigurationPresenter;

/**
 * This class is a AppCompactActivity class which allows users change their configuration. From here
 * is possible to modify the Name of the user and the phone number. The first time the application
 * is started, this activity is shown. Without the information of the user is impossible to
 * use the application.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public class UserConfigurationActivity extends AppCompatActivity implements UserConfigurationContract.View {

    /**
     * This variable is used to pass Intent information from previous activities.
     */
    public static final String FROM_ACTIVITY = "FROM";

    /**
     * Information passed when UserConfigurationActivity class is started from
     * EndUserActivity class.
     */
    public static final String FROM_END_USER_ACTIVITY = "0";

    /**
     * information passed when UserConfigurationActivity class is started from
     * UserSelectionActivity class
     */
    public static final String FROM_USER_SELECTION_ACTIVITY = "1";

    /**
     * Variable used for debug.
     */
    private static final String TAG = UserConfigurationActivity.class.getSimpleName();
    /**
     * Presenter used to preform tha actions from this activity.
     */
    private UserConfigurationPresenter mPresenter;

    /**
     * TextView Error message for empty user name information.
     */
    private TextView mErrorUserNameTxt;

    /**
     * TextView Error message for empty phone number information.
     */
    private TextView mErrorPhoneNumberTxt;

    /**
     * Button Preforms the save action.
     */
    private Button mSaveButton;

    /**
     * EditText Used to hold the information of user name.
     */
    private EditText mUserNameEditTxt;

    /**
     * EditText Used to hold the information of phone number.
     */
    private EditText mPhoneNumberEditTxt;

    /**
     * country code of the local contact.
     */
    private EditText mCountryCode;



    /**
     * Initial configuration is perform. The view is set and the presenter is created.
     * Also listener to the editText variables is add.
     *
     * @param savedInstanceState
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(pt.ulusofona.copelabs.oi.R.layout.activity_user_configuration1);
        //if(getIntent().getStringExtra(FROM_ACTIVITY).equals(FROM_END_USER_ACTIVITY))

        /*if(getIntent().getStringExtra(FROM_ACTIVITY).equals(FROM_END_USER_ACTIVITY))
            setContentView(pt.ulusofona.copelabs.oi.R.layout.activity_user_configuration);
        else
            setContentView(pt.ulusofona.copelabs.oi.R.layout.activity_user_configuration1);*/



        mPresenter = new UserConfigurationPresenter(this, this, getIntent().getStringExtra(FROM_ACTIVITY),this);



        //This listener is used to hide the error message once the user starts introduce content to the editText.
        mUserNameEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPresenter.userNameEditTxtChanged(editable.length());
            }
        });


        //This listener is used to hide the error message once the user starts introduce content to the editText.
        mPhoneNumberEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPresenter.phoneNumberEditTxtChanged(editable.length());
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(getIntent().getStringExtra(FROM_ACTIVITY).equals(UserConfigurationActivity.FROM_USER_SELECTION_ACTIVITY))
                    mPresenter.onFormFilled(mUserNameEditTxt.getText().toString(), mCountryCode.getText().toString() + mPhoneNumberEditTxt.getText().toString());
                else
                    mPresenter.onFormFilled(mUserNameEditTxt.getText().toString(), mPhoneNumberEditTxt.getText().toString());
            }
        });
    }

    /**
     * Displays the information of the user.
     *
     * @param name        String user name.
     * @param phoneNumber String phone number.
     */
    @Override
    public void showContactInformation(String name, String phoneNumber) {
        mUserNameEditTxt.setText(name);
        this.mPhoneNumberEditTxt.setText(phoneNumber);
    }

    /**
     * Shows the error message when the editText is empty.
     */
    @Override
    public void showUserNameErrorMessage() {
        mErrorUserNameTxt.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the error message when the editText is empty.
     */
    @Override
    public void showPhoneNumberErrorMessage() {
        mErrorPhoneNumberTxt.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the error message when the editText is no empty.
     */
    @Override
    public void hideUserNameErrorMessage() {
        mErrorUserNameTxt.setVisibility(View.INVISIBLE);
    }

    /**
     * Hides the error message when the editText is no empty.
     */
    @Override
    public void hidePhoneNumberErrorMessage() {
        mErrorPhoneNumberTxt.setVisibility(View.INVISIBLE);
    }

    /**
     * Starts the EndUserActivity class.
     */
    @Override
    public void startEndUserActivity() {
        Intent intent = new Intent(UserConfigurationActivity.this, EndUserActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Disables the save button when there is a field empty.
     */
    @Override
    public void disableSaveButton() {
        mSaveButton.setEnabled(false);
        mSaveButton.setBackgroundResource(pt.ulusofona.copelabs.oi.R.drawable.rounded_button_deseable);
    }

    /**
     * Finishes the actual activity.
     */
    @Override
    public void finishActivity() {
        finish();
    }

    /**
     * Enables the save button when the user introduce information in the empty field.
     */
    @Override
    public void enableSaveButton() {
        mSaveButton.setEnabled(true);
        mSaveButton.setBackgroundResource(pt.ulusofona.copelabs.oi.R.drawable.selector_button_blue);
    }

    @Override
    public void showActivityContuntryCode() {
        setContentView(pt.ulusofona.copelabs.oi.R.layout.activity_user_configuration1);
        mSaveButton = findViewById(pt.ulusofona.copelabs.oi.R.id.buttonDone);
        mErrorUserNameTxt = findViewById(pt.ulusofona.copelabs.oi.R.id.textView6);
        mErrorPhoneNumberTxt = findViewById(pt.ulusofona.copelabs.oi.R.id.textView7);
        mUserNameEditTxt = findViewById(pt.ulusofona.copelabs.oi.R.id.editTextUserName);
        mPhoneNumberEditTxt = findViewById(pt.ulusofona.copelabs.oi.R.id.editTextPhoneNumber);
        mCountryCode = findViewById(R.id.editTextCountryCode);

        mCountryCode.setText("+");

        //This listener is used to hide the error message once the user starts introduce content to the editText.
        mCountryCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().startsWith("+")){
                    Selection.setSelection(mCountryCode.getText(), mCountryCode.getText().length());

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().startsWith("+")){
                    mCountryCode.setText("+");
                    Selection.setSelection(mCountryCode.getText(), mCountryCode.getText().length());

                }
            }
        });
    }

    @Override
    public void showDefaultActivity() {
        setContentView(pt.ulusofona.copelabs.oi.R.layout.activity_user_configuration);
        mSaveButton = findViewById(pt.ulusofona.copelabs.oi.R.id.buttonDone);
        mErrorUserNameTxt = findViewById(pt.ulusofona.copelabs.oi.R.id.textView6);
        mErrorPhoneNumberTxt = findViewById(pt.ulusofona.copelabs.oi.R.id.textView7);
        mUserNameEditTxt = findViewById(pt.ulusofona.copelabs.oi.R.id.editTextUserName);
        mPhoneNumberEditTxt = findViewById(pt.ulusofona.copelabs.oi.R.id.editTextPhoneNumber);
    }


}
