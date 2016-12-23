package com.gmsyrimis.jwplayer.custom;

import android.view.View;

import com.gmsyrimis.jwplayer.views.JWSpinner;

/**
 * Created by gsyrimis on 10/17/16.
 */

public class RemoveEntryClickListener implements View.OnClickListener {

    private JWSpinner mSpinner;

    public RemoveEntryClickListener(JWSpinner mSpinner) {
        this.mSpinner = mSpinner;
    }

    @Override
    public void onClick(View v) {
        mSpinner.removeEntry(mSpinner.getSelectedItemPosition());
    }
}
