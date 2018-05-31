package com.gmsyrimis.jwplayer.custom;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.views.ApiSidebar;

/**
 * Created by gsyrimis on 10/13/16.
 */

public abstract class ToastItemSelectedListener implements AdapterView.OnItemSelectedListener {

    private AppCompatActivity mActivity;
    private DrawerLayout mDrawerLayout;
    private ApiSidebar mDrawerList;

    public ToastItemSelectedListener(AppCompatActivity activity, DrawerLayout drawerLayout, ApiSidebar drawerList) {
        mActivity = activity;
        mDrawerLayout = drawerLayout;
        mDrawerList = drawerList;


    }

    abstract public String setMessage(int index);

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(mActivity, setMessage(i), Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
