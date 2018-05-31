package com.gmsyrimis.jwplayer.providers;

import android.util.Log;

import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.media.ads.Ad;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.ads.AdType;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.ads.VMAPAdvertising;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.captions.CaptionType;
import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gsyrimis on 6/2/16.
 */
public class PlayerConfigProvider extends DataProvider{


    public PlayerConfigProvider() {
        mLabels.add("Debug Setup");
        mPayload.add(debugSetup());

        mLabels.add("Single File");
        mPayload.add(singleFile());

        mLabels.add("Multi Audio DASH");
        mPayload.add(multiAudioDash());

        mLabels.add("Playlist Item Advertising");
        mPayload.add(playlistItemAdvertising());

        mLabels.add("VAST Pre Mid Post");
        mPayload.add(vastPreMidPost());

        mLabels.add("Custom Skin");
        mPayload.add(customSkin());

        mLabels.add("Chapters");
        mPayload.add(chapters());

        mLabels.add("Caption Styling");
        mPayload.add(captionStyling());

        mLabels.add("Logo");
        mPayload.add(logo());

        mLabels.add("Playlist Advertising");
        mPayload.add(playlistLevelAdvertising());

        mLabels.add("Multisource Video");
        mPayload.add(multipleMediaSources());

        mLabels.add("VMAP Advertising");
        mPayload.add(vmapAdvertising());

        mLabels.add("VAST Waterfall");
        mPayload.add(vastWaterfall());

        mLabels.add("VAST Non-Linear");
        mPayload.add(vastNonLinear());

        mLabels.add("VAST Ad-Pods");
        mPayload.add(vastAdPods());

        mLabels.add("VAST VPAID2");
        mPayload.add(vastVPAID2());

        mLabels.add("VAST Ad Tag Macros");
        mPayload.add(vastAdTagMacros());

        mLabels.add("IMA Ad Tag Macros");
        mPayload.add(imaAdTagMacros());

        mLabels.add("IMA Mid+Post");
        mPayload.add(imaMidPost());

        mLabels.add("IMA Pre+Bumper VMAP");
        mPayload.add(imaPreBumperVmap());


    }

    private String debugSetup() {


        List<MediaSource> sources = new ArrayList<>();
        sources.add(new MediaSource.Builder()
                .file("http://video-auth8.iol.pt:1935/vod/_definst_/c/b/5/a/smil:5797ae0f0cf2edf5f6b5cb5a-L/playlist.m3u8?wmsAuthSign=c2VydmVyX3RpbWU9Ny8yOC8yMDE2IDEwOjIxOjMwIEFNJmhhc2hfdmFsdWU9S096anEwVmhVa1I1cXBEcmRweDRYQT09JnZhbGlkbWludXRlcz0xNDQwJmlkPWY4YjYxMGQ0LWI1ZTktNGZmNy04MmM0LTRiOWZmMzEyYzc0Zg==")
                .type(MediaType.HLS)
                .isdefault(true)
                .build());

        PlaylistItem pi = new PlaylistItem.Builder()
                .sources(sources)
                .build();

        List<PlaylistItem> playlist = new ArrayList<>();
        playlist.add(pi);

        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .build();


        return pc.toJson().toString();
    }



    private String multipleMediaSources() {
        List<MediaSource> sources = new ArrayList<>();

        sources.add(new MediaSource.Builder()
                .file("http://content.jwplatform.com/videos/PxnoM5gE-720.mp4")
                .label("720p")
                .isdefault(true)
                .type(MediaType.MP4)
                .build());

        sources.add(new MediaSource.Builder()
                .file("http://content.jwplatform.com/videos/PxnoM5gE-480.mp4")
                .label("480p")
                .isdefault(false)
                .type(MediaType.MP4)
                .build());


        List<PlaylistItem> playlist = new ArrayList<>();
        playlist.add(new PlaylistItem.Builder()
                .sources(sources)
                .mediaId("PxnoM5gE")
                .build());

        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .build();

        return pc.toJson().toString();
    }



    private String singleFile() {
        PlayerConfig pc = new PlayerConfig.Builder()
                .file("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8")
                .build();
        return pc.toJson().toString();
    }

    private String multiAudioDash() {
        PlayerConfig pc = new PlayerConfig.Builder()
                .file("http://qa.jwplayer.com/~george/manifest.mpd")
                .build();
        return pc.toJson().toString();
    }

    private String playlistItemAdvertising() {
        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        PlaylistItem video = new PlaylistItem("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8");
        Ad ad = new Ad(AdSource.VAST, "http://bob.jwplayer.com.s3.amazonaws.com/~george/preroll.xml");
        AdBreak adBreak = new AdBreak("pre", ad);
        List<AdBreak> adSchedule = new ArrayList<AdBreak>();
        adSchedule.add(adBreak);
        video.setAdSchedule(adSchedule);
        playlist.add(video);
        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .build();
        return pc.toJson().toString();
    }

    private String vastPreMidPost() {
        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        PlaylistItem video = new PlaylistItem("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8");
        Ad ad = new Ad(AdSource.VAST, "http://www.adotube.com/php/services/player/OMLService.php?avpid=oRYYzvQ&platform_version=vast20&ad_type=linear&groupbypass=1&HTTP_REFERER=http://www.longtailvideo.com&video_identifier=longtailvideo.com,test");
        AdBreak adBreak = new AdBreak("pre", ad);
        AdBreak adBreak1 = new AdBreak("50%", ad);
        AdBreak adBreak2 = new AdBreak("post", ad);
        List<AdBreak> adSchedule = new ArrayList<AdBreak>();
        adSchedule.add(adBreak);
        adSchedule.add(adBreak1);
        adSchedule.add(adBreak2);
        video.setAdSchedule(adSchedule);
        playlist.add(video);
        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .build();
        return pc.toJson().toString();
    }

    private String customSkin() {
        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        PlaylistItem video = new PlaylistItem("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8");
        playlist.add(video);

        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .skinUrl("http://qa.jwplayer.com/~george/skins/stormcolor.css")
                .skinName("stormcolor")
                .skinActive("#FFFB00") // Why don't the colors get applied on a custom skin?
                .skinInactive("#96CC00")
                .skinBackground("#AB00C9")
                .build();
        return pc.toJson().toString();
    }

    private String chapters() {
        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        PlaylistItem playlistItem = new PlaylistItem();

        Caption caption = new Caption();
        caption.setKind(CaptionType.CHAPTERS);
        caption.setLabel("EN");
        caption.setFile("http://content.jwplatform.com/tracks/SU1XXCee.vtt");
        List<Caption> captionList = new ArrayList<>();
        captionList.add(caption);

        playlistItem.setFile("http://content.jwplatform.com/manifests/PxnoM5gE.m3u8");
        playlistItem.setTitle("KUNG FURY");
        playlistItem.setMediaId("PxnoM5gE");
        playlistItem.setDescription("It takes a cop from the future to fight an enemy from the past.");
        playlistItem.setImage("http://content.jwplatform.com/thumbs/PxnoM5gE-720.jpg");
        playlistItem.setCaptions(captionList);

        playlist.add(playlistItem);

        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .build();

        return pc.toJson().toString();
    }

    private String logo() {
        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        PlaylistItem video = new PlaylistItem("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8");
        playlist.add(video);

        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .logoFile("http://qa.jwplayer.com.s3.amazonaws.com/~george/ic_launcher.png")
                .logoLink("https://developer.jwplayer.com/android-sdk/")
                .logoMargin(15)
                .logoPosition(PlayerConfig.LOGO_POSITION_TOP_LEFT)
                .logoHide(false)
                .build();
        return pc.toJson().toString();
    }

    private String playlistLevelAdvertising() {
        // Create your ad schedule
        Ad ad = new Ad(AdSource.VAST, "http://bob.jwplayer.com.s3.amazonaws.com/~george/preroll.xml");
        AdBreak adBreak = new AdBreak("pre", ad);
        List<AdBreak> adSchedule = new ArrayList<AdBreak>();
        adSchedule.add(adBreak);
        // Set your ad schedule to your advertising object
        Advertising advertising = new Advertising(AdSource.VAST, adSchedule);
        // Create a playlist, you'll need this to build your player config
        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        PlaylistItem video = new PlaylistItem("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8");
        playlist.add(video);
        // Create your player config
        PlayerConfig playerConfig = new PlayerConfig.Builder()
                .playlist(playlist)
                .advertising(advertising)
                .build();
        // Setup your player with the config

        return playerConfig.toJson().toString();
    }


    private String vmapAdvertising() {
        // Create your ad schedule

        VMAPAdvertising vmapAdvertising = new VMAPAdvertising(AdSource.VAST, "https://playertest.longtailvideo.com/adtags/vmap2.xml");
        // Create a playlist, you'll need this to build your player config
        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        PlaylistItem video = new PlaylistItem("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8");
        playlist.add(video);
        // Create your player config
        PlayerConfig playerConfig = new PlayerConfig.Builder()
                .playlist(playlist)
                .advertising(vmapAdvertising)
                .build();
        // Setup your player with the config

        Log.d("GEORGE", playerConfig.toJson().toString());
        return playerConfig.toJson().toString();
    }

    private String vastWaterfall() {

        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        // Create video
        PlaylistItem video = new PlaylistItem("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8");

        // Create waterfall string array
        String[] waterfall = new String[]{
                "http://demo.jwplayer.com/android/vast-tags/pbadreroll.xml",
                "http://bob.jwplayer.com.s3.amazonaws.com/~george/preroll.xml"
        };

        // Set waterfall to the Ad
        Ad ad = new Ad(AdSource.VAST, waterfall);

        // Create your ad schedule
        AdBreak adBreak = new AdBreak("pre", ad);
        List<AdBreak> adSchedule = new ArrayList<AdBreak>();
        adSchedule.add(adBreak);

        // Set the ad schedule to your video
        video.setAdSchedule(adSchedule);

        playlist.add(video);
        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .build();

        return pc.toJson().toString();
    }

    private String vastNonLinear() {
        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        // Create video
        PlaylistItem video = new PlaylistItem("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8");

        // Set waterfall to the Ad
        Ad ad = new Ad(AdSource.VAST, "http://playertest.longtailvideo.com/adtags/nonlinear.xml");

        // Set the ad break offset
        AdBreak adBreak = new AdBreak("pre", ad);

        // Specify the AdType as NONLINEAR
        adBreak.setAdType(AdType.NONLINEAR);
        List<AdBreak> adSchedule = new ArrayList<AdBreak>();
        adSchedule.add(adBreak);

        // Set the ad schedule to your video
        video.setAdSchedule(adSchedule);

        playlist.add(video);
        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .build();

        return pc.toJson().toString();

    }


    private String vastAdPods() {
        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        // Create video
        PlaylistItem video = new PlaylistItem("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8");

        // Set waterfall to the Ad
        Ad ad = new Ad(AdSource.VAST, "https://s3.amazonaws.com/demo.jwplayer.com/advertising/assets/vast3_jw_ads.xml");

        // Set the ad break offset
        AdBreak adBreak = new AdBreak("pre", ad);
        List<AdBreak> adSchedule = new ArrayList<AdBreak>();
        adSchedule.add(adBreak);

        // Set the ad schedule to your video
        video.setAdSchedule(adSchedule);

        playlist.add(video);
        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .build();

        return pc.toJson().toString();

    }

    private String vastVPAID2() {

        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        // Create video
        PlaylistItem video = new PlaylistItem("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8");

        // Set waterfall to the Ad
        Ad ad = new Ad(AdSource.VAST, "http://search.spotxchange.com/vast/2.00/85394?VPAID=js&content_page_url=www.testing123.com&cb=__random-number__&device[devicetype]=1&device[dnt]=0");

        // Set the ad break offset
        AdBreak adBreak = new AdBreak("pre", ad);
        List<AdBreak> adSchedule = new ArrayList<AdBreak>();
        adSchedule.add(adBreak);

        // Set the ad schedule to your video
        video.setAdSchedule(adSchedule);

        playlist.add(video);
        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .build();

        return pc.toJson().toString();

    }


    private String imaMidPost() {

        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        // Create video
        PlaylistItem video = new PlaylistItem("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8");

        // Set waterfall to the Ad
        Ad ad = new Ad(AdSource.IMA, "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator=");

        // Set the ad break offset
        AdBreak adBreak = new AdBreak("5", ad);
        AdBreak adBreak2 = new AdBreak("10", ad);
        AdBreak adBreak3 = new AdBreak("post", ad);
        List<AdBreak> adSchedule = new ArrayList<AdBreak>();
        adSchedule.add(adBreak);
        adSchedule.add(adBreak2);
        adSchedule.add(adBreak3);

        // Set the ad schedule to your video
        video.setAdSchedule(adSchedule);

        playlist.add(video);
        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .build();

        return pc.toJson().toString();
    }

    private String captionStyling() {
        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        PlaylistItem playlistItem = new PlaylistItem();

        Caption caption = new Caption();
        caption.setKind(CaptionType.CAPTIONS);
        caption.setLabel("EN");
        caption.setFile("http://content.jwplatform.com/tracks/0dmKf7G8.vtt");
        List<Caption> captionList = new ArrayList<>();
        captionList.add(caption);

        playlistItem.setFile("http://content.jwplatform.com/manifests/PxnoM5gE.m3u8");
        playlistItem.setTitle("KUNG FURY");
        playlistItem.setDescription("It takes a cop from the future to fight an enemy from the past.");
        playlistItem.setImage("http://content.jwplatform.com/thumbs/PxnoM5gE-720.jpg");
        playlistItem.setCaptions(captionList);
        playlistItem.setMediaId("PxnoM5gE");

        playlist.add(playlistItem);

        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .captionsFontSize(20)
                .captionsFontFamily("serif")
                .captionsFontOpacity(100)
                .captionsBackgroundColor("#1F33C4")
                .captionsBackgroundOpacity(100)
                .captionsColor("#FFD000")
                .build();

        return pc.toJson().toString();
    }

    private String vastAdTagMacros() {

        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        // Create video
        PlaylistItem video = new PlaylistItem("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8");
        // Make sure to set the item information you'll be expanding later
        video.setDescription("BipBop");
        // Set waterfall to the Ad
        Ad ad = new Ad(AdSource.VAST, "https://playertest.longtailvideo.com/adtags/vmap2.xml?descr=__item-description__&height=__player-height__&width=__player-width__");

        // Set the ad break offset
        AdBreak adBreak = new AdBreak("pre", ad);
        List<AdBreak> adSchedule = new ArrayList<AdBreak>();
        adSchedule.add(adBreak);

        // Set the ad schedule to your video
        video.setAdSchedule(adSchedule);

        playlist.add(video);
        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .build();

        return pc.toJson().toString();
    }

    private String imaAdTagMacros() {

        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        // Create video
        PlaylistItem video = new PlaylistItem("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8");
        // Make sure to set the item information you'll be expanding later
        video.setDescription("BipBop");
        // Set waterfall to the Ad
        Ad ad = new Ad(AdSource.IMA, "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator=");

        // Set the ad break offset
        AdBreak adBreak = new AdBreak("pre", ad);
        List<AdBreak> adSchedule = new ArrayList<AdBreak>();
        adSchedule.add(adBreak);

        // Set the ad schedule to your video
        video.setAdSchedule(adSchedule);

        playlist.add(video);
        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .build();

        return pc.toJson().toString();
    }

    private String imaPreBumperVmap() {

        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        // Create video
        PlaylistItem video = new PlaylistItem("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8");

        // Set waterfall to the Ad
        VMAPAdvertising vmapAdvertising = new VMAPAdvertising(AdSource.IMA, "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpreonlybumper&cmsid=496&vid=short_onecue&correlator=");


        playlist.add(video);
        PlayerConfig pc = new PlayerConfig.Builder()
                .playlist(playlist)
                .advertising(vmapAdvertising)
                .build();

        return pc.toJson().toString();
    }


}
