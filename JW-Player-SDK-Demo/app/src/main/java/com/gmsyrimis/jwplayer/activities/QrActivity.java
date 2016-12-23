package com.gmsyrimis.jwplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.gmsyrimis.jwplayer.JWApplication;
import com.gmsyrimis.jwplayer.R;

public class QrActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView mDecoderview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        mDecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mDecoderview.setOnQRCodeReadListener(this);

    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(JWApplication.RESULT, text);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mDecoderview.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDecoderview.getCameraManager().stopPreview();
    }
}
