package com.gmsyrimis.jwplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.gmsyrimis.jwplayer.JWApplication;
import com.gmsyrimis.jwplayer.R;
import com.gmsyrimis.jwplayer.custom.JWActivity;
import com.gmsyrimis.jwplayer.utilities.Utils;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

import static com.gmsyrimis.jwplayer.JWApplication.master_config;

public class RelatedActivity extends JWActivity<PlayerConfig> {


    private Spinner mRelatedOnCompleteSpinner;
    private ArrayAdapter<String> mRelatedOnCompleteAdapter;
    private String[] mRelatedOnCompleteArray = new String[]{
            PlayerConfig.RELATED_ONCOMPLETE_AUTOPLAY,
            PlayerConfig.RELATED_ONCOMPLETE_HIDE,
            PlayerConfig.RELATED_ONCOMPLETE_SHOW
    };

    private EditText mRelatedFile;
    private EditText mRelatedHeading;
    private EditText mRelatedAutoplayMessage;
    private EditText mRelatedAutoplayTimer;

    private Button mRelatedSave;
    private Button mQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related);

        getSupportActionBar().setTitle("Related");

        mRelatedOnCompleteSpinner = (Spinner) findViewById(R.id.related_on_complete_spinner);
        mRelatedOnCompleteAdapter = new ArrayAdapter<>(RelatedActivity.this, R.layout.spinner_row_item, mRelatedOnCompleteArray);
        mRelatedOnCompleteSpinner.setAdapter(mRelatedOnCompleteAdapter);

        mRelatedFile = (EditText) findViewById(R.id.related_file_et);
        mRelatedHeading = (EditText) findViewById(R.id.related_heading_et);
        mRelatedAutoplayMessage = (EditText) findViewById(R.id.related_autoplay_message_et);
        mRelatedAutoplayTimer = (EditText) findViewById(R.id.related_autoplay_timer_et);

        mQrCode = (Button) findViewById(R.id.related_file_qr);
        mQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), QrActivity.class), JWApplication.QR_FILE_URL);
            }
        });

        handleIntent(getIntent());
        parseObject(master_config);

        mRelatedSave = (Button) findViewById(R.id.related_save_btn);
        mRelatedSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseScreen();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }

    @Override
    protected PlayerConfig parseScreen() {
        master_config.setRelatedOnComplete(mRelatedOnCompleteAdapter.getItem(mRelatedOnCompleteSpinner.getSelectedItemPosition()));
        master_config.setRelatedFile(Utils.getStringData(mRelatedFile));
        master_config.setRelatedHeading(Utils.getStringData(mRelatedHeading));
        master_config.setRelatedAutoPlayMessage(Utils.getStringData(mRelatedAutoplayMessage));
        master_config.setRelatedAutoPlayTimer(Utils.getIntegerData(mRelatedAutoplayTimer));
        return master_config;
    }

    @Override
    protected void parseObject(PlayerConfig object) {
        mRelatedOnCompleteSpinner.setSelection(mRelatedOnCompleteAdapter.getPosition(Utils.handleObjectData(object.getRelatedOnComplete())));
        mRelatedFile.setText(Utils.handleObjectData(object.getRelatedFile()));
        mRelatedHeading.setText(Utils.handleObjectData(object.getRelatedHeading()));
        mRelatedAutoplayMessage.setText(Utils.handleObjectData(object.getRelatedAutoplayMessage()));
        mRelatedAutoplayTimer.setText(Utils.handleObjectData(object.getRelatedAutoPlayTimer()));
    }

    @Override
    protected void initDataStructures() {

    }

    @Override
    protected void handleIntent(Intent intent) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == JWApplication.QR_FILE_URL) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(JWApplication.RESULT);
                mRelatedFile.setText(result);
            }
        }
    }
}
