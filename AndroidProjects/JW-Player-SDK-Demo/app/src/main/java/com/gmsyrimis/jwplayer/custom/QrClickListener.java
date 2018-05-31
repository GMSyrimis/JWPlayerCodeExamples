package com.gmsyrimis.jwplayer.custom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gmsyrimis.jwplayer.activities.QrActivity;

/**
 * Created by gsyrimis on 9/7/16.
 */
public class QrClickListener implements View.OnClickListener {

    private AppCompatActivity mActivity;
    private int mRequestCode;
    public static final int REQUEST_CAMERA = 0;

    public QrClickListener(AppCompatActivity mActivity, int mRequestCode) {
        this.mActivity = mActivity;
        this.mRequestCode = mRequestCode;
    }

    @Override
    public void onClick(View view) {
//        requestCameraPermission();
        mActivity.startActivityForResult(new Intent(mActivity, QrActivity.class), mRequestCode);
    }



}
