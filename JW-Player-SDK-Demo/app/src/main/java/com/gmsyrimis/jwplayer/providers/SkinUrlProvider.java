package com.gmsyrimis.jwplayer.providers;

/**
 * Created by gsyrimis on 7/8/16.
 */
public class SkinUrlProvider extends DataProvider {

    public SkinUrlProvider() {
        super();
        // Labels
        mLabels.add("Bekle No Fullscreen");
        mPayload.add("http://qa.jwplayer.com.s3.amazonaws.com/~george/bekle_no_fullscreen.css");

        // Labels
        mLabels.add("No Error");
        mPayload.add("http://qa.jwplayer.com.s3.amazonaws.com/~george/no_error_skin.css");
    }

}
