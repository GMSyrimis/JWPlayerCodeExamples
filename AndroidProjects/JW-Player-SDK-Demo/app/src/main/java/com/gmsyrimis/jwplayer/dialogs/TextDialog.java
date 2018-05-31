package com.gmsyrimis.jwplayer.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gmsyrimis.jwplayer.R;




public class TextDialog extends Dialog {

    Button mOkBtn;
    String mMessage;
    TextView mMessageView;
    String mTitle;

    public TextDialog(Activity activity, String message, String title) {
        super(activity);
        mMessage = message;
        mTitle = title;
    }

    public TextDialog(Activity activity, String message) {
        super(activity);
        mMessage = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_text);

        if (mTitle != null) {
            setTitle(mTitle);
        }

        mOkBtn = (Button) findViewById(R.id.text_dialog_ok);
        mMessageView = (TextView) findViewById(R.id.text_dialog_message);
        mMessageView.setText(mMessage);
        mMessageView.setMovementMethod(new ScrollingMovementMethod());

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}