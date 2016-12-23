package com.gmsyrimis.jwplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.gmsyrimis.jwplayer.R;
import com.gmsyrimis.jwplayer.custom.JWActivity;
import com.gmsyrimis.jwplayer.utilities.Utils;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

import static com.gmsyrimis.jwplayer.JWApplication.master_config;
import static com.gmsyrimis.jwplayer.JWApplication.master_config;

public class LogoActivity extends JWActivity<PlayerConfig> {

    private Spinner mLogoPositionSpinner;
    private ArrayAdapter<String> mLogoPositionAdapter;
    private String[] mLogoPositionArray;

    private EditText mLogoFile;
    private EditText mLogoLink;
    private EditText mLogoMargin;

    private CheckBox mLogoHide;

    private Button mLogoSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        getSupportActionBar().setTitle("Logo");


        mLogoFile = (EditText) findViewById(R.id.related_file_et);
        mLogoLink = (EditText) findViewById(R.id.logo_link_et);
        mLogoMargin = (EditText) findViewById(R.id.logo_margin_et);

        mLogoHide = (CheckBox) findViewById(R.id.logo_hide_check);

        mLogoPositionSpinner = (Spinner) findViewById(R.id.related_on_complete_spinner);
        setupLogoPositionSpinner();


        // GET EXTRAS FROM INTENT
        handleIntent(getIntent());
        parseObject(master_config);

        mLogoSave = (Button) findViewById(R.id.logo_save_btn);
        mLogoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseScreen();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


    }

    private void setupLogoPositionSpinner() {
        // Positions derived from
        // https://support.jwplayer.com/customer/en/portal/articles/1406865-branding-your-player
        mLogoPositionArray = new String[]{"",
                PlayerConfig.LOGO_POSITION_TOP_RIGHT,
                PlayerConfig.LOGO_POSITION_TOP_LEFT,
                PlayerConfig.LOGO_POSITION_BOTTOM_RIGHT,
                PlayerConfig.LOGO_POSITION_BOTTOM_LEFT};

        mLogoPositionAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_row_item, mLogoPositionArray);
        mLogoPositionSpinner.setAdapter(mLogoPositionAdapter);
    }


    @Override
    protected PlayerConfig parseScreen() {
        master_config.setLogoPosition(Utils.getStringData(mLogoPositionSpinner));
        master_config.setLogoFile(Utils.getStringData(mLogoFile));
        master_config.setLogoLink(Utils.getStringData(mLogoLink));
        master_config.setLogoMargin((Utils.getIntegerData(mLogoMargin) == null) ? null : Utils.getIntegerData(mLogoMargin));
        master_config.setLogoHide(mLogoHide.isChecked());
        return master_config;
    }

    @Override
    protected void parseObject(PlayerConfig object) {
        mLogoPositionSpinner.setSelection(mLogoPositionAdapter.getPosition(Utils.handleObjectData(object.getLogoPosition())));
        mLogoFile.setText(Utils.handleObjectData(master_config.getLogoFile()));
        mLogoLink.setText(Utils.handleObjectData(master_config.getLogoLink()));
        mLogoMargin.setText(Utils.handleObjectData(master_config.getLogoMargin()));
        mLogoHide.setChecked(master_config.getLogoHide());
    }

    @Override
    protected void initDataStructures() {
        // NO DATASTRUCTURES NEEDED
    }

    @Override
    protected void handleIntent(Intent intent) {
        // used to handle playerconfig
    }
}
