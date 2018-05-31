package com.gmsyrimis.jwplayer.custom;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.R;

/**
 * Created by gsyrimis on 5/12/16.
 */
public class ColorTextWatcher implements TextWatcher {

    private View mColorHolder;
    private Context mContext;


    public ColorTextWatcher(Context mContext, View mColorHolder) {
        this.mColorHolder = mColorHolder;
        this.mContext = mContext;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() == 0) {
            mColorHolder.setBackgroundColor(Color.TRANSPARENT);
            mColorHolder.setBackground(mContext.getResources().getDrawable(R.drawable.playlist_item_border));
//            Toast.makeText(mContext, "Clear", Toast.LENGTH_SHORT).show();
        }
        if (s.toString().length() == 7) {
            try {
                mColorHolder.setBackgroundColor(Color.parseColor(s.toString()));
//                Toast.makeText(mContext, "Color Set", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                s.clear();
                Toast.makeText(mContext, "Invalid HEX color", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
