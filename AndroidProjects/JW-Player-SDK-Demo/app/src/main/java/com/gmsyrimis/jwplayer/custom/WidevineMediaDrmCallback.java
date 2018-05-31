package com.gmsyrimis.jwplayer.custom;

import android.annotation.TargetApi;
import android.media.MediaDrm;
import android.os.Build;
import android.text.TextUtils;

import com.longtailvideo.jwplayer.media.drm.MediaDrmCallback;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

/**
 * Created by gsyrimis on 7/29/16.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class WidevineMediaDrmCallback implements MediaDrmCallback {

    private final String mLicenseKeyServerUrl;

    public WidevineMediaDrmCallback(String licenseKeyServerUrl) {
        mLicenseKeyServerUrl = licenseKeyServerUrl;
    }

    public String getLicenseKeyServerUrl() {
        return mLicenseKeyServerUrl;
    }

    @Override
    public byte[] executeProvisionRequest(UUID uuid, MediaDrm.ProvisionRequest request) throws IOException {
        String url = request.getDefaultUrl() + "&signedRequest=" + new String(request.getData());
        return executePost(url, null, null);
    }

    @Override
    public byte[] executeKeyRequest(UUID uuid, MediaDrm.KeyRequest request) throws IOException {
        String url = request.getDefaultUrl();
        if (TextUtils.isEmpty(url)) {
            url = mLicenseKeyServerUrl;
        }
        return executePost(url, request.getData(), null);
    }

    /**
     * Executes a post request using {@link HttpURLConnection}.
     *
     * @param url               The request URL.
     * @param data              The request body, or null.
     * @param requestProperties Request properties, or null.
     * @return The response body.
     * @throws IOException If an error occurred making the request.
     */
    public static byte[] executePost(String url, byte[] data, Map<String, String> requestProperties)
            throws IOException {
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(data != null);
            urlConnection.setDoInput(true);
            if (requestProperties != null) {
                for (Map.Entry<String, String> requestProperty : requestProperties.entrySet()) {
                    urlConnection.setRequestProperty(requestProperty.getKey(), requestProperty.getValue());
                }
            }
            // Write the request body, if there is one.
            if (data != null) {
                OutputStream out = urlConnection.getOutputStream();
                try {
                    out.write(data);
                } finally {
                    out.close();
                }
            }
            // Read and return the response body.
            InputStream inputStream = urlConnection.getInputStream();
            try {
                return IOUtils.toByteArray(inputStream);
            } finally {
                inputStream.close();
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}