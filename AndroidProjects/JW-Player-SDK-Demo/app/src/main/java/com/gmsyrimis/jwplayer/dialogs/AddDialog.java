package com.gmsyrimis.jwplayer.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.views.JWSpinner;
import com.gmsyrimis.jwplayer.R;




public class AddDialog extends Dialog {


    private EditText mLabel;
    private EditText mPayload;

    private Button mSaveBtn;
    private Button mCancelBtn;

    private JWSpinner mJWSpinner;

    private AppCompatActivity mActivity;
    private String mStringPayload;


    public AddDialog(AppCompatActivity activity, JWSpinner jwSpinner) {
        super(activity);
        mActivity = activity;
        mJWSpinner = jwSpinner;
    }

    public AddDialog(AppCompatActivity activity, JWSpinner jwSpinner,String payload) {
        super(activity);
        mActivity = activity;
        mJWSpinner = jwSpinner;
        mStringPayload = payload;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add);

        setTitle("Add new entry");

        mLabel = (EditText) findViewById(R.id.add_dialog_label);
        mPayload = (EditText) findViewById(R.id.add_dialog_payload);
        if (mStringPayload!=null){
            mPayload.setText(mStringPayload);
            mPayload.setEnabled(false);
        }

        mSaveBtn = (Button) findViewById(R.id.add_save_btn);
        mCancelBtn = (Button) findViewById(R.id.add_cancel_btn);


        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = mLabel.getText().toString();
                String payload = mPayload.getText().toString();
                if (!label.isEmpty() && !payload.isEmpty()) {
                    mJWSpinner.addEntry(label,payload);
                    mJWSpinner.save();
                    dismiss();
                } else {
                    Toast.makeText(mActivity, "Please specify label & payload", Toast.LENGTH_SHORT).show();
                }
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