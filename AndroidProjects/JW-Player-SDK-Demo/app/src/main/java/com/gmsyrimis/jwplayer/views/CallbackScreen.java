package com.gmsyrimis.jwplayer.views;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmsyrimis.jwplayer.R;
import com.gmsyrimis.jwplayer.utilities.Utils;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.core.PlayerState;
import com.longtailvideo.jwplayer.events.AdClickEvent;
import com.longtailvideo.jwplayer.events.AdCompanionEvent;
import com.longtailvideo.jwplayer.events.AdCompleteEvent;
import com.longtailvideo.jwplayer.events.AdImpressionEvent;
import com.longtailvideo.jwplayer.events.AdPauseEvent;
import com.longtailvideo.jwplayer.events.AdPlayEvent;
import com.longtailvideo.jwplayer.events.AdRequestEvent;
import com.longtailvideo.jwplayer.events.AdSkippedEvent;
import com.longtailvideo.jwplayer.events.AdTimeEvent;
import com.longtailvideo.jwplayer.events.BufferChangeEvent;
import com.longtailvideo.jwplayer.events.ControlsEvent;
import com.longtailvideo.jwplayer.events.ErrorEvent;
import com.longtailvideo.jwplayer.events.RelatedCloseEvent;
import com.longtailvideo.jwplayer.events.RelatedOpenEvent;
import com.longtailvideo.jwplayer.events.RelatedPlayEvent;
import com.longtailvideo.jwplayer.events.listeners.AdvertisingEvents;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.adaptive.QualityLevel;
import com.longtailvideo.jwplayer.media.adaptive.VisualQuality;
import com.longtailvideo.jwplayer.media.ads.AdCompanion;
import com.longtailvideo.jwplayer.media.ads.AdPosition;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.audio.AudioTrack;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.meta.Metadata;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Outputs all JW Player Events to logging, with the exception of time events.
 */
public class CallbackScreen extends LinearLayout implements
        VideoPlayerEvents.OnIdleListener,
        VideoPlayerEvents.OnBufferListener,
        VideoPlayerEvents.OnPlayListener,
        VideoPlayerEvents.OnPauseListener,
        VideoPlayerEvents.OnTimeListener,
        VideoPlayerEvents.OnCompleteListener,
        VideoPlayerEvents.OnErrorListenerV2,
        VideoPlayerEvents.OnSeekListener,
        VideoPlayerEvents.OnFullscreenListener,
        VideoPlayerEvents.OnAudioTracksListener,
        VideoPlayerEvents.OnAudioTrackChangedListener,
        VideoPlayerEvents.OnLevelsListener,
        VideoPlayerEvents.OnLevelsChangedListener,
        VideoPlayerEvents.OnSeekedListener,
        VideoPlayerEvents.OnFirstFrameListener,
        VideoPlayerEvents.OnDisplayClickListener,
        VideoPlayerEvents.OnPlaylistCompleteListener,
        VideoPlayerEvents.OnMetaListener,
        VideoPlayerEvents.OnCaptionsChangedListener,
        VideoPlayerEvents.OnCaptionsListListener,
        VideoPlayerEvents.OnPlaylistItemListener,
        VideoPlayerEvents.OnPlaylistListener,
        VideoPlayerEvents.OnSetupErrorListener,
        VideoPlayerEvents.OnMuteListener,
        VideoPlayerEvents.OnVisualQualityListener,
        VideoPlayerEvents.OnControlsListener,
        VideoPlayerEvents.OnBufferChangeListener,
        VideoPlayerEvents.OnRelatedCloseListener,
        VideoPlayerEvents.OnRelatedOpenListener,
        VideoPlayerEvents.OnRelatedPlayListener,


        AdvertisingEvents.OnAdTimeListenerV2,
        AdvertisingEvents.OnAdPlayListenerV2,
        AdvertisingEvents.OnAdPauseListenerV2,
        AdvertisingEvents.OnAdImpressionListenerV2,
        AdvertisingEvents.OnAdClickListenerV2,
        AdvertisingEvents.OnAdErrorListener,
        AdvertisingEvents.OnAdCompleteListenerV2,
        AdvertisingEvents.OnAdSkippedListenerV2,
        AdvertisingEvents.OnAdRequestListenerV2,
        AdvertisingEvents.OnBeforeCompleteListener,
        AdvertisingEvents.OnBeforePlayListener,
        AdvertisingEvents.OnAdStartedListener,
        AdvertisingEvents.OnAdCompanionsListener {


    // CALLBACKS
    private TextView mCallbackLog;
    private TextView mCallbackPlayerVersion;
    private ArrayList<CheckBox> mCallbacksList;
    private CheckBox mCheckAll;
    private CheckBox mOnIdleCheckBox;
    private CheckBox mOnBufferingCheckBox;
    private CheckBox mOnPlayCheckBox;
    private CheckBox mOnPauseCheckBox;
    private CheckBox mOnCompleteCheckBox;
    private CheckBox mOnErrorCheckBox;
    private CheckBox mOnFullscreenCheckBox;
    private CheckBox mOnSeekCheckBox;
    private CheckBox mOnTimeCheckBox;
    private CheckBox mOnAdPlayCheckBox;
    private CheckBox mOnAdPauseCheckBox;
    private CheckBox mOnAdCompleteCheckBox;
    private CheckBox mOnAdErrorCheckBox;
    private CheckBox mOnAdSkipCheckBox;
    private CheckBox mOnAdTimeCheckBox;
    private CheckBox mOnAdClickCheckBox;
    private CheckBox mOnAdImpressionCheckBox;
    private CheckBox mOnAdRequestCheckBox;
    private CheckBox mOnAudioTracksCheckBox;
    private CheckBox mOnAudioTracksChangedCheckBox;
    private CheckBox mOnLevelsCheckBox;
    private CheckBox mOnLevelsChangedCheckBox;
    private CheckBox mOnSeekedCheckBox;
    private CheckBox mOnFirstFrameCheckBox;
    private CheckBox mOnDisplayClickCheckBox;
    private CheckBox mOnPlaylistCompleteCheckBox;
    private CheckBox mOnMetaCheckBox;
    private CheckBox mOnCaptionsChangedCheckBox;
    private CheckBox mOnCaptionsListCheckBox;
    private CheckBox mOnPlaylistItemCheckBox;
    private CheckBox mOnPlaylistCheckBox;
    private CheckBox mOnSetupErrorCheckBox;
    private CheckBox mOnBeforeCompleteCheckBox;
    private CheckBox mOnBeforePlayCheckBox;
    private CheckBox mOnMuteCheckBox;
    private CheckBox mOnVisualQualityCheckBox;
    private CheckBox mOnAdStartedCheckBox;
    private CheckBox mTimeSeconds;
    private CheckBox mOnControls;
    private CheckBox mOnBufferChange;
    private CheckBox mOnRelatedClose;
    private CheckBox mOnRelatedOpen;
    private CheckBox mOnRelatedPlay;
    private CheckBox mOnAdCompanion;

    private CallbackScreen mCallbackScreen;


    private JWPlayerView mPlayerView;


    public CallbackScreen(Context context) {
        super(context);
        inflate(getContext(), R.layout.view_callback_screen, this);
        mCallbackScreen = this;
    }

    public CallbackScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.view_callback_screen, this);
        mCallbackScreen = this;
    }

    public CallbackScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_callback_screen, this);
        mCallbackScreen = this;
    }


    @Override
    public void onFirstFrame(int i) {
        String output = "/// onFirstFrame START ///" + "\n" +
                "First Frame:" + "\n" +
                i + "ms" + "\n" +
                "/// onFirstFrame END ///";
        setOutput(output);
    }

    @Override
    public void onBeforeComplete() {
        String output = "/// onBeforeComplete START ///" + "\n" +
                "/// onBeforeComplete END ///";
        setOutput(output);
    }

    @Override
    public void onBeforePlay() {
        String output = "/// onBeforePlay START ///" + "\n" +
                "/// onBeforePlay END ///";
        setOutput(output);
    }

    @Override
    public void onPlaylistComplete() {
        String output = "/// onPlaylistComplete START ///" + "\n" +
                "/// onPlaylistComplete END ///";
        setOutput(output);
    }

    @Override
    public void onDisplayClick() {
        String output = "/// onDisplayClick START ///" + "\n" +
                "/// onDisplayClick END ///";
        setOutput(output);
    }

    @Override
    public void onAudioTracks(List<AudioTrack> list) {
        String prefix = "/// onAudioTracks START ///" + "\n";
        String body = "";

        for (AudioTrack currentTrack : list) {
            String separator = "-------------" + "\n";
            String name = "Name: " + currentTrack.getName() + "\n";
            String lang = "Language: " + currentTrack.getLanguage() + "\n";
            String groupId = "Group ID: " + currentTrack.getGroupId() + "\n";
            String autoSelect = "Autoselect: " + currentTrack.isAutoSelect() + "\n";
            String defaultTrack = "Default: " + currentTrack.isDefaultTrack() + "\n";
            body += name + lang + groupId + autoSelect + defaultTrack + separator;
        }

        String suffix = "\n" + "/// onAudioTracks END ///";
        setOutput(prefix + body + suffix);
    }

    @Override
    public void onCaptionsList(List<Caption> list) {
        String prefix = "/// onCaptionsList START ///" + "\n";
        String body = "";

        for (Caption currentCaption : list) {
            String separator = "-------------" + "\n";
            String isDefault = "";
            String file = "";
            String label = "";
            String kind = "";
            if (currentCaption.getFile() != null)
                file = "File: " + currentCaption.getFile() + "\n";
            if (currentCaption.getLabel() != null)
                label = "Label: " + currentCaption.getLabel() + "\n";
            if (currentCaption.getKind() != null)
                kind = "Kind: " + currentCaption.getKind().name() + "\n";
            isDefault = "isDefault: " + currentCaption.isDefault() + "\n";

            body += file + label + kind + isDefault + separator;
        }

        String suffix = "\n" + "/// onCaptionsList END ///";
        setOutput(prefix + body + suffix);
    }

    @Override
    public void onPlaylist(List<PlaylistItem> list) {
        String prefix = "/// onPlaylist START ///" + "\n";
        String body = "";

        for (PlaylistItem currentItem : list) {
            String separator = "-------------" + "\n";
            String item = currentItem.toJson().toString();

            body += item + separator;
        }

        String suffix = "\n" + "/// onPlaylist END ///";
        setOutput(prefix + body + suffix);
    }

    @Override
    public void onMeta(Metadata metadata) {
        String prefix = "/// onMeta START ///" + "\n";
        String body = "";

        String videoID = "Video ID: " + metadata.getVideoId() + "\n";
        String videoMimeType = "Video MimeType: " + metadata.getVideoMimeType() + "\n";
        String videoBitrate = "Video Bitrate: " + metadata.getVideoBitrate() + "\n";
        String framerate = "Framerate: " + metadata.getFramerate() + "\n";
        String droppedFrames = "Dropped Frames: " + metadata.getDroppedFrames() + "\n";
        String width = "Width: " + metadata.getWidth() + "\n";
        String height = "Height: " + metadata.getHeight() + "\n";
        String lang = "Language: " + metadata.getLanguage() + "\n";
        String audioID = "Audio ID: " + metadata.getAudioId() + "\n";
        String audioMimeType = "Audio MimeType: " + metadata.getAudioMimeType() + "\n";
        String audioBitrate = "Audio Bitrate: " + metadata.getAudioBitrate() + "\n";
        String audioChannels = "Audio Channels: " + metadata.getAudioChannels() + "\n";
        String audioSamplingRate = "Audio Sampling Rate: " + metadata.getAudioSamplingRate() + "\n";

        String meta = "Metadata:\n" + metadata.getId3Metadata().toString() + "\n";

        body += videoID + videoMimeType + videoBitrate + framerate + droppedFrames + width + height + lang + audioID + audioMimeType + audioBitrate + audioChannels + audioSamplingRate + meta;

        String suffix = "\n" + "/// onMeta END ///";

        setOutput(prefix + body + suffix);
    }

    @Override
    public void onLevels(List<QualityLevel> list) {
        String prefix = "/// onLevels START ///" + "\n";
        String body = "";

        for (QualityLevel currentLevel : list) {
            String separator = "-------------" + "\n";
            String index = "Track Index: " + currentLevel.getTrackIndex() + "\n";
            String label = "Label: " + currentLevel.getLabel() + "\n";
            String bitrate = "Bitrate: " + currentLevel.getBitrate() + "\n";
            String height = "Height: " + currentLevel.getHeight() + "\n";
            String width = "Width: " + currentLevel.getWidth() + "\n";
            String playlistPosition = "Playlist Position: " + currentLevel.getPlaylistPosition() + "\n";
            body += index + label + bitrate + height + width + playlistPosition + separator;
        }

        String suffix = "\n" + "/// onLevels END ///";
        setOutput(prefix + body + suffix);
    }

    @Override
    public void onAudioTrackChanged(int i) {
        String output = "/// onAudioTrackChanged START ///" + "\n" +
                "Current AudioTrack Index:" + "\n" +
                i + "\n" +
                "/// onAudioTrackChanged END ///";
        setOutput(output);
    }

    @Override
    public void onLevelsChanged(int i) {
        String output = "/// onLevelsChanged START ///" + "\n" +
                "Current Level Index:" + "\n" +
                i + "\n" +
                "/// onLevelsChanged END ///";
        setOutput(output);
    }

    @Override
    public void onCaptionsChanged(int i, List<Caption> list) {
        String output = "/// onCaptionsChanged START ///" + "\n" +
                "Current Caption Index:" + "\n" +
                i + "\n" +
                "/// onCaptionsChanged END ///";
        setOutput(output);
    }

    @Override
    public void onPlaylistItem(int i, PlaylistItem playlistItem) {
        String output = "/// onPlaylistItem START ///" + "\n" +
                "Current PlaylistItem Index:" + "\n" +
                i + "\n" +
                "PlaylistItem JSON:" + "\n" +
                playlistItem.toJson().toString() + "\n" +
                "/// onPlaylistItem END ///";
        setOutput(output);
    }

    @Override
    public void onAdRequest(AdRequestEvent event) {
        String client = event.getClient().name();
        String tag = event.getTag();
        AdPosition adPosition = event.getAdPosition();
        String offset = event.getOffset();


        String output = "/// onAdRequest START ///" + "\n" +
                "Tag:" + "\n" +
                tag + "\n" +
                "Ad Client:" + "\n" +
                client + "\n" +
                "Ad Position:" + "\n" +
                adPosition + "\n" +
                "Ad Offset:" + "\n" +
                offset + "\n" +
                "/// onAdRequest END ///";
        setOutput(output);
    }


    @Override
    public void onAdSkipped(AdSkippedEvent event) {
        String client = event.getClient().name();
        String creativeType = event.getCreativeType();
        String tag = event.getTag();
        String output = "/// onAdSkipped START ///" + "\n" +
                "Tag:" + "\n" +
                tag + "\n" +
                "Creative Type:" + "\n" +
                creativeType + "\n" +
                "Ad Client:" + "\n" +
                client + "\n" +
                "/// onAdSkipped END ///";
        setOutput(output);
    }

    @Override
    public void onFullscreen(boolean state) {
        String output = "/// onFullscreen START ///" + "\n" +
                "State:" + "\n" +
                state + "\n" +
                "/// onFullscreen END ///";
        setOutput(output);
    }

    @Override
    public void onAdClick(AdClickEvent event) {
        String creativeType = event.getCreativeType();
        String tag = event.getTag();
        String client = event.getClient().name();

        String output = "/// onAdClick START ///" + "\n" +
                "Tag:" + "\n" +
                tag + "\n" +
                "Creative Type:" + "\n" +
                creativeType + "\n" +
                "Client:" + "\n" +
                client + "\n" +
                "/// onAdClick END ///";
        setOutput(output);
    }

    @Override
    public void onAdImpression(AdImpressionEvent event) {
        String creativeType = event.getCreativeType();
        String tag = event.getTag();
        String client = event.getClient().name();
        // AdPosition is an enum now and can return pre, mid, post
        String adPosition = event.getAdPosition().name();
        String adSystem = event.getAdSystem();
        String adTitle = event.getAdTitle();
        String linear = event.getLinear();
        String mediaFile = event.getMediaFile().getFile();
        String vastVersion = event.getVastVersion();

        String output = "/// onAdImpression START ///" + "\n" +
                "Tag:" + "\n" +
                tag + "\n" +
                "Creative Type:" + "\n" +
                creativeType + "\n" +
                "Client:" + "\n" +
                client + "\n" +
                "Ad Position:" + "\n" +
                adPosition + "\n" +
                "Ad System:" + "\n" +
                adSystem + "\n" +
                "Ad Title:" + "\n" +
                adTitle + "\n" +
                "Linear:" + "\n" +
                linear + "\n" +
                "Media File:" + "\n" +
                mediaFile + "\n" +
                "Vast Version:" + "\n" +
                vastVersion + "\n" +
                "/// onAdImpression END ///";
        setOutput(output);
    }

    @Override
    public void onAdPause(AdPauseEvent event) {
        PlayerState oldState = event.getOldState();
        PlayerState currentState = event.getNewState();
        String tag = event.getTag();
        String creativeType = event.getCreativeType();

        String output = "/// onAdPause START ///" + "\n" +
                "Tag:" + "\n" +
                tag + "\n" +
                "Creative Type:" + "\n" +
                creativeType + "\n" +
                "Previous Player State:" + "\n" +
                oldState.name() + "\n" +
                "Current Player State:" + "\n" +
                currentState.name() + "\n" +
                "/// onAdPause END ///";
        setOutput(output);
    }

    @Override
    public void onAdPlay(AdPlayEvent event) {
        String tag = event.getTag();
        String creativeType = event.getCreativeType();
        PlayerState oldState = event.getOldState();
        PlayerState currentState = event.getNewState();

        String output = "/// onAdPlay START ///" + "\n" +
                "Tag:" + "\n" +
                tag + "\n" +
                "Creative Type:" + "\n" +
                creativeType + "\n" +
                "Previous Player State:" + "\n" +
                oldState.name() + "\n" +
                "Current Player State:" + "\n" +
                currentState.name() + "\n" +
                "/// onAdPlay END ///";
        setOutput(output);
    }

    private double mOldAdTimeSeconds = 0;
    private double mOldAdDurationSeconds = 0;

    @Override
    public void onAdTime(AdTimeEvent event) {
        AdSource client = event.getClient();
        String creativeType = event.getCreativeType();
        int sequence = event.getSequence();
        String tag = event.getTag();
        long position = event.getPosition();
        long duration = event.getDuration();

        if (mTimeSeconds.isChecked()) {
            double adTimeSeconds = Math.floor(position / 1000);
            double adDurationSeconds = Math.floor(duration / 1000);

            if (mOldAdTimeSeconds != adTimeSeconds || mOldAdDurationSeconds != adDurationSeconds) {

                mOldAdTimeSeconds = adTimeSeconds;
                mOldAdDurationSeconds = adDurationSeconds;

                String output = "/// onAdTime START ///" + "\n" +
                        "Client:" + "\n" +
                        client.name() + "\n" +
                        "Creative Type:" + "\n" +
                        creativeType + "\n" +
                        "Tag:" + "\n" +
                        tag + "\n" +
                        "Position:" + "\n" +
                        adTimeSeconds + "s" + "\n" +
                        "Duration:" + "\n" +
                        adDurationSeconds + "s" + "\n" +
                        "Sequence:" + "\n" +
                        sequence + "\n" +
                        "/// onAdTime END ///";
                setOutput(output);
            }
        } else {
            String output = "/// onAdTime START ///" + "\n" +
                    "Client:" + "\n" +
                    client.name() + "\n" +
                    "Creative Type:" + "\n" +
                    creativeType + "\n" +
                    "Tag:" + "\n" +
                    tag + "\n" +
                    "Position:" + "\n" +
                    position + "s" + "\n" +
                    "Duration:" + "\n" +
                    duration + "s" + "\n" +
                    "Sequence:" + "\n" +
                    sequence + "\n" +
                    "/// onAdTime END ///";
            setOutput(output);
        }
    }

    @Override
    public void onBuffer(PlayerState playerState) {
        String output = "/// onBuffer START ///" + "\n" +
                "Previous Player State:" + "\n" +
                playerState.name() + "\n" +
                "/// onBuffer END ///";
        setOutput(output);
    }


    @Override
    public void onIdle(PlayerState playerState) {
        String output = "/// onIdle START ///" + "\n" +
                "Previous Player State:" + "\n" +
                playerState.name() + "\n" +
                "/// onIdle END ///";
        setOutput(output);
    }

    @Override
    public void onPause(PlayerState playerState) {
        String output = "/// onPause START ///" + "\n" +
                "Previous Player State:" + "\n" +
                playerState.name() + "\n" +
                "/// onPause END ///";
        setOutput(output);
    }

    @Override
    public void onPlay(PlayerState playerState) {
        String output = "/// onPlay START ///" + "\n" +
                "Previous Player State:" + "\n" +
                playerState.name() + "\n" +
                "/// onPlay END ///";
        setOutput(output);
    }

    private double mOldTime = 0;

    @Override
    public void onTime(long l, long l1) {
        if (mTimeSeconds.isChecked()) {
            double time = Math.floor(l / 1000);
            double duration = Math.floor(l1 / 1000);
            if (mOldTime != time) {
                mOldTime = time;
                String output = "/// onTime START ///" + "\n" +
                        "Current time:" + "\n" +
                        time + "s" + "\n" +
                        "Total Duration:" + "\n" +
                        duration + "s" + "\n" +
                        "/// onTime END ///";
                setOutput(output);
            }
        } else {
            String output = "/// onTime START ///" + "\n" +
                    "Current time:" + "\n" +
                    l + "ms" + "\n" +
                    "Total Duration:" + "\n" +
                    l1 + "ms" + "\n" +
                    "/// onTime END ///";
            setOutput(output);
        }
    }


    @Override
    public void onAdComplete(AdCompleteEvent event) {
        String client = event.getClient().name();
        String creativeType = event.getCreativeType();
        String tag = event.getTag();

        String output = "/// onAdComplete START ///" + "\n" +
                "Tag:" + "\n" +
                tag + "\n" +
                "Creative Type:" + "\n" +
                creativeType + "\n" +
                "Ad Client:" + "\n" +
                client + "\n" +
                "/// onAdComplete END ///";
        setOutput(output);
    }

    @Override
    public void onAdError(String s, String s1) {
        String output = "/// onAdError START ///" + "\n" +
                "String 1:" + "\n" +
                s + "\n" +
                "String 2:" + "\n" +
                s1 + "\n" +
                "/// onAdError END ///";
        setOutput(output);
    }

    @Override
    public void onComplete() {
        String output = "/// onComplete START ///" + "\n" +
                "/// onComplete END ///";
        setOutput(output);
    }

    @Override
    public void onError(ErrorEvent event) {
        String message = event.getMessage();
        String output = "/// onError START ///" + "\n" +
                "Error Message:" + "\n" +
                message + "\n" +
                "/// onError END ///";
        setOutput(output);
    }

    @Override
    public void onSetupError(String s) {
        String output = "/// onSetupError START ///" + "\n" +
                "Error Message:" + "\n" +
                s + "\n" +
                "/// onSetupError END ///";
        setOutput(output);
    }

    @Override
    public void onSeek(int position, int offset) {
        String output = "/// onSeek START ///" + "\n" +
                "Position:" + "\n" +
                position + "s" + "\n" +
                "Offset:" + "\n" +
                offset + "s" + "\n" +
                "/// onSeek END ///";
        setOutput(output);
    }

    @Override
    public void onSeeked() {
        String output = "/// onSeeked START ///" + "\n" +
                "/// onSeeked END ///";
        setOutput(output);
    }

    private void setOutput(String output) {
        mCallbackLog.append(output + "\n\n");
    }

    public void registerListeners(JWPlayerView playerView) {

        mPlayerView = playerView;

        mCallbackPlayerVersion = (TextView) findViewById(R.id.callback_player_version);
        mCallbackPlayerVersion.setText("Player Version: " + mPlayerView.getVersionCode());

        mCallbackLog = (TextView) findViewById(R.id.callback_status_tv);
        mCallbackLog.setMovementMethod(new ScrollingMovementMethod());


//         This handles clearing the log
        findViewById(R.id.callback_clear_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbackLog.setText("");
            }
        });


        mTimeSeconds = (CheckBox) findViewById(R.id.callback_time_seconds);

        mOnCompleteCheckBox = (CheckBox) findViewById(R.id.callback_on_complete_check);
        mOnCompleteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnCompleteListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnCompleteListener(mCallbackScreen);
                }
            }
        });
        mOnErrorCheckBox = (CheckBox) findViewById(R.id.callback_on_error_check);
        mOnErrorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnErrorListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnErrorListener(mCallbackScreen);
                }
            }
        });
        mOnFullscreenCheckBox = (CheckBox) findViewById(R.id.callback_on_fullscreen_check);
        mOnFullscreenCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnFullscreenListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnFullscreenListener(mCallbackScreen);
                }
            }
        });
        mOnSeekCheckBox = (CheckBox) findViewById(R.id.callback_on_seek_check);
        mOnSeekCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnSeekListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnSeekListener(mCallbackScreen);
                }
            }
        });
        mOnSeekedCheckBox = (CheckBox) findViewById(R.id.callback_on_seeked_check);
        mOnSeekedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnSeekedListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnSeekedListener(mCallbackScreen);
                }
            }
        });
        mOnIdleCheckBox = (CheckBox) findViewById(R.id.callback_on_idle_check);
        mOnIdleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnIdleListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnIdleListener(mCallbackScreen);
                }
            }
        });
        mOnBufferingCheckBox = (CheckBox) findViewById(R.id.callback_on_buffer_check);
        mOnBufferingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnBufferListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnBufferListener(mCallbackScreen);
                }
            }
        });
        mOnPlayCheckBox = (CheckBox) findViewById(R.id.callback_on_play_check);
        mOnPlayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnPlayListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnPlayListener(mCallbackScreen);
                }
            }
        });
        mOnPauseCheckBox = (CheckBox) findViewById(R.id.callback_on_pause_check);
        mOnPauseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnPauseListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnPauseListener(mCallbackScreen);
                }
            }
        });
        mOnTimeCheckBox = (CheckBox) findViewById(R.id.callback_on_time_check);
        mOnTimeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnTimeListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnTimeListener(mCallbackScreen);
                }
            }
        });
        mOnAdCompleteCheckBox = (CheckBox) findViewById(R.id.callback_ad_on_complete_check);
        mOnAdCompleteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnAdCompleteListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnAdCompleteListener(mCallbackScreen);
                }
            }
        });
        mOnAdErrorCheckBox = (CheckBox) findViewById(R.id.callback_ad_on_error_check);
        mOnAdErrorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnAdErrorListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnAdErrorListener(mCallbackScreen);
                }
            }
        });
        mOnAdSkipCheckBox = (CheckBox) findViewById(R.id.callback_ad_on_skip_check);
        mOnAdSkipCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnAdSkippedListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnAdSkippedListener(mCallbackScreen);
                }
            }
        });
        mOnAdPlayCheckBox = (CheckBox) findViewById(R.id.callback_ad_on_play_check);
        mOnAdPlayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnAdPlayListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnAdPlayListener(mCallbackScreen);
                }
            }
        });
        mOnAdPauseCheckBox = (CheckBox) findViewById(R.id.callback_ad_on_pause_check);
        mOnAdPauseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnAdPauseListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnAdPauseListener(mCallbackScreen);
                }
            }
        });
        mOnAdTimeCheckBox = (CheckBox) findViewById(R.id.callback_ad_on_time_check);
        mOnAdTimeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnAdTimeListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnAdTimeListener(mCallbackScreen);
                }
            }
        });
        mOnAdImpressionCheckBox = (CheckBox) findViewById(R.id.callback_ad_on_impression_check);
        mOnAdImpressionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnAdImpressionListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnAdImpressionListener(mCallbackScreen);
                }
            }
        });
        mOnAdClickCheckBox = (CheckBox) findViewById(R.id.callback_ad_on_click_check);
        mOnAdClickCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnAdClickListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnAdClickListener(mCallbackScreen);
                }
            }
        });
        mOnAdRequestCheckBox = (CheckBox) findViewById(R.id.callback_ad_on_request_check);
        mOnAdRequestCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnAdRequestListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnAdRequestListener(mCallbackScreen);
                }
            }
        });
        mOnAudioTracksCheckBox = (CheckBox) findViewById(R.id.callback_on_audio_tracks_check);
        mOnAudioTracksCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnAudioTracksListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnAudioTracksListener(mCallbackScreen);
                }
            }
        });
        mOnAudioTracksChangedCheckBox = (CheckBox) findViewById(R.id.callback_on_audio_tracks_changed_check);
        mOnAudioTracksChangedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnAudioTrackChangedListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnAudioTrackChangedListener(mCallbackScreen);
                }
            }
        });
        mOnLevelsCheckBox = (CheckBox) findViewById(R.id.callback_on_levels_check);
        mOnLevelsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnLevelsListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnLevelsListener(mCallbackScreen);
                }
            }
        });
        mOnLevelsChangedCheckBox = (CheckBox) findViewById(R.id.callback_on_levels_changed_check);
        mOnLevelsChangedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnLevelsChangedListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnLevelsChangedListener(mCallbackScreen);
                }
            }
        });
        mOnFirstFrameCheckBox = (CheckBox) findViewById(R.id.callback_on_first_frame_check);
        mOnFirstFrameCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnFirstFrameListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnFirstFrameListener(mCallbackScreen);
                }
            }
        });
        mOnDisplayClickCheckBox = (CheckBox) findViewById(R.id.callback_on_display_click_check);
        mOnDisplayClickCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnDisplayClickListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnDisplayClickListener(mCallbackScreen);
                }
            }
        });
        mOnPlaylistCompleteCheckBox = (CheckBox) findViewById(R.id.callback_on_playlist_complete_check);
        mOnPlaylistCompleteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnPlaylistCompleteListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnPlaylistCompleteListener(mCallbackScreen);
                }
            }
        });
        mOnMetaCheckBox = (CheckBox) findViewById(R.id.callback_on_meta_check);
        mOnMetaCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnMetaListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnMetaListener(mCallbackScreen);
                }
            }
        });
        mOnCaptionsChangedCheckBox = (CheckBox) findViewById(R.id.callback_on_captions_changed_check);
        mOnCaptionsChangedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnCaptionsChangedListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnCaptionsChangedListener(mCallbackScreen);
                }
            }
        });
        mOnCaptionsListCheckBox = (CheckBox) findViewById(R.id.callback_on_captions_list_check);
        mOnCaptionsListCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnCaptionsListListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnCaptionsListListener(mCallbackScreen);
                }
            }
        });
        mOnPlaylistItemCheckBox = (CheckBox) findViewById(R.id.callback_on_playlist_item_check);
        mOnPlaylistItemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnPlaylistItemListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnPlaylistItemListener(mCallbackScreen);
                }
            }
        });
        mOnPlaylistCheckBox = (CheckBox) findViewById(R.id.callback_on_playlist_check);
        mOnPlaylistCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnPlaylistListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnPlaylistListener(mCallbackScreen);
                }
            }
        });
        mOnSetupErrorCheckBox = (CheckBox) findViewById(R.id.callback_on_setup_error_check);
        mOnSetupErrorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnSetupErrorListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnSetupErrorListener(mCallbackScreen);
                }
            }
        });
        mOnBeforeCompleteCheckBox = (CheckBox) findViewById(R.id.callback_ad_on_before_complete_check);
        mOnBeforeCompleteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnBeforeCompleteListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnBeforeCompleteListener(mCallbackScreen);
                }
            }
        });
        mOnBeforePlayCheckBox = (CheckBox) findViewById(R.id.callback_ad_on_before_play_check);
        mOnBeforePlayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnBeforePlayListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnBeforePlayListener(mCallbackScreen);
                }
            }
        });
        mOnMuteCheckBox = (CheckBox) findViewById(R.id.callback_on_mute_check);
        mOnMuteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnMuteListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnMuteListener(mCallbackScreen);
                }
            }
        });
        mOnVisualQualityCheckBox = (CheckBox) findViewById(R.id.callback_on_visual_quality_check);
        mOnVisualQualityCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnVisualQualityListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnVisualQualityListener(mCallbackScreen);
                }
            }
        });
        mOnAdStartedCheckBox = (CheckBox) findViewById(R.id.callback_ad_on_started_check);
        mOnAdStartedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnAdStartedListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnAdStartedListener(mCallbackScreen);
                }
            }
        });
        mOnControls = (CheckBox) findViewById(R.id.callback_on_controls_check);
        mOnControls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnControlsListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnControlsListener(mCallbackScreen);
                }
            }
        });
        mOnBufferChange = (CheckBox) findViewById(R.id.callback_on_buffer_change_check);
        mOnBufferChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnBufferChangeListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnBufferChangeListener(mCallbackScreen);
                }
            }
        });
        mOnRelatedClose = (CheckBox) findViewById(R.id.callback_on_related_close_check);
        mOnRelatedClose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnRelatedCloseListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnRelatedCloseListener(mCallbackScreen);
                }
            }
        });
        mOnRelatedOpen = (CheckBox) findViewById(R.id.callback_on_related_open_check);
        mOnRelatedOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnRelatedOpenListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnRelatedOpenListener(mCallbackScreen);
                }
            }
        });
        mOnRelatedPlay = (CheckBox) findViewById(R.id.callback_on_related_play_check);
        mOnRelatedPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnRelatedPlayListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnRelatedPlayListener(mCallbackScreen);
                }
            }
        });
        mOnAdCompanion = (CheckBox) findViewById(R.id.callback_ad_on_companion_check);
        mOnAdCompanion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayerView.addOnAdCompanionsListener(mCallbackScreen);
                } else {
                    mPlayerView.removeOnAdCompanionsListener(mCallbackScreen);
                }
            }
        });

        // handles unchecking all boxes
        mCallbacksList = new ArrayList<>();
        mCallbacksList.add(mOnCompleteCheckBox);
        mCallbacksList.add(mOnErrorCheckBox);
        mCallbacksList.add(mOnFullscreenCheckBox);
        mCallbacksList.add(mOnSeekCheckBox);
        mCallbacksList.add(mOnSeekedCheckBox);
        mCallbacksList.add(mOnIdleCheckBox);
        mCallbacksList.add(mOnBufferingCheckBox);
        mCallbacksList.add(mOnPlayCheckBox);
        mCallbacksList.add(mOnPauseCheckBox);
        mCallbacksList.add(mOnTimeCheckBox);
        mCallbacksList.add(mOnAdCompleteCheckBox);
        mCallbacksList.add(mOnAdErrorCheckBox);
        mCallbacksList.add(mOnAdSkipCheckBox);
        mCallbacksList.add(mOnAdPlayCheckBox);
        mCallbacksList.add(mOnAdPauseCheckBox);
        mCallbacksList.add(mOnAdTimeCheckBox);
        mCallbacksList.add(mOnAdImpressionCheckBox);
        mCallbacksList.add(mOnAdClickCheckBox);
        mCallbacksList.add(mOnAdRequestCheckBox);
        mCallbacksList.add(mOnAudioTracksCheckBox);
        mCallbacksList.add(mOnAudioTracksChangedCheckBox);
        mCallbacksList.add(mOnLevelsCheckBox);
        mCallbacksList.add(mOnLevelsChangedCheckBox);
        mCallbacksList.add(mOnFirstFrameCheckBox);
        mCallbacksList.add(mOnDisplayClickCheckBox);
        mCallbacksList.add(mOnPlaylistCompleteCheckBox);
        mCallbacksList.add(mOnMetaCheckBox);
        mCallbacksList.add(mOnCaptionsChangedCheckBox);
        mCallbacksList.add(mOnCaptionsListCheckBox);
        mCallbacksList.add(mOnPlaylistItemCheckBox);
        mCallbacksList.add(mOnPlaylistCheckBox);
        mCallbacksList.add(mOnSetupErrorCheckBox);
        mCallbacksList.add(mOnBeforeCompleteCheckBox);
        mCallbacksList.add(mOnBeforePlayCheckBox);
        mCallbacksList.add(mOnMuteCheckBox);
        mCallbacksList.add(mOnVisualQualityCheckBox);
        mCallbacksList.add(mOnAdStartedCheckBox);
        mCallbacksList.add(mOnControls);
        mCallbacksList.add(mOnBufferChange);
        mCallbacksList.add(mOnRelatedClose);
        mCallbacksList.add(mOnRelatedOpen);
        mCallbacksList.add(mOnRelatedPlay);
        mCallbacksList.add(mOnAdCompanion);

        // This handles unchecking all checkboxes
        mCheckAll = (CheckBox) findViewById(R.id.callback_check_all);
        mCheckAll = (CheckBox) findViewById(R.id.callback_check_all);
        mCheckAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (CheckBox currectCheckBox : mCallbacksList) {
                    currectCheckBox.setChecked(isChecked);
                }
            }
        });


    }

    public String getCallbackLog() {
        return mCallbackLog.getText().toString();
    }

    @Override
    public void onMute(boolean b) {
        String output = "/// onMute START ///" + "\n" +
                "Mute = " + b + "\n" +
                "/// onMute END ///";
        setOutput(output);
    }

    @Override
    public void onVisualQuality(VisualQuality visualQuality) {
        String prefix = "/// onVisualQuality START ///" + "\n";

        String mode = "Mode = " + visualQuality.getMode().name() + "\n";
        String qualityLevel = "Quality Level = " + visualQuality.getQualityLevel().toJson().toString() + "\n";
        String reason = "Reason = " + visualQuality.getReason().name();

        String suffix = "\n" + "/// onVisualQuality END ///";
        setOutput(prefix + mode + qualityLevel + reason + suffix);
    }

    @Override
    public void onAdStarted(String s, String s1) {
        String output = "/// onAdStarted START ///" + "\n" +
                "String 1:" + "\n" +
                s + "\n" +
                "String 2:" + "\n" +
                s1 + "\n" +
                "/// onAdStarted END ///";
        setOutput(output);
    }

    @Override
    public void onControls(ControlsEvent controlsEvent) {
        String prefix = "/// onControls START ///" + "\n";

        String controls = "Controls = " + controlsEvent.getControls() + "\n";
        String event = "ControlsEvent = " + controlsEvent.toString() + "\n";

        String suffix = "\n" + "/// onControls END ///";
        setOutput(prefix + controls + event + suffix);
    }

    @Override
    public void onBufferChange(BufferChangeEvent bufferChangeEvent) {
        String prefix = "/// onBufferChange START ///" + "\n";

        String percentage = "Buffer Percentage = " + bufferChangeEvent.getBufferPercent() + "\n";
        String duration = "Duration = " + bufferChangeEvent.getDuration() + "\n";
        String position = "Position = " + bufferChangeEvent.getPosition() + "\n";

        String suffix = "\n" + "/// onBufferChange END ///";
        setOutput(prefix + percentage + duration + position + suffix);
    }

    @Override
    public void onRelatedClose(RelatedCloseEvent relatedCloseEvent) {
        String prefix = "/// onRelatedClose START ///" + "\n";

        String method = "Method = " + relatedCloseEvent.getMethod() + "\n";

        String suffix = "\n" + "/// onRelatedClose END ///";
        setOutput(prefix + method + suffix);
    }

    @Override
    public void onRelatedOpen(RelatedOpenEvent relatedOpenEvent) {
        String prefix = "/// onRelatedOpen START ///" + "\n";

        String method = "Method = " + relatedOpenEvent.getMethod() + "\n";
        String file = "File = " + relatedOpenEvent.getRelatedFile() + "\n";
        String playlist = "Playlist = " + Utils.playlistToString(relatedOpenEvent.getItems()) + "\n";

        String suffix = "\n" + "/// onRelatedOpen END ///";
        setOutput(prefix + method + file + playlist + suffix);
    }

    @Override
    public void onRelatedPlay(RelatedPlayEvent relatedPlayEvent) {
        String prefix = "/// onRelatedPlay START ///" + "\n";

        String auto = "Auto = " + relatedPlayEvent.getAuto() + "\n";
        String item = "";
        try {
            item = "Item = " + relatedPlayEvent.getItem().toJson().toString(4) + "\n";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String suffix = "\n" + "/// onRelatedPlay END ///";
        setOutput(prefix + auto + item + suffix);
    }

    @Override
    public void onAdCompanions(AdCompanionEvent adCompanionEvent) {
        Log.d("CallbackScreen", "OnAdCompanion");
        String prefix = "/// onAdCompanion START ///" + "\n";

        String tag = "Tag = " + adCompanionEvent.getTag() + "\n";

        String item = "";
        try {
            List<AdCompanion> adCompanionList = adCompanionEvent.getCompanions();
            JSONArray jsonArray = new JSONArray();
            for (AdCompanion adCompanion : adCompanionList) {
                JSONObject companionJson = new JSONObject();
                companionJson.put("height:", adCompanion.getHeight());
                companionJson.put("width:", adCompanion.getWidth());
                companionJson.put("type:", adCompanion.getType());
                companionJson.put("resource:", adCompanion.getResource());
                companionJson.put("click:", adCompanion.getClick());
                companionJson.put("creatives", adCompanion.getCreativeViews());
                jsonArray.put(companionJson);
            }

            item = "Ad Companions = \n" + jsonArray.toString(4) + "\n";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String suffix = "\n" + "/// onAdCompanion END ///";
        setOutput(prefix + tag + item + suffix);
    }

    public void setJWPlayerView(JWPlayerView playerView) {
        this.mPlayerView = playerView;
    }
}