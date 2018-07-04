package com.tama.chat.ui.adapters.mapBlink;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tama.chat.BuildConfig;
import com.tama.chat.R;
import com.tama.chat.ui.activities.base.BaseActivity;
import com.tama.chat.ui.adapters.base.BaseClickListenerViewHolder;
import com.tama.chat.ui.adapters.base.BaseFilterAdapter;
import com.tama.chat.ui.adapters.base.BaseViewHolder;
import com.tama.chat.utils.helpers.TextViewHelper;
import com.tama.chat.gcm.MapBlinkElement;

import java.util.List;

import butterknife.Bind;

//chka
public class MapBlinkElementAdapter extends BaseFilterAdapter<MapBlinkElement, BaseClickListenerViewHolder<MapBlinkElement>> {


    public MapBlinkElementAdapter(BaseActivity baseActivity, List<MapBlinkElement> usersList) {
        super(baseActivity, usersList);
    }

    @Override
    protected boolean isMatch(MapBlinkElement item, String query) {
        return item.getName() != null && item.getName().toLowerCase().contains(query);
    }

    @Override
    public BaseClickListenerViewHolder<MapBlinkElement> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this, layoutInflater.inflate(R.layout.item_map_blink_element, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseClickListenerViewHolder<MapBlinkElement> baseClickListenerViewHolder, final int position) {
        MapBlinkElement mapElement = getItem(position);
        ViewHolder viewHolder = (ViewHolder) baseClickListenerViewHolder;

        initViewElement(viewHolder, mapElement);

        if (!TextUtils.isEmpty(query)) {
            TextViewHelper.changeTextColorView(baseActivity, viewHolder.nameTextView, query);
        }

    }

    private void initViewElement(ViewHolder viewHolder, MapBlinkElement mapElement){
        viewHolder.nameTextView.setText(mapElement.getName());
        if(mapElement.getImageResId()!=0){
            viewHolder.mapBlinkImageView.setImageResource(mapElement.getImageResId());
        }else{
            if(mapElement.getPhotosRefList()!=null&&!mapElement.getPhotosRefList().isEmpty()){
                displayAvatarImage(mapElement.getPhotoUrl(BuildConfig.GOOGLE_API_KEY,0,400), viewHolder.mapBlinkImageView);
            }else{
                viewHolder.mapBlinkImageView.setImageResource(R.drawable.quantum_ic_art_track_grey600_48);
            }
            viewHolder.mapBlink.setVisibility(View.VISIBLE);
            if(mapElement.getRating()>0){
                viewHolder.mapBlinkRatingText.setVisibility(View.VISIBLE);
                viewHolder.mapBlinkRatingBar.setVisibility(View.VISIBLE);
                viewHolder.mapBlinkRatingBar.setRating(mapElement.getRating());
            }else{
                viewHolder.mapBlinkRatingText.setVisibility(View.GONE);
                viewHolder.mapBlinkRatingBar.setVisibility(View.GONE);
            }
            if(mapElement.getDescription()!=null&&!mapElement.getDescription().equals("")){
                viewHolder.mapBlinkDescription.setVisibility(View.VISIBLE);
                viewHolder.mapBlinkDescription.setText(mapElement.getDescription());
            }else{
                viewHolder.mapBlinkDescription.setVisibility(View.GONE);
            }
            if(mapElement.getOpenNow()!=null){
                viewHolder.mapBlinkIsOpen.setVisibility(View.VISIBLE);
                if(mapElement.getOpenNow()){
                    viewHolder.mapBlinkIsOpen.setText("Now open");
                    viewHolder.mapBlinkIsOpen.setTextColor(Color.GREEN);
                }else{
                    viewHolder.mapBlinkIsOpen.setText("Close");
                    viewHolder.mapBlinkIsOpen.setTextColor(Color.RED);
                }
            }else{
                viewHolder.mapBlinkIsOpen.setVisibility(View.GONE);
            }
        }
    }

    protected static class ViewHolder extends BaseViewHolder<MapBlinkElement> {

        @Bind(R.id.map_blink_image_view)
        ImageView mapBlinkImageView;

        @Bind(R.id.map_blink_name_text_view)
        TextView nameTextView;

        @Bind(R.id.map_blink_rating_text)
        TextView mapBlinkRatingText;

        @Bind(R.id.map_blink_description)
        TextView mapBlinkDescription;

        @Bind(R.id.map_blink_is_open)
        TextView mapBlinkIsOpen;

        @Bind(R.id.map_blink)
        TextView mapBlink;

        @Bind(R.id.map_blink_rating_bar)
        RatingBar mapBlinkRatingBar;

        public ViewHolder(MapBlinkElementAdapter adapter, View view) {
            super(adapter, view);
        }
    }
}