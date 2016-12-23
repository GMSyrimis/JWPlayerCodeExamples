package com.gmsyrimis.jwplayer.dialogs;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.R;
import com.gmsyrimis.jwplayer.utilities.Utils;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

import org.json.JSONException;




public class ShareDialog extends Dialog {


    private TextView mPlayerVersionTv;
    private EditText mDescription;
    private CheckBox mIncludeConfig;
    private CheckBox mIncludeCallbacks;
    private Button mSendBtn;
    private Button mWebTestBtn;
    private Button mCancelBtn;

    private PlayerConfig mPlayerConfig;
    private String mCallbacksLog;
    private String mPlayerVersion;

    private AppCompatActivity mActivity;

    public ShareDialog(AppCompatActivity activity, PlayerConfig playerConfig, String callbacksLog, String playerVersion) {
        super(activity);
        this.mPlayerConfig = playerConfig;
        this.mCallbacksLog = callbacksLog;
        this.mPlayerVersion = "Player Version: " + playerVersion;
        this.mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        setTitle("Bug Report");

        mPlayerVersionTv = (TextView) findViewById(R.id.share_dialog_player_version);
        mDescription = (EditText) findViewById(R.id.share_dialog_description);
        mIncludeConfig = (CheckBox) findViewById(R.id.share_dialog_player_config);
        mIncludeCallbacks = (CheckBox) findViewById(R.id.share_dialog_callbacks);
        mSendBtn = (Button) findViewById(R.id.share_send_btn);
        mWebTestBtn = (Button) findViewById(R.id.share_web_test);
        mCancelBtn = (Button) findViewById(R.id.share_cancel_btn);

        mPlayerVersionTv.setText(mPlayerVersion);
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "\n\n" +
                        mPlayerVersion + "\n\n" +
                        "Description:\n"+
                        mDescription.getText().toString() + "\n\n";

                if (mIncludeConfig.isChecked()) {
                    try {
                        shareBody += "PlayerConfig=\n" +
                                mPlayerConfig.toJson().toString(4) + "\n\n";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (mIncludeCallbacks.isChecked()) {
                    shareBody += "CallbackLog=\n" +
                            mCallbacksLog;
                }
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sent from Android QA App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                mActivity.startActivity(Intent.createChooser(sharingIntent, "Share with:"));
                dismiss();
            }
        });

        mWebTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("config", Utils.configToWebConfig(mPlayerConfig));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "Config copied to clipboard!", Toast.LENGTH_SHORT).show();

                String url = "http://qa.jwplayer.com.s3.amazonaws.com/~george/sdk_781_test.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                mActivity.startActivity(i);
                dismiss();
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}