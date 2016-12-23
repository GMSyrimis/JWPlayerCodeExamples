package com.gmsyrimis.jwplayer.views;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.JWApplication;
import com.gmsyrimis.jwplayer.R;
import com.gmsyrimis.jwplayer.custom.AddEntryClickListener;
import com.gmsyrimis.jwplayer.custom.EditClickListener;
import com.gmsyrimis.jwplayer.custom.QrClickListener;
import com.gmsyrimis.jwplayer.custom.RemoveEntryClickListener;
import com.gmsyrimis.jwplayer.custom.TextDialogClickListener;
import com.gmsyrimis.jwplayer.custom.ToastClickListener;
import com.gmsyrimis.jwplayer.providers.PlayAdProvider;
import com.gmsyrimis.jwplayer.providers.PlayerConfigProvider;
import com.gmsyrimis.jwplayer.providers.PlaylistItemProvider;
import com.gmsyrimis.jwplayer.providers.SkinProvider;
import com.gmsyrimis.jwplayer.providers.SkinUrlProvider;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.configuration.Skin;
import com.longtailvideo.jwplayer.media.adaptive.QualityLevel;
import com.longtailvideo.jwplayer.media.adaptive.VisualQuality;
import com.longtailvideo.jwplayer.media.audio.AudioTrack;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;

import static com.gmsyrimis.jwplayer.JWApplication.master_config;

/**
 * Created by gsyrimis on 6/17/16.
 */

/* STEPS TO ADD NEW JWPLAYERVIEW API CALLS
*
* 1. Add views to api_sidebar.xml
* 2. Add member variable to ApiSidebar class
* 3. Create private method that sets up the view and it's behavior
* 4. Call the new method in the public initialize() method
*
* */


public class ApiSidebar extends LinearLayout {


    private Spinner mPlaySpinner;
    private Spinner mPauseSpinner;
    private Spinner mSkinSkinSpinner;

    // Below spinners will all be of type JWSpinner
    private com.gmsyrimis.jwplayer.views.JWSpinner mSkinStringSpinner;
    private com.gmsyrimis.jwplayer.views.JWSpinner mPlayerConfigSpinner;
    private com.gmsyrimis.jwplayer.views.JWSpinner mPlayAdSpinner;
    private com.gmsyrimis.jwplayer.views.JWSpinner mLoadSpinner;

    private EditText mSeekEt;
    private EditText mCurrentAudioTrackIndex;
    private EditText mCurrentCaptionsIndex;
    private EditText mCurrentQualityIndex;
    private CheckBox mSetControls;
    private CheckBox mSetMute;
    private CheckBox mSetBackgroundAudio;
    public CheckBox mSetFullscreen;
    private CheckBox mAllowRotation;

    private EditText mPlaylistItem;

    private JWPlayerView mPlayerView;

    private DrawerLayout mDrawerLayout;

    private ApiSidebar mDrawerList;

    private AppCompatActivity mActivity;

    private SkinProvider mSkinProvider;


    String[] spinnerStates = new String[]{"toggle", "true", "false"};


    public ApiSidebar(Context context) {
        super(context);
        mDrawerList = this;
        inflate(getContext(), R.layout.view_api_sidebar, this);
    }

    public ApiSidebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDrawerList = this;
        inflate(getContext(), R.layout.view_api_sidebar, this);
    }

    public ApiSidebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDrawerList = this;
        inflate(getContext(), R.layout.view_api_sidebar, this);
    }


    // TODO I suppose this can't be shared by inheritance but should be constant
    public void initialize(AppCompatActivity activity, JWPlayerView jwPlayerView, DrawerLayout drawerLayout) {

        mActivity = activity;
        mPlayerView = jwPlayerView;
        mDrawerLayout = drawerLayout;

        // Spinner dependant
        setupPlaySpinner();
        setupPauseSpinner();
        setupLoadSpinner();
        setupPlayerConfigSpinner();
        // TODO Uncomment if you have a license key that can play Ads
//        setupAdsSpinner();
        setupSkinSkinSpinner();
        setupSkinString();

        //Checkbox Dependant
        setupSetControls();
        setupSetBackgroundAudio();
        setupSetMute();
        setupSetFullscreen();

        // EditText Dependant
        setupSeekEt();
        setupSetCurrentAudioTrack();
        setupSetCurrentCaptions();
        setupSetCurrentQuality();

        // Dialog dependant
        setupGetStateBtn();
        setupGetCurrentAudioTrackBtn();
        setupGetCurrentQualityBtn();
        setupGetCurrentCaptionsBtn();
        setupGetPlaylistIndexBtn();
        setupGetFullscreenBtn();
        setupGetDurationBtn();
        setupGetPositionBtn();
        setupStopBtn();
        setupGetAudioTracksBtn();
        setupGetCaptionsListBtn();
        setupGetQualityLevelsBtn();
        setupGetPlaylistBtn();
        setupGetConfigBtn();
        setupGetVersionCode();
        setupGetMute();
        setupGetVisualQuality();
        setupCloseRelatedOverlay();
        setupOpenRelatedOverlay();
        setupDestroySurface();
        setupInitializeSurface();
        setupPlayListItem();
//        TODO setWindowOpenHandler maybe this can be part of the callbacks screen
//        mPlayerView.setWindowOpenHandler(null);

    }


    private void setupPlayListItem() {
        mPlaylistItem = (EditText) findViewById(R.id.play_list_item_et);
        Button goBtn = (Button) findViewById(R.id.play_list_item_go);
        goBtn.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                if (!mPlaylistItem.getText().toString().isEmpty()) {
                    int index = Integer.parseInt(mPlaylistItem.getText().toString());
                    if (index < mPlayerView.getPlaylist().size()) {
                        mPlayerView.playlistItem(index);
                        mPlaylistItem.setText("");
                        return "Played item at index: " + index;
                    } else {
                        return "Please select an index within range";
                    }
                }
                return "Field was empty";
            }
        });
    }

    private void setupInitializeSurface() {
        Button initializeSurface = (Button) findViewById(R.id.initialize_surface);
        initializeSurface.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                mPlayerView.initializeSurface();
                return "Initialize Surface";
            }
        });
    }

    private void setupDestroySurface() {
        Button destroySurface = (Button) findViewById(R.id.destroy_surface);
        destroySurface.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                mPlayerView.destroySurface();
                return "Destroy Surface";
            }
        });
    }

    private void setupCloseRelatedOverlay() {
        Button mCloseRelatedOverlay = (Button) findViewById(R.id.close_related_overlay);
        mCloseRelatedOverlay.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                mPlayerView.closeRelatedOverlay();
                return "Close Related Overlay";
            }
        });
    }

    private void setupOpenRelatedOverlay() {
        Button mOpenRelatedOverlay = (Button) findViewById(R.id.open_related_overlay);
        mOpenRelatedOverlay.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                mPlayerView.openRelatedOverlay();
                return "Open Related Overlay";
            }
        });
    }

    private void setupGetConfigBtn() {
        Button mGetConfig = (Button) findViewById(R.id.get_player_config);
        mGetConfig.setOnClickListener(new TextDialogClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                return handleJSON(mPlayerView.getConfig().toJson());
            }
        });
    }

    private void setupGetVersionCode() {
        Button mGetVersionCode = (Button) findViewById(R.id.get_version_code);
        mGetVersionCode.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                return "SDK Version: " + mPlayerView.getVersionCode();
            }
        });
    }

    private void setupGetVisualQuality() {
        Button mGetVisualQuality = (Button) findViewById(R.id.get_visual_quality);
        mGetVisualQuality.setOnClickListener(new TextDialogClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                VisualQuality visualQuality = mPlayerView.getVisualQuality();

                String message = "null";
                if (visualQuality != null) {
                    String header = "Visual Quality\n";
                    String mode = "Mode = " + visualQuality.getMode().name() + "\n";
                    String qualityLevel = "";
                    try {
                        qualityLevel = "Quality Level = " + visualQuality.getQualityLevel().toJson().toString(4) + "\n";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String reason = "Reason = " + visualQuality.getReason().name();
                    message = header + mode + qualityLevel + reason;
                }

                return message;
            }
        });

    }

    private void setupGetMute() {
        Button mGetMute = (Button) findViewById(R.id.get_mute);
        mGetMute.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                return "Mute: " + Boolean.toString(mPlayerView.getMute());
            }
        });
    }


    private void setupStopBtn() {
        Button mStopBtn = (Button) findViewById(R.id.clear_skin_config_btn);
        mStopBtn.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                mPlayerView.stop();
                return "Stop";
            }
        });
    }


    private void setupPlaySpinner() {

        mPlaySpinner = (Spinner) findViewById(R.id.play_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                mActivity,
                R.layout.spinner_row_item,
                spinnerStates
        );

        mPlaySpinner.setAdapter(adapter);
        mPlaySpinner.setSelection(0, false);


        Button goBtn = (Button) findViewById(R.id.play_spinner_go);
        goBtn.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                if (mPlaySpinner.getSelectedItem().equals("toggle")) {
                    mPlayerView.play();
                } else {
                    mPlayerView.play(Boolean.parseBoolean((String) mPlaySpinner.getSelectedItem()));
                }
                return "Play: " + (String) mPlaySpinner.getSelectedItem();
            }
        });

    }


    private void setupPauseSpinner() {
        mPauseSpinner = (Spinner) findViewById(R.id.pause_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                mActivity,
                R.layout.spinner_row_item,
                spinnerStates
        );

        mPauseSpinner.setAdapter(adapter);
        mPauseSpinner.setSelection(0, false);

        Button goBtn = (Button) findViewById(R.id.pause_spinner_go);
        goBtn.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                if (mPauseSpinner.getSelectedItem().equals("toggle")) {
                    mPlayerView.pause();
                } else {
                    mPlayerView.pause(Boolean.parseBoolean((String) mPauseSpinner.getSelectedItem()));
                }
                return "Pause: " + (String) mPauseSpinner.getSelectedItem();
            }
        });

    }


    private void setupLoadSpinner() {
        mLoadSpinner = (com.gmsyrimis.jwplayer.views.JWSpinner) findViewById(R.id.load_spinner);
        mLoadSpinner.initialize(JWApplication.MAIN_LOAD_PLAYLIST_ITEMS, new PlaylistItemProvider());
        // UPDATE DATA FROM PREFERENCES
        mLoadSpinner.load();
        mLoadSpinner.setSelection(0, false);

        Button goBtn = (Button) findViewById(R.id.load_go);
        goBtn.setOnClickListener(new TextDialogClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                String payload = mLoadSpinner.getPayloadAt(mLoadSpinner.getSelectedItemPosition());
                if (payload != null) {
                    String message = "";
                    try {
                        PlaylistItem video = PlaylistItem.parseJson(payload);
                        message = video.toJson().toString(4);
                        mPlayerView.load(video);
                        master_config = mPlayerView.getConfig();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return message;
                }
                return "Please select a valid item";
            }
        });

        Button qr = (Button) findViewById(R.id.load_qr);
        qr.setOnClickListener(new QrClickListener(mActivity, JWApplication.QR_PLAYLIST_ITEM));

        // TODO refactor local video selection
        Button lcl = (Button) findViewById(R.id.load_lcl);
        lcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("video/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("video/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Video");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                mActivity.startActivityForResult(chooserIntent, JWApplication.LOCAL_PLAYLIST_ITEM);
            }
        });

        Button add = (Button) findViewById(R.id.load_add);
        add.setOnClickListener(new AddEntryClickListener(mActivity, mLoadSpinner));

        Button remove = (Button) findViewById(R.id.load_remove);
        remove.setOnClickListener(new RemoveEntryClickListener(mLoadSpinner));

        Button edit = (Button) findViewById(R.id.load_edit);
        edit.setOnClickListener(new EditClickListener(mLoadSpinner, mActivity));

    }

    private void setupSkinSkinSpinner() {

        mSkinSkinSpinner = (Spinner) findViewById(R.id.skin_skin_spinner);

        mSkinProvider = new SkinProvider();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                mActivity,
                R.layout.spinner_row_item,
                mSkinProvider.labels
        );

        mSkinSkinSpinner.setAdapter(adapter);
        mSkinSkinSpinner.setSelection(0, false);

//        mSkinSkinSpinner.setOnItemSelectedListener(new ToastItemSelectedListener(mActivity, mDrawerLayout, mDrawerList) {
//            @Override
//            public String setMessage(int index) {
//                String skinName = mSkinProvider.labels.get(index);
//                mPlayerView.setSkin(Skin.valueOf(skinName));
//                return "Skin: " + skinName;
//            }
//        });

        Button goBtn = (Button) findViewById(R.id.skin_skin_go);
        goBtn.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                String skinName = mSkinProvider.labels.get(mSkinSkinSpinner.getSelectedItemPosition());
                mPlayerView.setSkin(Skin.valueOf(skinName));
                return "Skin: " + skinName;
            }
        });


    }

    /* To setup a JWSpinner first copy layout of SkinStringSpinner
    * this will give you all the additional buttons you'll need to handle CRUD
    * Next findViewById and initialize with your Prefs name and DataProvider
    * Call load and setSelection to 0,false
    * Set listeners for the buttons and interactions
    *
    * onItemSelected will call a method on mPlayerView, show a Toast or Dialog and close the Drawer
    * goBtn will do the same as onItemSelected
    *
    * qrBtn will start QrActivity, remember to add a String identifier to JWApplication and handle
    * adding the result to the spinner as necessary
    *
    * lclBtn will handle creating an intent to startActivityForResult, remember to add a String
    * identifier to JWApplication and handle the activity result
    *
    * addBtn will pop an AddDialog
    *
    * removeBtn will remove current entry
    *
    * editbtn will pop an EditDialog
    * */
    private void setupSkinString() {

        mSkinStringSpinner = (com.gmsyrimis.jwplayer.views.JWSpinner) findViewById(R.id.set_skin_string_spinner);
        mSkinStringSpinner.initialize(JWApplication.MAIN_SKIN_URLS, new SkinUrlProvider());

        // UPDATE DATA FROM PREFERENCES
        mSkinStringSpinner.load();

        mSkinStringSpinner.setSelection(0, false);
//        mSkinStringSpinner.setOnItemSelectedListener(new ToastItemSelectedListener(mActivity, mDrawerLayout, mDrawerList) {
//            @Override
//            public String setMessage(int index) {
//                String payload = mSkinStringSpinner.getPayloadAt(index);
//                if (payload != null) {
//                    mPlayerView.setSkin(payload);
//                    return "Skin: " + payload;
//                }
//                return "Please select a valid skin";
//            }
//        });

        Button goBtn = (Button) findViewById(R.id.set_skin_string_go);
        goBtn.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                String payload = mSkinStringSpinner.getPayloadAt(mSkinStringSpinner.getSelectedItemPosition());
                if (payload != null) {
                    mPlayerView.setSkin(payload);
                    return "Skin: " + payload;
                }
                return "Please select a valid skin";
            }
        });

        Button qr = (Button) findViewById(R.id.set_skin_string_qr);
        qr.setOnClickListener(new QrClickListener(mActivity, JWApplication.QR_SKIN_URL));

        Button lcl = (Button) findViewById(R.id.set_skin_string_lcl);
        lcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("*/*");
                getIntent.addCategory(Intent.CATEGORY_OPENABLE);
                Intent chooserIntent = Intent.createChooser(getIntent, "Select File");
                mActivity.startActivityForResult(chooserIntent, JWApplication.LOCAL_SKIN_FILE);
            }
        });

        Button add = (Button) findViewById(R.id.set_skin_string_add);
        add.setOnClickListener(new AddEntryClickListener(mActivity, mSkinStringSpinner));

        Button remove = (Button) findViewById(R.id.set_skin_string_remove);
        remove.setOnClickListener(new RemoveEntryClickListener(mSkinStringSpinner));

        Button edit = (Button) findViewById(R.id.set_skin_string_edit);
        edit.setOnClickListener(new EditClickListener(mSkinStringSpinner, mActivity));

    }

    private void setupAdsSpinner() {
        mPlayAdSpinner = (com.gmsyrimis.jwplayer.views.JWSpinner) findViewById(R.id.play_ad_spinner);
        mPlayAdSpinner.initialize(JWApplication.MAIN_PLAY_AD_URLS, new PlayAdProvider());

        // UPDATE DATA FROM PREFERENCES
        mPlayAdSpinner.load();

        mPlayAdSpinner.setSelection(0, false);
//
//        mPlayAdSpinner.setOnItemSelectedListener(new ToastItemSelectedListener(mActivity, mDrawerLayout, mDrawerList) {
//            @Override
//            public String setMessage(int index) {
//                String payload = mPlayAdSpinner.getPayloadAt(index);
//                if (payload != null) {
//                    mPlayerView.playAd(payload);
//                    return payload;
//                }
//                return "Please select a valid ad";
//            }
//        });

        Button goBtn = (Button) findViewById(R.id.play_ad_go);
        goBtn.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                String payload = mPlayAdSpinner.getPayloadAt(mPlayAdSpinner.getSelectedItemPosition());
                if (payload != null) {
                    mPlayerView.playAd(payload);
                    return payload;
                }
                return "Please select a valid ad";
            }
        });

        Button qr = (Button) findViewById(R.id.play_ad_qr);
        qr.setOnClickListener(new QrClickListener(mActivity, JWApplication.QR_PLAY_AD_URL));


        Button add = (Button) findViewById(R.id.play_ad_add);
        add.setOnClickListener(new AddEntryClickListener(mActivity, mPlayAdSpinner));

        Button remove = (Button) findViewById(R.id.play_ad_remove);
        remove.setOnClickListener(new RemoveEntryClickListener(mPlayAdSpinner));

        Button edit = (Button) findViewById(R.id.play_ad_edit);
        edit.setOnClickListener(new EditClickListener(mPlayAdSpinner, mActivity));
    }


    private void setupPlayerConfigSpinner() {

        mPlayerConfigSpinner = (com.gmsyrimis.jwplayer.views.JWSpinner) findViewById(R.id.player_config_spinner);
        mPlayerConfigSpinner.initialize(JWApplication.MAIN_PLAYER_CONFIGS, new PlayerConfigProvider());

        // UPDATE DATA FROM PREFERENCES
        mPlayerConfigSpinner.load();

        mPlayerConfigSpinner.setSelection(0, false);

//        mPlayerConfigSpinner.setOnItemSelectedListener(new TextDialogItemSelectedListener(mActivity, mDrawerLayout, mDrawerList) {
//            @Override
//            public String setMessage(int index) {
//                String payload = mPlayerConfigSpinner.getPayloadAt(index);
//                if (payload != null) {
//                    master_config = PlayerConfig.parseJson(payload);
//                    mPlayerView.setup(master_config);
//                    return handleJSON(master_config.toJson());
//                }
//                return "Please select a valid config";
//            }
//        });

        Button goBtn = (Button) findViewById(R.id.player_config_go);
        goBtn.setOnClickListener(new TextDialogClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                String payload = mPlayerConfigSpinner.getPayloadAt(mPlayerConfigSpinner.getSelectedItemPosition());
                if (payload != null) {
                    PlayerConfig config = PlayerConfig.parseJson(payload);
                    mPlayerView.setup(config);
                    return handleJSON(config.toJson());
                }
                return "Please select a valid config";
            }
        });

        Button add = (Button) findViewById(R.id.player_config_add);
        add.setOnClickListener(new AddEntryClickListener(mActivity, mPlayerConfigSpinner, mPlayerView.getConfig().toJson().toString()));

        Button remove = (Button) findViewById(R.id.player_config_remove);
        remove.setOnClickListener(new RemoveEntryClickListener(mPlayerConfigSpinner));

        Button edit = (Button) findViewById(R.id.player_config_edit);
        edit.setOnClickListener(new EditClickListener(mPlayerConfigSpinner, mActivity));

    }

    private void setupSetControls() {
        mSetControls = (CheckBox) findViewById(R.id.set_controls_check);
        mSetControls.setChecked(true);
        mSetControls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPlayerView.setControls(isChecked);
                Toast.makeText(mActivity, "Controls: " + isChecked, Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });

        Button mSetControlsGo = (Button) findViewById(R.id.set_controls_go);
        mSetControlsGo.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                mPlayerView.setControls(mSetControls.isChecked());
                return "Controls: " + mSetControls.isChecked();
            }
        });

    }

    private void setupSetMute() {

        mSetMute = (CheckBox) findViewById(R.id.set_mute_check);
        mSetMute.setChecked(false);
        mSetMute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPlayerView.setMute(isChecked);
                Toast.makeText(mActivity, "Mute: " + isChecked, Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
        // TODO should I refactor onCheckChanged?

        Button mSetMuteGo = (Button) findViewById(R.id.set_mute_go);
        mSetMuteGo.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                mPlayerView.setMute(mSetMute.isChecked());
                return "Mute: " + mSetMute.isChecked();
            }
        });
    }

    private void setupSetBackgroundAudio() {
        mSetBackgroundAudio = (CheckBox) findViewById(R.id.set_background_audio_check);
        mSetBackgroundAudio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPlayerView.setBackgroundAudio(mSetBackgroundAudio.isChecked());
                Toast.makeText(mActivity, "Background Audio: " + mSetBackgroundAudio.isChecked(), Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });

        Button goBtn = (Button) findViewById(R.id.set_background_audio_go);
        goBtn.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                mPlayerView.setBackgroundAudio(mSetBackgroundAudio.isChecked());
                return "Background Audio: " + mSetBackgroundAudio.isChecked();
            }
        });
    }

    private void setupSetFullscreen() {
        mSetFullscreen = (CheckBox) findViewById(R.id.set_fullscreen_check);
        mAllowRotation = (CheckBox) findViewById(R.id.allow_rotation_check);

        Button goBtn = (Button) findViewById(R.id.set_fullscreen_go);
        goBtn.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                mPlayerView.setFullscreen(mSetFullscreen.isChecked(), mAllowRotation.isChecked());
                return "Fullscreen: " + mSetFullscreen.isChecked() + " Allow Rotation: " + mAllowRotation.isChecked();
            }
        });
    }

    private void setupSeekEt() {
        mSeekEt = (EditText) findViewById(R.id.seek_et);
        Button goBtn = (Button) findViewById(R.id.seek_go);
        goBtn.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                String seekPosition = mSeekEt.getText().toString();
                if (!seekPosition.isEmpty()) {
                    mPlayerView.seek(Long.parseLong(seekPosition));
                    mSeekEt.setText("");
                    return "Seek to: " + seekPosition;
                }
                return "No seek entry";
            }
        });
    }

    private void setupSetCurrentAudioTrack() {

        mCurrentAudioTrackIndex = (EditText) findViewById(R.id.set_current_audio_track_et);
        Button goBtn = (Button) findViewById(R.id.set_current_audio_track_go);
        goBtn.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                String selectedAudioTrack = mCurrentAudioTrackIndex.getText().toString();
                if (!selectedAudioTrack.isEmpty()) {
                    mPlayerView.setCurrentAudioTrack(Integer.parseInt(selectedAudioTrack));
                    mCurrentAudioTrackIndex.setText("");
                    return "Current AudioTrack: " + selectedAudioTrack;
                }
                return "No AudioTrack entry";
            }
        });
    }

    private void setupSetCurrentCaptions() {
        mCurrentCaptionsIndex = (EditText) findViewById(R.id.set_current_captions_et);
        Button goBtn = (Button) findViewById(R.id.set_current_captions_go);
        goBtn.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                String caption = mCurrentCaptionsIndex.getText().toString();
                if (!caption.isEmpty()) {
                    mPlayerView.setCurrentCaptions(Integer.parseInt(caption));
                    mCurrentCaptionsIndex.setText("");
                    return "Current Captions: " + caption;
                }
                return "No Caption entry";
            }
        });
    }

    private void setupSetCurrentQuality() {
        mCurrentQualityIndex = (EditText) findViewById(R.id.set_current_quality_et);
        Button goBtn = (Button) findViewById(R.id.set_current_quality_go);
        goBtn.setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                String quality = mCurrentQualityIndex.getText().toString();
                if (!quality.isEmpty()) {
                    mPlayerView.setCurrentQuality(Integer.parseInt(quality));
                    mCurrentQualityIndex.setText("");
                    return "Current Quality: " + quality;
                }
                return "No Quality entry";
            }
        });
    }

    private void setupGetStateBtn() {
        findViewById(R.id.get_state_btn).setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                return "Player State: " + mPlayerView.getState().name();
            }
        });
    }

    private void setupGetCurrentAudioTrackBtn() {
        findViewById(R.id.get_current_audio_track_btn).setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                return "Current AudioTrack: " + mPlayerView.getCurrentAudioTrack();
            }
        });
    }

    private void setupGetCurrentQualityBtn() {
        findViewById(R.id.get_current_quality_btn).setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                return "Current Quality: " + mPlayerView.getCurrentQuality();
            }
        });
    }

    private void setupGetCurrentCaptionsBtn() {
        findViewById(R.id.get_current_captions_btn).setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                return "Current Captions: " + mPlayerView.getCurrentCaptions();
            }
        });
    }

    private void setupGetPlaylistIndexBtn() {
        findViewById(R.id.get_playlist_index_btn).setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                return "Playlist Index: " + mPlayerView.getPlaylistIndex();
            }
        });
    }

    private void setupGetFullscreenBtn() {
        findViewById(R.id.get_fullscreen_btn).setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                return "Fullscreen: " + mPlayerView.getFullscreen();
            }
        });
    }

    private void setupGetDurationBtn() {
        findViewById(R.id.get_duration_btn).setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                return "Duration: " + mPlayerView.getDuration() + "ms";
            }
        });
    }


    private void setupGetPositionBtn() {
        findViewById(R.id.get_position_btn).setOnClickListener(new ToastClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                return "Position: " + mPlayerView.getPosition() + "ms";
            }
        });
    }


    private void setupGetAudioTracksBtn() {
        findViewById(R.id.get_audio_tracks_btn).setOnClickListener(new TextDialogClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                if (mPlayerView.getAudioTracks() != null) {
                    JSONArray array = new JSONArray();
                    for (AudioTrack current : mPlayerView.getAudioTracks()) {
                        array.put(current.toJson());
                    }
                    return handleJSON(array);
                }
                return "null";
            }
        });
    }

    private void setupGetCaptionsListBtn() {
        findViewById(R.id.get_captions_list_btn).setOnClickListener(new TextDialogClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                if (mPlayerView.getCaptionsList() != null) {
                    JSONArray array = new JSONArray();
                    for (Caption current : mPlayerView.getCaptionsList()) {
                        array.put(current.toJson());
                    }
                    return handleJSON(array);
                }
                return "null";
            }
        });
    }

    private void setupGetQualityLevelsBtn() {
        findViewById(R.id.get_quality_levels_btn).setOnClickListener(new TextDialogClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                if (mPlayerView.getQualityLevels() != null) {
                    JSONArray array = new JSONArray();
                    for (QualityLevel current : mPlayerView.getQualityLevels()) {
                        array.put(current.toJson());
                    }
                    return handleJSON(array);
                }
                return "null";
            }
        });
    }

    private void setupGetPlaylistBtn() {
        findViewById(R.id.get_playlist_btn).setOnClickListener(new TextDialogClickListener(mActivity, mDrawerLayout, mDrawerList) {
            @Override
            public String setMessage() {
                if (mPlayerView.getPlaylist() != null) {
                    JSONArray array = new JSONArray();
                    for (PlaylistItem current : mPlayerView.getPlaylist()) {
                        array.put(current.toJson());
                    }
                    return handleJSON(array);
                }
                return "null";
            }
        });
    }

    public void setJWPlayerView(JWPlayerView playerView) {
        mPlayerView = playerView;
    }


    public com.gmsyrimis.jwplayer.views.JWSpinner getPlayAdSpinner() {
        return mPlayAdSpinner;
    }

    public com.gmsyrimis.jwplayer.views.JWSpinner getSkinStringSpinner() {
        return mSkinStringSpinner;
    }

    public com.gmsyrimis.jwplayer.views.JWSpinner getLoadSpinner() {
        return mLoadSpinner;
    }

}
