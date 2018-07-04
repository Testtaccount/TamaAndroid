package com.tama.chat.ui.activities.mapBlink;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tama.chat.BuildConfig;
import com.tama.chat.R;
import com.tama.chat.gcm.LatLngSer;
import com.tama.chat.gcm.MapBlinkElement;
import com.tama.chat.ui.activities.base.MapBlinkBaseActivity;
import com.tama.chat.utils.image.ImageLoaderUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class MapBlinkElementActivity extends MapBlinkBaseActivity {

    @Bind(R.id.rating_text_view)
    TextView ratingTextView;

    @Bind(R.id.description_text)
    TextView descriptionText;

    @Bind(R.id.address_text)
    TextView addressText;

    @Bind(R.id.open_now_text)
    TextView openNowText;

    @Bind(R.id.week_day_text)
    TextView weekDayText;

    @Bind(R.id.scrolling_lin_layout)
    LinearLayout scrollingLinLayout;

    @Bind(R.id.first_photo)
    ImageView firstPhoto;

    @Bind(R.id.rating_view)
    RatingBar ratingView;

    @Bind(R.id.directions)
    Button directionButton;

    @Bind(R.id.get_offer_code)
    Button getOfferCode;

    private int maxWidth;
    private static final String ORIGIN = "origin";
    private static final String DIRECTION = "direction";
    private static final String MAP_BLINK_ELEMENT = "MapBlinkElement";
    private static final String NEW_TITLE = "newTitle";

    private MapBlinkElement mapBlinkElement;
    private LatLngSer originLocation;

    @Override
    protected int getContentResId() {
        return R.layout.activity_map_blink_element;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mapBlinkElement = (MapBlinkElement) intent.getSerializableExtra(MAP_BLINK_ELEMENT);
        originLocation = (LatLngSer) intent.getSerializableExtra(ORIGIN);

        title = mapBlinkElement.getName();
        setUpActionBarWithUpButton();
        initViewElement();
    }

    @OnClick(R.id.directions)
    public void sendRequest() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(NEW_TITLE, title);
        intent.putExtra(ORIGIN, originLocation);
        intent.putExtra(DIRECTION, mapBlinkElement.getOrigin());
        startActivity(intent);
    }

    private void initViewElement(){
        Display display = getWindowManager().getDefaultDisplay();
        maxWidth = display.getWidth();

        if(mapBlinkElement.getPhotosRefList()!=null
                &&!mapBlinkElement.getPhotosRefList().isEmpty()){
            ImageLoader.getInstance().displayImage(mapBlinkElement.getPhotoUrl(BuildConfig.GOOGLE_API_KEY,0, maxWidth),
                    firstPhoto, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
        }
        if(mapBlinkElement.getRating()>0){
            ratingTextView.setVisibility(View.VISIBLE);
            ratingView.setVisibility(View.VISIBLE);
            ratingView.setRating(mapBlinkElement.getRating());
        }
        if(mapBlinkElement.getDescription()!=null){
            descriptionText.setText(mapBlinkElement.getDescription());
        }
        if(mapBlinkElement.getAddress()!=null){
            addressText.setText(getString(R.string.address) + mapBlinkElement.getAddress());
        }
        if(mapBlinkElement.getOpenNow()!=null){
            if(mapBlinkElement.getOpenNow()){
                openNowText.setText(getString(R.string.now_open));
            }else{
                openNowText.setText(getString(R.string.close));
            }
        }
        if(mapBlinkElement.getWeekDay()!=null){
            weekDayText.setText(mapBlinkElement.getWeekDay());
        }
        if(mapBlinkElement.getPhotosRefList()!=null
                &&mapBlinkElement.getPhotosRefList().size()>1){
            for(int i = 1; i<mapBlinkElement.getPhotosRefList().size();++i){
                ImageView imageView = new ImageView(this);
                scrollingLinLayout.addView(imageView);
                ImageLoader.getInstance().displayImage(mapBlinkElement.getPhotoUrl(BuildConfig.GOOGLE_API_KEY,i, maxWidth),
                        imageView, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
            }
        }
    }
}
