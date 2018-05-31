package com.gmsyrimis.jwplayer.utilities;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gmsyrimis.jwplayer.views.JWSpinner;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by gsyrimis on 4/18/16.
 */
public class Utils {


    public static int DpToPx(Context context, int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, context.getResources().getDisplayMetrics()));
    }

    public static String configToWebConfig(PlayerConfig playerConfig){
        JSONObject config = playerConfig.toJson();
        try {
            config.put("aspectratio","16:9");
            config.remove("height");
            config.remove("base");
            config.remove("mobileSdk");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return config.toString();
    }

    public static String playlistToString(List<PlaylistItem> playlist) {
        String result = "[\n";

        for (PlaylistItem item : playlist) {
            try {
                result += item.toJson().toString(4);
                result += ",";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        result += "\n]";
        return result;
    }

    public static String handleObjectData(String s) {
        if (s != null) {
            return s;
        } else {
            return "";
        }
    }

    public static String handleObjectData(Integer i) {
        if (i != null) {
            return String.valueOf(i);
        } else {
            return "";
        }
    }

    public static String getStringData(View view) {
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            if (!editText.getText().toString().isEmpty()) {
                return editText.getText().toString();
            }
        }

        if (view instanceof Spinner) {
            Spinner spinner = (Spinner) view;
            if (!spinner.getSelectedItem().equals("")) {
                return (String) spinner.getSelectedItem();
            }
        }

        return null;
    }

    public static Integer getIntegerData(View view) {
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            if (!editText.getText().toString().isEmpty()) {
                return Integer.valueOf(editText.getText().toString());
            }
        }
        return null;
    }

    public static void enableViews(List<View> views, boolean isEnabled) {
        for (View current : views) {
            if (current instanceof LinearLayout) {
                LinearLayout container = (LinearLayout) current;
                for (int i = 0; i < container.getChildCount(); i++) {
                    TextView tv = (TextView) container.getChildAt(i);
                    tv.setEnabled(isEnabled);
                }
            }else{
                current.setEnabled(isEnabled);
            }

        }
    }

    public static void clearViews(List<View> views) {
        for (View current : views) {
            if (current instanceof EditText) {
                EditText editText = (EditText) current;
                editText.setText("");
            }

            if (current instanceof LinearLayout) {
                LinearLayout container = (LinearLayout) current;
                container.removeAllViews();
            }

            if (current instanceof JWSpinner){
                JWSpinner jwSpinner = (JWSpinner) current;
                jwSpinner.setSelection("");
            }

        }
    }

    public static String getVideoPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Video.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getAudioPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
//            String[] proj = {MediaStore.Audio.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, null, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getImagePathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getDocumentsFilePath(Context context, Uri contentUri) {
//        String docPath = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            docPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
//        }
//        String fileName = contentUri.toString().substring(contentUri.toString().indexOf("%2F") + 3);
//        String path = "file://" + docPath +"/"+ fileName;
        return getPath(context, contentUri);
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
