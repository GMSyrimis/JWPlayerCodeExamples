package com.gmsyrimis.jwplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.JWApplication;
import com.gmsyrimis.jwplayer.R;
import com.gmsyrimis.jwplayer.custom.JWActivity;
import com.gmsyrimis.jwplayer.utilities.Utils;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.ads.AdvertisingBase;
import com.longtailvideo.jwplayer.media.ads.VMAPAdvertising;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gmsyrimis.jwplayer.JWApplication.master_config;

public class VastAdvertisingActivity extends JWActivity<AdvertisingBase> {

    // Objects to be returned
    private VMAPAdvertising mVmapAdvertising;
    private Advertising mAdvertising;

    // Spinner
    private Spinner mAdSourceSpinner;
    private ArrayAdapter<String> mAdSourceSpinnerAdapter;

    // List View
    private ListView mAdScheduleListView;
    private ArrayList<String> mAdScheduleLabels;
    private ArrayAdapter<String> mAdScheduleAdapter;

    private EditText mSkipOffsetEditText;
    private EditText mSkipTextEditText;
    private EditText mSkipMessageEditText;
    private EditText mCueTextEditText;
    private EditText mAdMessageEditText;
    private EditText mVmapUrlEditText;
    private Button mEditAdScheduleBtn;

    private Button mSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vast_advertising);
        getSupportActionBar().setTitle("Vast Advertising");

        initDataStructures();

        mVmapUrlEditText = (EditText) findViewById(R.id.advertising_vmap_url);
        mSkipOffsetEditText = (EditText) findViewById(R.id.advertising_skip_offset);
        mSkipTextEditText = (EditText) findViewById(R.id.advertising_skip_text);
        mSkipMessageEditText = (EditText) findViewById(R.id.advertising_skip_message);
        mCueTextEditText = (EditText) findViewById(R.id.advertising_cue_text);
        mAdMessageEditText = (EditText) findViewById(R.id.advertising_ad_message);

        mAdSourceSpinner = (Spinner) findViewById(R.id.advertising_ad_source);
        setupAdSourceSpinner();

        mAdScheduleListView = (ListView) findViewById(R.id.advertising_ad_schedule_lv);
        setupListView();

        parseObject(master_config.getAdvertising());

        mEditAdScheduleBtn = (Button) findViewById(R.id.advertising_edit_ad_schedule);
        mEditAdScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(getApplicationContext(), AdBreakActivity.class);
                returnIntent.putExtra(JWApplication.IS_PLAYLIST_ITEM, false);
                startActivityForResult(returnIntent, JWApplication.AD_BREAK_ACTIVITY);

            }
        });

        mSaveBtn = (Button) findViewById(R.id.advertising_set_advertising);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            parseScreen();
                                        }
                                    }

        );

    }

    private void setupAdSourceSpinner() {
        String[] adSources = new String[]{
                "",
                AdSource.VAST.name(),
                AdSource.IMA.name()
        };
        mAdSourceSpinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                adSources);
        mAdSourceSpinner.setAdapter(mAdSourceSpinnerAdapter);
    }

    private void setupListView() {
        mAdScheduleAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                mAdScheduleLabels);
        mAdScheduleListView.setAdapter(mAdScheduleAdapter);
    }

    private Advertising setAdvertising() {
        mAdvertising.setSkipOffset((Utils.getIntegerData(mSkipOffsetEditText) == null) ? -1 : Utils.getIntegerData(mSkipOffsetEditText));
        mAdvertising.setSkipText(Utils.getStringData(mSkipTextEditText));
        mAdvertising.setSkipMessage(Utils.getStringData(mSkipMessageEditText));
        mAdvertising.setCueText(Utils.getStringData(mCueTextEditText));
        mAdvertising.setAdMessage(Utils.getStringData(mAdMessageEditText));
        mAdvertising.setClient((Utils.getStringData(mAdSourceSpinner) == null) ? null : AdSource.valueOf(Utils.getStringData(mAdSourceSpinner)));
        List<AdBreak> adBreakList = new ArrayList<>();
        for (String current : mAdScheduleLabels) {
            try {
                adBreakList.add(AdBreak.parseJson(current));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mAdvertising.setSchedule(adBreakList);
        return mAdvertising;
    }

    private VMAPAdvertising setVMAPAdvertising() {
        mVmapAdvertising.setSkipOffset((Utils.getIntegerData(mSkipOffsetEditText) == null) ? -1 : Utils.getIntegerData(mSkipOffsetEditText));
        mVmapAdvertising.setSkipText(Utils.getStringData(mSkipTextEditText));
        mVmapAdvertising.setSkipMessage(Utils.getStringData(mSkipMessageEditText));
        mVmapAdvertising.setCueText(Utils.getStringData(mCueTextEditText));
        mVmapAdvertising.setAdMessage(Utils.getStringData(mAdMessageEditText));
        mVmapAdvertising.setClient((Utils.getStringData(mAdSourceSpinner) == null) ? null : AdSource.valueOf(Utils.getStringData(mAdSourceSpinner)));
        mVmapAdvertising.setTag(Utils.getStringData(mVmapUrlEditText));
        return mVmapAdvertising;
    }

    @Override
    protected void initDataStructures() {
        List<AdBreak> dummySchedule = new ArrayList<>();
        mAdvertising = new Advertising(AdSource.VAST, dummySchedule);
        mVmapAdvertising = new VMAPAdvertising(AdSource.VAST, "");
        // mAdBreakLabels to be filled in depending on intent or result
        mAdScheduleLabels = new ArrayList<>();
    }

    @Override
    protected void handleIntent(Intent intent) {
        // Used to handle the playeconfig
    }

    @Override
    protected void parseObject(AdvertisingBase object) {
        // Get AdvertisingBase
        if (object != null) {
            // Check instance of
            if (object instanceof Advertising) {
                mAdvertising = (Advertising) object;
                mAdMessageEditText.setText(Utils.handleObjectData(mAdvertising.getAdMessage()));
                mAdSourceSpinner.setSelection((mAdvertising.getClient() == null) ? 0 : mAdSourceSpinnerAdapter.getPosition(mAdvertising.getClient().name()));
                mCueTextEditText.setText(Utils.handleObjectData(mAdvertising.getCueText()));
                mSkipMessageEditText.setText(Utils.handleObjectData(mAdvertising.getSkipMessage()));
                mSkipTextEditText.setText(Utils.handleObjectData(mAdvertising.getSkipText()));
                mSkipOffsetEditText.setText(Utils.handleObjectData(mAdvertising.getSkipOffset()));
                // Translate data into string for list view row
                List<AdBreak> adBreakList = mAdvertising.getSchedule();
                for (AdBreak current : adBreakList) {
                    mAdScheduleLabels.add(current.toJson().toString());
                }
            } else if (object instanceof VMAPAdvertising) {
                mVmapAdvertising = (VMAPAdvertising) object;
                mVmapUrlEditText.setText(Utils.handleObjectData(mVmapAdvertising.getTag()));
                mAdMessageEditText.setText(Utils.handleObjectData(mVmapAdvertising.getAdMessage()));
                mAdSourceSpinner.setSelection((mVmapAdvertising.getClient() == null) ? 0 : mAdSourceSpinnerAdapter.getPosition(mVmapAdvertising.getClient().name()));
                mCueTextEditText.setText(Utils.handleObjectData(mVmapAdvertising.getCueText()));
                mSkipMessageEditText.setText(Utils.handleObjectData(mVmapAdvertising.getSkipMessage()));
                mSkipTextEditText.setText(Utils.handleObjectData(mVmapAdvertising.getSkipText()));
                mSkipTextEditText.setText(Utils.handleObjectData(mVmapAdvertising.getSkipOffset()));
            }
        }

    }

    @Override
    protected AdvertisingBase parseScreen() {
        // Unusable due to split case and AdvertisingBase not retaining info
        boolean hasAdSource = mAdSourceSpinner.getSelectedItemPosition() != 0;
        boolean hasVMAP = !mVmapUrlEditText.getText().toString().isEmpty();
        boolean hasAdSchedule = !mAdScheduleLabels.isEmpty();

        if (hasAdSchedule && hasVMAP) {
            Toast.makeText(VastAdvertisingActivity.this, "Cannot set VMAP and AdSchedule", Toast.LENGTH_SHORT).show();
        }

        if ((!hasAdSource && hasAdSchedule) || (!hasAdSource && hasVMAP)) {
            Toast.makeText(VastAdvertisingActivity.this, "Missing AdSource", Toast.LENGTH_SHORT).show();
        }
        if ((!hasAdSource && !hasVMAP && !hasAdSchedule)) {
            master_config.setAdvertising(null);
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }

        if (hasAdSource && !hasVMAP && !hasAdSchedule) {
            mAdvertising.setClient(AdSource.valueOf(Utils.getStringData(mAdSourceSpinner)));

            // VAST SPECIFIC DATA
            if (AdSource.valueOf(Utils.getStringData(mAdSourceSpinner)) == AdSource.VAST) {
                mAdvertising.setSkipOffset((Utils.getIntegerData(mSkipOffsetEditText) == null) ? -1 : Utils.getIntegerData(mSkipOffsetEditText));
                mAdvertising.setSkipText(Utils.getStringData(mSkipTextEditText));
                mAdvertising.setSkipMessage(Utils.getStringData(mSkipMessageEditText));
                mAdvertising.setCueText(Utils.getStringData(mCueTextEditText));
                mAdvertising.setAdMessage(Utils.getStringData(mAdMessageEditText));
            }

            List<AdBreak> emptyList = new ArrayList<>();
            mAdvertising.setSchedule(emptyList);
            master_config.setAdvertising(mAdvertising);
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }

        if (hasAdSchedule && hasAdSource) {
            master_config.setAdvertising(setAdvertising());
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }

        if (hasVMAP && hasAdSource) {
            master_config.setAdvertising(setVMAPAdvertising());
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == JWApplication.AD_BREAK_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                if (mAdScheduleLabels.size() > 0) {
                    mAdScheduleLabels.clear();
                }
                String result = data.getStringExtra(JWApplication.RESULT);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject current = (JSONObject) jsonArray.get(i);
                        mAdScheduleLabels.add(current.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdScheduleAdapter.notifyDataSetChanged();
            }
        }
    }

}
