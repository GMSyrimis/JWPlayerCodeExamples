package com.gmsyrimis.jwplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.JWApplication;
import com.gmsyrimis.jwplayer.R;
import com.gmsyrimis.jwplayer.custom.ColorTextWatcher;
import com.gmsyrimis.jwplayer.custom.JWActivity;
import com.gmsyrimis.jwplayer.providers.SkinProvider;
import com.gmsyrimis.jwplayer.utilities.Utils;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

import it.moondroid.colormixer.HSLFragment;

import static com.gmsyrimis.jwplayer.JWApplication.master_config;

public class SkinActivity extends JWActivity<PlayerConfig> implements HSLFragment.OnColorChangeListener {

    private EditText mSkinActiveHex;
    private View mSkinActiveSelector;

    private EditText mSkinInactiveHex;
    private View mSkinInactiveSelector;

    private EditText mSkinbackgroundHex;
    private View mSkinBackgroundSelector;

    private Spinner mSkinSkinSpinner;

    private EditText mSkinName;
    private EditText mSkinUrl;
    private Button mSkinUrlQr;
    private Button mSkinUrlLcl;
    private Button mSkinSave;

    private final int NO_COLOR = -1;
    private final int BACKGROUND_COLOR = 0;
    private final int ACTIVE_COLOR = 1;
    private final int INACTIVE_COLOR = 2;

    private int mCurrentColor = -1;
    private ArrayAdapter<String> mSkinAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);

        getSupportActionBar().setTitle("Skins");

        handleIntent(getIntent());

        mSkinSkinSpinner = (Spinner) findViewById(R.id.stretching_spinner);

        SkinProvider skinProvider = new SkinProvider();


        mSkinAdapter = new ArrayAdapter<>(
                SkinActivity.this,
                R.layout.spinner_row_item,
                skinProvider.labels
        );

        mSkinSkinSpinner.setAdapter(mSkinAdapter);


        mSkinName = (EditText) findViewById(R.id.skin_name);
        mSkinUrl = (EditText) findViewById(R.id.skin_url);
        mSkinUrlQr = (Button) findViewById(R.id.skin_url_qr);
        mSkinUrlQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), QrActivity.class), JWApplication.QR_FILE_URL);
            }
        });

        mSkinUrlLcl = (Button) findViewById(R.id.skin_url_lcl);
        mSkinUrlLcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("*/*");
                getIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Files.getContentUri("external"));
//
//                pickIntent.setType("*/*");
//                pickIntent.addCategory(Intent.CATEGORY_OPENABLE);

                Intent chooserIntent = Intent.createChooser(getIntent, "Select File");
//                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, JWApplication.LOCAL_SKIN_FILE);
            }
        });


        mSkinActiveSelector = findViewById(R.id.skin_active_selector);
        mSkinActiveHex = (EditText) findViewById(R.id.skin_active_hex);
        mSkinActiveHex.addTextChangedListener(new ColorTextWatcher(this, mSkinActiveSelector));
        mSkinActiveSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentColor = ACTIVE_COLOR;
                showColorPicker(mSkinActiveHex);
            }
        });

        mSkinInactiveSelector = findViewById(R.id.skin_inactive_selector);
        mSkinInactiveHex = (EditText) findViewById(R.id.skin_inactive_hex);
        mSkinInactiveHex.addTextChangedListener(new ColorTextWatcher(this, mSkinInactiveSelector));
        mSkinInactiveSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentColor = INACTIVE_COLOR;
                showColorPicker(mSkinInactiveHex);
            }
        });

        mSkinBackgroundSelector = findViewById(R.id.skin_background_selector);
        mSkinbackgroundHex = (EditText) findViewById(R.id.skin_background_hex);
        mSkinbackgroundHex.addTextChangedListener(new ColorTextWatcher(this, mSkinBackgroundSelector));
        mSkinBackgroundSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentColor = BACKGROUND_COLOR;
                showColorPicker(mSkinbackgroundHex);
            }
        });

        // GET EXTRAS FROM INTENT
        parseObject(master_config);


        mSkinSave = (Button) findViewById(R.id.skin_save_btn);
        mSkinSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parseScreen() != null) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(SkinActivity.this, "Missing Name or Url", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void showColorPicker(EditText color) {
        int black = Color.parseColor("#000000");
        try {
            if (color.getText().toString().length() != 0)
                black = Color.parseColor(color.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        HSLFragment fragment = HSLFragment.newInstance(black);
        fragment.show(getFragmentManager(), "dialog");
    }

    @Override
    protected PlayerConfig parseScreen() {
        master_config.setSkinUrl(Utils.getStringData(mSkinUrl));
        master_config.setSkinName(Utils.getStringData(mSkinName));
        master_config.setSkinActive(Utils.getStringData(mSkinActiveHex));
        master_config.setSkinInactive(Utils.getStringData(mSkinInactiveHex));
        master_config.setSkinBackground(Utils.getStringData(mSkinbackgroundHex));
        return master_config;
    }

    @Override
    protected void parseObject(PlayerConfig object) {
        mSkinUrl.setText(Utils.handleObjectData(object.getSkinUrl()));
        mSkinName.setText(Utils.handleObjectData(object.getSkinName()));
        mSkinActiveHex.setText(Utils.handleObjectData(object.getSkinActive()));
        mSkinInactiveHex.setText(Utils.handleObjectData(object.getSkinInactive()));
        mSkinbackgroundHex.setText(Utils.handleObjectData(object.getSkinBackground()));

        if (master_config.getSkin() != null) {
            mSkinSkinSpinner.setSelection(mSkinAdapter.getPosition(object.getSkin().name()));
            mSkinSkinSpinner.setEnabled(false);
        }
    }

    @Override
    protected void initDataStructures() {
        // NO DATASTRUCTURES NEEDED
    }

    @Override
    protected void handleIntent(Intent intent) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == JWApplication.QR_FILE_URL) {
                String result = data.getStringExtra(JWApplication.RESULT);
                mSkinUrl.setText(result);

            } else if (requestCode == JWApplication.LOCAL_SKIN_FILE) {
                // Get the Uri of the selected file
                Uri uri = data.getData();
                // Get the path
                String path = Utils.getDocumentsFilePath(this, uri);
                mSkinUrl.setText(path);
            }
        }
    }

    @Override
    public void onColorChange(int color) {

    }

    @Override
    public void onColorConfirmed(int color) {
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        switch (mCurrentColor) {
            case NO_COLOR:
                break;
            case BACKGROUND_COLOR:
                mSkinbackgroundHex.setText(hexColor);
                break;
            case INACTIVE_COLOR:
                mSkinInactiveHex.setText(hexColor);
                break;
            case ACTIVE_COLOR:
                mSkinActiveHex.setText(hexColor);
                break;
        }

    }

    @Override
    public void onColorCancel() {

    }
}
