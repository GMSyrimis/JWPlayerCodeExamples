package com.gmsyrimis.jwplayer.custom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by gsyrimis on 5/12/16.
 */
public abstract class JWActivity<T> extends AppCompatActivity {

    protected abstract T parseScreen();
    protected abstract void parseObject(T object);
    protected abstract void initDataStructures();
    protected abstract void handleIntent(Intent intent);


}
