package com.gmsyrimis.jwplayer.custom;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.views.ApiSidebar;

/**
 * Created by gsyrimis on 10/13/16.
 */

public abstract class ToastClickListener implements View.OnClickListener {

    private AppCompatActivity mActivity;
    private DrawerLayout mDrawerLayout;
    private ApiSidebar mDrawerList;

    public ToastClickListener(AppCompatActivity activity, DrawerLayout drawerLayout, ApiSidebar drawerList) {
        mActivity = activity;
        mDrawerLayout = drawerLayout;
        mDrawerList = drawerList;
    }

    abstract public String setMessage();

    @Override
    public void onClick(View v) {
        Toast.makeText(mActivity, setMessage(), Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawer(mDrawerList);
    }
}
