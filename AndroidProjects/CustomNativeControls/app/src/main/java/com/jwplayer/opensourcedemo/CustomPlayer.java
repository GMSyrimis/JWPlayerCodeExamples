package com.jwplayer.opensourcedemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.core.PlayerState;
import com.longtailvideo.jwplayer.events.ErrorEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.adaptive.QualityLevel;
import com.longtailvideo.jwplayer.media.audio.AudioTrack;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by gsyrimis on 12/24/17.
 */

public class CustomPlayer extends JWPlayerView implements
        VideoPlayerEvents.OnPlaylistItemListener,
        VideoPlayerEvents.OnIdleListener,
        VideoPlayerEvents.OnPlayListener,
        VideoPlayerEvents.OnTimeListener,
        VideoPlayerEvents.OnPauseListener,
        VideoPlayerEvents.OnBufferListener,
        VideoPlayerEvents.OnLevelsListener,
        VideoPlayerEvents.OnAudioTracksListener,
        VideoPlayerEvents.OnCaptionsListListener,
        VideoPlayerEvents.OnErrorListenerV2,
        VideoPlayerEvents.OnPlaylistCompleteListener {

    // center
    private ProgressBar mBufferSpinner;
    private View mRewindButton;
    private View mPlayButton;
    private View mPauseButton;
    private View mNextButton;
    private View mPreviousButton;
    private View mRepeatButton;
    private boolean mShouldShowNext = false;
    private boolean mShouldShowPrevious = false;

    // controlbar
    private LinearLayout mControlBarContainer;
    private SeekBar mSeekBar;
    private TextView mTimerPosition;
    private View mFullscreenButton;

    // options
    private View mOpenOptionsButton;
    private RelativeLayout mOptionsLayout;
    private View mAudioButton;
    private View mCaptionsButton;
    private View mQualitiesButton;
    private ListView mOptionsList;
    private ArrayAdapter<String> mListAdapter;
    private final int QUALITIES_SELECTED = 1;
    private final int AUDIO_SELECTED = 2;
    private final int CAPTIONS_SELECTED = 3;
    private int mCurrentOptionSelection = QUALITIES_SELECTED;


    private final long REWIND_CONSTANT = 10000;

    // -2 because -1 is for livestreams
    private long mLastKnownDuration = -2;

    // Overlay logic
    private RelativeLayout mOverlay;
    private Runnable mHideOverlayRunnable;
    private final long AUTOHIDE_DELAY = 3000;
    private GestureDetector mGestureDetector;
    private boolean mCanHideOverlay = false;
    private boolean mCanUpdateProgress = true;


    public CustomPlayer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        inflate(context, R.layout.view_player_overlay, this);
        if (!this.isInEditMode()) {
            findViews();
            setupButtonBehavior();
            mGestureDetector = new GestureDetector(context, new GestureListener());
        }
    }

    public CustomPlayer(Context context, PlayerConfig playerConfig) {
        super(context, playerConfig);
        inflate(context, R.layout.view_player_overlay, this);
        if (!this.isInEditMode()) {
            findViews();
            setupButtonBehavior();
            mGestureDetector = new GestureDetector(context, new GestureListener());
        }
    }

    @Override
    public void setup(PlayerConfig playerConfig) {
        playerConfig.setControls(false);
        super.setup(playerConfig);
    }

    private void findViews() {
        // entire controls overlay
        mOverlay = (RelativeLayout) findViewById(R.id.jw_controls_root);

        // center controls
        mBufferSpinner = (ProgressBar) findViewById(R.id.jw_buffer_spinner);
        mRewindButton = findViewById(R.id.jw_rewind);
        mPlayButton = findViewById(R.id.jw_play);
        mPauseButton = findViewById(R.id.jw_pause);
        mNextButton = findViewById(R.id.jw_next);
        mPreviousButton = findViewById(R.id.jw_previous);
        mRepeatButton = findViewById(R.id.jw_repeat);

        // controlbar
        mControlBarContainer = (LinearLayout) findViewById(R.id.jw_controlbar_container);
        mSeekBar = (SeekBar) findViewById(R.id.jw_seek_bar);
        mTimerPosition = (TextView) findViewById(R.id.jw_timer_position);
        mOpenOptionsButton = findViewById(R.id.jw_open_options);
        mFullscreenButton = findViewById(R.id.jw_fullscreen);

        // Options
        mOptionsLayout = (RelativeLayout) findViewById(R.id.jw_options_layout);
        mOptionsList = (ListView) findViewById(R.id.jw_options_list);
        mAudioButton = findViewById(R.id.jw_audio);
        mCaptionsButton = findViewById(R.id.jw_captions);
        mQualitiesButton = findViewById(R.id.jw_qualities);
    }

    private void setupButtonBehavior() {
        // center controls
        mPlayButton.setVisibility(VISIBLE);
        mPauseButton.setVisibility(GONE);
        mBufferSpinner.setVisibility(GONE);
        mRewindButton.setVisibility(GONE);
        mNextButton.setVisibility(GONE);
        mPreviousButton.setVisibility(GONE);
        mRepeatButton.setVisibility(GONE);

        // controlbar
        mControlBarContainer.setVisibility(INVISIBLE);

        // overlay
        mCanHideOverlay = false;
        mOverlay.animate().alpha(1.0f);

        // center controls
        addOnPlayListener(this);
        addOnIdleListener(this);
        addOnPauseListener(this);
        addOnBufferListener(this);
        addOnPlaylistCompleteListener(this);
        mNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPlaylistIndex() + 1 < getPlaylist().size()) {
                    playlistItem(getPlaylistIndex() + 1);
                }
            }
        });
        mPreviousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPlaylistIndex() > 0) {
                    playlistItem(getPlaylistIndex() - 1);
                }
            }
        });
        mRewindButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPosition() > REWIND_CONSTANT) {
                    seek(getPosition() - REWIND_CONSTANT);
                } else {
                    seek(0L);
                }
            }
        });
        mPlayButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                play(true);
            }
        });
        mPauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pause(true);
            }
        });
        mRepeatButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setup(getConfig());
                play(true);
            }
        });

        // controlbar
        addOnTimeListener(this);
        addOnLevelsListener(this);
        addOnAudioTracksListener(this);
        addOnCaptionsListListener(this);
        addOnPlaylistItemListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mCanUpdateProgress = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seek(seekBar.getProgress() * 1000L);
                mCanUpdateProgress = true;
            }
        });

        mFullscreenButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setFullscreen(!getFullscreen(), true);
            }
        });

        mOpenOptionsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOptionsLayout.getVisibility() == INVISIBLE) {
                    // show cog options
                    mOptionsLayout.setVisibility(VISIBLE);
                    mCanHideOverlay = false;
                    updateList();
                } else {
                    mOptionsLayout.setVisibility(INVISIBLE);
                    mCanHideOverlay = true;
                }
            }
        });


        // options
        List<String> dummyData = new ArrayList<>();
        mListAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_selectable_list_item, dummyData);
        mOptionsList.setSelector(R.drawable.gradient_reverse);
        mOptionsList.setAdapter(mListAdapter);
        mOptionsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mQualitiesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentOptionSelection = QUALITIES_SELECTED;
                updateList();
            }
        });

        mAudioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentOptionSelection = AUDIO_SELECTED;
                updateList();
            }
        });

        mCaptionsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentOptionSelection = CAPTIONS_SELECTED;
                updateList();
            }
        });


    }

    private void updateList() {
        switch (mCurrentOptionSelection) {
            case AUDIO_SELECTED:
                mAudioButton.setBackgroundResource(R.drawable.audio);
                mQualitiesButton.setBackgroundResource(R.drawable.bars_dark);
                mCaptionsButton.setBackgroundResource(R.drawable.cc_dark);
                List<AudioTrack> audioTracks = getAudioTracks();
                List<String> audioLabels = new ArrayList<>();
                for (AudioTrack track : audioTracks) {
                    audioLabels.add(track.getName());
                }
                mListAdapter.clear();
                mListAdapter.addAll(audioLabels);
                mListAdapter.notifyDataSetChanged();
                mOptionsList.clearChoices();
                mOptionsList.setSelection(getCurrentAudioTrack());
                Log.d("George", "getCurrentAudioTrack=" + getCurrentAudioTrack());
                mOptionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mOptionsList.setSelection(position);
                        setCurrentAudioTrack(position);
                        mOptionsLayout.setVisibility(INVISIBLE);
                        play(true);
                    }
                });

                break;
            case QUALITIES_SELECTED:
                mQualitiesButton.setBackgroundResource(R.drawable.bars);
                mCaptionsButton.setBackgroundResource(R.drawable.cc_dark);
                mAudioButton.setBackgroundResource(R.drawable.audio_dark);
                List<QualityLevel> qualityLevels = getQualityLevels();
                List<String> qualityLabels = new ArrayList<>();
                for (QualityLevel track : qualityLevels) {
                    int height = track.getHeight();
                    if (height == -1) {
                        qualityLabels.add("Auto");
                    } else {
                        qualityLabels.add(String.valueOf(height));
                    }
                }
                mListAdapter.clear();
                mListAdapter.addAll(qualityLabels);
                mListAdapter.notifyDataSetChanged();
                mOptionsList.clearChoices();
                mOptionsList.setSelection(getCurrentQuality());
                Log.d("George", "getCurrentQuality=" + getCurrentQuality());

                mOptionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mOptionsList.setSelection(position);
                        setCurrentQuality(position);
                        mOptionsLayout.setVisibility(INVISIBLE);
                        play(true);
                    }
                });

                break;
            case CAPTIONS_SELECTED:
                mCaptionsButton.setBackgroundResource(R.drawable.cc);
                mAudioButton.setBackgroundResource(R.drawable.audio_dark);
                mQualitiesButton.setBackgroundResource(R.drawable.bars_dark);
                List<Caption> captionsList = getCaptionsList();
                List<String> captionLabels = new ArrayList<>();
                for (Caption track : captionsList) {
                    captionLabels.add(track.getLabel());
                }
                mListAdapter.clear();
                mListAdapter.addAll(captionLabels);
                mListAdapter.notifyDataSetChanged();
                mOptionsList.clearChoices();
                mOptionsList.setSelection(getCurrentCaptions());
                Log.d("George", "CurrentCaptions=" + getCurrentCaptions());
                mOptionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mOptionsList.setSelection(position);
                        setCurrentCaptions(position);
                        mOptionsLayout.setVisibility(INVISIBLE);
                        play(true);
                    }
                });
                break;
        }
    }


    @Override
    public void onTime(long positionMs, long durationMs) {

        // controlbar
        if (durationMs == -1) {
            // Stream is live
            if (mLastKnownDuration != durationMs) {
                mLastKnownDuration = durationMs;
                mSeekBar.setVisibility(GONE);
                mTimerPosition.setText("Live Broadcast");
            }
        } else {
            // Show timestamp
            int positionSec = (int) positionMs / 1000;
            int durationSec = (int) durationMs / 1000;

            String positionHMS = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(positionMs),
                    TimeUnit.MILLISECONDS.toMinutes(positionMs) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(positionMs)),
                    TimeUnit.MILLISECONDS.toSeconds(positionMs) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(positionMs)));

            String durationHMS = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(durationMs),
                    TimeUnit.MILLISECONDS.toMinutes(durationMs) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(durationMs)),
                    TimeUnit.MILLISECONDS.toSeconds(durationMs) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(durationMs)));

            if (durationHMS.indexOf("00:") == 0) {
                durationHMS = durationHMS.substring(3);
                positionHMS = positionHMS.substring(3);
            }

            mTimerPosition.setText(positionHMS + " / " + durationHMS);

            // set duration and recover from livestreams once
            if (mLastKnownDuration != durationMs) {
                mLastKnownDuration = durationMs;
                mSeekBar.setMax(durationSec);

                // recover from live stream once
                mSeekBar.setVisibility(VISIBLE);
            }

            if (mCanUpdateProgress) {
                mSeekBar.setProgress(positionSec, true);
            }
        }
    }

    @Override
    public void onPlay(PlayerState playerState) {
        // center controls
        mPlayButton.setVisibility(GONE);
        mPauseButton.setVisibility(VISIBLE);
        mBufferSpinner.setVisibility(GONE);
        mRewindButton.setVisibility(VISIBLE);
        mNextButton.setVisibility(mShouldShowNext ? VISIBLE : GONE);
        mPreviousButton.setVisibility(mShouldShowPrevious ? VISIBLE : GONE);
        mRepeatButton.setVisibility(GONE);

        // controlbar
        mControlBarContainer.setVisibility(VISIBLE);

        // overlay
        mCanHideOverlay = true;
        mOverlay.removeCallbacks(mHideOverlayRunnable);
        mOverlay.postDelayed(fadeOut(), AUTOHIDE_DELAY);
    }

    @Override
    public void onPause(PlayerState playerState) {
        // center controls
        mPlayButton.setVisibility(VISIBLE);
        mPauseButton.setVisibility(GONE);
        mBufferSpinner.setVisibility(GONE);
        mRewindButton.setVisibility(VISIBLE);
        mNextButton.setVisibility(mShouldShowNext ? VISIBLE : GONE);
        mPreviousButton.setVisibility(mShouldShowPrevious ? VISIBLE : GONE);
        mRepeatButton.setVisibility(GONE);

        // controlbar
        mControlBarContainer.setVisibility(VISIBLE);

        // overlay
        mCanHideOverlay = false;
    }


    @Override
    public void onBuffer(PlayerState playerState) {
        // center controls
        mPlayButton.setVisibility(GONE);
        mPauseButton.setVisibility(GONE);
        mBufferSpinner.setVisibility(VISIBLE);
        mRewindButton.setVisibility(GONE);
        mNextButton.setVisibility(GONE);
        mPreviousButton.setVisibility(GONE);
        mRepeatButton.setVisibility(GONE);

        // controlbar
        mControlBarContainer.setVisibility(INVISIBLE);

        // overlay
        mCanHideOverlay = false;
    }

    @Override
    public void onIdle(PlayerState playerState) {
        Log.d("GEORGE", "OnIdle");
        // center controls
        mPlayButton.setVisibility(VISIBLE);
        mPauseButton.setVisibility(GONE);
        mBufferSpinner.setVisibility(GONE);
        mRewindButton.setVisibility(GONE);
        mNextButton.setVisibility(GONE);
        mPreviousButton.setVisibility(GONE);
        mRepeatButton.setVisibility(GONE);

        // controlbar
        mControlBarContainer.setVisibility(INVISIBLE);

        // overlay
        mCanHideOverlay = false;
        mOverlay.animate().alpha(1.0f);
    }

    @Override
    public void onPlaylistItem(int i, PlaylistItem playlistItem) {
        // should we show the "next" icon in the center controls
        mShouldShowNext = i + 1 < getPlaylist().size();
        mShouldShowPrevious = i > 0;
    }

    @Override
    public void onError(ErrorEvent errorEvent) {
        Log.d("George", errorEvent.getMessage());
        // center controls
        mPlayButton.setVisibility(GONE);
        mPauseButton.setVisibility(GONE);
        mBufferSpinner.setVisibility(GONE);
        mRewindButton.setVisibility(GONE);
        mNextButton.setVisibility(GONE);
        mPreviousButton.setVisibility(GONE);
        mRepeatButton.setVisibility(VISIBLE);
    }

    @Override
    public void onPlaylistComplete() {
        Log.d("George", "playistComplete");
        // center controls
        mPlayButton.setVisibility(GONE);
        mPauseButton.setVisibility(GONE);
        mBufferSpinner.setVisibility(GONE);
        mRewindButton.setVisibility(GONE);
        mNextButton.setVisibility(GONE);
        mPreviousButton.setVisibility(GONE);
        mRepeatButton.setVisibility(VISIBLE);
    }

    @Override
    public void onLevels(List<QualityLevel> list) {
        // options
        if (list.size() > 1) {
            mQualitiesButton.setVisibility(VISIBLE);
        } else {
            mQualitiesButton.setVisibility(GONE);
        }

    }

    @Override
    public void onAudioTracks(List<AudioTrack> list) {
        // options
        if (list.size() > 1) {
            mAudioButton.setVisibility(VISIBLE);
        } else {
            mAudioButton.setVisibility(GONE);
        }
    }

    @Override
    public void onCaptionsList(List<Caption> list) {
        // options
        if (list.size() > 1) {
            mCaptionsButton.setVisibility(VISIBLE);
        } else {
            mCaptionsButton.setVisibility(GONE);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Handle touch events to manage the overlay showing / hiding
        return mGestureDetector.onTouchEvent(ev);
    }

    private Runnable fadeOut() {
        // hide the overlay when it is permitted
        mHideOverlayRunnable = new Runnable() {
            @Override
            public void run() {
                if (mCanHideOverlay) {
                    mOverlay.animate().alpha(0.0f);
                }
            }
        };
        return mHideOverlayRunnable;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Touch events are affecting the views as they are fading in. You need to handle
            // overlay
            mOverlay.animate().alpha(1.0f);
            mOverlay.removeCallbacks(mHideOverlayRunnable);
            mOverlay.postDelayed(fadeOut(), AUTOHIDE_DELAY);

            // handle indefinite buffer quirks
            if (getState() == PlayerState.BUFFERING) {
                mPlayButton.setVisibility(VISIBLE);
            }
            return super.onDown(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

    }


}
