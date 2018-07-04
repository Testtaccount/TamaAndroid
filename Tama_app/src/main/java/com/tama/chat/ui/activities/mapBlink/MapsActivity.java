package com.tama.chat.ui.activities.mapBlink;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tama.chat.R;
import com.tama.chat.gcm.DirectionFinder;
import com.tama.chat.gcm.DirectionFinderListener;
import com.tama.chat.gcm.LatLngSer;
import com.tama.chat.gcm.Route;
import com.tama.chat.ui.activities.base.MapBlinkBaseActivity;
import com.tama.chat.ui.adapters.mapBlink.SimpleImageArrayAdapter;
import com.tama.chat.utils.ToastUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnItemSelected;

public class MapsActivity extends MapBlinkBaseActivity implements OnMapReadyCallback, DirectionFinderListener {

    @Bind(R.id.tv_distance)
    TextView tvDistance;

    @Bind(R.id.tv_duration)
    TextView tvDuration;

    @Bind(R.id.map_type_normal)
    Button mapTypeNormal;

    @Bind(R.id.map_type_satellite)
    Button mapTypeSatellite;

    @Bind(R.id.map_type_hybrid)
    Button mapTypeHybrid;

    @Bind(R.id.mode_spinner)
    Spinner modeSpinner;

    private static final String NEW_TITLE = "newTitle";
    private static final String ORIGIN = "origin";
    private static final String DIRECTION = "direction";
    private Integer[] images = {R.drawable.emoji_1f699,
            R.drawable.emoji_1f6b6,
            R.drawable.emoji_1f6b4,
            R.drawable.emoji_1f68c};

    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private LatLngSer originLocation,destinationLocation;
    private int color = Color.BLUE;




    @Override
    protected int getContentResId() {
        return R.layout.activity_maps;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        if(intent!=null){
            originLocation = (LatLngSer) intent.getSerializableExtra(ORIGIN);
            destinationLocation = (LatLngSer) intent.getSerializableExtra(DIRECTION);
            title =intent.getExtras().getString(NEW_TITLE);
        }
        setUpActionBarWithUpButton();
        initFields();
    }

    private void initFields(){
        SimpleImageArrayAdapter adapter = new SimpleImageArrayAdapter(this, images);
        modeSpinner.setAdapter(adapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        LatLng hcmus = new LatLng(10.762963, 106.682394);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
//        originMarkers.add(mMap.addMarker(new MarkerOptions()
//                .title("Đại học Khoa học tự nhiên")
//                .position(hcmus)));

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void selectMapType(View v) {
        switch (v.getId()){
            case R.id.map_type_normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.map_type_satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.map_type_hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            default:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
        }

    }

    public void onDirectionCreate(ArrayList<Route> routes, int color) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(route.startLocation.latitude,route.startLocation.longitude), 16));
            tvDuration.setText(route.duration.text);
            tvDistance.setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                    .title(route.startAddress)
                    .position(new LatLng(route.startLocation.latitude,route.startLocation.longitude))));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                    .title(route.endAddress)
                    .position(new LatLng(route.endLocation.latitude,route.endLocation.longitude))));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(color).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(new LatLng(route.points.get(i).latitude,route.points.get(i).longitude));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @OnItemSelected(R.id.mode_spinner)
    void snapChatTimeChange(int position){
        String MODE;
        switch (position){
            case 0:
                MODE = "driving";
                color = Color.BLUE;
                break;
            case 1:
                MODE = "walking";
                color = Color.GREEN;
                break;
            case 2:
                MODE = "bicycling";
                color = Color.CYAN;
                break;
            case 3:
                MODE = "transit";
                color = Color.YELLOW;
                break;
            default:
                MODE = "driving";
                color = Color.BLUE;
                break;
        }
        sendRequest(MODE);
    }

    public void sendRequest(String mode) {
        String destination = String.valueOf(destinationLocation.latitude)+ "," + String.valueOf(destinationLocation.longitude);
        String origin = String.valueOf(originLocation.latitude)+ "," + String.valueOf(originLocation.longitude);
        try {
            new DirectionFinder(this, origin, destination, mode).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(ArrayList<Route> routes) {
        progressDialog.dismiss();
        if(routes.size()==0){
            ToastUtils.longToast("Direction not found");
            tvDistance.setText("");
            tvDuration.setText("");
        }else{
            onDirectionCreate(routes, color);
        }
    }
}
