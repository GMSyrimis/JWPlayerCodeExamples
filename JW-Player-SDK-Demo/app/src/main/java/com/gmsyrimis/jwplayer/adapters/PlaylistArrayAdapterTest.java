package com.gmsyrimis.jwplayer.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gmsyrimis.jwplayer.R;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/* This class is responsible for parsing each vPlaylist item into a visual vPlaylist row item
*  Right now we have:
*  Image
*  Captions
*  Sources
*  Ads
*  Title
*  Description
*  MediaID
*
* */
public class PlaylistArrayAdapterTest extends ArrayAdapter<PlaylistItem> {

    private Typeface mFont;
    private Context mContext;


    public PlaylistArrayAdapterTest(Context context, List<PlaylistItem> objects, Typeface font) {
        super(context, 0, objects);
        this.mFont = font;
        this.mContext = context;
    }

    public PlaylistArrayAdapterTest(Context context, List<PlaylistItem> objects) {
        super(context, 0, objects);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get row data from arraylist
        PlaylistItem currentItem = getItem(position);

        // create row view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.playlist_row_item, parent, false);
        }

        // Get Display Metrics
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();

        // Define player row height and width
        int px = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics));

        int playerRowPortraitWidth = parent.getWidth() - px;
        int playerRowPortraitHeight = (playerRowPortraitWidth * 9) / 16;


        RelativeLayout posterImageContainer = (RelativeLayout) convertView.findViewById(R.id.root_layout);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(playerRowPortraitWidth, playerRowPortraitHeight);


        posterImageContainer.setLayoutParams(layoutParams);

        convertView.requestLayout();


        ImageView posterImage = (ImageView) convertView.findViewById(R.id.playlist_row_image);
        Picasso.with(mContext).load(currentItem.getImage()).into(posterImage);

        TextView title = (TextView) convertView.findViewById(R.id.playlist_row_title);

        title.setText((currentItem.getTitle().length() > 9) ? currentItem.getTitle().substring(0, 6) + "..." : currentItem.getTitle());
        if (mFont != null) {
            title.setTypeface(mFont);
        }

        TextView description = (TextView) convertView.findViewById(R.id.playlist_row_description);
        description.setText((currentItem.getDescription().length() > 14) ? currentItem.getDescription().substring(0, 11) + "..." : currentItem.getDescription());
        if (mFont != null) {
            description.setTypeface(mFont);
        }

        TextView playlistItemCount = (TextView) convertView.findViewById(R.id.playlist_row_count);
        playlistItemCount.setText("Playlist Item: " + position);

        TextView mediaid = (TextView) convertView.findViewById(R.id.playlist_row_mediaid);
        mediaid.setText(currentItem.getMediaId());
        if (mFont != null) {
            mediaid.setTypeface(mFont);
        }


        CheckedTextView hasAds = (CheckedTextView) convertView.findViewById(R.id.playlist_row_ads_check);
        hasAds.setChecked(currentItem.getAdSchedule() != null && currentItem.getAdSchedule().size() > 0);
        if (mFont != null) {
            hasAds.setTypeface(mFont);
        }


        CheckedTextView hasCaptions = (CheckedTextView) convertView.findViewById(R.id.playlist_row_captions_check);
        hasCaptions.setChecked(currentItem.getTracks() != null && currentItem.getTracks().size() > 0);
        if (mFont != null) {
            hasCaptions.setTypeface(mFont);
        }


        CheckedTextView hasMultisource = (CheckedTextView) convertView.findViewById(R.id.playlist_row_multisource_check);
        hasMultisource.setChecked(currentItem.getSources() != null && currentItem.getSources().size() > 0);
        if (mFont != null) {
            hasMultisource.setTypeface(mFont);
        }


        convertView.findViewById(R.id.playlist_row_container).bringToFront();

        return convertView;
    }
}