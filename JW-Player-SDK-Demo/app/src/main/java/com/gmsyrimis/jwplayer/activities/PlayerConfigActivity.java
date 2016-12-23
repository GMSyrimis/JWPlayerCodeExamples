package com.gmsyrimis.jwplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.JWApplication;
import com.gmsyrimis.jwplayer.R;
import com.gmsyrimis.jwplayer.custom.AddEntryClickListener;
import com.gmsyrimis.jwplayer.custom.EditClickListener;
import com.gmsyrimis.jwplayer.custom.JWActivity;
import com.gmsyrimis.jwplayer.custom.LocalClickListener;
import com.gmsyrimis.jwplayer.custom.QrClickListener;
import com.gmsyrimis.jwplayer.custom.RemoveEntryClickListener;
import com.gmsyrimis.jwplayer.providers.FileUrlProvider;
import com.gmsyrimis.jwplayer.utilities.Utils;
import com.gmsyrimis.jwplayer.views.JWSpinner;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

import static com.gmsyrimis.jwplayer.JWApplication.LICENSE_KEY;
import static com.gmsyrimis.jwplayer.JWApplication.master_config;

public class PlayerConfigActivity extends JWActivity<PlayerConfig> {

    private JWSpinner mFileSpinner;

    private CheckBox mAutostart;
    private CheckBox mRepeat;
    private CheckBox mControls;
    private CheckBox mMute;

    private EditText mLicenseKey;

    private Button mPlaylist;
    private String PLAYLIST_LABEL = "Playlist     ";

    private Spinner mStretchingSpinner;
    private ArrayAdapter<String> mStretchingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_config);

        getSupportActionBar().setTitle("Player Config");

        // GET EXTRAS FROM INTENT
        handleIntent(getIntent());

        setupSetLicenseKey();
        setupFileConfig();
        setupStretchingConfig();

        setupAutostartConfig();
        setupRepeatConfig();
        setupMuteConfig();
        setupControlsConfig();

        setupPlaylistConfig();
        setupVastAdvertisingConfig();
        setupImaAdvertisingConfig();
        setupCaptionStylingConfig();
        setupLogoConfig();
        setupRelatedConfig();
        setupSkinConfig();

        setupSave();

        parseObject(master_config);
    }


    private void setupSetLicenseKey() {
        mLicenseKey = (EditText) findViewById(R.id.set_license_key_et);

        Button qr = (Button) findViewById(R.id.set_license_key_qr);
        qr.setOnClickListener(new QrClickListener(this, JWApplication.QR_LICENSE_KEY));
    }

    private void setupFileConfig() {
        Button qrCode = (Button) findViewById(R.id.config_jw_spinner_qr);
        Button localFile = (Button) findViewById(R.id.config_jw_spinner_lcl);
        Button editLabel = (Button) findViewById(R.id.config_jw_spinner_edit);
        Button removeEntry = (Button) findViewById(R.id.config_jw_spinner_remove);
        Button addEntry = (Button) findViewById(R.id.config_jw_spinner_add);

        mFileSpinner = (JWSpinner) findViewById(R.id.config_jw_file_spinner);
        mFileSpinner.initialize(JWApplication.CONFIG_FILE_URLS, new FileUrlProvider());

        // UPDATE DATA FROM PREFERENCES
        mFileSpinner.load();

        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), com.gmsyrimis.jwplayer.activities.QrActivity.class), JWApplication.QR_FILE_URL);
            }
        });

        localFile.setOnClickListener(new LocalClickListener(PlayerConfigActivity.this));

        mFileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                master_config.setFile(mFileSpinner.getPayloadAt(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editLabel.setOnClickListener(new EditClickListener(mFileSpinner, PlayerConfigActivity.this));

        addEntry.setOnClickListener(new AddEntryClickListener(PlayerConfigActivity.this, mFileSpinner));

        removeEntry.setOnClickListener(new RemoveEntryClickListener(mFileSpinner));
    }

    private void setupStretchingConfig() {
        mStretchingSpinner = (Spinner) findViewById(R.id.stretching_spinner);

        String[] stretchingOptions = new String[]{
                PlayerConfig.STRETCHING_NONE,
                PlayerConfig.STRETCHING_FILL,
                PlayerConfig.STRETCHING_UNIFORM,
                PlayerConfig.STRETCHING_EXACT_FIT
        };

        mStretchingAdapter = new ArrayAdapter<>(
                PlayerConfigActivity.this,
                R.layout.spinner_row_item,
                stretchingOptions
        );

        mStretchingSpinner.setAdapter(mStretchingAdapter);
    }

    private void setupAutostartConfig() {
        mAutostart = (CheckBox) findViewById(R.id.config_autostart);
    }

    private void setupRepeatConfig() {
        mRepeat = (CheckBox) findViewById(R.id.config_repeat);
    }

    private void setupMuteConfig() {
        mMute = (CheckBox) findViewById(R.id.config_mute);
    }

    private void setupControlsConfig() {
        mControls = (CheckBox) findViewById(R.id.config_controls);
    }

    private void setupPlaylistConfig() {
        mPlaylist = (Button) findViewById(R.id.config_playlist);
        mPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), com.gmsyrimis.jwplayer.activities.PlaylistItemActivityTest.class);
                startActivityForResult(i, JWApplication.PLAYLIST_ITEM_ACTIVITY);
            }
        });
    }

    private void setupVastAdvertisingConfig() {
        Button advertising = (Button) findViewById(R.id.config_advertising_vast);
        advertising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), com.gmsyrimis.jwplayer.activities.VastAdvertisingActivity.class);
                startActivityForResult(i, JWApplication.VAST_ADVERTISING_ACTIVITY);
            }
        });
    }

    private void setupImaAdvertisingConfig() {
        Button advertising = (Button) findViewById(R.id.config_advertising_ima);
        advertising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ImaAdvertisingActivity.class);
                startActivityForResult(i, JWApplication.IMA_ADVERTISING_ACTIVITY);
            }
        });
    }

    private void setupCaptionStylingConfig() {
        Button captionStyling = (Button) findViewById(R.id.config_captions);
        captionStyling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CaptionStyleActivity.class);
                startActivityForResult(i, JWApplication.CAPTION_STYLE_ACTIVITY);
            }
        });
    }

    private void setupLogoConfig() {
        Button logo = (Button) findViewById(R.id.config_logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LogoActivity.class);
                startActivityForResult(i, JWApplication.LOGO_ACTIVITY);
            }
        });
    }

    private void setupRelatedConfig() {
        Button related = (Button) findViewById(R.id.config_related);
        related.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), com.gmsyrimis.jwplayer.activities.RelatedActivity.class);
                startActivityForResult(i, JWApplication.RELATED_ACTIVITY);
            }
        });
    }

    private void setupSkinConfig() {
        Button skin = (Button) findViewById(R.id.config_skin);
        skin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), com.gmsyrimis.jwplayer.activities.SkinActivity.class);
                startActivityForResult(i, JWApplication.SKIN_ACTIVITY);
            }
        });
    }

    private void setupSave() {
        Button save = (Button) findViewById(R.id.player_config_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasFile = master_config.getFile() != null && !master_config.getFile().equals("");
                boolean hasPlaylist = master_config.getPlaylist() != null && !master_config.getPlaylist().isEmpty();
                if (hasFile && hasPlaylist) {
                    Toast.makeText(PlayerConfigActivity.this, "Cannot set both File and Playlist", Toast.LENGTH_SHORT).show();
                } else if (!hasFile && !hasPlaylist) {
                    Toast.makeText(PlayerConfigActivity.this, "Missing File or Playlist", Toast.LENGTH_SHORT).show();
                } else {
                    parseScreen();
                    mFileSpinner.save();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(LICENSE_KEY, mLicenseKey.getText().toString());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }


            }
        });
    }

    @Override
    protected PlayerConfig parseScreen() {
        master_config.setFile(mFileSpinner.getPayloadAt(mFileSpinner.getSelectedItemPosition()));
        // Playlist is set during activity result
        // Advertising is set during activity result
        master_config.setAutostart(mAutostart.isChecked());
        master_config.setRepeat(mRepeat.isChecked());
        master_config.setControls(mControls.isChecked());
        master_config.setMute(mMute.isChecked());
        master_config.setStretching(mStretchingAdapter.getItem(mStretchingSpinner.getSelectedItemPosition()));
        return master_config;
    }

    @Override
    protected void parseObject(PlayerConfig object) {
        // CLEAR VIEWS
        String file = object.getFile();
        mFileSpinner.setSelection(file);

        if (object.getPlaylist() != null) {
            mPlaylist.setText(PLAYLIST_LABEL + object.getPlaylist().size());
        } else {
            mPlaylist.setText(PLAYLIST_LABEL);
        }
        mStretchingSpinner.setSelection(mStretchingAdapter.getPosition(Utils.handleObjectData(master_config.getStretching())));
        mControls.setChecked(object.getControls());
        mAutostart.setChecked(object.getAutostart());
        mRepeat.setChecked(object.getRepeat());
        mMute.setChecked(object.getMute());
    }

    @Override
    protected void initDataStructures() {
    }

    @Override
    protected void handleIntent(Intent intent) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == JWApplication.PLAYLIST_ITEM_ACTIVITY ||
                    requestCode == JWApplication.VAST_ADVERTISING_ACTIVITY ||
                    requestCode == JWApplication.IMA_ADVERTISING_ACTIVITY ||
                    requestCode == JWApplication.CAPTION_STYLE_ACTIVITY ||
                    requestCode == JWApplication.LOGO_ACTIVITY ||
                    requestCode == JWApplication.SKIN_ACTIVITY ||
                    requestCode == JWApplication.RELATED_ACTIVITY) {
                parseObject(master_config);
            } else if (requestCode == JWApplication.QR_FILE_URL) {
                String result = data.getStringExtra(JWApplication.RESULT);
                mFileSpinner.setSelection(result);
                mFileSpinner.save();
            } else if (requestCode == JWApplication.LOCAL_VIDEO_FILE) {
                String path = "file://" + Utils.getVideoPathFromURI(this, data.getData());
                mFileSpinner.setSelection(path);
                mFileSpinner.save();
            } else if (requestCode == JWApplication.LOCAL_AUDIO_FILE) {
                String path = "file://" + Utils.getAudioPathFromURI(this, data.getData());
                mFileSpinner.setSelection(path);
                mFileSpinner.save();
            } else if (requestCode == JWApplication.QR_LICENSE_KEY) {
                String result = data.getStringExtra(JWApplication.RESULT);
                mLicenseKey.setText(result);
            }
        }
    }

    @Override
    protected void onPause() {
        mFileSpinner.save();
        super.onPause();
    }
}
