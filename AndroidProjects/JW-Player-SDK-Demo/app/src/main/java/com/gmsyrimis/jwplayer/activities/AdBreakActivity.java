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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.JWApplication;
import com.gmsyrimis.jwplayer.R;
import com.gmsyrimis.jwplayer.custom.EditClickListener;
import com.gmsyrimis.jwplayer.custom.JWActivity;
import com.gmsyrimis.jwplayer.utilities.Utils;
import com.gmsyrimis.jwplayer.views.JWAdBreakOffsetSpinner;
import com.longtailvideo.jwplayer.media.ads.Ad;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.ads.AdType;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.gmsyrimis.jwplayer.JWApplication.master_config;

public class AdBreakActivity extends JWActivity<AdBreak> {

    // DATA FROM PREVIOUS ACTIVITY
    private int mPlaylistItemIndex;
    private boolean mIsPlaylistItem;


    // Data to be exported
    private List<AdBreak> mAdSchedule;

    private Button mQrReader;
    private EditText mAdUrl;

    //    EditText mAdbreakOffset;
    private JWAdBreakOffsetSpinner mAdbreakOffsetSpinner;

    private CheckBox mNonLinear;

    private Button mAddToWaterfall;
    private LinearLayout mWaterfallContainer;

    // Spinner
    private Spinner mAdSourceSpinner;
    private ArrayList<String> mAdSourceSpinnerData;
    private ArrayAdapter<String> mAdSourceSpinnerAdapter;

    // ListView
    private ListView mAdBreakListView;
    private List<String> mAdBreakLabels;
    private ArrayAdapter<String> mAdBreakListViewAdapter;
    private int mCurrentAdBreak = -1;


    private Button mAddToSchedule;
    private Button mEditBtn;
    private Button mNewBtn;
    private Button mCommitBtn;
    private Button mSetListBtn;

    private Button mClearList;
    private Button mLoadList;
    private Button mSaveList;

    private Button mEditLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_break);

        getSupportActionBar().setTitle("AdBreak");

        // INITIALIZING mAdSchedule and mAdBreakLabels
        initDataStructures();
        // Get AdSchedule data from intent
        handleIntent(getIntent());

        mAdSourceSpinner = (Spinner) findViewById(R.id.adbreak_adsource);
        setupAdSourceSpinner();

        mQrReader = (Button) findViewById(R.id.adbreak_qr_btn);
        mQrReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), QrActivity.class), JWApplication.QR_AD_URL);
            }
        });

        // Build Waterfall logic
        mAdUrl = (EditText) findViewById(R.id.adbreak_ad_tag);
        mWaterfallContainer = (LinearLayout) findViewById(R.id.adbreak_tag_container);
        mAddToWaterfall = (Button) findViewById(R.id.adbreak_add_tag_btn);
        mAddToWaterfall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mAdUrl.getText().toString().equals("")) {
                    final TextView textView = new TextView(getApplicationContext());

                    textView.setPadding(20, 0, 0, 0);
                    textView.setText(mAdUrl.getText().toString());

                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAdUrl.setText(textView.getText().toString());
                            mWaterfallContainer.removeView(textView);
                        }
                    });
                    mWaterfallContainer.addView(textView);
                    mAdUrl.setText("");
                }
            }
        });

        // Build AdBreak and Add to schedule
        mNonLinear = (CheckBox) findViewById(R.id.adbreak_non_linear);


        mAdbreakOffsetSpinner = (JWAdBreakOffsetSpinner) findViewById(R.id.adbreak_offset_jwspinner);
        mAdbreakOffsetSpinner.initialize("adbreak_offsets");
        mAdbreakOffsetSpinner.load();

        mEditLabel = (Button) findViewById(R.id.adbreak_edit_label);
        mEditLabel.setOnClickListener(new EditClickListener(mAdbreakOffsetSpinner, AdBreakActivity.this, true));


        mAddToSchedule = (Button) findViewById(R.id.adbreak_add_btn);
        mAddToSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdBreak adBreak = parseScreen();
                if (adBreak != null) {
                    // ADD THE ADBREAK to mAdSchedule and mAdBreakLabels
                    mAdSchedule.add(adBreak);
                    mAdBreakLabels.add(adBreak.toJson().toString());
                    mAdBreakListViewAdapter.notifyDataSetChanged();
                    clearViews();
                    mEditBtn.setVisibility(View.GONE);
                    mCommitBtn.setVisibility(View.GONE);
                    mAddToSchedule.setVisibility(View.VISIBLE);
                    mNewBtn.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "Missing Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Enable Editting
        mEditBtn = (Button) findViewById(R.id.adbreak_edit_btn);
        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SET ENABLED ON VIEWS TO TRUE
                enableViews(true);
                // HIDE EDIT BTN
                mEditBtn.setVisibility(View.GONE);
                // SHOW COMMIT BTN
                mCommitBtn.setVisibility(View.VISIBLE);
                mAddToSchedule.setVisibility(View.GONE);
                mNewBtn.setVisibility(View.VISIBLE);
            }
        });
        // Commit AdBreak to list
        mCommitBtn = (Button) findViewById(R.id.adbreak_commit_btn);
        mCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // BUILD AD BREAK
                AdBreak adBreak = parseScreen();
                if (adBreak != null) {
                    // ADD THE ADBREAK AT THE CORRECT POSITION IN mAdSchedule and mAdBreakLabels
                    mAdSchedule.set(mCurrentAdBreak, adBreak);
                    mAdBreakLabels.set(mCurrentAdBreak, adBreak.toJson().toString());
                    // UPDATE ADAPTER
                    mAdBreakListViewAdapter.notifyDataSetChanged();
                    clearViews();
                    mEditBtn.setVisibility(View.GONE);
                    mCommitBtn.setVisibility(View.GONE);
                    mAddToSchedule.setVisibility(View.VISIBLE);
                    mNewBtn.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "Missing Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Clear views and setup to build a new AdBreak
        mNewBtn = (Button) findViewById(R.id.adbreak_new_btn);
        mNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearViews();
                mEditBtn.setVisibility(View.GONE);
                mCommitBtn.setVisibility(View.GONE);
                mAddToSchedule.setVisibility(View.VISIBLE);
                mNewBtn.setVisibility(View.GONE);
                enableViews(true);
            }
        });

        mAdBreakListView = (ListView) findViewById(R.id.adbreak_list_view);
        setupListView();
        mAdBreakListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // UPDATE mCurrentAdBreak
                mCurrentAdBreak = position;
                // GET ADBREAK AT POSITION
                AdBreak currentAdBreak = mAdSchedule.get(position);
                // POPULATE ADBREAK DATA INTO VIEWS
                parseObject(currentAdBreak);
                // SET ENABLED ON VIEWS TO FALSE
                enableViews(false);
                // HIDE ADD BTN
                mAddToSchedule.setVisibility(View.GONE);
                // HIDE COMMIT BTN
                mCommitBtn.setVisibility(View.GONE);
                // SHOW NEW BTN
                mNewBtn.setVisibility(View.VISIBLE);
                // SHOW EDIT BTN
                mEditBtn.setVisibility(View.VISIBLE);
            }
        });

        mSetListBtn = (Button) findViewById(R.id.adbreak_set_list);
        mSetListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray jsonArray = new JSONArray();
                for (AdBreak current : mAdSchedule) {
                    jsonArray.put(current.toJson());
                }
                // THIS IS A LEAF ACTIVITY IT MAKES SENSE TO REUSE IT WITH A RESULT AND FINISH
                Intent returnIntent = new Intent();
                returnIntent.putExtra(JWApplication.RESULT, jsonArray.toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        mClearList = (Button) findViewById(R.id.adbreak_clear_list);
        mClearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearViews();
                mAdSchedule.clear();
                mAdBreakLabels.clear();
                mAdBreakListViewAdapter.notifyDataSetChanged();
                mCurrentAdBreak = 0;
            }
        });


    }

    private void setupAdSourceSpinner() {
        mAdSourceSpinnerData = new ArrayList<>();
        mAdSourceSpinnerData.add("");
        mAdSourceSpinnerData.add(AdSource.VAST.name());
        mAdSourceSpinnerData.add(AdSource.IMA.name());
        mAdSourceSpinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                mAdSourceSpinnerData);
        mAdSourceSpinner.setAdapter(mAdSourceSpinnerAdapter);
    }

    private void setupListView() {
        mAdBreakListViewAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                mAdBreakLabels);
        mAdBreakListView.setAdapter(mAdBreakListViewAdapter);
    }

    private void clearViews() {
        mWaterfallContainer.removeAllViews();
        mAdbreakOffsetSpinner.setSelection(0);
        mAdUrl.setText("");
        mAdSourceSpinner.setSelection(0);
        mNonLinear.setChecked(false);
    }


    private void enableViews(boolean isEnabled) {
        mAdbreakOffsetSpinner.setEnabled(isEnabled);
        mAdUrl.setEnabled(isEnabled);
        mAdSourceSpinner.setEnabled(isEnabled);
        mQrReader.setEnabled(isEnabled);
        mAddToWaterfall.setEnabled(isEnabled);
        mNonLinear.setEnabled(isEnabled);
        int numOfAds = mWaterfallContainer.getChildCount();
        for (int i = 0; i < numOfAds; i++) {
            TextView tv = (TextView) mWaterfallContainer.getChildAt(i);
            tv.setEnabled(isEnabled);
        }
    }

    @Override
    protected AdBreak parseScreen() {
        // NEW REGULATIONS
        // ONLY IF THERE IS DATA IN THE VIEW SHALL WE RETURN THE OBJECT. ELSE WE RETURN NULL
        // GET DATA
        String adSourceString = (String) mAdSourceSpinner.getSelectedItem();
        int numOfAds = mWaterfallContainer.getChildCount();
        // CHECK DATA
        boolean hasAdSource = (!adSourceString.equals(""));
        boolean hasAdTags = (numOfAds > 0);
        boolean hasOffset = !(mAdbreakOffsetSpinner.getSelectedItemPosition() == 0);
        // IF ALL IS GOOD CREATE OBJECT
        if (hasAdSource && hasAdTags && hasOffset) {
            Ad ad = new Ad();
            ad.setSource(AdSource.valueOf(adSourceString));
            String[] adlist = new String[numOfAds];
            for (int i = 0; i < numOfAds; i++) {
                TextView tv = (TextView) mWaterfallContainer.getChildAt(i);
                adlist[i] = tv.getText().toString();
                if (!tv.getText().toString().isEmpty()) {
                    adlist[i] = tv.getText().toString();
                }
            }
            ad.setTag(adlist);
            AdBreak adBreak = new AdBreak();
            adBreak.setAd(ad);
            adBreak.setOffset(mAdbreakOffsetSpinner.getPayloadAt(mAdbreakOffsetSpinner.getSelectedItemPosition()));
            adBreak.setAdType((mNonLinear.isChecked()) ? AdType.NONLINEAR : AdType.LINEAR);
            return adBreak;
        }

        return null;
    }

    @Override
    protected void parseObject(AdBreak object) {
        // CLEAR VIEWS
        clearViews();
        // POPULATE VIEWS
        Ad ad = object.getAd();
        // Setting ad source in mAdSourceSpinner

        mAdSourceSpinner.setSelection((ad.getSource() == null) ? 0 : mAdSourceSpinnerAdapter.getPosition(Utils.handleObjectData(object.getAd().getSource().name())));

        // Setting ad tags in mWaterfallContainer
        List<String> adTags = ad.getTag();
        for (String current : adTags) {
            final TextView textView = new TextView(getApplicationContext());
            textView.setText(current);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdUrl.setText(textView.getText().toString());
                    mWaterfallContainer.removeView(textView);
                }
            });
            mWaterfallContainer.addView(textView);
        }
        // Set offset
        mAdbreakOffsetSpinner.setSelection(Utils.handleObjectData(object.getOffset()));

        mNonLinear.setChecked((object.getAdType() == AdType.NONLINEAR));
    }

    @Override
    protected void initDataStructures() {
        mAdSchedule = new ArrayList<>();
        mAdBreakLabels = new ArrayList<>();
    }

    @Override
    protected void handleIntent(Intent intent) {
        // PlayerConfig is the master plan
        // mIsPlaylistItem helps decide on what to parse
        mIsPlaylistItem = intent.getExtras().getBoolean(JWApplication.IS_PLAYLIST_ITEM, false);
        // IF it is a playlistItem get the item at index X and get its AdSchedule
        // ELSE it is a Advertising object, check instance and cast
        if (mIsPlaylistItem) {
            // Get index, then get playlistItem
            mPlaylistItemIndex = intent.getExtras().getInt(JWApplication.PLAYLIST_ITEM_INDEX);

            boolean shouldParseAds =
                    master_config.getPlaylist() != null &&
                    master_config.getPlaylist().size() > 0 &&
                    mPlaylistItemIndex != JWApplication.NEW_PLAYLIST_ITEM;

            if (shouldParseAds) {
                PlaylistItem currentItem = master_config.getPlaylist().get(mPlaylistItemIndex);
                // null check AdSchedule since we could be editing a mPlaylist item with no AdSchedule
                if (currentItem.getAdSchedule() != null) {
                    mAdSchedule = currentItem.getAdSchedule();
                    mAdBreakLabels = new ArrayList<>();
                    for (AdBreak current : mAdSchedule) {
                        mAdBreakLabels.add(current.toJson().toString());
                    }
                }
            }

        } else {
            // Checking is getAdvertising is instanceof Advertising since it could be VMAPAdvertising object
            if (master_config.getAdvertising() instanceof Advertising) {
                Advertising advertising = (Advertising) master_config.getAdvertising();
                if (advertising.getSchedule() != null) {
                    mAdSchedule = advertising.getSchedule();
                    mAdBreakLabels = new ArrayList<>();
                    for (AdBreak current : mAdSchedule) {
                        mAdBreakLabels.add(current.toJson().toString());
                    }
                }
            } else {
                //TODO THIS IS A VMAPAdvertising OBJECT AND WE WISH TO CHANGE IT TO ADVERTISING OBJECT
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == JWApplication.QR_AD_URL) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(JWApplication.RESULT);
                mAdUrl.setText(result);
                mAddToWaterfall.performClick();
                mAdUrl.setText("");
            }
        }
    }
}
