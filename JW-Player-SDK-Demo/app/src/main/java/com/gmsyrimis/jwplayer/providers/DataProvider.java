package com.gmsyrimis.jwplayer.providers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gsyrimis on 7/10/16.
 */
public class DataProvider {
    protected ArrayList<String> mPayload;
    protected ArrayList<String> mLabels;

    public DataProvider() {
        mPayload = new ArrayList<>();
        mPayload.add("");
        mLabels = new ArrayList<>();
        mLabels.add("");
    }

    public List<String> getPayload() {
        return mPayload;
    }

    ;

    public List<String> getLabels() {
        return mLabels;
    }

    ;
}
