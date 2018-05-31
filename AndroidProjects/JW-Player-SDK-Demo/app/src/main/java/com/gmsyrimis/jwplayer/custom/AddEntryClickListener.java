package com.gmsyrimis.jwplayer.custom;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gmsyrimis.jwplayer.dialogs.AddDialog;
import com.gmsyrimis.jwplayer.views.JWSpinner;

/**
 * Created by gsyrimis on 10/17/16.
 */

public class AddEntryClickListener implements View.OnClickListener {
    private AppCompatActivity mActivity;
    private JWSpinner mSpinner;
    private String mPayload;

    public AddEntryClickListener(AppCompatActivity mActivity, JWSpinner mSpinner) {
        this.mActivity = mActivity;
        this.mSpinner = mSpinner;
    }

    public AddEntryClickListener(AppCompatActivity mActivity, JWSpinner mSpinner, String payload) {
        this.mActivity = mActivity;
        this.mSpinner = mSpinner;
        this.mPayload = payload;
    }

    @Override
    public void onClick(View v) {

        if (mPayload != null) {
            AddDialog addDialog = new AddDialog(mActivity, mSpinner, mPayload);
            addDialog.show();
        } else {
            AddDialog addDialog = new AddDialog(mActivity, mSpinner);
            addDialog.show();
        }

    }
}
