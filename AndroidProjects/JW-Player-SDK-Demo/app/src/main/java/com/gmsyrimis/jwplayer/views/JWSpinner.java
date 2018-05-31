package com.gmsyrimis.jwplayer.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.gmsyrimis.jwplayer.JWApplication;
import com.gmsyrimis.jwplayer.providers.DataProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gsyrimis on 7/9/16.
 */
public class JWSpinner extends Spinner {

    protected Spinner mSpinner;

    protected List<String> mLabels;
    protected List<String> mPayload;

    protected ArrayAdapter<String> mSpinnerAdapter;

    protected Context mContext;

    protected String mPreferenceFileName;
    protected final String PAYLOAD_KEY = "Payload_Key";

    public JWSpinner(Context context) {
        super(context);
        mContext = context;
        mSpinner = this;
    }

    public JWSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mSpinner = this;
    }

    public JWSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mSpinner = this;
    }


    /* This will initialize the label and payload datasets.
    * We could initialize the labels only when the labels and payload are the same to minimize size
    * We initialize with a single empty entry to allow nulls to be passed to different objects
    * but also to make it easier to remove entries
    * */
    protected void initData() {
        // Empty entries to allow nulls to be passed
        mLabels = new ArrayList<>();
        mLabels.add("");
        mPayload = new ArrayList<>();
        mPayload.add("");
    }

    protected void setupSpinnerAdapter() {
        mSpinnerAdapter = new ArrayAdapter<>(
                mContext,
                android.R.layout.simple_spinner_dropdown_item,
                mLabels);
        mSpinner.setAdapter(mSpinnerAdapter);
    }

    public String getPayloadAt(int index) {
        return (index == 0) ? null : mPayload.get(index);
    }

    /* Use this method to select the correct item from the spinner
    * if the spinner does not contain the entry it will add it by default
    * I can see instances where we may not want to add an entry if it doesn't exist
    * ex. adBreakOffset
    * in which case we may want to override this method
    * the override will edit a similar entry instead of adding a new one
    * */
    public void setSelection(String payload) {
        if (payload != null && !payload.isEmpty()) {
            if (mPayload.contains(payload)) {
                mSpinner.setSelection(mPayload.indexOf(payload));
            } else {
                addEntry(payload, payload);
            }
        } else {
            mSpinner.setSelection(0);
        }
    }

    public void editEntry(int index, String label) {
        mLabels.set(index, label);
        save();
        notifyDatasetChanged();
    }

    public void editEntry(int index, String label, String payload) {
        mLabels.set(index, label);
        mPayload.set(index, payload);
        save();
        notifyDatasetChanged();
    }

    /* public method that handles adding an entry to the spinner
    * To add an entry here you would have to provide both a label and the payload
    * this reflects on the internal structure that has both a label and payload dataset
    * I can see instances where we won't need a payload dataset since the labels and payload are the same
    * ex. mPlayerView.setSkin(Skin)
    * but at the moment it doesn't make sense to create a new class to handle that
    * */
    public void addEntry(String label, String payload) {
        if (!mLabels.contains(label) && !mPayload.contains(payload)) {
            mLabels.add(label);
            mPayload.add(payload);
            mSpinnerAdapter.notifyDataSetChanged();
            mSpinner.setSelection(mPayload.indexOf(payload));
        } else {
            mSpinner.setSelection(mPayload.indexOf(payload));
        }
    }

    private void addEntryFromJsonArray(String jsonArray) {
        try {
            JSONArray array = new JSONArray(jsonArray);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                addEntry(obj.getString(JWApplication.LABEL), obj.getString(JWApplication.PAYLOAD));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String toJsonArray() {
        JSONArray array = new JSONArray();
        for (int i = 0; i < mLabels.size(); i++) {
            JSONObject obj = new JSONObject();
            try {
                obj.put(JWApplication.LABEL, mLabels.get(i));
                obj.put(JWApplication.PAYLOAD, mPayload.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(obj);
        }
        return array.toString();
    }

    /* This method handles removing an entry from a speficic index
    * I can see instances where we may not want to remove an entry
    * ex. adBreakOffset
    * In which case we could just hide the remove entry button
    * There's also a change when the spinner only contains the labels dataset
    * which actually may just be a different class since labels and payload are both member variables now
    * */
    public void removeEntry(int index) {
        // Do not allow developer to remove the empty entry from spinner
        if (index != 0) {
            // Set selection to 0 to ensure we are within bounds
            mSpinner.setSelection(0);
            mLabels.remove(index);
            mPayload.remove(index);
            mSpinnerAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(mContext, "You cannot remove this entry", Toast.LENGTH_SHORT).show();
        }
    }

    public void save() {
        SharedPreferences settings = mContext.getSharedPreferences(mPreferenceFileName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PAYLOAD_KEY, toJsonArray());
        editor.apply();
    }

    public void load() {
        SharedPreferences settings = mContext.getSharedPreferences(mPreferenceFileName, 0);
        String jsonArray = settings.getString(PAYLOAD_KEY, null);
        // if null it's the first time we started the activity
        if (jsonArray != null) {
            addEntryFromJsonArray(jsonArray);
        }
    }


    public void initialize(String prefName, DataProvider dataProvider) {
        mPreferenceFileName = prefName;
        initData();
        setupSpinnerAdapter();
        for (int i = 0; i < dataProvider.getLabels().size(); i++) {
            addEntry(dataProvider.getLabels().get(i), dataProvider.getPayload().get(i));
        }
    }


    public List<String> getLabels() {
        return mLabels;
    }

    public List<String> getPayload() {
        return mPayload;
    }

    public void notifyDatasetChanged() {
        mSpinnerAdapter.notifyDataSetChanged();
    }
}
