package com.gmsyrimis.jwplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.JWApplication;
import com.gmsyrimis.jwplayer.R;
import com.gmsyrimis.jwplayer.adapters.PlaylistArrayAdapterTest;
import com.gmsyrimis.jwplayer.custom.AddEntryClickListener;
import com.gmsyrimis.jwplayer.custom.EditClickListener;
import com.gmsyrimis.jwplayer.custom.JWActivity;
import com.gmsyrimis.jwplayer.custom.JWScreen;
import com.gmsyrimis.jwplayer.custom.LocalClickListener;
import com.gmsyrimis.jwplayer.custom.RemoveEntryClickListener;
import com.gmsyrimis.jwplayer.custom.WidevineMediaDrmCallback;
import com.gmsyrimis.jwplayer.providers.FileUrlProvider;
import com.gmsyrimis.jwplayer.utilities.Utils;
import com.gmsyrimis.jwplayer.views.HttpHeadersView;
import com.gmsyrimis.jwplayer.views.JWSpinner;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.gmsyrimis.jwplayer.JWApplication.master_config;

public class PlaylistItemActivityTest extends JWActivity<PlaylistItem> {

    // LIST TO BE EXPORTED
    private List<PlaylistItem> mPlaylistItemList;
    private ListView mPlaylistItemListView;
    private PlaylistArrayAdapterTest mPlaylistItemListAdapter;
    private int mCurrentItemSelected = JWApplication.NEW_PLAYLIST_ITEM;

    private JWSpinner mFileSpinner;
    private EditText mImageET;
    private EditText mDrmET;
    private EditText mTitleET;
    private EditText mDescriptionET;
    private EditText mMediaIdET;
    private LinearLayout mSourcesContainer;
    private LinearLayout mCaptionsContainer;

    // TODO Uncomment if you have a license key that can play Ads
//    private LinearLayout mAdsContainer;

    private Button mAddToPlaylistBtn;
    private Button mEditBtn;
    private Button mNewBtn;
    private Button mCommitBtn;

    private Button mClearList;
    private Button mLoadList;
    private Button mSaveList;

    private Button mSetListBtn;
    private JWScreen mPlaylistItemScreen;

    private HttpHeadersView mHttpHeadersView;

    private boolean mCanPlayDrm = false;


    private ActionBar mActionBar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mPlaylistSidebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_item_test);


        initDataStructures();
        // GET PLAYLIST FROM INTENT
        handleIntent(getIntent());

        setupActionBar();
        setupNavDrawer();
        setupDrm();

        mPlaylistItemScreen = new JWScreen(getWindow(), new int[]{
                R.id.playlistitem_title_et,
                R.id.playlistitem_description_et,
                R.id.playlistitem_mediaid_et,
                R.id.playlistitem_jw_file_spinner,
                R.id.playlistitem_jw_spinner_add,
                R.id.playlistitem_jw_spinner_edit,
                R.id.playlistitem_jw_spinner_lcl,
                R.id.playlistitem_jw_spinner_qr,
                R.id.playlistitem_jw_spinner_remove,
                R.id.playlistitem_image_et,
                R.id.playlistitem_image_qr,
                R.id.playlistitem_image_lcl,
                // TODO Uncomment if you have a license key that can play Ads
//                R.id.playlistitem_ads_container,
//                R.id.playlistitem_edit_ads_btn,
                R.id.playlistitem_sources_container,
                R.id.playlistitem_edit_sources_btn,
                R.id.playlistitem_captions_container,
                R.id.playlistitem_edit_captions_btn
        });

        mTitleET = (EditText) findViewById(R.id.playlistitem_title_et);
        mDescriptionET = (EditText) findViewById(R.id.playlistitem_description_et);
        mMediaIdET = (EditText) findViewById(R.id.playlistitem_mediaid_et);

        setupFileSpinner();
        setupImage();

        mHttpHeadersView = (HttpHeadersView) findViewById(R.id.playlistitem_http_headers_view);
        mHttpHeadersView.initialize(this);

        // TODO Uncomment if you have a license key that can play Ads
//        setupAds();

        setupSources();
        setupCaptions();

        setupListView();
        setupAddToPlaylistButton();


        mEditBtn = (Button) findViewById(R.id.playlistitem_edit_btn);
        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SET ENABLED ON VIEWS TO TRUE
                Utils.enableViews(mPlaylistItemScreen.getListOfViews(), true);
                mHttpHeadersView.setEnabled(true);
                // HIDE EDIT BTN
                mEditBtn.setVisibility(View.GONE);
                // SHOW COMMIT BTN
                mCommitBtn.setVisibility(View.VISIBLE);
                mAddToPlaylistBtn.setVisibility(View.GONE);
                mNewBtn.setVisibility(View.VISIBLE);
            }
        });

        mNewBtn = (Button) findViewById(R.id.playlistitem_new_btn);
        mNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.clearViews(mPlaylistItemScreen.getListOfViews());
                mHttpHeadersView.clear();
                mEditBtn.setVisibility(View.GONE);
                mCommitBtn.setVisibility(View.GONE);
                mAddToPlaylistBtn.setVisibility(View.VISIBLE);
                mNewBtn.setVisibility(View.GONE);
                Utils.enableViews(mPlaylistItemScreen.getListOfViews(), true);
                mHttpHeadersView.setEnabled(true);
                mCurrentItemSelected = JWApplication.NEW_PLAYLIST_ITEM;
            }
        });

        mCommitBtn = (Button) findViewById(R.id.playlistitem_commit_btn);
        mCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // BUILD PlaylistItem
                PlaylistItem playlistItem = parseScreen();
                if (playlistItem != null) {
                    // ADD THE PlaylistItem AT THE CORRECT POSITION IN playlistItemList
                    mPlaylistItemList.set(mCurrentItemSelected, playlistItem);
                    // UPDATE ADAPTER
                    mPlaylistItemListAdapter.notifyDataSetChanged();

                    Utils.clearViews(mPlaylistItemScreen.getListOfViews());
                    mHttpHeadersView.clear();
                    mEditBtn.setVisibility(View.GONE);
                    mCommitBtn.setVisibility(View.GONE);
                    mAddToPlaylistBtn.setVisibility(View.VISIBLE);
                    mNewBtn.setVisibility(View.GONE);
                } else {
                    Toast.makeText(PlaylistItemActivityTest.this, "Missing File or Sources", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mClearList = (Button) findViewById(R.id.playlistitem_clear_list);
        mClearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.clearViews(mPlaylistItemScreen.getListOfViews());
                mHttpHeadersView.clear();
                mPlaylistItemList.clear();
                mPlaylistItemListAdapter.notifyDataSetChanged();
                mCurrentItemSelected = 0;
            }
        });

        mSetListBtn = (Button) findViewById(R.id.playlistitem_set_list);
        mSetListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlaylistItemList.size() == 0) {
                    master_config.setPlaylist(null);
                } else {
                    master_config.setPlaylist(mPlaylistItemList);
                }
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    private void setupAddToPlaylistButton() {
        mAddToPlaylistBtn = (Button) findViewById(R.id.playlistitem_add_btn);
        mAddToPlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaylistItem playlistItem = parseScreen();
                if (playlistItem != null) {
                    mPlaylistItemList.add(playlistItem);
                    mPlaylistItemListAdapter.notifyDataSetChanged();
                    Utils.clearViews(mPlaylistItemScreen.getListOfViews());
                    mHttpHeadersView.clear();
                    mEditBtn.setVisibility(View.GONE);
                    mCommitBtn.setVisibility(View.GONE);
                    mAddToPlaylistBtn.setVisibility(View.VISIBLE);
                    mNewBtn.setVisibility(View.GONE);
                    Toast.makeText(PlaylistItemActivityTest.this, "Item added to playlist", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PlaylistItemActivityTest.this, "Missing File or Sources", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // TODO Uncomment if you have a license key that can play Ads

//    private void setupAds() {
//        mAdsContainer = (LinearLayout) findViewById(R.id.playlistitem_ads_container);
//        Button editAdsBtn = (Button) findViewById(R.id.playlistitem_edit_ads_btn);
//        editAdsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent returnIntent = new Intent(getApplicationContext(), AdBreakActivity.class);
//                returnIntent.putExtra(JWApplication.IS_PLAYLIST_ITEM, true);
//                returnIntent.putExtra(JWApplication.PLAYLIST_ITEM_INDEX, mCurrentItemSelected);
//                startActivityForResult(returnIntent, JWApplication.AD_BREAK_ACTIVITY);
//            }
//        });
//    }

    private void setupSources() {
        mSourcesContainer = (LinearLayout) findViewById(R.id.playlistitem_sources_container);
        Button editSourcesBtn = (Button) findViewById(R.id.playlistitem_edit_sources_btn);
        editSourcesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(getApplicationContext(), MediaSourceActivity.class);
                returnIntent.putExtra(JWApplication.PLAYLIST_ITEM_INDEX, mCurrentItemSelected);
                startActivityForResult(returnIntent, JWApplication.MEDIA_SOURCE_ACTIVITY);
            }
        });
    }

    private void setupCaptions() {
        mCaptionsContainer = (LinearLayout) findViewById(R.id.playlistitem_captions_container);
        Button editCaptionsBtn = (Button) findViewById(R.id.playlistitem_edit_captions_btn);
        editCaptionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(getApplicationContext(), CaptionActivity.class);
                returnIntent.putExtra(JWApplication.PLAYLIST_ITEM_INDEX, mCurrentItemSelected);
                startActivityForResult(returnIntent, JWApplication.CAPTION_ACTIVITY);
            }
        });
    }

    private void setupImage() {
        mImageET = (EditText) findViewById(R.id.playlistitem_image_et);
        Button imageQR = (Button) findViewById(R.id.playlistitem_image_qr);
        imageQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), com.gmsyrimis.jwplayer.activities.QrActivity.class), JWApplication.QR_IMAGE_URL);
            }
        });

        Button imageLcl = (Button) findViewById(R.id.playlistitem_image_lcl);
        imageLcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, JWApplication.LOCAL_IMAGE_FILE);
            }
        });
    }

    private void setupDrm() {
        mCanPlayDrm = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
        if (mCanPlayDrm) {
            mDrmET = (EditText) findViewById(R.id.playlistitem_drm_et);
            Button drmQR = (Button) findViewById(R.id.playlistitem_drm_qr);
            drmQR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(getApplicationContext(), com.gmsyrimis.jwplayer.activities.QrActivity.class), JWApplication.QR_DRM);
                }
            });
        } else {
            findViewById(R.id.playlistitem_drm_container).setVisibility(View.GONE);
        }
    }

    private void setupActionBar() {
        // Setup Actionbar
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle("Playlist Item");
        }
    }

    private void setupFileSpinner() {
        Button qrCode = (Button) findViewById(R.id.playlistitem_jw_spinner_qr);
        Button localFile = (Button) findViewById(R.id.playlistitem_jw_spinner_lcl);
        Button editLabel = (Button) findViewById(R.id.playlistitem_jw_spinner_edit);
        Button removeEntry = (Button) findViewById(R.id.playlistitem_jw_spinner_remove);
        Button addEntry = (Button) findViewById(R.id.playlistitem_jw_spinner_add);

        mFileSpinner = (JWSpinner) findViewById(R.id.playlistitem_jw_file_spinner);
        mFileSpinner.initialize(JWApplication.CONFIG_FILE_URLS, new FileUrlProvider());

        // UPDATE DATA FROM PREFERENCES
        mFileSpinner.load();
        mFileSpinner.setSelection("");

        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), com.gmsyrimis.jwplayer.activities.QrActivity.class), JWApplication.QR_FILE_URL);
            }
        });

        localFile.setOnClickListener(new LocalClickListener(PlaylistItemActivityTest.this));

        editLabel.setOnClickListener(new EditClickListener(mFileSpinner, PlaylistItemActivityTest.this));

        addEntry.setOnClickListener(new AddEntryClickListener(PlaylistItemActivityTest.this, mFileSpinner));

        removeEntry.setOnClickListener(new RemoveEntryClickListener(mFileSpinner));
    }

    private void setupNavDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mPlaylistSidebar = (LinearLayout) findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.menu, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                mActionBar.setTitle("Playlist Item");
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                mActionBar.setTitle("Playlist");
                invalidateOptionsMenu();
            }
        };


        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }

    private void setupListView() {
        mPlaylistItemListView = (ListView) findViewById(R.id.playlistitem_list_view);
        mPlaylistItemListAdapter = new PlaylistArrayAdapterTest(this, mPlaylistItemList);
        mPlaylistItemListView.setAdapter(mPlaylistItemListAdapter);
        mPlaylistItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // UPDATE mCurrentAdBreak
                mCurrentItemSelected = position;
                // GET MediaSource AT POSITION
                PlaylistItem currentPlaylistItem = mPlaylistItemList.get(position);
                // POPULATE MediaSource DATA INTO VIEWS
                parseObject(currentPlaylistItem);
                // SET ENABLED ON VIEWS TO FALSE
                Utils.enableViews(mPlaylistItemScreen.getListOfViews(), false);
                mHttpHeadersView.setEnabled(false);
                // HIDE ADD BTN
                mAddToPlaylistBtn.setVisibility(View.GONE);
                // HIDE COMMIT BTN
                mCommitBtn.setVisibility(View.GONE);
                // SHOW NEW BTN
                mNewBtn.setVisibility(View.VISIBLE);
                // SHOW EDIT BTN
                mEditBtn.setVisibility(View.VISIBLE);
                mDrawerLayout.closeDrawer(mPlaylistSidebar);
            }
        });
    }

    @Override
    protected PlaylistItem parseScreen() {
        Log.d("PlaylistItemActivity", "parseScreen()");
        // ONLY IF THERE IS DATA IN THE MANDATORY FIELDS SHALL WE RETURN THE OBJECT. ELSE WE RETURN NULL
        String file = mFileSpinner.getPayloadAt(mFileSpinner.getSelectedItemPosition());
        boolean hasFile = (!file.isEmpty());
        boolean hasSources = (mSourcesContainer.getChildCount() == 1);
        if (hasFile || hasSources) {
            PlaylistItem playlistItem = new PlaylistItem();
            playlistItem.setFile(file);
            playlistItem.setTitle(Utils.getStringData(mTitleET));
            playlistItem.setDescription(Utils.getStringData(mDescriptionET));
            playlistItem.setMediaId(Utils.getStringData(mMediaIdET));
            playlistItem.setImage(Utils.getStringData(mImageET));
            Log.d("PlaylistItemActivity", "parseScreen() getHeaders");
            Log.d("PlaylistItemActivity", "parseScreen() getHeaders=" + mHttpHeadersView.getHttpHeaders());
            playlistItem.setHttpHeaders((mHttpHeadersView.getHttpHeaders() == null) ? null : new HashMap<>(mHttpHeadersView.getHttpHeaders()));
            Log.d("PlaylistItemActivity", "parseScreen() playlist headers = " + playlistItem.getHttpHeaders());

            if (mCanPlayDrm) {
                String drm = Utils.getStringData(mDrmET);
                if (drm != null) {
                    playlistItem.setMediaDrmCallback(new WidevineMediaDrmCallback(drm));
                }
            }

            if (hasSources) {
                // Get sources
                TextView tv = (TextView) mSourcesContainer.getChildAt(0);
                String sources = tv.getText().toString();
                try {
                    JSONArray jsonArray = new JSONArray(sources);
                    List<MediaSource> mediaSourceList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        MediaSource current = MediaSource.parseJson(jsonArray.getJSONObject(i));
                        mediaSourceList.add(current);
                    }
                    playlistItem.setSources(mediaSourceList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                playlistItem.setSources(null);
            }

            // Get ad schedule
            // TODO Uncomment if you have a license key that can play Ads
//            if (mAdsContainer.getChildCount() == 1) {
//                TextView tv = (TextView) mAdsContainer.getChildAt(0);
//                String ads = tv.getText().toString();
//                try {
//                    JSONArray jsonArray = new JSONArray(ads);
//                    List<AdBreak> adBreakList = new ArrayList<>();
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        AdBreak current = AdBreak.parseJson(jsonArray.getJSONObject(i));
//                        adBreakList.add(current);
//                    }
//                    playlistItem.setAdSchedule(adBreakList);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                playlistItem.setAdSchedule(null);
//            }

            // Get mCaptions
            if (mCaptionsContainer.getChildCount() == 1) {
                TextView tv = (TextView) mCaptionsContainer.getChildAt(0);
                String captions = tv.getText().toString();
                try {
                    JSONArray jsonArray = new JSONArray(captions);
                    List<Caption> captionList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Caption current = Caption.parseJson(jsonArray.getJSONObject(i));
                        captionList.add(current);
                    }
                    playlistItem.setCaptions(captionList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                playlistItem.setCaptions(null);
            }
            return playlistItem;
        }
        return null;
    }

    @Override
    protected void parseObject(PlaylistItem object) {
        Utils.clearViews(mPlaylistItemScreen.getListOfViews());

        mHttpHeadersView.clear();
        mHttpHeadersView.parseMap(object.getHttpHeaders());

        mFileSpinner.setSelection(Utils.handleObjectData(object.getFile()));

        mTitleET.setText(Utils.handleObjectData(object.getTitle()));
        mDescriptionET.setText(Utils.handleObjectData(object.getDescription()));
        mMediaIdET.setText(Utils.handleObjectData(object.getMediaId()));
        mImageET.setText(Utils.handleObjectData(object.getImage()));


        if (mCanPlayDrm) {
            if (object.getMediaDrmCallback() != null &&
                    object.getMediaDrmCallback().getClass() == WidevineMediaDrmCallback.class) {
                WidevineMediaDrmCallback drm = (WidevineMediaDrmCallback) object.getMediaDrmCallback();
                mDrmET.setText(drm.getLicenseKeyServerUrl());

            }
        }

        // TODO Uncomment if you have a license key that can play Ads
//        if (object.getAdSchedule() != null) {
//            JSONArray jsonArray = new JSONArray();
//            for (AdBreak current : object.getAdSchedule()) {
//                jsonArray.put(current.toJson());
//            }
//            if (mAdsContainer.getChildCount() == 0) {
//                TextView textView = new TextView(getApplicationContext());
//                textView.setText(jsonArray.toString());
//                mAdsContainer.addView(textView);
//            } else {
//                mAdsContainer.removeAllViews();
//                TextView textView = new TextView(getApplicationContext());
//                textView.setText(jsonArray.toString());
//                mAdsContainer.addView(textView);
//            }
//        } else {
//            mAdsContainer.removeAllViews();
//        }

        if (object.getSources() != null) {
            JSONArray jsonArray = new JSONArray();
            for (MediaSource current : object.getSources()) {
                jsonArray.put(current.toJson());
            }
            if (mSourcesContainer.getChildCount() == 0) {
                TextView textView = new TextView(getApplicationContext());
                textView.setText(jsonArray.toString());
                mSourcesContainer.addView(textView);
            } else {
                mSourcesContainer.removeAllViews();
                TextView textView = new TextView(getApplicationContext());
                textView.setText(jsonArray.toString());
                mSourcesContainer.addView(textView);
            }
        } else {
            mSourcesContainer.removeAllViews();
        }

        if (object.getTracks() != null) {
            JSONArray jsonArray = new JSONArray();
            for (Caption current : object.getTracks()) {
                jsonArray.put(current.toJson());
            }
            if (mCaptionsContainer.getChildCount() == 0) {
                TextView textView = new TextView(getApplicationContext());
                textView.setText(jsonArray.toString());
                mCaptionsContainer.addView(textView);
            } else {
                mCaptionsContainer.removeAllViews();
                TextView textView = new TextView(getApplicationContext());
                textView.setText(jsonArray.toString());
                mCaptionsContainer.addView(textView);
            }
        } else {
            mCaptionsContainer.removeAllViews();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mDrawerToggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    @Override
    protected void initDataStructures() {
        mPlaylistItemList = new ArrayList<>();
    }

    @Override
    protected void handleIntent(Intent intent) {
        if (master_config.getPlaylist() != null) {
            mPlaylistItemList = master_config.getPlaylist();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == JWApplication.QR_FILE_URL) {
                String result = data.getStringExtra(JWApplication.RESULT);
                mFileSpinner.setSelection(result);
            } else if (requestCode == JWApplication.LOCAL_VIDEO_FILE) {
                String path = "file://" + Utils.getVideoPathFromURI(this, data.getData());
                mFileSpinner.setSelection(path);

            } else if (requestCode == JWApplication.LOCAL_AUDIO_FILE) {
                String path = "file://" + Utils.getAudioPathFromURI(this, data.getData());
                mFileSpinner.setSelection(path);
                mFileSpinner.save();
            } else if (requestCode == JWApplication.QR_IMAGE_URL) {
                String result = data.getStringExtra(JWApplication.RESULT);
                mImageET.setText(result);

            } else if (requestCode == JWApplication.QR_DRM) {
                String result = data.getStringExtra(JWApplication.RESULT);
                mDrmET.setText(result);

            } else if (requestCode == JWApplication.LOCAL_IMAGE_FILE) {
                String path = "file://" + Utils.getImagePathFromURI(this, data.getData());
                mImageET.setText(path);

            }
            // TODO Uncomment if you have a license key that can play Ads
//            else if (requestCode == JWApplication.AD_BREAK_ACTIVITY) {
//                if (mAdsContainer.getChildCount() > 0) {
//                    mAdsContainer.removeAllViews();
//                }
//                String result = data.getStringExtra(JWApplication.RESULT);
//                try {
//                    JSONArray jsonArray = new JSONArray(result);
//                    if (jsonArray.length() > 0) {
//                        TextView textView = new TextView(getApplicationContext());
//                        textView.setText(result);
//                        mAdsContainer.addView(textView);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
            else if (requestCode == JWApplication.CAPTION_ACTIVITY) {
                if (mCaptionsContainer.getChildCount() > 0) {
                    mCaptionsContainer.removeAllViews();
                }
                String result = data.getStringExtra(JWApplication.RESULT);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    if (jsonArray.length() > 0) {
                        TextView textView = new TextView(getApplicationContext());
                        textView.setText(result);
                        mCaptionsContainer.addView(textView);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == JWApplication.MEDIA_SOURCE_ACTIVITY) {
                if (mSourcesContainer.getChildCount() > 0) {
                    mSourcesContainer.removeAllViews();
                }
                String result = data.getStringExtra(JWApplication.RESULT);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    if (jsonArray.length() > 0) {
                        TextView textView = new TextView(getApplicationContext());
                        textView.setText(result);
                        mSourcesContainer.addView(textView);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                mHttpHeadersView.onActivityResult(requestCode, resultCode, data);
            }

        }


    }
}
