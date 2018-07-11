package com.jwplayer.webviewfullscreen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by gsyrimis on 5/22/17.
 */

public class JWWebChromeClient extends WebChromeClient {

    /**
     * Reference to the WebView this WebChromeClient is handling poster image requests for
     */
    private WebView mWebView;

    public JWWebChromeClient(WebView webView) {
        mWebView = webView;
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        Bitmap bitmap = Bitmap.createBitmap(mWebView.getWidth(), mWebView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        return bitmap;
    }

    @Override
    public View getVideoLoadingProgressView() {
        return null;
    }
}