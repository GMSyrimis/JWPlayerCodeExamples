package com.gmsyrimis.jwplayer.custom;

import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gsyrimis on 5/14/16.
 */
public class JWScreen {
    int[] mViews;
    Window mWindow;

    public JWScreen(Window context, int[] views) {
        this.mViews = views;
        this.mWindow = context;
    }

    public List<View> getListOfViews() {
        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < mViews.length; i++) {
            viewList.add(mWindow.findViewById(mViews[i]));
        }
        return viewList;
    }

    public View getView(int id){
        return mWindow.findViewById(id);
    }

}
