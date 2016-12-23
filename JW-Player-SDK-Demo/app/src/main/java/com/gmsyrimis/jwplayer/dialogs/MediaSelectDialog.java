package com.gmsyrimis.jwplayer.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gmsyrimis.jwplayer.JWApplication;
import com.gmsyrimis.jwplayer.R;




public class MediaSelectDialog extends Dialog {

    private Button mSelectVideo;
    private Button mSelectAudio;
    private Button mSelectCancel;
    private Activity mActivity;

    public MediaSelectDialog(Activity activity) {
        super(activity);
        mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_media_select);


        mSelectVideo = (Button) findViewById(R.id.media_select_video);
        mSelectAudio = (Button) findViewById(R.id.media_select_audio);
        mSelectCancel = (Button) findViewById(R.id.media_select_cancel);

        mSelectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("video/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("video/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Video");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                mActivity.startActivityForResult(chooserIntent, JWApplication.LOCAL_VIDEO_FILE);
                dismiss();
            }
        });

        mSelectAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("audio/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("audio/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Video");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                mActivity.startActivityForResult(chooserIntent, JWApplication.LOCAL_AUDIO_FILE);
                dismiss();
            }
        });

        mSelectCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}