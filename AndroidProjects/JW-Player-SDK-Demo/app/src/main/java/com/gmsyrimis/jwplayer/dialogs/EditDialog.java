package com.gmsyrimis.jwplayer.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.views.JWSpinner;
import com.gmsyrimis.jwplayer.R;




public class EditDialog extends Dialog {


    private TextView mOldLabel;
    private EditText mEditLabel;
    private Button mSaveBtn;
    private Button mCancelBtn;

    private int mIndex;
    private JWSpinner mJWSpinner;

    private AppCompatActivity mActivity;

    private boolean mEditPayload;

    public EditDialog(AppCompatActivity activity, JWSpinner jwSpinner, int index) {
        super(activity);
        mActivity = activity;
        mJWSpinner = jwSpinner;
        mIndex = index;
    }

    public EditDialog(AppCompatActivity activity, JWSpinner jwSpinner, int index, boolean editPayload) {
        super(activity);
        mIndex = index;
        mJWSpinner = jwSpinner;
        mActivity = activity;
        mEditPayload = editPayload;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit);

        if (mEditPayload) {
            setTitle("Edit Payload");
        } else {
            setTitle("Edit Label");
        }

        mOldLabel = (TextView) findViewById(R.id.edit_dialog_old_label);
        mEditLabel = (EditText) findViewById(R.id.edit_dialog_new_label);
        mSaveBtn = (Button) findViewById(R.id.edit_save_btn);
        mCancelBtn = (Button) findViewById(R.id.edit_cancel_btn);

        mOldLabel.setText(mJWSpinner.getLabels().get(mIndex));

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEditLabel.getText().toString().isEmpty()) {

                    if (mEditPayload) {
                        mJWSpinner.editEntry(mIndex,mEditLabel.getText().toString(),mEditLabel.getText().toString());
                        mJWSpinner.save();
                        dismiss();
                    } else {
                        mJWSpinner.editEntry(mIndex,mEditLabel.getText().toString());
                        mJWSpinner.save();
                        dismiss();
                    }

                } else {
                    Toast.makeText(mActivity, "Please specify label", Toast.LENGTH_SHORT).show();
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