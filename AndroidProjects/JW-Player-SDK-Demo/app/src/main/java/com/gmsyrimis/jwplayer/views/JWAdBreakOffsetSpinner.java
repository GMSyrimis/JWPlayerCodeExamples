package com.gmsyrimis.jwplayer.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by gsyrimis on 7/19/16.
 */
public class JWAdBreakOffsetSpinner extends com.gmsyrimis.jwplayer.views.JWSpinner {

    private final int PRE = 1;
    private final int SECONDS = 2;
    private final int FRACTIONAL_SECONDS = 3;
    private final int PERCENTAGE = 4;
    private final int TIMECODE = 5;
    private final int POST = 6;

    public JWAdBreakOffsetSpinner(Context context) {
        super(context);
    }

    public JWAdBreakOffsetSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JWAdBreakOffsetSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /* Use this method to select the correct item from the spinner
    * if the spinner does not contain the entry it will add it by default
    * I can see instances where we may not want to add an entry if it doesn't exist
    * ex. adBreakOffset
    * in which case we may want to override this method
    * the override will edit a similar entry instead of adding a new one
    *
    * */
    @Override
    public void setSelection(String payload) {
        if (payload != null && !payload.isEmpty()) {
            if (mPayload.contains(payload)) {
                mSpinner.setSelection(mPayload.indexOf(payload));
            } else {
                // we want to identify the type of payload we have
                boolean isFractionalSeconds = payload.contains(".");
                boolean isPercentage = payload.contains("%");
                boolean isTimecode = payload.contains(":");

                if (!isFractionalSeconds && !isPercentage && !isTimecode){
                    // if it's none of the above it must be seconds
                    editEntry(SECONDS,payload,payload);
                }else if (isFractionalSeconds){
                    editEntry(FRACTIONAL_SECONDS,payload,payload);
                }else if (isPercentage){
                    editEntry(PERCENTAGE,payload,payload);
                }else if (isTimecode){
                    editEntry(TIMECODE,payload,payload);
                }

            }
        } else {
            mSpinner.setSelection(0);
        }
    }


    @Override
    protected void initData() {
        super.initData();
        mLabels.add(PRE,"pre");
        mPayload.add(PRE,"pre");

        mLabels.add(SECONDS,"10");
        mPayload.add(SECONDS,"10");

        mLabels.add(FRACTIONAL_SECONDS, "10.1");
        mPayload.add(FRACTIONAL_SECONDS, "10.1");

        mLabels.add(PERCENTAGE,"10%");
        mPayload.add(PERCENTAGE,"10%");

        mLabels.add(TIMECODE,"00:00:10:000");
        mPayload.add(TIMECODE,"00:00:10:000");

        mLabels.add(POST,"post");
        mPayload.add(POST,"post");
    }

    public void initialize(String prefName) {
        mPreferenceFileName = prefName;
        initData();
        setupSpinnerAdapter();
    }

}
