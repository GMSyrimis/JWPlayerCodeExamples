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
import com.gmsyrimis.jwplayer.utilities.Utils;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.captions.CaptionType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


import static com.gmsyrimis.jwplayer.JWApplication.NEW_PLAYLIST_ITEM;
import static com.gmsyrimis.jwplayer.JWApplication.master_config;

public class CaptionActivity extends JWActivity<Caption> {

    private int mPlaylistItemIndex;

    private List<Caption> mCaptionList;

    private Button mQrReader;
    private Button mFileLcl;
    private EditText mFileUrl;
    private EditText mCaptionLabel;

    // CaptionKind Spinner
    private Spinner mCaptionKindSpinner;
    private ArrayList<String> mCaptionKindSpinnerData;
    private ArrayAdapter<String> mCaptionKindSpinnerAdapter;

    // isDefault Spinner
    private CheckBox mIsDefaultCheckBox;

    // ListView
    private ListView mCaptionListView;
    private List<String> mCaptionLabelList;
    private ArrayAdapter<String> mCaptionListAdapter;
    private int mCurrentItemSelected = -1;

    private Button mAddToSources;
    private Button mEditBtn;
    private Button mNewBtn;
    private Button mCommitBtn;
    private Button mSetListBtn;

    private Button mClearList;
    private Button mLoadList;
    private Button mSaveList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);

        getSupportActionBar().setTitle("Captions");

        // Initialize data stores
        initDataStructures();
        // GET EXTRAS FROM INTENT
        handleIntent(getIntent());

        // FIND VIEWS
        mCaptionLabel = (EditText) findViewById(R.id.caption_label_et);
        mFileUrl = (EditText) findViewById(R.id.caption_file_et);

        mCaptionKindSpinner = (Spinner) findViewById(R.id.caption_kind_spinner);
        setupCaptionKindSpinner();

        mIsDefaultCheckBox = (CheckBox) findViewById(R.id.caption_default_spinner);


        mCaptionListView = (ListView) findViewById(R.id.caption_list_view);
        setupListView();

        mQrReader = (Button) findViewById(R.id.caption_file_qr);
        mQrReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), com.gmsyrimis.jwplayer.activities.QrActivity.class), JWApplication.QR_CAPTION_URL);
            }
        });

        mFileLcl = (Button) findViewById(R.id.caption_file_lcl);
        mFileLcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("*/*");
                getIntent.addCategory(Intent.CATEGORY_OPENABLE);
                // TODO I feel this is wrong
//                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Files.getContentUri("external"));
//
//                pickIntent.setType("*/*");
//                pickIntent.addCategory(Intent.CATEGORY_OPENABLE);

                Intent chooserIntent = Intent.createChooser(getIntent, "Select File");
//                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, JWApplication.LOCAL_CAPTION_FILE);

            }
        });


        mAddToSources = (Button) findViewById(R.id.caption_add_btn);
        mAddToSources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Caption caption = parseScreen();
                if (caption != null) {
                    mCaptionList.add(caption);
                    mCaptionLabelList.add(caption.toJson().toString());
                    mCaptionListAdapter.notifyDataSetChanged();
                    clearViews();
                    mEditBtn.setVisibility(View.GONE);
                    mCommitBtn.setVisibility(View.GONE);
                    mAddToSources.setVisibility(View.VISIBLE);
                    mNewBtn.setVisibility(View.GONE);
                } else {
                    Toast.makeText(CaptionActivity.this, "Missing File", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // TODO Build control bar class
        mCaptionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // UPDATE mCurrentAdBreak
                mCurrentItemSelected = position;
                // GET Caption AT POSITION
                Caption currentCaption = mCaptionList.get(position);
                // POPULATE caption DATA INTO VIEWS
                parseObject(currentCaption);
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

        mEditBtn = (Button) findViewById(R.id.caption_edit_btn);
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

        mCommitBtn = (Button) findViewById(R.id.caption_commit_btn);
        mCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // BUILD CAPTION
                Caption caption = parseScreen();
                if (caption != null) {
                    // ADD THE CAPTION AT THE CORRECT POSITION IN captionList and captionLabelList
                    mCaptionList.set(mCurrentItemSelected, caption);
                    mCaptionLabelList.set(mCurrentItemSelected, caption.toJson().toString());
                    // UPDATE ADAPTER
                    mCaptionListAdapter.notifyDataSetChanged();
                    clearViews();
                    mEditBtn.setVisibility(View.GONE);
                    mCommitBtn.setVisibility(View.GONE);
                    mAddToSources.setVisibility(View.VISIBLE);
                    mNewBtn.setVisibility(View.GONE);
                } else {
                    Toast.makeText(CaptionActivity.this, "Missing File", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mNewBtn = (Button) findViewById(R.id.caption_new_btn);
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


        mClearList = (Button) findViewById(R.id.caption_clear_list);
        mClearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearViews();
                mCaptionList.clear();
                mCaptionLabelList.clear();
                mCaptionListAdapter.notifyDataSetChanged();
                mCurrentItemSelected = 0;
            }
        });

        mSetListBtn = (Button) findViewById(R.id.caption_set_list);
        mSetListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray jsonArray = new JSONArray();
                for (Caption current : mCaptionList) {
                    jsonArray.put(current.toJson());
                }
                // THIS IS A LEAF ACTIVITY IT MAKES SENSE TO REUSE IT WITH A RESULT AND FINISH
                Intent returnIntent = new Intent();
                returnIntent.putExtra(JWApplication.RESULT, jsonArray.toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


    }

    private void setupCaptionKindSpinner() {
        mCaptionKindSpinnerData = new ArrayList<>();
        mCaptionKindSpinnerData.add("");
        mCaptionKindSpinnerData.add(CaptionType.CAPTIONS.name());
        mCaptionKindSpinnerData.add(CaptionType.CHAPTERS.name());
        mCaptionKindSpinnerData.add(CaptionType.THUMBNAILS.name());
        mCaptionKindSpinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                mCaptionKindSpinnerData);
        mCaptionKindSpinner.setAdapter(mCaptionKindSpinnerAdapter);
    }


    private void setupListView() {
        mCaptionListAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                mCaptionLabelList);
        mCaptionListView.setAdapter(mCaptionListAdapter);
    }

    private void clearViews() {
        mCaptionLabel.setText("");
        mFileUrl.setText("");
        mCaptionKindSpinner.setSelection(0);
        mIsDefaultCheckBox.setChecked(false);
    }

    private void enableViews(boolean isEnabled) {
        mCaptionLabel.setEnabled(isEnabled);
        mFileUrl.setEnabled(isEnabled);
        mCaptionKindSpinner.setEnabled(isEnabled);
        mIsDefaultCheckBox.setEnabled(isEnabled);
        mQrReader.setEnabled(isEnabled);
    }


    @Override
    protected void initDataStructures() {
        mCaptionLabelList = new ArrayList<>();
        mCaptionList = new ArrayList<>();
    }

    @Override
    protected void handleIntent(Intent intent) {
        // GET EXTRAS FROM INTENT
        mPlaylistItemIndex = intent.getExtras().getInt(JWApplication.PLAYLIST_ITEM_INDEX);

        boolean shouldParseCaptions =
                master_config.getPlaylist() != null &&
                master_config.getPlaylist().size() > 0 &&
                mPlaylistItemIndex != NEW_PLAYLIST_ITEM;

        if (shouldParseCaptions) {
            PlaylistItem currentItem = master_config.getPlaylist().get(mPlaylistItemIndex);
            // null check AdSchedule since we could be editing a mPlaylist item with no AdSchedule
            if (currentItem.getTracks() != null) {
                mCaptionList = currentItem.getTracks();
                mCaptionLabelList = new ArrayList<>();
                for (Caption current : mCaptionList) {
                    mCaptionLabelList.add(current.toJson().toString());
                }
            }
        }


    }

    @Override
    protected void parseObject(Caption object) {
        // CLEAR VIEWS
        clearViews();
        // POPULATE VIEWS
        mFileUrl.setText(Utils.handleObjectData(object.getFile()));
        mCaptionKindSpinner.setSelection((object.getKind() == null) ? 0 : mCaptionKindSpinnerAdapter.getPosition(Utils.handleObjectData(object.getKind().name())));
        mIsDefaultCheckBox.setChecked(object.isDefault());
        mCaptionLabel.setText(Utils.handleObjectData(object.getLabel()));
    }

    @Override
    protected Caption parseScreen() {
        if (!Utils.getStringData(mFileUrl).isEmpty()) {
            Caption caption = new Caption();
            caption.setFile(Utils.getStringData(mFileUrl));
            caption.setKind((Utils.getStringData(mCaptionKindSpinner) == null) ? null : CaptionType.valueOf(Utils.getStringData(mCaptionKindSpinner)));
            caption.setDefault(mIsDefaultCheckBox.isChecked());
            caption.setLabel(Utils.getStringData(mCaptionLabel));
            return caption;
        }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case JWApplication.QR_CAPTION_URL:
                    String result = data.getStringExtra(JWApplication.RESULT);
                    mFileUrl.setText(result);
                    break;
                case JWApplication.LOCAL_CAPTION_FILE:
                    String path = Utils.getDocumentsFilePath(this, data.getData());
                    mFileUrl.setText(path);
                    break;
            }
        }
    }


}
