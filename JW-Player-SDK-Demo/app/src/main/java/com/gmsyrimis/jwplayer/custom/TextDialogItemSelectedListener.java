package com.gmsyrimis.jwplayer.custom;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import com.gmsyrimis.jwplayer.dialogs.TextDialog;
import com.gmsyrimis.jwplayer.views.ApiSidebar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gsyrimis on 10/13/16.
 */

public abstract class TextDialogItemSelectedListener implements AdapterView.OnItemSelectedListener {

    private AppCompatActivity mActivity;
    private DrawerLayout mDrawerLayout;
    private ApiSidebar mDrawerList;

    public TextDialogItemSelectedListener(AppCompatActivity activity, DrawerLayout drawerLayout, ApiSidebar drawerList) {
        mActivity = activity;
        mDrawerLayout = drawerLayout;
        mDrawerList = drawerList;
    }

    abstract public String setMessage(int index);


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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextDialog textDialog = new TextDialog(mActivity, setMessage(i));
        textDialog.show();
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
