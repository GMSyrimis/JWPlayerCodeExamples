package com.gmsyrimis.jwplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.gmsyrimis.jwplayer.R;
import com.gmsyrimis.jwplayer.custom.ColorTextWatcher;
import com.gmsyrimis.jwplayer.custom.JWActivity;
import com.gmsyrimis.jwplayer.utilities.Utils;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

import it.moondroid.colormixer.HSLFragment;

import static com.gmsyrimis.jwplayer.JWApplication.master_config;

public class CaptionStyleActivity extends JWActivity<PlayerConfig> implements HSLFragment.OnColorChangeListener {

    private EditText mFontColorHex;
    private View mFontColorSelector;

    private EditText mBackgroundColorHex;
    private View mBackgroundColorSelector;

    private EditText mWindowColorHex;
    private View mWindowColorSelector;

    private EditText mFontSize;
    private Spinner mFontFamilySpinner;
    private Spinner mEdgeStyleSpinner;
    private EditText mFontOpacity;
    private EditText mBackgroundOpacity;
    private EditText mWindowOpacity;

    private Button mSaveBtn;

    private String[] mFontFamilyArray;
    private ArrayAdapter<String> mFontFamilyAdapter;

    private String[] mEdgeStyleArray;
    private ArrayAdapter<String> mEdgeStyleAdapter;

    private final int FONT_COLOR = 0;
    private final int BACKGROUND_COLOR = 1;
    private final int WINDOW_COLOR = 2;

    private int mCurrentColor = -1;

    /* This class is responsible for reading the player config and populating the caption styling data into different fields
    * We get the player config from the intent extras
    * We find the views, assign the logic, then assign the data if not null
    * During our save sequence we check the fields, if empty we assign null to the player config, then we build a return intent and finish the activity
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption_style);
        getSupportActionBar().setTitle("Caption Styling");

        // GET EXTRAS FROM INTENT
        handleIntent(getIntent());

        mFontColorSelector = findViewById(R.id.caption_style_font_color_selector);
        mFontColorHex = (EditText) findViewById(R.id.caption_style_font_color_hex);
        mFontColorHex.addTextChangedListener(new ColorTextWatcher(this, mFontColorSelector));
        mFontColorSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentColor = FONT_COLOR;
                int black = Color.parseColor("#000000");
                try {
                    if (mFontColorHex.getText().toString().length() != 0)
                        black = Color.parseColor(mFontColorHex.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                HSLFragment fragment = HSLFragment.newInstance(black);
                fragment.show(getFragmentManager(), "dialog");
            }
        });

        mFontSize = (EditText) findViewById(R.id.caption_style_font_size);

        mFontFamilySpinner = (Spinner) findViewById(R.id.caption_style_font_family);
        setupFontFamilySpinner();

        mEdgeStyleSpinner = (Spinner) findViewById(R.id.caption_style_edge_style);
        setupEdgeStyleSpinner();

        mFontOpacity = (EditText) findViewById(R.id.caption_style_font_opacity);

        mBackgroundColorSelector = findViewById(R.id.caption_style_background_color_selector);
        mBackgroundColorHex = (EditText) findViewById(R.id.caption_style_background_color_hex);
        mBackgroundColorHex.addTextChangedListener(new ColorTextWatcher(this, mBackgroundColorSelector));
        mBackgroundColorSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentColor = BACKGROUND_COLOR;
                int black = Color.parseColor("#000000");
                try {
                    if (mBackgroundColorHex.getText().toString().length() != 0)
                        black = Color.parseColor(mBackgroundColorHex.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                HSLFragment fragment = HSLFragment.newInstance(black);
                fragment.show(getFragmentManager(), "dialog");
            }
        });

        mWindowColorSelector = findViewById(R.id.caption_style_window_color_selector);
        mWindowColorHex = (EditText) findViewById(R.id.caption_style_window_color_hex);
        mWindowColorHex.addTextChangedListener(new ColorTextWatcher(this, mWindowColorSelector));
        mWindowColorSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentColor = WINDOW_COLOR;
                int black = Color.parseColor("#000000");
                try {
                    if (mWindowColorHex.getText().toString().length() != 0)
                        black = Color.parseColor(mWindowColorHex.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                HSLFragment fragment = HSLFragment.newInstance(black);
                fragment.show(getFragmentManager(), "dialog");
            }
        });


        mWindowOpacity = (EditText) findViewById(R.id.caption_style_window_opacity);

        mBackgroundOpacity = (EditText) findViewById(R.id.caption_style_background_opacity);

        parseObject(master_config);

        mSaveBtn = (Button) findViewById(R.id.caption_style_save_btn);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseScreen();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }

    private void setupFontFamilySpinner() {
        // FONT FAMILIES DERIVED FROM
        // http://www.w3schools.com/cssref/pr_font_font-family.asp
        mFontFamilyArray = new String[]{"",
                "serif",
                "sans-serif",
                "monospace",
                "cursive",
                "fantasy"};
        mFontFamilyAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_row_item, mFontFamilyArray);
        mFontFamilySpinner.setAdapter(mFontFamilyAdapter);
    }

    private void setupEdgeStyleSpinner() {
        // FONT FAMILIES DERIVED FROM
        // http://www.w3schools.com/cssref/pr_font_font-family.asp
        mEdgeStyleArray = new String[]{"",
                PlayerConfig.CAPTION_EDGE_STYLE_DEPRESSED,
                PlayerConfig.CAPTION_EDGE_STYLE_DROP_SHADOW,
                PlayerConfig.CAPTION_EDGE_STYLE_NONE,
                PlayerConfig.CAPTION_EDGE_STYLE_RAISED,
                PlayerConfig.CAPTION_EDGE_STYLE_UNIFORM,
                PlayerConfig.DEFAULT_CAPTIONS_EDGE_STYLE};
        mEdgeStyleAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_row_item, mEdgeStyleArray);
        mEdgeStyleSpinner.setAdapter(mEdgeStyleAdapter);
    }

    @Override
    protected PlayerConfig parseScreen() {
        master_config.setCaptionsColor((Utils.getStringData(mFontColorHex).length() != 6) ? null : Utils.getStringData(mFontColorHex));
        master_config.setCaptionsFontSize((Utils.getIntegerData(mFontSize) == null) ? null : Utils.getIntegerData(mFontSize));

        master_config.setCaptionsFontFamily(Utils.getStringData(mFontFamilySpinner));

        master_config.setCaptionsFontOpacity((Utils.getIntegerData(mFontOpacity) == null) ? null : Utils.getIntegerData(mFontOpacity));

        master_config.setCaptionsWindowColor((Utils.getStringData(mWindowColorHex).length() != 6) ? null : Utils.getStringData(mWindowColorHex));
        master_config.setCaptionsWindowOpacity((Utils.getIntegerData(mWindowOpacity) == null) ? null : Utils.getIntegerData(mWindowOpacity));

        master_config.setCaptionsBackgroundColor((Utils.getStringData(mBackgroundColorHex).length() != 6) ? null : Utils.getStringData(mBackgroundColorHex));

        master_config.setCaptionsBackgroundOpacity((Utils.getIntegerData(mBackgroundOpacity) == null) ? null : Utils.getIntegerData(mBackgroundOpacity));

        master_config.setCaptionsEdgeStyle(Utils.getStringData(mEdgeStyleSpinner));

        return master_config;
    }

    @Override
    protected void parseObject(PlayerConfig object) {
        mBackgroundOpacity.setText(Utils.handleObjectData(String.valueOf(object.getCaptionsBackgroundOpacity())));
        mBackgroundColorHex.setText(Utils.handleObjectData(object.getCaptionsBackgroundColor()));

        mWindowOpacity.setText(Utils.handleObjectData(String.valueOf(object.getCaptionsWindowOpacity())));
        mWindowColorHex.setText(Utils.handleObjectData(object.getCaptionsWindowColor()));

        mFontOpacity.setText(Utils.handleObjectData(object.getCaptionsFontOpacity()));
        mFontFamilySpinner.setSelection(mFontFamilyAdapter.getPosition(Utils.handleObjectData(object.getCaptionsFontFamily())));

        mFontSize.setText(Utils.handleObjectData(object.getCaptionsFontSize()));
        mFontColorHex.setText(Utils.handleObjectData(object.getCaptionsColor()));

        mEdgeStyleSpinner.setSelection(mEdgeStyleAdapter.getPosition(Utils.handleObjectData(object.getCaptionsEdgeStyle())));

    }

    @Override
    protected void initDataStructures() {
        // No data structures need to be initialized
    }

    @Override
    protected void handleIntent(Intent intent) {
        // used to handle playerconfig
    }

    @Override
    public void onColorChange(int color) {

    }

    @Override
    public void onColorConfirmed(int color) {
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        switch (mCurrentColor) {
            case FONT_COLOR:
                mFontColorHex.setText(hexColor);
                break;
            case BACKGROUND_COLOR:
                mBackgroundColorHex.setText(hexColor);
                break;
            case WINDOW_COLOR:
                mWindowColorHex.setText(hexColor);
                break;
        }
    }

    @Override
    public void onColorCancel() {

    }
}
