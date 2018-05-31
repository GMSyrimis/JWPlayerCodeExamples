package com.gmsyrimis.jwplayer.providers;

/**
 * Created by gsyrimis on 3/7/16.
 */
public class PlayAdProvider extends DataProvider {

    public PlayAdProvider() {
        mLabels.add("VAST");
        mPayload.add("http://www.adotube.com/php/services/player/OMLService.php?avpid=oRYYzvQ&platform_version=vast20&ad_type=linear&groupbypass=1&HTTP_REFERER=http://www.longtailvideo.com&video_identifier=longtailvideo.com,test");


        mLabels.add("VAST Ad Pod");
        mPayload.add("http://demo.jwplayer.com/advertising/assets/vast3_jw_ads.xml");


        mLabels.add("VAST Non-Linear");
        mPayload.add("http://bob.jwplayer.com.s3.amazonaws.com/~george/non_linear.xml");


        mLabels.add("IMA");
        mPayload.add("https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator=");

        mLabels.add("IMA Non-Linear");
        mPayload.add("https://pubads.g.doubleclick.net/gampad/ads?sz=480x70&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dnonlinear&correlator=");


    }

}
