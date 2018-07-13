package com.jwplayer.opensourcedemo;

import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

/**
 * Outputs all JW Player Events to logging, with the exception of time events.
 */
public class JWEventHandler implements VideoPlayerEvents.OnSeekedListener,
        VideoPlayerEvents.OnSeekListener {

    private TextView mOutput;
    private JWPlayerView mPlayerView;

    public JWEventHandler(JWPlayerView jwPlayerView, TextView output) {
        mOutput = output;
        mPlayerView = jwPlayerView;
        // Subscribe to all JW Player events
        mPlayerView.addOnSeekListener(this);
        mPlayerView.addOnSeekedListener(this);
    }

    private void updateOutput(String output) {
        mOutput.append(output +"\n");
    }

    /**
     * Regular playback events below here
     */

    @Override
    public void onSeek(int position, int offset) {
        updateOutput("onSeek(" + position + ", " + offset + ")");
    }

    @Override
    public void onSeeked() {
        updateOutput("onSeeked(\"" + "seeked" + "\")" +
                " getPosition()=" + mPlayerView.getPosition());
    }
}
