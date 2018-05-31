package com.gmsyrimis.jwplayer.providers;

/**
 * Created by gsyrimis on 7/8/16.
 */
public class FileUrlProvider extends DataProvider {

    public FileUrlProvider() {
        super();
        // Label and payload
        mLabels.add("Multi Audio DASH");
        mPayload.add("http://qa.jwplayer.com/~george/manifest.mpd");
    }

}
