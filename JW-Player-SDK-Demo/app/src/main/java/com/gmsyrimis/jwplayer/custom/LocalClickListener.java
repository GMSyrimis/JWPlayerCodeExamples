package com.gmsyrimis.jwplayer.custom;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gmsyrimis.jwplayer.dialogs.MediaSelectDialog;

/**
 * Created by gsyrimis on 10/17/16.
 */

public class LocalClickListener implements View.OnClickListener {

    private AppCompatActivity mActivity;

    public LocalClickListener(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void onClick(View v) {
        new MediaSelectDialog(mActivity).show();
    }
}
