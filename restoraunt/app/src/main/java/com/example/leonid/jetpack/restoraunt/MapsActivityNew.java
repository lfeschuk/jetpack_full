package com.example.leonid.jetpack.restoraunt;



import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Objects.Delivery;


public class MapsActivityNew extends AppCompatActivity implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap mMap;
    private ArrayList<Marker> marker_array = new ArrayList<>();
    List<Delivery> array = new ArrayList<Delivery>();
    Boolean first_time = true;
    private Query mDatabase = FirebaseDatabase.getInstance().getReference();
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final String TAG = "MapsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragments_map);

        Toolbar tb = findViewById(R.id.toolbar_map);
        setSupportActionBar(tb);
        tb.setSubtitle("MapView");

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        String arr = "";
        arr = getIntent().getExtras().getString("array");
        array = Arrays.asList(new Gson().fromJson(arr, Delivery[].class));



        mMapView = findViewById(R.id.map_view);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }



    private Marker get_marker(String index)
    {
        for(Marker m : marker_array)
        {
            if (m.getTag().equals(index))
            {
                return m;
            }
        }
        return null;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(10);
        mMap.setMaxZoomPreference(16);
        LatLng modiin = new LatLng(31.8903, 35.0104);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(modiin));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
       // mMap.getUiSettings().setZoomControlsEnabled(true);



        ArrayList<LatLng> llList = new ArrayList<>();
        for(Delivery d: array)
        {
            LatLng ll = new LatLng(d.getDest_cord_lat(),d.getDest_cord_long());
            llList.add(ll);
            Log.d(TAG,"lat: " + d.getDest_cord_lat()+" long: " +  d.getDest_cord_long());
        }
        Toast.makeText(getApplicationContext(), "size: " + llList.size(), Toast.LENGTH_SHORT).show();

        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                .data(llList)
                .build();

        // Add a tile overlay to the map, using the heat map tile provider.
        TileOverlay mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));


//        mDatabase =  FirebaseDatabase.getInstance().getReference("Deliveries").orderByChild("restoraunt_key").equalTo(MainActivity.this_restoraunt.getKey());
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    Delivery temp = new Delivery(ds.getValue(Delivery.class));
//                    if (!temp.getIs_deleted() && temp.getStatus().equals("D")) {
//        for(Delivery d: array)
//        {
//
//             Marker m = get_marker(d.getIndexString());
//            //new marker
//            if (m == null) {
//                MarkerOptions guy = new MarkerOptions();
//                guy.position(new LatLng(temp.getLatetude(), temp.getLongtitude()));
//                guy.title(temp.getName() + "(" + temp.getTimeBeFree() + ")");
//                m = mMap.addMarker(guy);
//                m.setTag(temp.getIndex_string());
//                m.showInfoWindow();
//                marker_array.add(m);
//                Log.d(TAG, "DeliveryGuy is :  " + temp.getName());
//            }
//            //existed marker
//            else {
//                m.setPosition(new LatLng(temp.getLatetude(), temp.getLongtitude()));
//                m.setTitle(temp.getName() + "(" + temp.getTimeBeFree() + ")");
//            }
//        }
//
//                        Marker m = get_marker(temp.getIndex_string());
//                        //new marker
//                        if (m == null) {
//                            MarkerOptions guy = new MarkerOptions();
//                            guy.position(new LatLng(temp.getLatetude(), temp.getLongtitude()));
//                            guy.title(temp.getName() + "(" + temp.getTimeBeFree() + ")");
//                            m = mMap.addMarker(guy);
//                            m.setTag(temp.getIndex_string());
//                            m.showInfoWindow();
//                            marker_array.add(m);
//                            Log.d(TAG, "DeliveryGuy is :  " + temp.getName());
//                        }
//                        //existed marker
//                        else {
//                            m.setPosition(new LatLng(temp.getLatetude(), temp.getLongtitude()));
//                            m.setTitle(temp.getName() + "(" + temp.getTimeBeFree() + ")");
//                        }
//
//                    }
//                }
//                Log.d(TAG,"Maps Done retrieving DeliveryGuy " + array.size());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//            }
//        });
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
//        Log.d(TAG,"onCreateView");
//        mMapView = (MapView) rootView.findViewById(R.id.mapView);
//        mMapView.onCreate(savedInstanceState);
//
//        mMapView.onResume(); // needed to get the map to display immediately
//
//        try {
//            MapsInitializer.initialize(getActivity().getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        mMapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                Log.d(TAG,"onMapReady inside on create");
//                mMap = googleMap;
//
//                // For showing a move to my location button
//              //  googleMap.setMyLocationEnabled(true);
//
//                // For dropping a marker at a point on the Map
//                LatLng sydney = new LatLng(-34, 151);
//                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
//
//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
//                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            }
//        });
//
//        return rootView;
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//      //  mMapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//    //    mMapView.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    //    mMapView.onDestroy();
//    }
//
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        Log.d(TAG,"onMapReady other");
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
}
