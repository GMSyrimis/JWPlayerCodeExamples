package com.gmsyrimis.jwplayer.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.JWApplication;
import com.gmsyrimis.jwplayer.R;
import com.gmsyrimis.jwplayer.activities.QrActivity;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by gsyrimis on 10/27/16.
 */

public class HttpHeadersView extends LinearLayout {

    private Button mAdd;
    private Button mRemove;
    private Button mKeyQr;
    private Button mValueQr;
    private LinearLayout mHttpHeadersContainer;
    private EditText mHttpHeaderKey;
    private EditText mHttpHeaderValue;
    private AppCompatActivity mActivity;
    private Map<String, String> mHttpHeadersMap;

    public HttpHeadersView(Context context) {
        super(context);
        inflate(getContext(), R.layout.view_http_headers, this);
        findViews();
    }

    public HttpHeadersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_http_headers, this);
        findViews();
    }

    public HttpHeadersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.view_http_headers, this);
        findViews();
    }

    private void findViews() {
        mHttpHeadersContainer = (LinearLayout) findViewById(R.id.http_headers_container);

        mHttpHeaderKey = (EditText) findViewById(R.id.http_headers_key);
        mHttpHeaderValue = (EditText) findViewById(R.id.http_headers_value);

        mKeyQr = (Button) findViewById(R.id.http_headers_key_qr);
        mValueQr = (Button) findViewById(R.id.http_headers_value_qr);

        mAdd = (Button) findViewById(R.id.http_headers_add);
        mRemove = (Button) findViewById(R.id.http_headers_remove);

    }

    public void initialize(AppCompatActivity activity) {
        mActivity = activity;
        mHttpHeadersMap = new HashMap<>();
        setupViewBehavior();
    }


    private void setupViewBehavior() {

        mKeyQr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.startActivityForResult(new Intent(mActivity, QrActivity.class), JWApplication.QR_HTTP_HEADER_KEY);
            }
        });

        mValueQr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.startActivityForResult(new Intent(mActivity, QrActivity.class),
                        JWApplication.QR_HTTP_HEADER_VALUE);
            }
        });

        mAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = mHttpHeaderKey.getText().toString();
                String value = mHttpHeaderValue.getText().toString();

                if (!key.isEmpty() && !value.isEmpty()) {
                    Log.d("httpHeadersView", "mAdd.setOnClickListener");
                    createKeyValuePair(key, value);

                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("httpHeadersView", "UI Thread mAdd.setOnClickListener");
                            mHttpHeaderKey.setText("");
                            mHttpHeaderValue.setText("");
                        }
                    });

                } else {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mActivity, "Specify key value pair", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        mRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mHttpHeaderKey.setText("");
                        mHttpHeaderValue.setText("");
                    }
                });
            }
        });

    }


    public void parseMap(Map<String, String> httpHeaders) {
        if (httpHeaders != null) {
            for (String currentKey : httpHeaders.keySet()) {
                createKeyValuePair(currentKey, httpHeaders.get(currentKey));
            }
        }else{
            Log.d("httpHeadersView","parseMap = null");
            Log.d("httpHeadersView","headersMap = " + mHttpHeadersMap);
        }
    }

    public Map<String, String> getHttpHeaders() {
        Log.d("httpHeadersView","getHttpHeaders");
        return (mHttpHeadersMap.isEmpty()) ? null : mHttpHeadersMap;
    }

    private void createKeyValuePair(String key, String value) {
        if (!mHttpHeadersMap.keySet().contains(key)){
            // create key/value container
            final LinearLayout keyValueContainer = new LinearLayout(mActivity);
            keyValueContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            keyValueContainer.setOrientation(HORIZONTAL);

            // create key/value textviews
            final TextView keyTextView = new TextView(mActivity);
            final TextView valueTextView = new TextView(mActivity);
            LayoutParams textParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            keyTextView.setLayoutParams(textParams);
            valueTextView.setLayoutParams(textParams);
            // set the text to the key/value
            keyTextView.setText(key);
            valueTextView.setText(value);
            // add the views to the container
            keyValueContainer.addView(keyTextView);
            keyValueContainer.addView(valueTextView);
            Log.d("HttpHeaderView", String.format("Key:%s Value:%s", key, value));
            mHttpHeadersMap.put(key, value);

            keyValueContainer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("httpHeadersView", "keyValueContainer.setOnClickListener");
                            String key = keyTextView.getText().toString();
                            String value = valueTextView.getText().toString();
                            mHttpHeaderKey.setText(key);
                            mHttpHeaderValue.setText(value);
                            mHttpHeadersContainer.removeView(keyValueContainer);
                            mHttpHeadersMap.remove(key);
                        }
                    });
                }
            });
            mHttpHeadersContainer.addView(keyValueContainer);
        }else{
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mActivity, "Keys must be unique", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra(JWApplication.RESULT);
            switch (requestCode) {
                case JWApplication.QR_HTTP_HEADER_KEY:
                    mHttpHeaderKey.setText(result);
                    break;

                case JWApplication.QR_HTTP_HEADER_VALUE:
                    mHttpHeaderValue.setText(result);
                    break;
            }
        }
    }

    public void clear() {
        Log.d("httpHeadersView", "clear()");
        mHttpHeadersMap = new HashMap<>();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mHttpHeadersContainer.removeAllViews();
                mHttpHeaderKey.setText("");
                mHttpHeaderValue.setText("");
            }
        });

    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mAdd.setEnabled(enabled);
        mRemove.setEnabled(enabled);
        mKeyQr.setEnabled(enabled);
        mValueQr.setEnabled(enabled);
        mHttpHeadersContainer.setEnabled(enabled);
        mHttpHeaderKey.setEnabled(enabled);
        mHttpHeaderValue.setEnabled(enabled);
    }
}
