package com.gmsyrimis.jwplayer.custom;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.views.JWSpinner;
import com.gmsyrimis.jwplayer.dialogs.EditDialog;

/**
 * Created by gsyrimis on 8/26/16.
 */
public class EditClickListener implements View.OnClickListener {

    private JWSpinner mSpinner;
    private AppCompatActivity mActivity;
    private boolean mCanEditPayload;

    public EditClickListener(JWSpinner spinner, AppCompatActivity activity) {
        mSpinner = spinner;
        mActivity = activity;
    }

    public EditClickListener(JWSpinner spinner, AppCompatActivity activity, boolean canEditPayload) {
        mSpinner = spinner;
        mActivity = activity;
        mCanEditPayload = canEditPayload;
    }

    @Override
    public void onClick(View v) {
        if (mSpinner.getSelectedItemPosition() != 0) {

            if (mCanEditPayload) {
                EditDialog editDialog = new EditDialog(
                        mActivity,
                        mSpinner,
                        mSpinner.getSelectedItemPosition(),
                        mCanEditPayload
                );
                editDialog.show();
            }else{
                EditDialog editDialog = new EditDialog(
                        mActivity,
                        mSpinner,
                        mSpinner.getSelectedItemPosition()
                );
                editDialog.show();
            }
        } else {
            Toast.makeText(mActivity, "You cannot edit this entry", Toast.LENGTH_SHORT).show();
        }
    }
}
