package com.gmsyrimis.jwplayer.providers;

import com.longtailvideo.jwplayer.configuration.Skin;

import java.util.ArrayList;

/**
 * Created by gsyrimis on 6/3/16.
 */
public class SkinProvider {

    public ArrayList<String> labels;

    public SkinProvider() {
        labels = new ArrayList<>();
        labels.add(Skin.SEVEN.name());
        labels.add(Skin.FIVE.name());
        labels.add(Skin.SIX.name());
        labels.add(Skin.BEELDEN.name());
        labels.add(Skin.BEKLE.name());
        labels.add(Skin.ROUNDSTER.name());
        labels.add(Skin.STORMTROOPER.name());
        labels.add(Skin.GLOW.name());
        labels.add(Skin.VAPOR.name());
    }

}
