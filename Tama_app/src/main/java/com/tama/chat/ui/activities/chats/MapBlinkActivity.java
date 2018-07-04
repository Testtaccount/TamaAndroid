package com.tama.chat.ui.activities.chats;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tama.chat.R;
import com.tama.chat.gcm.LatLngSer;
import com.tama.chat.gcm.MapBlinkElement;
import com.tama.chat.ui.activities.base.BaseActivity;
import com.tama.chat.ui.activities.mapBlink.MapBlinkElementActivity;
import com.tama.chat.ui.adapters.mapBlink.MapBlinkElementAdapter;
import com.tama.chat.utils.KeyboardUtils;
import com.tama.chat.utils.listeners.simple.SimpleOnRecycleItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
//chka
public class MapBlinkActivity extends BaseActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener, LocationListener{

    private static final String TAG = "myLogs";
    private static final String JSON_STRING = "jsonString";
    private static final String ORIGIN = "origin";
    private static final String MAP_BLINK_ELEMENT = "MapBlinkElement";
    private static final String NEW_TITLE = "newTitle";
    @Bind(R.id.friends_recyclerview)
    RecyclerView friendsRecyclerView;

    private MapBlinkElementAdapter mapBlinkElementAdapter;
    private Location location;

    public static void start(Context context, List<MapBlinkElement> mapBlinkElementsList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", (Serializable) mapBlinkElementsList);
        Intent intent = new Intent(context, MapBlinkActivity.class);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MapBlinkActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_friends_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initRecyclerView();
        initFields();
        setUpActionBarWithUpButton();
        initCustomListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;

        if (searchMenuItem != null) {
            searchView = (SearchView) searchMenuItem.getActionView();
        }

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(this);
            searchView.setOnCloseListener(this);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onClose() {
        cancelSearch();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String searchQuery) {
        KeyboardUtils.hideKeyboard(this);
        search(searchQuery);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchQuery) {
        search(searchQuery);
        return true;
    }

    @Override
    public void onChangedUserStatus(int userId, boolean online) {
        super.onChangedUserStatus(userId, online);
        mapBlinkElementAdapter.notifyDataSetChanged();
    }

    private void initFields() {
        Bundle extras=getIntent().getExtras();
        if(extras !=null) {
            title = extras.getString(NEW_TITLE);
        }

    }

    private void initRecyclerView() {

        List<MapBlinkElement> mapBlinkElementsList= new ArrayList<>();
        Intent i = getIntent();
        Bundle extras=i.getExtras();

        if(extras !=null) {
            String jsonRes = extras.getString(JSON_STRING);
            try {
                mapBlinkElementsList = parseJSon(jsonRes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mapBlinkElementAdapter = new MapBlinkElementAdapter(this, mapBlinkElementsList);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendsRecyclerView.setAdapter(mapBlinkElementAdapter);
    }

    private void initCustomListeners() {



        mapBlinkElementAdapter.setOnRecycleItemClickListener(new SimpleOnRecycleItemClickListener<MapBlinkElement>() {
            @Override
            public void onItemClicked(View view, MapBlinkElement mapBlinkElement, int position) {
                super.onItemClicked(view, mapBlinkElement, position);

                Intent intent = new Intent(getApplicationContext(), MapBlinkElementActivity.class);
                intent.putExtra(ORIGIN, new LatLngSer(location.getLatitude(), location.getLongitude()));
                intent.putExtra(MAP_BLINK_ELEMENT, mapBlinkElement);
                startActivity(intent);
            }
        });
    }

    public void getLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return ;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 60000, this);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location == null){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10,60000, this);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
    }

    private void search(String searchQuery) {
        if (mapBlinkElementAdapter != null) {
            mapBlinkElementAdapter.setFilter(searchQuery);
        }
    }

    private void cancelSearch() {
        if (mapBlinkElementAdapter != null) {
            mapBlinkElementAdapter.flushFilter();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private List<MapBlinkElement> parseJSon(String data) throws JSONException {
        if (data == null){
            return null;
        }
        List<MapBlinkElement> mapBlinkElements = new ArrayList<>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonElements = jsonData.getJSONArray("results");
        for (int i = 0; i < jsonElements.length(); i++) {
            JSONObject jsonElement = jsonElements.getJSONObject(i);
            MapBlinkElement mapBlinkElement = new MapBlinkElement();

            String address = jsonElement.optString("vicinity");
            if(address != null){
                mapBlinkElement.setAddress(address);
            }
            String name = jsonElement.optString("name");
            if(name!=null){
                mapBlinkElement.setName(name);
            }
            float rating = (float) jsonElement.optDouble("rating");
            if(rating!=0){
                mapBlinkElement.setRating(rating);
            }

            JSONArray jsonPhotos = jsonElement.optJSONArray("photos");
            if(jsonPhotos != null && jsonPhotos.length()>0){
                ArrayList<Pair<String,Integer>> listRef = new ArrayList<>();
                for(int j =0; j<jsonPhotos.length();++j){
                    String reference =((JSONObject)jsonPhotos.get(0)).optString("photo_reference");
                    Integer maxWidth = ((JSONObject)jsonPhotos.get(0)).optInt("photo_reference");
                    listRef.add(new Pair<>(reference,maxWidth));
                }
                mapBlinkElement.setPhotosRefList(listRef);
            }

            JSONArray jsonDescription = jsonElement.optJSONArray("types");
            String description;
            if(jsonDescription!=null){
                description = "";
                for(int j = 0; j <jsonDescription.length(); ++j){
                    description = description + jsonDescription.getString(j) + ",";
                }
                if (description.length() > 0 && description.charAt(description.length()-1)==',') {
                    description = description.substring(0, description.length()-1);
                    mapBlinkElement.setDescription(description);
                }
            }

            JSONObject jsonGeometry = jsonElement.optJSONObject("geometry");
            if(jsonGeometry!=null) {
                JSONObject jsonLocation = jsonGeometry.optJSONObject("location");
                if(jsonLocation!=null) {
                    Double latitude = jsonLocation.optDouble("lat");
                    Double longitude = jsonLocation.optDouble("lng");
                    if(latitude!=0 && longitude!=0){
                        mapBlinkElement.setLatLng(latitude, longitude);
                    }
                }
            }

            JSONObject jsonOpeningHours = jsonElement.optJSONObject("opening_hours");
            if(jsonOpeningHours!=null){
                if(!jsonOpeningHours.isNull("open_now")){
                    mapBlinkElement.setOpenNow(jsonOpeningHours.optBoolean("open_now"));
                }
                JSONArray jsonWeekDay = jsonOpeningHours.optJSONArray("weekday_text");
                String weekDay;
                if(jsonWeekDay!=null && jsonWeekDay.length()>0){
                    weekDay = "";
                    for (int j=0; j < jsonWeekDay.length();++j){
                        weekDay = weekDay + ((JSONArray)jsonWeekDay.get(j)).getString(j) + ",";
                    }
                    if (weekDay.length() > 0 && weekDay.charAt(weekDay.length()-1)==',') {
                        weekDay = weekDay.substring(0, weekDay.length()-1);
                    }
                    mapBlinkElement.setWeekDay(weekDay);
                }
            }
            mapBlinkElements.add(mapBlinkElement);
        }
        return mapBlinkElements;
    }
}
