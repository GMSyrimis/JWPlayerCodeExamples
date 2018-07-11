package com.jwplayer.webviewfullscreen;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private Button mNativePlayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Setup webview
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // need this for javascript
        webSettings.setMediaPlaybackRequiresUserGesture(false); // need this for autostart - programmatic play
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // Always get a fresh page
        webSettings.setDomStorageEnabled(true); // Need this for localStorage
        WebView.setWebContentsDebuggingEnabled(true); // Need this to debug the WebView
        mWebView.setWebChromeClient(new JWWebChromeClient(mWebView)); // Need this to reset the default video poster image on android

        JavascriptBridge playerBridgeObj = new JavascriptBridge(); //  Need this to handle fullscreen calls
        mWebView.addJavascriptInterface(playerBridgeObj, "Android");
        // Load external url
        mWebView.loadUrl("http://qa.jwplayer.com.s3.amazonaws.com/~george/webview_test_jw8_android.html");

        // Native button controlling the player within the WebView
        mNativePlayBtn = findViewById(R.id.play_btn);
        mNativePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // You could use this to call JW Player API's from native code
                String playCommand = "javascript:jwplayer().play(true)";
                mWebView.loadUrl(playCommand);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

    }

    public class JavascriptBridge {
        @JavascriptInterface
        public void fullscreen(boolean isFullscreen) {
            // DO FULLSCREEN ALL OPERATIONS
            // in this demo I only do a few
            if (isFullscreen) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getSupportActionBar().hide();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        setUiVisibility(false);
                        // Hide your native UI components
                        mNativePlayBtn.setVisibility(View.GONE);
                    }
                });

            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getSupportActionBar().show();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        setUiVisibility(true);
                        // Show your native UI components
                        mNativePlayBtn.setVisibility(View.VISIBLE);
                    }
                });

            }
        }

    }


    private void setUiVisibility(boolean visible) {
        int flags;
        if (visible) {
            flags = View.SYSTEM_UI_FLAG_VISIBLE;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                flags = View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            } else {
                // Jelly bean handling
                flags = View.SYSTEM_UI_FLAG_FULLSCREEN;

                if (shouldUseFlags()) {
                    flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                }

                // Check for on screen controls, and if they are present
                // add the hide navigation flag which hides them
                // We can't just hide them since it causes issues on (Samsung) phones that
                // do not have on screen controls.
                boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
                boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
                if (!hasBackKey || !hasHomeKey) {
                    flags |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;


                    if (shouldUseFlags()) {
                        flags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                    }
                }
            }
        }
        getWindow().getDecorView().setSystemUiVisibility(flags);
    }


    // Since I'm using WebView.setWebContentsDebuggingEnabled(true); my project is set to a minimum of API 19
    private boolean shouldUseFlags() {
        return !(Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN ||
                Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1 ||
                Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2);
    }


}

