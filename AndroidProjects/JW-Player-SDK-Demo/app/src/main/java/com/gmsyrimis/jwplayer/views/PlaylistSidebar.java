package com.gmsyrimis.jwplayer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.gmsyrimis.jwplayer.R;


/**
 * This class handles the PlaylistSidebar in the PlaylistActivity
 * We broke this into a separate class to keep the ListView logic outside of the Activity
 * Steps to create:
 * 1) Create layout in view_ naming format
 */


public class PlaylistSidebar extends LinearLayout {

    public PlaylistSidebar(Context context) {
        super(context);
        inflate(context, R.layout.view_playlist_sidebar,this);
        // Get Display Metrics

    }

    public PlaylistSidebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_playlist_sidebar,this);

    }

    public PlaylistSidebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_playlist_sidebar,this);

    }
}
