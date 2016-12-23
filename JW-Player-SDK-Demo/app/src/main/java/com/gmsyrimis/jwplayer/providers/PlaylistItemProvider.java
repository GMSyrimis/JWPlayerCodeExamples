package com.gmsyrimis.jwplayer.providers;

import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.captions.CaptionType;
import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gsyrimis on 3/6/16.
 */
public class PlaylistItemProvider extends DataProvider{



    public PlaylistItemProvider() {
        mPayload.add(new PlaylistItem("http://content.jwplatform.com/manifests/PxnoM5gE.m3u8").toJson().toString());
        mPayload.add(new PlaylistItem("http://content.jwplatform.com/videos/PxnoM5gE-720.mp4").toJson().toString());
        mPayload.add(setupMultiSourceMP4());
        mPayload.add(new PlaylistItem("http://qa.jwplayer.com/~george/manifest.mpd").toJson().toString());
        mPayload.add(multiQualityDash());
        mPayload.add(setupCaption());

        mLabels.add("Multi Quality HLS");
        mLabels.add("Single MP4");
        mLabels.add("Multi Source MP4");
        mLabels.add("Multi Audio DASH");
        mLabels.add("Multi Quality DASH");
        mLabels.add("HLS + SRT Captions");
    }

    private String setupMultiSourceMP4() {
        PlaylistItem multiSourceMp4 = new PlaylistItem();
        MediaSource hd = new MediaSource();
        hd.setFile("http://content.jwplatform.com/videos/PxnoM5gE-720.mp4");
        hd.setLabel("720p");
        hd.setType(MediaType.MP4);
        hd.setDefault(true);
        MediaSource sd = new MediaSource();
        sd.setFile("http://content.jwplatform.com/videos/PxnoM5gE-360.mp4");
        sd.setLabel("360p");
        sd.setType(MediaType.MP4);
        sd.setDefault(false);
        List<MediaSource> sources = new ArrayList<>();
        sources.add(sd);
        sources.add(hd);
        multiSourceMp4.setSources(sources);
        return multiSourceMp4.toJson().toString();
    }

    private String setupCaption(){
        PlaylistItem video = new PlaylistItem("http://content.jwplatform.com/manifests/PxnoM5gE.m3u8");
        Caption eng = new Caption();
        eng.setFile("http://content.jwplatform.com/tracks/BHfkqu5d.srt");
        eng.setLabel("EN");
        eng.setKind(CaptionType.CAPTIONS);
        eng.setDefault(true);
        List<Caption> captionList = new ArrayList<>();
        captionList.add(eng);
        video.setCaptions(captionList);
        return video.toJson().toString();
    }

    private String multiQualityDash(){
        //http://samplescdn.origin.mediaservices.windows.net/e0e820ec-f6a2-4ea2-afe3-1eed4e06ab2c/AzureMediaServices_Overview.ism/manifest(format=mpd-time-csf)
        PlaylistItem dash = new PlaylistItem();
        MediaSource hd = new MediaSource();
        hd.setFile("http://samplescdn.origin.mediaservices.windows.net/e0e820ec-f6a2-4ea2-afe3-1eed4e06ab2c/AzureMediaServices_Overview.ism/manifest(format=mpd-time-csf)");
        hd.setType(MediaType.MPD);
        hd.setDefault(true);
        List<MediaSource> sources = new ArrayList<>();
        sources.add(hd);
        dash.setSources(sources);
        return dash.toJson().toString();
    }

}
