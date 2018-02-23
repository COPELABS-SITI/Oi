package pt.ulusofona.copelabs.oi_ndn.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pt.ulusofona.copelabs.oi_ndn.helpers.NameModule;
import pt.ulusofona.copelabs.oi_ndn.interfaces.AuthoritySelectionContract;
import pt.ulusofona.copelabs.oi_ndn.presenters.AuthoritySelectionPresenter;

/**
 * This class is part of Oi application.
 * It provides the interaction between the AuthorityActivity class and the
 * AuthorityPresenter class.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18 3:05 PM
 */

public class AuthoritySelectionActivity extends AppCompatActivity implements View.OnClickListener,
        AuthoritySelectionContract.View {

    /**
     * This variable represents the AuthoritySelectionPresenter.
     */
    private AuthoritySelectionPresenter mPresenter;

    /**
     * In this method is perform the initial setup of the view. Buttons are added to the activity and
     * the OnClickListener is implemented in the class. Also, the presenter is initialized.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(pt.ulusofona.copelabs.oi_ndn.R.layout.activity_authority_selection);

        Button btnPolice = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.buttonPolice);
        btnPolice.setOnClickListener(this);

        Button btnFireFighter = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.buttonFireFighter);
        btnFireFighter.setOnClickListener(this);

        Button btnSpecialServices = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.buttonSpecialService);
        btnSpecialServices.setOnClickListener(this);

        Button btnRescueTeam = findViewById(pt.ulusofona.copelabs.oi_ndn.R.id.buttonRescueTeam);
        btnRescueTeam.setOnClickListener(this);

        mPresenter = new AuthoritySelectionPresenter(this);
    }

    /**
     * This method receives the actions from the buttons present in the activity.
     *
     * @param view The view of th buttons.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case pt.ulusofona.copelabs.oi_ndn.R.id.buttonPolice:
                mPresenter.onAuthoritySelected(NameModule.POLICE_ID);
                break;
            case pt.ulusofona.copelabs.oi_ndn.R.id.buttonFireFighter:
                mPresenter.onAuthoritySelected(NameModule.FIREFIGHTER_ID);
                break;
            case pt.ulusofona.copelabs.oi_ndn.R.id.buttonSpecialService:
                mPresenter.onAuthoritySelected(NameModule.SPECIAL_SERVICE_ID);
                break;
            case pt.ulusofona.copelabs.oi_ndn.R.id.buttonRescueTeam:
                mPresenter.onAuthoritySelected(NameModule.RESCUE_TEAM_ID);
                break;
        }
    }

    /**
     * Starts a new activity with the information about the authority selected.
     *
     * @param authority Integer which represented the authority selected.
     */
    @Override
    public void startAuthorityActivity(int authority) {
        Intent intent= new Intent(AuthoritySelectionActivity.this,AuthorityActivity.class);
        intent.putExtra("AUTHORITY",authority);
        startActivity(intent);
    }
}
