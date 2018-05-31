package com.gmsyrimis.jwplayer;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;


/**
 * Created by gsyrimis on 11/13/15.
 */
public class JWApplication extends Application {

    public static PlayerConfig master_config = new PlayerConfig.Builder().build();

    public static final String RESULT = "result";
    public static final String LICENSE_KEY = "LICENSE_KEY";
    public static final String PLAYLIST_ITEM_INDEX = "PLAYLIST_ITEM_INDEX";
    public static final String IS_PLAYLIST_ITEM = "IS_PLAYLIST_ITEM";
    public static final int NEW_PLAYLIST_ITEM = -1;

    public static final int PLAYLIST_ITEM_ACTIVITY = 230;
    public static final int AD_BREAK_ACTIVITY = 231;
    public static final int CAPTION_ACTIVITY = 232;
    public static final int MEDIA_SOURCE_ACTIVITY = 233;
    public static final int PLAYER_CONFIG_ACTIVITY = 234;
    public static final int VAST_ADVERTISING_ACTIVITY = 236;
    public static final int CAPTION_STYLE_ACTIVITY = 237;
    public static final int LOGO_ACTIVITY = 238;
    public static final int SKIN_ACTIVITY = 239;
    public static final int RELATED_ACTIVITY = 240;
    public static final int IMA_ADVERTISING_ACTIVITY = 241;

    public static final int QR_AD_URL = 120;
    public static final int QR_CAPTION_URL = 121;
    public static final int QR_IMAGE_URL = 122;
    public static final int QR_FILE_URL = 123;
    public static final int QR_SOURCE_URL = 124;
    public static final int QR_LICENSE_KEY = 125;
    public static final int QR_SKIN_URL = 126;
    public static final int QR_PLAY_AD_URL = 127;
    public static final int QR_PLAYLIST_ITEM = 128;
    public static final int QR_HTTP_HEADER_KEY = 129;
    public static final int QR_DRM = 130;
    public static final int QR_HTTP_HEADER_VALUE = 131;

    public static final int LOCAL_VIDEO_FILE = 300;
    public static final int LOCAL_IMAGE_FILE = 301;
    public static final int LOCAL_CAPTION_FILE = 302;
    public static final int LOCAL_SKIN_FILE = 303;
    public static final int LOCAL_PLAYLIST_ITEM = 304;
    public static final int LOCAL_AUDIO_FILE = 305;


    public static final String CONFIG_FILE_URLS = "CONFIG_FILE_URLS";
    public static final String MAIN_SKIN_URLS = "MAIN_SKIN_URLS";
    public static final String MAIN_PLAY_AD_URLS = "MAIN_PLAY_AD_URLS";
    public static final String MAIN_PLAYER_CONFIGS = "MAIN_PLAYER_CONFIGS";
    public static final String MAIN_LOAD_PLAYLIST_ITEMS = "MAIN_LOAD_PLAYLIST_ITEMS";


    public static final String LABEL = "label";
    public static final String PAYLOAD = "payload";


    @Override
    public void onCreate() {
        super.onCreate();
        CastManager.initialize(this);
        MultiDex.install(this);
        Picasso picasso = new Picasso.Builder(this)
                .memoryCache(new LruCache(this))
                .downloader(new OkHttpDownloader(this))
                .build();
        Picasso.setSingletonInstance(picasso);
    }
}
