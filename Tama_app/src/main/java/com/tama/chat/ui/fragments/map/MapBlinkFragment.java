package com.tama.chat.ui.fragments.map;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tama.chat.R;
import com.tama.chat.gcm.MapBlinkElement;
import com.tama.chat.gcm.NearObjectsFinder;
import com.tama.chat.gcm.NearObjectsFinderListener;
import com.tama.chat.ui.activities.chats.MapBlinkActivity;
import com.tama.chat.ui.adapters.mapBlink.MapBlinkElementAdapter;
import com.tama.chat.ui.fragments.base.BaseFragment;
import com.tama.chat.utils.KeyboardUtils;
import com.tama.chat.utils.listeners.simple.SimpleOnRecycleItemClickListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
//chka
public class MapBlinkFragment extends BaseFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener, NearObjectsFinderListener, LocationListener{

    private static final String TAG = "myLogs";
    private static final String JSON_STRING = "jsonString";
    private static final String ORIGIN = "origin";
    private static final String MAP_BLINK_ELEMENT = "MapBlinkElement";
    private static final String NEW_TITLE = "newTitle";
    public static final int PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int PERMISSIONS_REQUEST_NETWORK_PROVIDER = 98;

    @Bind(R.id.friends_recyclerview)
    RecyclerView friendsRecyclerView;

    private MapBlinkElementAdapter mapBlinkElementAdapter;
    private Location location;

    public static MapBlinkFragment newInstance() {
        MapBlinkFragment fragment = new MapBlinkFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_element_list, container, false);
        activateButterKnife(view);
        initFields();
        initCustomListeners();



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"Fragment onResume");
        baseActivity.setActionBarIcon(null);
        baseActivity.setActionBarTitle(getString(R.string.action_bar_map));
        getLocation();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(baseActivity, Manifest.permission. ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(baseActivity,
                    new String[]{Manifest.permission. ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchManager searchManager = (SearchManager) baseActivity.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;

        if (searchMenuItem != null) {
            searchView = (SearchView) searchMenuItem.getActionView();
        }

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(baseActivity.getComponentName()));
            searchView.setOnQueryTextListener(this);
            searchView.setOnCloseListener(this);
        }
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
        KeyboardUtils.hideKeyboard(getActivity());
        search(searchQuery);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchQuery) {
        search(searchQuery);
        return true;
    }

//    @Override
//    public void onConnectedToService(QBService service) {
//        super.onConnectedToService(service);
////        if (friendListHelper != null) {
////            friendsAdapter.setFriendListHelper(friendListHelper);
////        }
//    }

    @Override
    public void onChangedUserStatus(int userId, boolean online) {
        super.onChangedUserStatus(userId, online);
        mapBlinkElementAdapter.notifyDataSetChanged();
    }

    private void initFields() {

        baseActivity.setActionBarIcon(null);
        baseActivity.setActionBarTitle(getString(R.string.action_bar_map));
        List<MapBlinkElement> mapBlinkElementsList= new ArrayList<>();

        mapBlinkElementsList.add(new MapBlinkElement.Builder().setName("Restaurant").
                setImageResId(R.drawable.restaurant).setType("restaurant").build());
        mapBlinkElementsList.add(new MapBlinkElement.Builder().setName("Cafe").
                setImageResId(R.drawable.cafe).setType("cafe").build());

        mapBlinkElementAdapter = new MapBlinkElementAdapter( baseActivity, mapBlinkElementsList);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        friendsRecyclerView.setAdapter(mapBlinkElementAdapter);
    }

    private void initCustomListeners() {

        mapBlinkElementAdapter.setOnRecycleItemClickListener(new SimpleOnRecycleItemClickListener<MapBlinkElement>() {
            @Override
            public void onItemClicked(View view, MapBlinkElement mapBlinkElement, int position) {
                super.onItemClicked(view, mapBlinkElement, position);
                if(checkLocationPermission()) {
                    sendRequest(mapBlinkElement.getType(), mapBlinkElement.getName());
                }
            }
        });
    }

    private void sendRequest(String type, String title) {
        if(checkGPS()){
            if(location!=null) {
                baseActivity.showProgress();
                try {
                    new NearObjectsFinder(this, location,type, title).execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else{
                getLocation();
            }
        }else{
            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(onGPS);
        }
    }

    private boolean checkGPS(){
        int off = 0;
        try {
            off = Settings.Secure.getInt(baseActivity.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return off!=0;
    }

    public void getLocation() {
        LocationManager locationManager = (LocationManager) baseActivity.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(baseActivity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(baseActivity, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return ;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 60000, this);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location == null){
            if (ActivityCompat.checkSelfPermission(baseActivity, Manifest.permission.ACCESS_NETWORK_STATE) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(baseActivity, Manifest.permission.ACCESS_NETWORK_STATE) !=
                            PackageManager.PERMISSION_GRANTED) {
                return ;
            }else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 60000, this);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        LocationManager locationManager = (LocationManager) baseActivity.getSystemService(Context.LOCATION_SERVICE);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(baseActivity, Manifest.permission. ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates("provider", 400, 1, this);
                    }
                }
                break;
            case PERMISSIONS_REQUEST_NETWORK_PROVIDER :
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 60000, this);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                break;
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

    @Override
    public void onNearObjectsFinderSuccess(String jsonArr, String title) { //het gal
        baseActivity.hideProgress();
        Intent i = new Intent(baseActivity, MapBlinkActivity.class);
        i.putExtra(JSON_STRING,  jsonArr);
        i.putExtra(NEW_TITLE,  title);
        startActivity(i);
    }

//    private List<MapBlinkElement> parseJSon(String data) throws JSONException {
//        if (data == null){
//            return null;
//        }
//        List<MapBlinkElement> mapBlinkElements = new ArrayList<>();
//        JSONObject jsonData = new JSONObject(data);
//        JSONArray jsonElements = jsonData.getJSONArray("results");
//        for (int i = 0; i < jsonElements.length(); i++) {
//            JSONObject jsonElement = jsonElements.getJSONObject(i);
//            MapBlinkElement mapBlinkElement = new MapBlinkElement();
//
//            String address = jsonElement.optString("vicinity");
//            if(address != null){
//                mapBlinkElement.setAddress(address);
//            }
//            String name = jsonElement.optString("name");
//            if(name!=null){
//                mapBlinkElement.setName(name);
//            }
//            float rating = (float) jsonElement.optDouble("rating");
//            if(rating!=0){
//                mapBlinkElement.setRating(rating);
//            }
//
//            JSONArray jsonPhotos = jsonElement.optJSONArray("photos");
//            if(jsonPhotos != null && jsonPhotos.length()>0){
//                ArrayList<Pair<String,Integer>> listRef = new ArrayList<>();
//                for(int j =0; j<jsonPhotos.length();++j){
//                    String reference =((JSONObject)jsonPhotos.get(0)).optString("photo_reference");
//                    Integer maxWidth = ((JSONObject)jsonPhotos.get(0)).optInt("photo_reference");
//                    listRef.add(new Pair<>(reference,maxWidth));
//                }
//                mapBlinkElement.setPhotosRefList(listRef);
//            }
//
//            JSONArray jsonDescription = jsonElement.optJSONArray("types");
//            String description;
//            if(jsonDescription!=null){
//                description = "";
//                for(int j = 0; j <jsonDescription.length(); ++j){
//                    description = description + jsonDescription.getString(j) + ",";
//                }
//                if (description.length() > 0 && description.charAt(description.length()-1)==',') {
//                    description = description.substring(0, description.length()-1);
//                    mapBlinkElement.setDescription(description);
//                }
//            }
//
//            JSONObject jsonGeometry = jsonElement.optJSONObject("geometry");
//            if(jsonGeometry!=null) {
//                JSONObject jsonLocation = jsonGeometry.optJSONObject("location");
//                if(jsonLocation!=null) {
//                    Double latitude = jsonLocation.optDouble("lat");
//                    Double longitude = jsonLocation.optDouble("lng");
//                    if(latitude!=0 && longitude!=0){
//                        mapBlinkElement.setLatLng(latitude, longitude);
//                    }
//                }
//            }
//
//            JSONObject jsonOpeningHours = jsonElement.optJSONObject("opening_hours");
//            if(jsonOpeningHours!=null){
//                if(!jsonOpeningHours.isNull("open_now")){
//                    mapBlinkElement.setOpenNow(jsonOpeningHours.optBoolean("open_now"));
//                }
//                JSONArray jsonWeekDay = jsonOpeningHours.optJSONArray("weekday_text");
//                String weekDay;
//                if(jsonWeekDay!=null && jsonWeekDay.length()>0){
//                    weekDay = "";
//                    for (int j=0; j < jsonWeekDay.length();++j){
//                        weekDay = weekDay + ((JSONArray)jsonWeekDay.get(j)).getString(j) + ",";
//                    }
//                    if (weekDay.length() > 0 && weekDay.charAt(weekDay.length()-1)==',') {
//                        weekDay = weekDay.substring(0, weekDay.length()-1);
//                    }
//                    mapBlinkElement.setWeekDay(weekDay);
//                }
//            }
//            mapBlinkElements.add(mapBlinkElement);
//        }
//        return mapBlinkElements;
//    }
}
