package com.gmsyrimis.jwplayer.custom;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gmsyrimis.jwplayer.dialogs.TextDialog;
import com.gmsyrimis.jwplayer.views.ApiSidebar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gsyrimis on 10/13/16.
 */

public abstract class TextDialogClickListener implements View.OnClickListener {

    private AppCompatActivity mActivity;
    private DrawerLayout mDrawerLayout;
    private ApiSidebar mDrawerList;

    public TextDialogClickListener(AppCompatActivity activity, DrawerLayout drawerLayout, ApiSidebar drawerList) {
        mActivity = activity;
        mDrawerLayout = drawerLayout;
        mDrawerList = drawerList;
    }

    abstract public String setMessage();


    public String handleJSON(JSONObject jsonObject){
        String message="";
        try {
            message = jsonObject.toString(4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    public String handleJSON(JSONArray jsonArray){
        String message="";
        try {
            message = jsonArray.toString(4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }


    @Override
    public void onClick(View v) {
        TextDialog textDialog = new TextDialog(mActivity, setMessage());
        textDialog.show();
        mDrawerLayout.closeDrawer(mDrawerList);
    }
}
