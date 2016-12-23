package com.gmsyrimis.jwplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.JWApplication;
import com.gmsyrimis.jwplayer.R;
import com.gmsyrimis.jwplayer.custom.JWActivity;
import com.gmsyrimis.jwplayer.custom.LocalClickListener;
import com.gmsyrimis.jwplayer.utilities.Utils;
import com.gmsyrimis.jwplayer.views.HttpHeadersView;
import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.gmsyrimis.jwplayer.JWApplication.NEW_PLAYLIST_ITEM;
import static com.gmsyrimis.jwplayer.JWApplication.master_config;

public class MediaSourceActivity extends JWActivity<MediaSource> {

    private int mPlaylistItemIndex;

    private List<MediaSource> mMediaSourceList;

    private Button mFileQr;
    private Button mFileLcl;
    private EditText mFileUrl;
    private EditText mMediaSourceLabel;


    // Media Type Spinner
    private Spinner mMediaTypeSpinner;
    private ArrayList<String> mMediaTypeSpinnerData;
    private ArrayAdapter<String> mMediaTypeSpinnerAdapter;

    // isDefault Spinner
    private CheckBox mIsDefaultSpinner;

    // ListView
    private ListView mMediaSourceListView;
    private List<String> mMediaSourceLabelList;
    private ArrayAdapter<String> mMediaSourceListAdapter;
    private int mCurrentItemSelected = -1;

    private Button mAddToSources;
    private Button mEditBtn;
    private Button mNewBtn;
    private Button mCommitBtn;
    private Button mSetListBtn;

    private Button mClearList;
    private Button mLoadList;
    private Button mSaveList;

    private HttpHeadersView mHttpHeadersView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_source);

        getSupportActionBar().setTitle("Media Source");

        // Initialize major data stores
        initDataStructures();
        handleIntent(getIntent());

        // FIND VIEWS
        mMediaSourceLabel = (EditText) findViewById(R.id.mediasource_label_et);
        mFileUrl = (EditText) findViewById(R.id.mediasource_file_et);

        mMediaTypeSpinner = (Spinner) findViewById(R.id.mediasource_type_spinner);
        setupMediaTypeSpinner();

        mIsDefaultSpinner = (CheckBox) findViewById(R.id.mediasource_default_check);

        mHttpHeadersView = (HttpHeadersView) findViewById(R.id.mediasource_http_headers_view);
        mHttpHeadersView.initialize(this);

        mMediaSourceListView = (ListView) findViewById(R.id.mediasource_list_view);
        setupListView();

        mFileQr = (Button) findViewById(R.id.mediasource_file_qr);
        mFileQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), com.gmsyrimis.jwplayer.activities.QrActivity.class), JWApplication.QR_SOURCE_URL);
            }
        });

        mFileLcl = (Button) findViewById(R.id.mediasource_file_lcl);
        mFileLcl.setOnClickListener(new LocalClickListener(MediaSourceActivity.this));


        mAddToSources = (Button) findViewById(R.id.mediasource_add_btn);
        mAddToSources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaSource mediaSource = parseScreen();

                if (mediaSource != null) {
                    mMediaSourceList.add(mediaSource);
                    mMediaSourceLabelList.add(mediaSource.toJson().toString());
                    mMediaSourceListAdapter.notifyDataSetChanged();
                    clearViews();
                    mEditBtn.setVisibility(View.GONE);
                    mCommitBtn.setVisibility(View.GONE);
                    mAddToSources.setVisibility(View.VISIBLE);
                    mNewBtn.setVisibility(View.GONE);
                } else {
                    Toast.makeText(MediaSourceActivity.this, "Missing File", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mMediaSourceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // UPDATE mCurrentAdBreak
                mCurrentItemSelected = position;
                // GET MediaSource AT POSITION
                MediaSource currentMediaSource = mMediaSourceList.get(position);
                // POPULATE MediaSource DATA INTO VIEWS
                parseObject(currentMediaSource);
                // SET ENABLED ON VIEWS TO FALSE
                enableViews(false);
                // HIDE ADD BTN
                mAddToSources.setVisibility(View.GONE);
                // HIDE COMMIT BTN
                mCommitBtn.setVisibility(View.GONE);
                // SHOW NEW BTN
                mNewBtn.setVisibility(View.VISIBLE);
                // SHOW EDIT BTN
                mEditBtn.setVisibility(View.VISIBLE);
            }
        });

        mEditBtn = (Button) findViewById(R.id.mediasource_edit_btn);
        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SET ENABLED ON VIEWS TO TRUE
                enableViews(true);
                // HIDE EDIT BTN
                mEditBtn.setVisibility(View.GONE);
                // SHOW COMMIT BTN
                mCommitBtn.setVisibility(View.VISIBLE);
                mAddToSources.setVisibility(View.GONE);
                mNewBtn.setVisibility(View.VISIBLE);
            }
        });

        mCommitBtn = (Button) findViewById(R.id.mediasource_commit_btn);
        mCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // BUILD MEDIASOURCE
                MediaSource mediaSource = parseScreen();

                if (mediaSource != null) {
                    // ADD THE MEDIASOURCE AT THE CORRECT POSITION IN mediaSources and mediaSourceLabels
                    mMediaSourceList.set(mCurrentItemSelected, mediaSource);
                    mMediaSourceLabelList.set(mCurrentItemSelected, mediaSource.toJson().toString());
                    // UPDATE ADAPTER
                    mMediaSourceListAdapter.notifyDataSetChanged();
                    clearViews();
                    mEditBtn.setVisibility(View.GONE);
                    mCommitBtn.setVisibility(View.GONE);
                    mAddToSources.setVisibility(View.VISIBLE);
                    mNewBtn.setVisibility(View.GONE);
                } else {
                    Toast.makeText(MediaSourceActivity.this, "Missing File", Toast.LENGTH_SHORT).show();

                }

            }
        });

        mNewBtn = (Button) findViewById(R.id.mediasource_new_btn);
        mNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearViews();
                mEditBtn.setVisibility(View.GONE);
                mCommitBtn.setVisibility(View.GONE);
                mAddToSources.setVisibility(View.VISIBLE);
                mNewBtn.setVisibility(View.GONE);
                enableViews(true);
            }
        });

        mSetListBtn = (Button) findViewById(R.id.mediasource_set_list);
        mSetListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray jsonArray = new JSONArray();
                for (MediaSource current : mMediaSourceList) {
                    jsonArray.put(current.toJson());
                }
                // THIS IS A LEAF ACTIVITY IT MAKES SENSE TO REUSE IT WITH A RESULT AND FINISH
                Intent returnIntent = new Intent();
                returnIntent.putExtra(JWApplication.RESULT, jsonArray.toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


        mClearList = (Button) findViewById(R.id.mediasource_clear_list);
        mClearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearViews();
                mMediaSourceList.clear();
                mMediaSourceLabelList.clear();
                mMediaSourceListAdapter.notifyDataSetChanged();
                mCurrentItemSelected = 0;
            }
        });


    }


    private void setupMediaTypeSpinner() {
        mMediaTypeSpinnerData = new ArrayList<>();
        mMediaTypeSpinnerData.add("");
        mMediaTypeSpinnerData.add(MediaType.HLS.name());
        mMediaTypeSpinnerData.add(MediaType.MP4.name());
        mMediaTypeSpinnerData.add(MediaType.MPD.name());
        mMediaTypeSpinnerData.add(MediaType.ISM.name());
        mMediaTypeSpinnerData.add(MediaType.WEBM.name());
        mMediaTypeSpinnerData.add(MediaType.AAC.name());
        mMediaTypeSpinnerData.add(MediaType.MP3.name());


        mMediaTypeSpinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                mMediaTypeSpinnerData);
        mMediaTypeSpinner.setAdapter(mMediaTypeSpinnerAdapter);
    }


    private void setupListView() {
        mMediaSourceListAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                mMediaSourceLabelList);
        mMediaSourceListView.setAdapter(mMediaSourceListAdapter);
    }

    private void clearViews() {
        mMediaSourceLabel.setText("");
        mFileUrl.setText("");
        mMediaTypeSpinner.setSelection(0);
        mIsDefaultSpinner.setChecked(false);
        mHttpHeadersView.clear();
    }


    private void enableViews(boolean isEnabled) {
        mMediaSourceLabel.setEnabled(isEnabled);
        mFileUrl.setEnabled(isEnabled);
        mMediaTypeSpinner.setEnabled(isEnabled);
        mIsDefaultSpinner.setEnabled(isEnabled);
        mFileQr.setEnabled(isEnabled);
        mHttpHeadersView.setEnabled(false);
    }

    @Override
    protected MediaSource parseScreen() {
        if (Utils.getStringData(mFileUrl) != null) {
            MediaSource mediaSource = new MediaSource();
            mediaSource.setFile(Utils.getStringData(mFileUrl));
            mediaSource.setType((Utils.getStringData(mMediaTypeSpinner) == null) ? null : MediaType.valueOf(Utils.getStringData(mMediaTypeSpinner)));
            mediaSource.setDefault(mIsDefaultSpinner.isChecked());
            mediaSource.setLabel(Utils.getStringData(mMediaSourceLabel));
            mediaSource.setHttpHeaders(mHttpHeadersView.getHttpHeaders());
            return mediaSource;
        }
        return null;
    }

    @Override
    protected void parseObject(MediaSource object) {
        clearViews();
        mFileUrl.setText(Utils.handleObjectData(object.getFile()));
        mMediaTypeSpinner.setSelection((object.getType() == null) ? 0 : mMediaTypeSpinnerAdapter.getPosition(Utils.handleObjectData(object.getType().name())));
        mIsDefaultSpinner.setChecked(object.getDefault());
        mMediaSourceLabel.setText(Utils.handleObjectData(object.getLabel()));
        if (object.getHttpHeaders() != null) {
            mHttpHeadersView.parseMap(object.getHttpHeaders());
        }
    }

    @Override
    protected void initDataStructures() {
        mMediaSourceList = new ArrayList<>();
        mMediaSourceLabelList = new ArrayList<>();
    }

    @Override
    protected void handleIntent(Intent intent) {
        // GET EXTRAS FROM INTENT
        mPlaylistItemIndex = intent.getExtras().getInt(JWApplication.PLAYLIST_ITEM_INDEX);

        boolean shouldParsePlaylist =
                master_config.getPlaylist() != null &&
                master_config.getPlaylist().size() > 0 &&
                mPlaylistItemIndex != NEW_PLAYLIST_ITEM;

        if (shouldParsePlaylist) {
            PlaylistItem currentItem = master_config.getPlaylist().get(mPlaylistItemIndex);
            // null check AdSchedule since we could be editing a mPlaylist item with no AdSchedule
            if (currentItem.getSources() != null) {
                mMediaSourceList = currentItem.getSources();
                mMediaSourceLabelList = new ArrayList<>();
                for (MediaSource current : mMediaSourceList) {
                    mMediaSourceLabelList.add(current.toJson().toString());
                }
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case JWApplication.QR_SOURCE_URL:
                    String result = data.getStringExtra(JWApplication.RESULT);
                    mFileUrl.setText(result);
                    break;

                case JWApplication.LOCAL_VIDEO_FILE:
                    String path = "file://" + Utils.getVideoPathFromURI(this, data.getData());
                    mFileUrl.setText(path);
                    break;
            }
            mHttpHeadersView.onActivityResult(requestCode, resultCode, data);
        }
    }
}
