package com.gmsyrimis.jwplayer.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gmsyrimis.jwplayer.R;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.core.PlayerState;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

import java.util.concurrent.TimeUnit;



/**
 * Created by gsyrimis on 6/22/16.
 */
public class JWControlBar extends LinearLayout implements
        VideoPlayerEvents.OnCompleteListener,
        VideoPlayerEvents.OnPauseListener,
        VideoPlayerEvents.OnPlayListener,
        VideoPlayerEvents.OnTimeListener {

    private SeekBar mSeekBar;
    private TextView mTimer;
    private ImageButton mStopBtn;
    private ImageButton mPlayBtn;
    private ImageButton mPauseBtn;
    private ImageButton mQualityBtn;
    private ImageButton mCaptionBtn;
    private ImageButton mAudioBtn;

    private JWPlayerView mPlayerView;

    public JWControlBar(Context context) {
        super(context);
        inflate(context, R.layout.view_jw_controlbar, this);
        setBackgroundColor(Color.WHITE);
        setOrientation(LinearLayout.HORIZONTAL);
        findViews();
    }

    public JWControlBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_jw_controlbar, this);
        setBackgroundColor(Color.WHITE);
        setOrientation(LinearLayout.HORIZONTAL);
        findViews();
    }

    public JWControlBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_jw_controlbar, this);
        setBackgroundColor(Color.WHITE);
        setOrientation(LinearLayout.HORIZONTAL);
        findViews();
    }

    private void findViews() {
        mSeekBar = (SeekBar) findViewById(R.id.jw_seek_bar);
        mTimer = (TextView) findViewById(R.id.jw_timer_tv);
        mStopBtn = (ImageButton) findViewById(R.id.jw_stop_btn);
        mPlayBtn = (ImageButton) findViewById(R.id.jw_play_btn);
        mPauseBtn = (ImageButton) findViewById(R.id.jw_pause_btn);
        mQualityBtn = (ImageButton) findViewById(R.id.jw_quality_btn);
        mAudioBtn = (ImageButton) findViewById(R.id.jw_audio_btn);
        mCaptionBtn = (ImageButton) findViewById(R.id.jw_captions_btn);
    }

    private void setViewInteractions() {
        // HANDLE SEEK BAR TOUCH GESTURES
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayerView.seek((long) seekBar.getProgress() * 1000 * 16);
            }
        });


        // BUILD BUTTONS
        mPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayerView.pause(true);
            }
        });

        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayerView.play(true);
            }
        });

        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayerView.stop();
                mSeekBar.setProgress(0);
                mPauseBtn.setVisibility(View.GONE);
                mPlayBtn.setVisibility(View.VISIBLE);
            }
        });

        mQualityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                findViewById(R.id.main_quality_bar).setVisibility(View.VISIBLE);
//                findViewById(R.id.main_low_quality).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getApplicationContext(), "LOW", Toast.LENGTH_SHORT).show();
//                        mPlayerView.setCurrentQuality(0);
//                        findViewById(R.id.main_quality_bar).setVisibility(View.GONE);
//                    }
//                });
//                findViewById(R.id.main_mid_quality).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getApplicationContext(), "MID", Toast.LENGTH_SHORT).show();
//                        mPlayerView.setCurrentQuality(1);
//                        findViewById(R.id.main_quality_bar).setVisibility(View.GONE);
//                    }
//                });
//                findViewById(R.id.main_high_quality).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getApplicationContext(), "HIGH", Toast.LENGTH_SHORT).show();
//                        mPlayerView.setCurrentQuality(2);
//                        findViewById(R.id.main_quality_bar).setVisibility(View.GONE);
//                    }
//                });
            }
        });


    }


    public void setPlayer(JWPlayerView player) {
        mPlayerView = player;
        mPlayerView.addOnCompleteListener(this);
        mPlayerView.addOnPauseListener(this);
        mPlayerView.addOnPlayListener(this);
        mPlayerView.addOnTimeListener(this);
        setViewInteractions();
    }

    @Override
    public void onComplete() {
        mPauseBtn.setVisibility(View.GONE);
        mPlayBtn.setVisibility(View.VISIBLE);
        mSeekBar.setProgress(0);
    }

    @Override
    public void onPause(PlayerState playerState) {
        mPauseBtn.setVisibility(View.GONE);
        mPlayBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPlay(PlayerState playerState) {
        mPauseBtn.setVisibility(View.VISIBLE);
        mPlayBtn.setVisibility(View.GONE);
    }

    @Override
    public void onTime(long l, long l1) {
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(l),
                TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l)),
                TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
        mTimer.setText(hms);

        int i = Math.round(l / 1000);
        int i1 = Math.round(l1 / 1000);

        if (i % 16 >= 0 && i % 16 <= 15) {
            mSeekBar.setMax(i1 / 16);
            mSeekBar.setProgress(i / 16);
        }

    }
}
