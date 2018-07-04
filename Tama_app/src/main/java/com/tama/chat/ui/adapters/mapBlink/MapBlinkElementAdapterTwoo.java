package com.tama.chat.ui.adapters.mapBlink;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tama.chat.BuildConfig;
import com.tama.chat.R;
import com.tama.chat.gcm.MapBlinkElement;
import com.tama.chat.ui.activities.base.MapBlinkBaseActivity;
import com.tama.chat.utils.image.ImageLoaderUtils;
import com.tama.chat.utils.listeners.OnRecycleItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

//chka
public class MapBlinkElementAdapterTwoo extends RecyclerView.Adapter<MapBlinkElementAdapterTwoo.MapBlinkElementViewHolder> implements OnRecycleItemClickListener {
    private List<MapBlinkElement> usersList;
    private List<MapBlinkElement> visibleList;
    private MapBlinkBaseActivity activity;
    protected String query;
    OnRecycleItemClickListener<MapBlinkElement> onRecycleItemClickListener;


    public MapBlinkElementAdapterTwoo(MapBlinkBaseActivity baseActivity, List<MapBlinkElement> usersList) {
        this.usersList = usersList;
        this.activity = baseActivity;
    }

    public void setOnRecycleItemClickListener(OnRecycleItemClickListener<MapBlinkElement> onRecycleItemClickListener) {
        this.onRecycleItemClickListener = onRecycleItemClickListener;
    }

//    @Override
//    public BaseClickListenerViewHolder<MapBlinkElement> onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ViewHolder(this, layoutInflater.inflate(R.layout.item_map_blink_element, parent, false));
//    }


    @Override
    public MapBlinkElementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MapBlinkElementViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(MapBlinkElementViewHolder holder, int position) {
        MapBlinkElement mapElement = getItem(position);
        initViewElement(holder, mapElement);
    }

    private MapBlinkElement getItem(int position) {
        return usersList.get(position);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    private void initViewElement(MapBlinkElementViewHolder viewHolder, MapBlinkElement mapElement){
        viewHolder.nameTextView.setText(mapElement.getName());
        if(mapElement.getImageResId()!=0){
            viewHolder.mapBlinkImageView.setImageResource(mapElement.getImageResId());
        }else{
            if(mapElement.getPhotosRefList()!=null&&!mapElement.getPhotosRefList().isEmpty()){
                ImageLoader.getInstance().displayImage(mapElement.getPhotoUrl(BuildConfig.GOOGLE_API_KEY,0,400),
                        viewHolder.mapBlinkImageView, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
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

    public void setFilter(String query) {
        this.query = query;
        if (TextUtils.isEmpty(query)) {
            flushFilter();
            return;
        }
        query = query.toLowerCase();
        visibleList = new ArrayList<>();
        for (MapBlinkElement item : usersList) {
            if (isMatch(item, query)) {
                visibleList.add(item);
            }
        }
        notifyDataSetChanged();
    }

    protected boolean isMatch(MapBlinkElement item, String query) {
        return item.getName() != null && item.getName().toLowerCase().contains(query);
    }

    public void flushFilter() {
        this.query = "";
        visibleList = new ArrayList<>(usersList);
        notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(View view, Object entity, int position) {

    }

    @Override
    public void onItemLongClicked(View view, Object entity, int position) {

    }

    protected static class MapBlinkElementViewHolder extends RecyclerView.ViewHolder {

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

        public MapBlinkElementViewHolder(View view) {
            super(view);

        }
    }
}