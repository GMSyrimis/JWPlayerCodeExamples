package com.gmsyrimis.jwplayer.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.gmsyrimis.jwplayer.JWApplication;
import com.gmsyrimis.jwplayer.R;
import com.gmsyrimis.jwplayer.dialogs.ShareDialog;
import com.gmsyrimis.jwplayer.utilities.Utils;
import com.gmsyrimis.jwplayer.views.ApiSidebar;
import com.gmsyrimis.jwplayer.views.CallbackScreen;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.configuration.Skin;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

import static com.gmsyrimis.jwplayer.JWApplication.LICENSE_KEY;
import static com.gmsyrimis.jwplayer.JWApplication.master_config;


public class MainActivity extends AppCompatActivity implements
        VideoPlayerEvents.OnFullscreenListener,
        ActivityCompat.OnRequestPermissionsResultCallback {


    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;


    // VIEWS
    private JWPlayerView mPlayerView;

    // NAVIGATION DRAWER
    private ActionBar mActionBar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ApiSidebar mApiSidebar;

    private CallbackScreen mCallbackScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();
        JWPlayerView.setLicenseKey(this, getString(R.string.free_key));
        setupNewPlayer(updateConfig());
        setupNavDrawer();
        setupCallbacks();
        requestPermissions();

    }

    private void setupActionBar() {
        // Setup Actionbar
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(getString(R.string.app_name));
        }
    }

    private PlayerConfig updateConfig() {
//        TODO Uncomment if you have a license key that can play Ads
//        List<AdBreak> emptyAdList = new ArrayList<>();
//        Advertising advertising = new Advertising(AdSource.VAST, emptyAdList);

        List<PlaylistItem> playlist = new ArrayList<>();
        PlaylistItem video = new PlaylistItem("http://content.jwplatform.com/manifests/njFhMDbi.m3u8");
        video.setImage("http://content.jwplatform.com/thumbs/njFhMDbi-720.jpg");
        video.setMediaId("njFhMDbi");
        video.setTitle("JW Player");
        video.setDescription("Press play");
        playlist.add(video);

        // create config
        master_config = new PlayerConfig.Builder()
                .playlist(playlist)
                //        TODO Uncomment if you have a license key that can play Ads
//                .advertising(advertising)
                .skin(Skin.FIVE)
                .skinBackground("")
                .skinActive("")
                .skinInactive("")
                .build();

        return master_config;
    }

    private void setupNewPlayer(PlayerConfig playerConfig) {

        LinearLayout jwContainer = (LinearLayout) findViewById(R.id.jw_container);
        if (jwContainer.getChildCount() > 0) {
            mPlayerView.onPause();
            mPlayerView.onDestroy();
            mPlayerView = null;
            jwContainer.removeAllViews();
        }


        mPlayerView = new JWPlayerView(this, playerConfig);

        // set layout params
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int playerWidth = displayMetrics.widthPixels;
        int playerHeight = (playerWidth * 9) / 16;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(playerWidth, playerHeight);
        mPlayerView.setLayoutParams(params);
        mPlayerView.addOnFullscreenListener(this);

        jwContainer.addView(mPlayerView);


    }


    private void setupNavDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mApiSidebar = (ApiSidebar) findViewById(R.id.left_drawer);
        mApiSidebar.initialize(this, mPlayerView, mDrawerLayout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.menu, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                mActionBar.setTitle(getString(R.string.app_name));
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                mActionBar.setTitle("JWPlayerView API");
                invalidateOptionsMenu();
            }
        };


        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }


    private void setupCallbacks() {
        // Setup Callbacks
        mCallbackScreen = (CallbackScreen) findViewById(R.id.callback_screen);
        mCallbackScreen.registerListeners(mPlayerView);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        mDrawerToggle.onConfigurationChanged(newConfig);
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        // Let JW Player know that the app has returned from the background
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        // Let JW Player know that the app is going to the background
        mPlayerView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // Let JW Player know that the app is being destroyed
        mPlayerView.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Exit fullscreen when the user pressed the Back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mPlayerView.getFullscreen()) {
                mPlayerView.setFullscreen(false, false);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        CastManager.getInstance().addMediaRouterButton(menu, R.id.media_route_menu_item);
        return true;
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

        switch (item.getItemId()) {

            case R.id.edit_player_config:
                Intent i = new Intent(getApplicationContext(), com.gmsyrimis.jwplayer.activities.PlayerConfigActivity.class);
                startActivityForResult(i, JWApplication.PLAYER_CONFIG_ACTIVITY);
                return true;

            case R.id.share_player_config:
                ShareDialog shareDialog = new ShareDialog(MainActivity.this, mPlayerView.getConfig(), mCallbackScreen.getCallbackLog(), mPlayerView.getVersionCode());
                shareDialog.show();
                return true;

        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra(JWApplication.RESULT);
            switch (requestCode) {
                case JWApplication.PLAYER_CONFIG_ACTIVITY:
                    String licenseKey = data.getStringExtra(LICENSE_KEY);
                    if (!LICENSE_KEY.isEmpty()) {
                        JWPlayerView.setLicenseKey(this, licenseKey);
                        Log.d("GEORGE", licenseKey);
                    }
                    setupNewPlayer(master_config);
                    mApiSidebar.setJWPlayerView(mPlayerView);
                    mCallbackScreen.setJWPlayerView(mPlayerView);
                    break;
                case JWApplication.QR_SKIN_URL:
                    mApiSidebar.getSkinStringSpinner().setSelection(result);
                    mApiSidebar.getSkinStringSpinner().save();
                    break;

                case JWApplication.QR_PLAY_AD_URL:
                    mApiSidebar.getPlayAdSpinner().setSelection(result);
                    mApiSidebar.getPlayAdSpinner().save();
                    break;

                case JWApplication.LOCAL_SKIN_FILE:
                    result = Utils.getDocumentsFilePath(this, data.getData());
                    mApiSidebar.getSkinStringSpinner().setSelection(result);
                    mApiSidebar.getSkinStringSpinner().save();
                    break;

                case JWApplication.LOCAL_PLAYLIST_ITEM:
                    result = "file://" + Utils.getVideoPathFromURI(this, data.getData());
                    mApiSidebar.getLoadSpinner().setSelection(new PlaylistItem(result).toJson().toString());
                    mApiSidebar.getLoadSpinner().save();
                    break;

                case JWApplication.QR_PLAYLIST_ITEM:
                    mApiSidebar.getLoadSpinner().setSelection(new PlaylistItem(result).toJson().toString());
                    mApiSidebar.getLoadSpinner().save();
                    break;


            }

        }
    }


    @Override
    public void onFullscreen(boolean isFullscreen) {
        mApiSidebar.mSetFullscreen.setChecked(isFullscreen);
        if (isFullscreen) {
            mActionBar.hide();

        } else {
            mActionBar.show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (!(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Snackbar.make(this.findViewById(R.id.root), "Camera permission denied",
                        Snackbar.LENGTH_SHORT).show();
            } else {
                requestLocalStorage();
            }
        }
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (!(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Snackbar.make(findViewById(R.id.root), "Local Storage Denied",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_EXTERNAL_STORAGE);
                            }
                        })
                        .show();
            }
        }
    }


    private void requestPermissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            requestCamera();
        }

    }

    private void requestCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                MainActivity.this, Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Snackbar.make(findViewById(R.id.root), "Camera permission required to read QR Code",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
    }

    private void requestLocalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Snackbar.make(findViewById(R.id.root), "Local storage permission required to load videos and images",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


}
