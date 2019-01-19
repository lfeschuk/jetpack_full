package com.example.leonid.jetpack.jetpack_shlihim;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;

import com.example.leonid.jetpack.jetpack_shlihim.HelpfulClasses.GoogleService;
import com.example.leonid.jetpack.jetpack_shlihim.Objects.Alerts;
import com.example.leonid.jetpack.jetpack_shlihim.Objects.DataParser;
import com.example.leonid.jetpack.jetpack_shlihim.Objects.Delivery;
import com.example.leonid.jetpack.jetpack_shlihim.Objects.DeliveryGuysShift;
import com.example.leonid.jetpack.jetpack_shlihim.Objects.Destination;

import com.example.leonid.jetpack.jetpack_shlihim.Objects.DistanceDuration;
import com.example.leonid.jetpack.jetpack_shlihim.Objects.InfoAttached;
import com.example.leonid.jetpack.jetpack_shlihim.Objects.RouteCalculation;
import com.example.leonid.jetpack.jetpack_shlihim.Objects.User;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.location.LocationListener;

import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonid.jetpack.jetpack_shlihim.HelpfulClasses.MyLocationListener;
import com.example.leonid.jetpack.jetpack_shlihim.Objects.DeliveryGuys;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.leonid.jetpack.jetpack_shlihim.Objects.DeliveryGuysShift.KindShift.NONE;
import static com.example.leonid.jetpack.jetpack_shlihim.Objects.DeliveryGuysShift.KindShift.valueOf;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int CAMERA_DONE_DRIVER = 104;
    private static final int PICK_IMAGE_DRIVER = 105;
    private static final int CAMERA_DONE_ID = 106;
    private static final int PICK_IMAGE_ID = 107;
    private static final int CAMERA_DONE_101 = 108;
    private static final int PICK_IMAGE_101 = 109;
    public static User this_user = null;
    public static DeliveryGuys this_delivery_guy = null;
    private Toolbar toolbar;
    private GoogleMap mMap;
    DrawerLayout dLayout;
    Query mDatabase;
    DeliveryGuysShift.KindShift[] chosen_shifts = new DeliveryGuysShift.KindShift[7];
    MapView mMapView;
    ArrayList<Delivery> array;
    ArrayList<TextView> hidden_text_array = new ArrayList<>();
    ArrayList<Destination> array_dest;
    Delivery current_delivery = null;
    private LocationRequest mLocationRequest;
    private GoogleApiClient googleApiClient;
    BroadcastReceiver broadcastReceiver;
    int index_to_remove = 0;
    public static Activity main_activity;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 0;
    private static final int REQUEST_PHONE_CALL = 1;
    private static final int CAMERA_DONE = 100;
    private static final int PICK_IMAGE = 101;
    private static final int CAMERA_DONE_PROFILE = 102;
    private static final int PICK_IMAGE_PROFILE = 103;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    public final String TAG = "MainActivity";
    Bundle mapViewBundle = null;
    ImageView profile_image;
    ImageView drivers_license;
    ImageView id_image;
    ImageView form_101;
    String license_exp;
    Uri file_photo;
    View.OnClickListener first_on_click;
     String costumer_phone ;
     String restoraunt_phone ;
    View.OnClickListener second_on_click;
    public static MediaPlayer player ;
    Boolean is_first_on_child_added = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_activity = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setNavigationDrawer();
        Arrays.fill(chosen_shifts,NONE);
        //   googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();

        Log.d(TAG, "fff");
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String[] arr = email.split("@");
        String phone = arr[0];
        Log.d(TAG,"phone: " + phone);

        LocationManager locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        Boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSEnable)
        {
            Toast.makeText(MainActivity.this, "הדלק בבקשה שירותי מיקום",Toast.LENGTH_LONG ).show();
            finish();

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

            get_current_user();
            Intent intent = new Intent(this, GoogleService.class);
            startService(intent);
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    double latitude = Double.valueOf(intent.getStringExtra("latutide"));
                    double longitude = Double.valueOf(intent.getStringExtra("longitude"));
                    Log.d(TAG,"long: " + longitude + " lat: " + latitude);
                    if (this_delivery_guy != null)
                    {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Delivery_Guys");
                        mDatabase.child(this_delivery_guy.getIndex_string()).child("latetude").setValue(latitude);
                        mDatabase.child(this_delivery_guy.getIndex_string()).child("longtitude").setValue(longitude);
                        this_delivery_guy.setLatetude(latitude);
                        this_delivery_guy.setLongtitude(longitude);

                        RouteCalculation rc = new RouteCalculation(true,null,false,"" ,
                                new ArrayList<DistanceDuration>(),this_delivery_guy,this_delivery_guy.getDestinations(),this_delivery_guy.getDeliveries(),MainActivity.this);
                        rc.calculate_routes();

                    }


                }
            };

            if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
            }




    }
    public static Delivery getDelivery(Destination des,ArrayList<Delivery> deliv_array)
    {
        for (Delivery d : deliv_array)
        {
            if (d.getIndexString().equals(des.getIndex_string()))
            {
                return d;
            }
        }
        return null;
    }

    public void updateUiCallback(ArrayList<Destination> array,ArrayList<Delivery> deliv_array,DeliveryGuys deliv_guy)
    {

     //   dbm.writeDeliveryDestArray(deliv_guy,array);
          DatabaseReference mDatabase =  FirebaseDatabase.getInstance().getReference();
     //   mDatabase.child("Delivery_Guys").child(deliv_guy.getIndex_string()).child("destinations").setValue(array);
      // dbm.setAproxTime(array,deliv_array,deliv_guy.getIndex_string());

        for (Destination d : array)
        {
            Delivery to_update = getDelivery(d,deliv_array);
            if (d.getTo_costumer())
            {
                mDatabase.child("Deliveries").child(to_update.getKey()).child("time_aprox_deliver").setValue(d.getTimeDeliver());
            }
            else
            {
                mDatabase.child("Deliveries").child(to_update.getKey()).child("time_aprox_deliver_to_rest").setValue(d.getTimeDeliver());
            }
        }
        mDatabase.child("Delivery_Guys").child(deliv_guy.getIndex_string()).child("timeBeFree").setValue(array.get(array.size() - 1).getTimeDeliver());

//            Intent intent = new Intent(DeliveryDataActivity.this, MainActivity.class);
////            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
////            startActivity(intent);
////            finish();


    }
    public void setMap()
    {
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

//not in use for now
    public void get_current_user()
    {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String[] arr = email.split("@");
        String phone = arr[0];
        Query query =  FirebaseDatabase.getInstance().getReference("User_Delivery").orderByKey().equalTo(phone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    User temp = new User(ds.getValue(User.class));
                    this_user  = temp;
                    getDeliveryGuyAndSetUI();
                    Log.d(TAG,"UserAdmin is :  " + temp);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
    public void getDeliveryGuyAndSetUI ()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("Delivery_Guys");
        mDatabase.orderByKey().equalTo(this_user.getIndexString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "hhh");
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        DeliveryGuys temp = new DeliveryGuys(ds.getValue(DeliveryGuys.class));
                        this_delivery_guy = temp;
                       // remove_delivery("11");
                        array_dest = this_delivery_guy.getDestinations();
                        array = this_delivery_guy.getDeliveries();
                        setUI();
                        setMap();
                        set_buttons();
                        toggleSwitchButton();
                        setListenerOnDeliveries();
                        setListenerToStartShift();
                        Alerts.register(MainActivity.this);
                        Log.d(TAG, "bb");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void setListenerToStartShift()
    {
       final TextView tv = findViewById(R.id.textViewNoActive);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User_Delivery").child(this_user.getPhone()).child("enable_notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean is_pressed = new Boolean(dataSnapshot.getValue(Boolean.class));
                if (is_pressed)
                {
                    tv.setVisibility(View.GONE);
                }
                else
                {
                    tv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child("Delivery_Guys").child(this_user.getIndexString()).child("is_active").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean is_active = dataSnapshot.getValue(Boolean.class);
                Log.d(TAG,"fff123");
                if (is_active)
                {
                    if (!this_delivery_guy.getSent_start_shift_report())
                    {
                        Log.d(TAG,"mmm123");
                        Intent intent = new Intent(MainActivity.this, FillStartShiftActivity.class);
                        startActivity(intent);
                    }
                }
                else
                    {
                        this_delivery_guy.setSent_start_shift_report(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        mDatabase.child("Delivery_Guys").child(this_user.getIndexString()).child("sent_start_shift_report").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Boolean is_sent = dataSnapshot.getValue(Boolean.class);
//                if (is_sent)
//                {
//                    this_delivery_guy.setSent_start_shift_report(true);
//                }
//                else
//                {
//                    this_delivery_guy.setSent_start_shift_report(false);
//                    Intent intent = new Intent(MainActivity.this, FillStartShiftActivity.class);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }
    public void setListenerOnDeliveries()
    {
        final Query mDatabase_ = FirebaseDatabase.getInstance().getReference("Delivery_Guys").child(this_delivery_guy.getIndex_string());

        mDatabase_.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Log.d(TAG, "onChildAdded  destinations " + dataSnapshot.getValue() + " count " + dataSnapshot.getChildrenCount());
                    if (dataSnapshot.getKey().equals("destinations") && !is_first_on_child_added) {
                        array_dest.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Destination d = new Destination(ds.getValue(Destination.class));
                            array_dest.add(d);
                        }
                        Toast.makeText(MainActivity.this, "שים לב כי מסלולך השתנה", Toast.LENGTH_LONG).show();
                        setUI();
                        Alerts.displayDialog(getApplicationContext());


                    }
                    else if (dataSnapshot.getKey().equals("destinations"))
                    {
                        is_first_on_child_added = false;
                    }


//                Map<String, Destination> td = (HashMap<String,Destination>) dataSnapshot.getValue();
//                ArrayList<Destination> objectArrayList = new ArrayList<Destination>(td.values());
//                array_dest = objectArrayList;
//                Log.d(TAG, "המסלול התווסף " + objectArrayList);
//
//                for(Destination ds : objectArrayList)
//                {
//                    Log.d(TAG, "222  " + ds);
//                }
//
//                for(DataSnapshot ds : dataSnapshot.getChildren())
//                {
//                    Log.d(TAG, "2223  " + ds.getValue());
//                }
//
//
//                setUI();
//
//                //toast new deliveries arrived
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "s: " + dataSnapshot.getValue());
                if (dataSnapshot.getKey().equals("destinations")) {
                    array_dest.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Destination d = new Destination(ds.getValue(Destination.class));
                        array_dest.add(d);
                    }
                    Toast.makeText(MainActivity.this, "שים לב כי מסלולך השתנה", Toast.LENGTH_LONG).show();
                    setUI();
                    Alerts.displayDialog(getApplicationContext());


                }
            }




            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final Query mDatabase_2 = FirebaseDatabase.getInstance().getReference("Delivery_Guys").child(this_delivery_guy.getIndex_string());

        mDatabase_2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "deliveries onChildAdded " + dataSnapshot.getValue());
//                Map<String, Delivery> td = (HashMap<String,Delivery>) dataSnapshot.getValue();
//                array = new ArrayList<Delivery>(td.values());
//                Log.d(TAG, "new deliveries recieved " + array);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if ( dataSnapshot.getKey().equals("deliveries"))
                {
                    array.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        Delivery d = new Delivery(ds.getValue(Delivery.class));
                        array.add(d);
                    }
                }
            }




            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void notify_route_changed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        final View myView = li.inflate(R.layout.dialog_on_pick_delivery, null);
        TextView tv = myView.findViewById(R.id.textView);
        tv.setText("שים לב כי מסלולך השתנה");
        builder.setView(myView);
        builder.setTitle("שינוי מסלול");
        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        final AlertDialog dialog = builder.create();

        dialog.show();
    }
    //if the delivery guy thinks he is active
    public void toggleSwitchButton()
    {
        final SwitchCompat sc = findViewById(R.id.on_or_off);
        sc.setChecked( this_user.getEnable_notification());
        if (this_user.getEnable_notification())
        {
            MainActivity.this.registerReceiver(broadcastReceiver, new IntentFilter("MyApplication.DisplayLocation"));
        }
        sc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sc.setChecked(b);
                this_user.setEnable_notification(b);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("User_Delivery").child(this_user.getPhone()).child("enable_notification").setValue(b);
                if (b)
                {

                    Toast.makeText(MainActivity.this, "המשמרת הופעלה",Toast.LENGTH_LONG ).show();
                    MainActivity.this.registerReceiver(broadcastReceiver, new IntentFilter("MyApplication.DisplayLocation"));

                }
                else
                {
                    Toast.makeText(MainActivity.this, "המשמרת נסגרה",Toast.LENGTH_LONG ).show();
                    unregisterReceiver(broadcastReceiver);
                }

            }
        });




    }
    public void setUI() {
        final TextView tv_main = findViewById(R.id.title);
        final TextView tv_adress = findViewById(R.id.adress);
        final TextView num_Deliv = findViewById(R.id.num_of_deliveries);
        num_Deliv.setText("משלוחים במשמרת:" + this_delivery_guy.getNum_of_deliveries());
        final RelativeLayout lt = findViewById(R.id.relative_layout);
        if (array_dest == null || array_dest.isEmpty()) {
            Toast.makeText(this, "No routes available", Toast.LENGTH_SHORT).show();
           // toolbar.setVisibility(View.GONE);
            RelativeLayout rl = findViewById(R.id.relative_layout);
            tv_adress.setVisibility(View.GONE);
            tv_main.setText("\n" +"ללא משלוחים פעילים");
            rl.setVisibility(View.GONE);

            return;
        }
        else
        {
         //   toolbar.setVisibility(View.VISIBLE);
            RelativeLayout rl = findViewById(R.id.relative_layout);
            rl.setVisibility(View.VISIBLE);
            tv_adress.setVisibility(View.VISIBLE);
            if (mMap != null)
            onAdressReady();
        }
        Destination first = array_dest.get(0);

        lt.setVisibility(View.VISIBLE);
        final TextView tv_num_pack = MainActivity.this.findViewById(R.id.num_of_packs);
        //Log.d(TAG,"eee7 " + MainActivity.this.findViewById(R.id.num_of_packs));
        int max = lt.getChildCount();
        for (int i=2;i<max;i++)
        {
            lt.removeViewAt(2);
        }
        tv_main.setText(first.getBusiness_name());
        tv_main.setTextSize(20);
        String begining = "";
        Log.d(TAG, "name: " + first.getBusiness_name());
        tv_adress.setVisibility(View.VISIBLE);
        hidden_text_array.clear();

        int prev_view_id = R.id.num_of_packs;
        final TextView info1 = new TextView(MainActivity.this);
//        info1.setLines(3);
        final TextView info2 = new TextView(MainActivity.this);
        final TextView info3 = new TextView(MainActivity.this);
        final TextView info4 = new TextView(MainActivity.this);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
             info1.setId(View.generateViewId());
             info2.setId(View.generateViewId());
             info3.setId(View.generateViewId());
             info4.setId(View.generateViewId());
        }
      //  Log.d(TAG,"eee5 " + MainActivity.this.findViewById(R.id.num_of_packs));
        if (first.getTo_costumer()) {
            begining = "כתובת לקוח: ";
            Button pickedup = findViewById(R.id.picked_the_delivery);
            pickedup.setText("מסרתי את המשלוח");
            ImageButton photo = findViewById(R.id.photo_butt);
            photo.setVisibility(View.VISIBLE);
            Button just_arrived = findViewById(R.id.just_arrived);
            just_arrived.setVisibility(View.INVISIBLE);
            //pickedup.setBackgroundResource(R.drawable.delivered);
            String costumer_name = first.getName_costumer();
            tv_adress.setText(begining + first.getAdressTo() + " (" + costumer_name + " )");
            tv_num_pack.setVisibility(View.GONE);
            info1.setVisibility(View.GONE);
            info1.setGravity(Gravity.START);
            info2.setVisibility(View.GONE);
            info2.setGravity(Gravity.START);
            info3.setVisibility(View.GONE);
            info3.setGravity(Gravity.START);
            info4.setVisibility(View.GONE);
            info4.setGravity(Gravity.START);
            String out3 = "";
            String out4 = "";
            String out= "";
            for (Delivery d : array) {
                if (d.getIndexString().equals(first.getDelivery_index())) {

                    if (!d.getFloor().equals(""))
                    {
                        out += "קומה:" + d.getFloor();
                        if (!d.getEntrance().equals(""))
                        {
                            out += ", כניסה:" + d.getEntrance();
                        }
                    }
                    else if (!d.getEntrance().equals(""))
                    {
                        out += "כניסה:" + d.getEntrance();
                    }
                    if (d.getIs_cash()) {
                        out3 = "התשלום במזומן";
                        if (!d.getPrice().equals("")) {
                            out3 = ": " + d.getPrice() + "ש''ח";
                        }
                    } else {
                        out3 = "התשלום באשראי";
                    }
                    if (!d.getIntercum_num().equals("")) {
                        out4 = "הקוד לאינרקום: " + d.getIntercum_num();
                    }

                    if (!out.equals(""))
                    {
                        info1.setText(out);
                    }
                    else
                    {
                        info1.setText("לא הוזנו קומה וכניסה");
                    }
                    if (!d.getComment().equals(""))
                    {
                        info2.setText("הערה:" + d.getComment());
                    }
                    else
                    {
                        info2.setText("ללא הערות");
                    }
                    info3.setText(out3);
                    if (!out4.equals(""))
                    {
                        info4.setText(out4);
                    }
                    else {
                        info4.setText("לא הוזן קודם לאינרקום");
                    }

                    break;
                }

            }
            final RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params1.addRule(RelativeLayout.BELOW,prev_view_id);
            final RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params2.addRule(RelativeLayout.BELOW,info1.getId());
            final RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params3.addRule(RelativeLayout.BELOW,info2.getId());
            Log.d(TAG,"r1 " +info1.getId() +" prev: " + prev_view_id + " r2: " + info2.getId());

            lt.addView(info1, params1);
            lt.addView(info2, params2);
            lt.addView(info3,params3);
            if (!out4.equals(""))
            {
                final RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params4.addRule(RelativeLayout.BELOW,info3.getId());
                lt.addView(info4,params4);

            }

        } else {
            Button pickedup = findViewById(R.id.picked_the_delivery);
            pickedup.setText("אספתי את המשלוח");
            ImageButton photo = findViewById(R.id.photo_butt);
            photo.setVisibility(View.INVISIBLE);
            Button just_arrived = findViewById(R.id.just_arrived);
            just_arrived.setVisibility(View.VISIBLE);
            if (first.getIs_from_another_adress())
            {
                begining = "כתובת אחרת לאיסוף: ";
            }
            else
            {
                begining = "כתובת מסעדה: ";
            }

            tv_adress.setText(begining + first.getAdressFrom());
            if (first.getMerged_indeces().size() > 0) {
                tv_num_pack.setText(first.getMerged_indeces().size() + " חבילות");
            } else {
                tv_num_pack.setText("חבילה אחת");
            }

            for (int i = 1; i < array_dest.size(); i++) {
                final TextView adressTo = new TextView(MainActivity.this);
                adressTo.setVisibility(View.GONE);
                adressTo.setGravity(Gravity.START);
                if (array_dest.get(i).getTo_costumer()) {
                    adressTo.setText(array_dest.get(i).getAdressTo());
                    adressTo.setTextColor(Color.GREEN);
                } else {
                    adressTo.setText(array_dest.get(i).getAdressFrom());
                    adressTo.setTextColor(Color.RED);

                }
                final RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params1.addRule(RelativeLayout.BELOW,prev_view_id);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    adressTo.setId(View.generateViewId());
                }
                lt.addView(adressTo, params1);
                prev_view_id = adressTo.getId();
                hidden_text_array.add(adressTo);

            }
        }

        //starts from second

        for (TextView adress : hidden_text_array) {
            adress.setVisibility(View.VISIBLE);
        }
        info1.setVisibility(View.VISIBLE);
        info2.setVisibility(View.VISIBLE);
        info3.setVisibility(View.VISIBLE);
        info4.setVisibility(View.VISIBLE);
//        final ImageView ib = findViewById(R.id.image);
//
//        second_on_click = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ib.setImageResource(R.drawable.baseline_keyboard_arrow_down_black_18dp);
//                for (TextView adress : hidden_text_array) {
//                    adress.setVisibility(View.GONE);
//                }
//                info1.setVisibility(View.GONE);
//                info2.setVisibility(View.GONE);
//                info3.setVisibility(View.GONE);
//                info4.setVisibility(View.GONE);
//                ib.setOnClickListener(first_on_click);
//            }
//        };
//        first_on_click = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ib.setImageResource(R.drawable.baseline_keyboard_arrow_up_black_18dp);
//                for (TextView adress : hidden_text_array) {
//                    adress.setVisibility(View.VISIBLE);
//                }
//                info1.setVisibility(View.VISIBLE);
//                info2.setVisibility(View.VISIBLE);
//                info3.setVisibility(View.VISIBLE);
//                info4.setVisibility(View.VISIBLE);
//
//                ib.setOnClickListener(second_on_click);
//            }
//        };
//        ib.setOnClickListener(first_on_click);


    }
    public String parse_number_to_int(String phone)
    {
        return phone.replaceFirst("0","972");
    }

    public void set_buttons() {

        ImageButton photo = findViewById(R.id.photo_butt);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "מצלמה לא עובדת כרגע",Toast.LENGTH_LONG ).show();
                   return;
// if (array_dest.isEmpty())
//                {
//                    Toast.makeText(MainActivity.this, "אין משלוחים פעילים כרגע",Toast.LENGTH_LONG ).show();
//                    return;
//                }
//                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            3);
//                }
//                else
//                {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                     file_photo = Uri.fromFile(getOutputMediaFile());
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, file_photo);
//
//                    startActivityForResult(intent, CAMERA_DONE);
//                }
            }
        });



        ImageButton whatsapp = findViewById(R.id.whatsapp_butt);
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = parse_number_to_int("0525641938");
                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + phone);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
//https://waze.com/ul?q=66%20Acacia%20Avenue
        //https://www.waze.com/ul?ll=32.7799656%2C32.7799656&navigate=yes&zoom=17
        ImageButton waze = findViewById(R.id.waze_butt);
        waze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("waze://?ll=32.7799656, 35.01872445&navigate=yes"));
//                PackageManager managerclock = getPackageManager();
//                i = managerclock.getLaunchIntentForPackage("com.waze");
//                i.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(i);
            }
        });

        Button pickedup = findViewById(R.id.picked_the_delivery);
        pickedup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (array_dest.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "אין משלוחים פעילים כרגע",Toast.LENGTH_LONG ).show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                final View myView = li.inflate(R.layout.dialog_on_pick_delivery, null);
                TextView tv = myView.findViewById(R.id.textView);
                final Boolean to_costumer = array_dest.get(0).getTo_costumer();
                if (to_costumer)
                {
                    tv.setText("האם אתה בטוח שמסרת את כל המשלוחים?");
                }
                else
                {
                    tv.setText("האם אתה בטוח כי אספת את כל המשלוחים?");
                }
                builder.setView(myView);
                builder.setTitle(array_dest.get(0).getBusiness_name());
                builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String timeStamp = new SimpleDateFormat("HH:mm").format(new Date());
                        Delivery current = find_delivery(array_dest.get(0));
                        if (current == null )
                        {
                            Toast.makeText(MainActivity.this, "Something wrong happend,Delivery not found", Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"Something wrong happend,Delivery not found");
                        }
                        else
                        {
                            if (to_costumer)
                            {
                                current.setTimeDeliver(timeStamp);
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Deliveries");
                                mDatabase.child(current.getKey()).child("timeDeliver").setValue(timeStamp);

                                remove_delivery(current.getIndexString());




                            }
                            else
                            {
                                current.setTimeTaken(timeStamp);
                                if (current.getTimeArriveToRestoraunt().equals(""))
                                {
                                    current.setTimeArriveToRestoraunt(timeStamp);
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Deliveries");
                                    mDatabase.child(current.getKey()).child("timeArriveToRestoraunt").setValue(timeStamp);
                                }
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Deliveries");
                                mDatabase.child(current.getKey()).child("timeTaken").setValue(timeStamp);

                            }

                        }
                        remove_route();



                        array_dest.remove(0);
                        //two in a row
                        if (!array_dest.isEmpty() && !array_dest.get(0).getTo_costumer() && !to_costumer)
                        {
                            displayDialog("ANOTHER_DELIVERY");
                        }
                        if (to_costumer)
                        {
                            this_delivery_guy.incNumOfDeliveries();
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Delivery_Guys");
                            mDatabase.child(this_delivery_guy.getIndex_string()).child("num_of_deliveries").setValue(this_delivery_guy.getNum_of_deliveries());
                        }
                        if (current != null)
                        {
                            try {
                                Date arrived =  new SimpleDateFormat("HH:mm").parse(current.getTimeArriveToRestoraunt());
                                Date time_taken = new SimpleDateFormat("HH:mm").parse(current.getTimeTaken());
                                Log.d(TAG,"taken: " + time_taken + " arrived: " + arrived);
                                long diff = time_taken.getTime() - arrived.getTime();
                                long min =  diff / (60 * 1000) % 60;
//                                Log.d(TAG,"diff:" + diff);
//                               // String minutes = new SimpleDateFormat("HH:mm").format(diff);
//                                Log.d(TAG,"min: " + minutes);
                           //     int minutes_int = Integer.valueOf(minutes);
                                if (min != 0)
                                {
                                    add_time_bonus((int)min);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }
                        setUI();
                    }
                });
                builder.setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                final AlertDialog dialog = builder.create();

                dialog.show();

            }
        });

        ImageButton call = findViewById(R.id.call_butt);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_PHONE_CALL);
                }
                Destination first = null;
                if (array_dest.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "אין משלוחים פעילים כרגע",Toast.LENGTH_LONG ).show();
                    return;
                }
                else
                {
                     first = array_dest.get(0);
                    for (Delivery d: array)
                    {
                        if (first.getDelivery_index().equals(d.getIndexString()))
                        {
                            restoraunt_phone = d.getRestoraunt_phone();
                            costumer_phone = d.getCostumer_phone();
                            break;
                        }
                    }
                }



                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                // Inflate the menu from xml
                popup.getMenuInflater().inflate(R.menu.popup_call, popup.getMenu());
                final Menu popupMenu = popup.getMenu();
                if (first != null) {
                    if (first.getTo_costumer()) {
                        popupMenu.findItem(R.id.two).setEnabled(true);
                    } else {
                        popupMenu.findItem(R.id.two).setEnabled(false);
                    }
                }
                else
                {
                    popupMenu.findItem(R.id.two).setEnabled(false);
                    popupMenu.findItem(R.id.one).setEnabled(false);
                }


                // Setup menu item selection
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            //restoraunt
                            case R.id.one:
                                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                                            REQUEST_PHONE_CALL);
                                }
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + restoraunt_phone ));
                                startActivity(intent);
                                return true;
                                //costumer
                            case R.id.two:
                                 intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + costumer_phone));
                                startActivity(intent);
                                return true;
                            case R.id.tree:
                                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0525641938"));
                                startActivity(intent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                // Handle dismissal with: popup.setOnDismissListener(...);
                // Show the menu
                popup.show();

            }
        });

        final Button just_arrived = findViewById(R.id.just_arrived);
        just_arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (array_dest.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "אין משלוחים פעילים כרגע",Toast.LENGTH_LONG ).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                final View myView = li.inflate(R.layout.dialog_on_pick_delivery, null);
                TextView tv = myView.findViewById(R.id.textView);
               final  Destination first = array_dest.get(0);


                tv.setText("הגעת למסעדה?");


                builder.setView(myView);
                builder.setTitle(array_dest.get(0).getBusiness_name());
                builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String timeStamp = new SimpleDateFormat("HH:mm").format(new Date());
                       Delivery current = find_delivery(array_dest.get(0));
                       if (current == null )
                       {
                           Toast.makeText(MainActivity.this, "Something wrong happend,Delivery not found", Toast.LENGTH_SHORT).show();
                           Log.d(TAG,"Something wrong happend,Delivery not found");
                       }
                       else
                       {
                           Log.d(TAG,"clicked on just_arrived + timestamp:" + timeStamp + "current: " + current.getKey());
                           current.setTimeArriveToRestoraunt(timeStamp);
                           DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Deliveries");
                           mDatabase.child(current.getKey()).child("timeArriveToRestoraunt").setValue(timeStamp);
                           just_arrived.setVisibility(View.INVISIBLE);
                       }

                    }
                });
                builder.setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                final AlertDialog dialog = builder.create();

                dialog.show();
            }
        });

    }

    void remove_delivery(final String index)
    {
        final ArrayList<Delivery> temp_arr = new ArrayList<>();
        Log.d(TAG,"remove_delivery: " + index);
        Query mDatabase = FirebaseDatabase.getInstance().getReference("Delivery_Guys").child(this_delivery_guy.getIndex_string()).child("deliveries");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Delivery temp = new Delivery(ds.getValue(Delivery.class));
                    Log.d(TAG,"remove_delivery  Delivery : key index: " + temp.getIndexString() + " while index: " + index);
                    if (!temp.getIndexString().equals(index) )
                    {
                        temp_arr.add(ds.getValue(Delivery.class));
                    }

//                    Log.d(TAG,"remove_delivery key index: " + ds.getKey() + " val: " + ds.getValue());
//                    DatabaseReference mDatabase =  FirebaseDatabase.getInstance().getReference("Delivery_Guys").child(this_delivery_guy.getIndex_string()).child("deliveries");
//                    mDatabase.child(ds.getKey()).removeValue();
                }
                DatabaseReference mDatabase =  FirebaseDatabase.getInstance().getReference("Delivery_Guys").child(this_delivery_guy.getIndex_string()).child("deliveries");
                 mDatabase.setValue(temp_arr);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
//will remove first route
    void remove_route()
    {
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Delivery_Guys");
//        mDatabase.child(this_delivery_guy.getIndex_string()).child("destinations").child(String.valueOf(index_to_remove)).removeValue();
//        Log.d(TAG,"removing route: " + array_dest.get(0).getBusiness_name() + " to_customer: " +  array_dest.get(0).getTo_costumer()
//                + " index: " + index_to_remove);
        final ArrayList<Destination> temp_arr = new ArrayList<>();
        Log.d(TAG,"remove_route: ");

        Query mDatabase = FirebaseDatabase.getInstance().getReference("Delivery_Guys").child(this_delivery_guy.getIndex_string()).child("destinations");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean is_first = true;
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    if(is_first)
                    {
                       is_first = false;
                    }
                    else
                    {
                        Destination temp = new Destination(ds.getValue(Destination.class));
                        temp_arr.add(temp);
                    }

                    Log.d(TAG,"remove_delivery key index: " + ds.getKey() + " val: " + ds.getValue());
                }
                DatabaseReference mDatabase =  FirebaseDatabase.getInstance().getReference("Delivery_Guys").child(this_delivery_guy.getIndex_string()).child("destinations");
                mDatabase.setValue(temp_arr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void displayDialog (String context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        final View myView = li.inflate(R.layout.dialog_on_pick_delivery, null);
        TextView tv = myView.findViewById(R.id.textView);
      switch (context)
        {
            case "ANOTHER_DELIVERY":
                tv.setText("שים לב כי הינך נדרש לאסוף עוד משלוח!");
                break;

        }
        builder.setView(myView);
        builder.setTitle(array_dest.get(0).getBusiness_name());
        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        final AlertDialog dialog = builder.create();

        dialog.show();
    }

    void add_time_bonus(int time)
    {
        for (Destination d : array_dest)
        {
            if (!d.getTo_costumer()){
                Delivery temp = find_delivery(d);
                if (temp != null)
                {
                    temp.setTime_bonus(time);
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Deliveries");
                    mDatabase.child(temp.getKey()).child("time_bonus").setValue(time);
                }
            }
        }
    }
    Delivery find_delivery(Destination d)
    {
        for (Delivery deliv : array)
        {
            if (deliv.getIndexString().equals(d.getDelivery_index()))
            {
                return deliv;
            }
        }
        return null;
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
    try {
    mMapView.onSaveInstanceState(mapViewBundle);
    }
    catch (NullPointerException e)
    {

    }

    }


    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    public String calculateFileSize(String filepath) {
        //String filepathstr=filepath.toString();
        File file = new File(filepath);
        float fileSizeInBytes = file.length();

        float fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        float fileSizeInMB = fileSizeInKB / 1024;

        String calString = Float.toString(fileSizeInMB);
        return calString;
    }
    Uri[] uri_arr = new Uri[4];
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_DONE) {
            if (resultCode == RESULT_OK) {
//                imageView.setImageURI(file);

            }
        }
        else if ( requestCode == PICK_IMAGE)
        {
//            Uri selectedImage = data.getData();
//            ImageView iv = findViewById(R.id.iv);
//            iv.setImageURI(selectedImage);
        }
        else if (requestCode == CAMERA_DONE_PROFILE)
        {
          // Log.d(TAG,"file:size: " + calculateFileSize(file_photo.toString()) + " path:L " + file_photo.toString());
            profile_image.setImageURI(file_photo);
            uri_arr[0] = file_photo;

        }
        else if (requestCode == PICK_IMAGE_PROFILE)
        {
            profile_image.setImageURI(data.getData());
            uri_arr[0] = data.getData();
        }
        else if (requestCode == CAMERA_DONE_DRIVER)
        {
            drivers_license.setImageURI(file_photo);
            uri_arr[1] = file_photo;
        }
        else if (requestCode == PICK_IMAGE_DRIVER)
        {
            drivers_license.setImageURI(data.getData());
            uri_arr[1] =  data.getData();
        }
        else if (requestCode == CAMERA_DONE_ID)
        {
            id_image.setImageURI(file_photo);
            uri_arr[2] = file_photo;
        }
        else if (requestCode == PICK_IMAGE_ID)
        {
            id_image.setImageURI(data.getData());
            uri_arr[2] = data.getData();
        }
        else if (requestCode == CAMERA_DONE_101)
        {
            form_101.setImageURI(file_photo);
            uri_arr[3] = file_photo;
        }
        else if (requestCode == PICK_IMAGE_101)
        {
            form_101.setImageURI(data.getData());
            uri_arr[3] = data.getData();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Please approve camera", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mMapView != null)
        mMapView.onStart();
//        if (googleApiClient != null) {
//            googleApiClient.connect();
//        }
    }

    @Override
    protected void onStop() {
 //       googleApiClient.disconnect();
        super.onStop();
        if (mMapView != null)
        mMapView.onStop();
     //   unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onPause() {
        if(mMapView != null)
            mMapView.onPause();
        super.onPause();

//        if (googleApiClient.isConnected()) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
//            googleApiClient.disconnect();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        if(mMapView != null)
        mMapView.onResume();
      //  this.registerReceiver(broadcastReceiver, new IntentFilter("MyApplication.DisplayLocation"));
//        if (!this_delivery_guy.getSent_start_shift_report())
//        {
//            Intent intent = new Intent(MainActivity.this, FillStartShiftActivity.class);
//            startActivity(intent);
//        }
    }

    String num_of_byke = "";
    String  error_info = "";
    String num_of_byke2;

    private void setNavigationDrawer() {
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = (NavigationView) findViewById(R.id.navigation); // initiate a Navigation View
// implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment frag = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id
// check selected menu item's id and replace a Fragment Accordingly
                //return to main
                if (itemId == R.id.nav_1) {
                    frag = null;
                    //deliveries oin road
                } else if (itemId == R.id.nav_2) {
                    frag = null;
                   //your route
                } else if (itemId == R.id.nav_3) {
                    frag = null;
                  //num of deliveries
                } else if (itemId == R.id.nav_4) {
                    frag = null;
                    //presence time
                } else if (itemId == R.id.nav_15) {
                    frag = null;
                    //broken motorcycle
                } else if (itemId == R.id.nav_5) {
                    frag = null;
                    //notification button
                } else if (itemId == R.id.nav_6) {
                    frag = null;
                    //gas station
                } else if (itemId == R.id.nav_7) {
                    frag = null;
                    //packages
                } else if (itemId == R.id.nav_8) {
                    frag = null;
                }
                 else if (itemId == R.id.nav_9)
                {
                    frag = null;
                }

// display a toast message with menu item's title

                Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                if (frag != null) {
//                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.container, frag); // replace a Fragment with Frame Layout
//                    //  transaction.addToBackStack(frag.getClass().getSimpleName()); //maybe null instead
//                    //  transaction.addToBackStack(deliveries_fragment.getClass().getSimpleName());
//                    transaction.remove(deliveries_fragment);
//                    //    transaction.addToBackStack(BACK_STACK_ROOT_TAG); //maybe null instead
//                    transaction.commit(); // commit the changes
//                    dLayout.closeDrawers(); // close the all open Drawer Views
                    return true;
                }
                else if (itemId == R.id.nav_1)
                {
                    Intent i=new Intent(getApplicationContext(), MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                //deliveries current
                else if (itemId == R.id.nav_2)
                {
                    if (array_dest == null ||  array_dest.isEmpty() || !array_dest.get(0).getTo_costumer())
                    {
                        Toast.makeText(MainActivity.this, "לא נמצאו כתובות", Toast.LENGTH_SHORT).show();
                        dLayout.closeDrawers();
                        return false;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    RelativeLayout rl = new RelativeLayout(MainActivity.this);
                    final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    rl.setLayoutParams(params);
                    int prev_id = -1;
                    for (final Destination d : array_dest)
                    {
                        if (!d.getTo_costumer())
                        {
                            break;
                        }
                        else
                        {
                            View v =   LayoutInflater.from(MainActivity.this).inflate(R.layout.current_deliveires_element, null);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                v.setId(View.generateViewId());
                            }
                            TextView adress = v.findViewById(R.id.adress);
                            adress.setText(d.getAdressTo());
                            ImageButton ib = v.findViewById(R.id.call);
                            ib.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                                                REQUEST_PHONE_CALL);
                                    }
                                    else
                                    {
                                        Delivery deliv = find_delivery(d);
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + deliv.getCostumer_phone() ));
                                        startActivity(intent);
                                    }
                                }
                            });
                            //first
                            if (prev_id == -1)
                            {
                                final RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                rl.addView(v,params1);
                                prev_id = v.getId();
                            }
                            else
                            {
                                final RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                params2.addRule(RelativeLayout.BELOW,prev_id);
                                rl.addView(v,params2);
                            }


                        }
                    }
                    //rl.addView(cdv);
                    builder.setView(rl);
                    builder.setPositiveButton("סיום", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    final AlertDialog dialog = builder.create();

                    dialog.show();
                    dLayout.closeDrawers();
                }
                else if (itemId == R.id.nav_3)
                {
                    if (array_dest == null ||  array_dest.isEmpty() || !array_dest.get(0).getTo_costumer())
                    {
                        Toast.makeText(MainActivity.this, "לא נמצאו כתובות", Toast.LENGTH_SHORT).show();
                        dLayout.closeDrawers();
                        return false;
                    }
                    LatLng origin = new LatLng(this_delivery_guy.getLatetude(),this_delivery_guy.getLongtitude());
                    LatLng dest = new LatLng(array_dest.get(0).getLatitude(),array_dest.get(0).getLongitude());
                    Log.d(TAG,"dest:" + dest);

                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);
                    Log.d(TAG,"url:" + url.toString());
                    FetchUrl FetchUrl = new FetchUrl();

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                    dLayout.closeDrawers();
                }
                else if (itemId == R.id.nav_4)
                {

                    Toast.makeText(MainActivity.this, "מספר המשלוחים שבוצעו:" + this_delivery_guy.getNum_of_deliveries() + " מספר החבילות שנמסרו:" +
                            this_delivery_guy.getNum_of_packages(), Toast.LENGTH_LONG).show();
                    dLayout.closeDrawers();
                }
                //broken motor
                else if (itemId == R.id.nav_5)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_broken_motorcycle, null);

                    EditText num_motor = v.findViewById(R.id.num_of_byke);
                    num_motor.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            num_of_byke = editable.toString();
                        }
                    });

                    EditText error_info_et = v.findViewById(R.id.error_info);
                    error_info_et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            error_info = editable.toString();
                        }
                    });
                    builder.setView(v);
                    builder.setPositiveButton("דווח", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "eror info: " + error_info + " num byke: " + num_of_byke, Toast.LENGTH_LONG).show();
                        }
                    });
                    final AlertDialog dialog = builder.create();

                    dialog.show();
//                    Toast.makeText(MainActivity.this, "מספר המשלוחים שבוצעו:" + this_delivery_guy.getNum_of_deliveries() + " מספר החבילות שנמסרו:" +
//                            this_delivery_guy.getNum_of_packages(), Toast.LENGTH_LONG).show();
                    dLayout.closeDrawers();
                }
                else if (itemId == R.id.nav_6)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_take_byke, null);

                   TextView tv = v.findViewById(R.id.textView2);
                   tv.setText( "הקטנוע שברשותי יאפשר לי אך ורק להגיע לביתי, ולחזור למחרת לעבודה." + "\n");
                   EditText et = v.findViewById(R.id.edit_num);
                    String num_of_byke;
                   et.addTextChangedListener(new TextWatcher() {
                       @Override
                       public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                       }

                       @Override
                       public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                       }

                       @Override
                       public void afterTextChanged(Editable editable) {
                           num_of_byke2 = editable.toString();
                       }
                   });
                    builder.setView(v);
                    builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "אישרת את התקנון", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("לא אישור", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "לא אישרת את התקנון", Toast.LENGTH_SHORT).show();
                        }
                    });
                    final AlertDialog dialog = builder.create();

                    dialog.show();
//                    Toast.makeText(MainActivity.this, "מספר המשלוחים שבוצעו:" + this_delivery_guy.getNum_of_deliveries() + " מספר החבילות שנמסרו:" +
//                            this_delivery_guy.getNum_of_packages(), Toast.LENGTH_LONG).show();
                    dLayout.closeDrawers();
                }
                else if (itemId == R.id.nav_7)
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_pick_info, null);

                    ImageButton camera = v.findViewById(R.id.camera);
                    ImageButton galery = v.findViewById(R.id.galery);
                    profile_image = v.findViewById(R.id.profile_image);
                    set_pick_info(camera,galery,CAMERA_DONE_PROFILE,PICK_IMAGE_PROFILE);

                    ImageButton camera2 = v.findViewById(R.id.camera2);
                    ImageButton galery2 = v.findViewById(R.id.galery2);
                    drivers_license = v.findViewById(R.id.profile_image2);
                    set_pick_info(camera2,galery2,CAMERA_DONE_DRIVER,PICK_IMAGE_DRIVER);

                    ImageButton camera3 = v.findViewById(R.id.camera3);
                    ImageButton galery3 = v.findViewById(R.id.galery3);
                    id_image = v.findViewById(R.id.profile_image3);
                    set_pick_info(camera3,galery3,CAMERA_DONE_ID,PICK_IMAGE_ID);

                    ImageButton camera4 = v.findViewById(R.id.camera4);
                    ImageButton galery4 = v.findViewById(R.id.galery4);
                    form_101 = v.findViewById(R.id.profile_image4);
                    set_pick_info(camera4,galery4,CAMERA_DONE_101,PICK_IMAGE_101);

                    EditText et = v.findViewById(R.id.license_exp);
                    et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            license_exp = editable.toString();
                        }
                    });





                    builder.setView(v);
                    builder.setPositiveButton("סיום", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            try {
                                Bitmap profile = null;
                                Bitmap drivers_license = null;
                                Bitmap id_image = null;
                                Bitmap form_101 = null ;
                                if (uri_arr[0] != null)
                                 profile = MediaStore.Images.Media.getBitmap(getContentResolver(),  uri_arr[0]);
                                if (uri_arr[1] != null)
                                 drivers_license = MediaStore.Images.Media.getBitmap(getContentResolver(),  uri_arr[1]);
                                if (uri_arr[2] != null)
                                id_image = MediaStore.Images.Media.getBitmap(getContentResolver(),  uri_arr[2]);
                                if (uri_arr[3] != null)
                                    form_101 = MediaStore.Images.Media.getBitmap(getContentResolver(),  uri_arr[3]);

                               final InfoAttached ia = new InfoAttached(this_delivery_guy.getIndex_string(),this_delivery_guy.getName(),profile,drivers_license,id_image,form_101,license_exp);
                                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("InfoAttached");
                                mDatabase.child(this_delivery_guy.getIndex_string()).child("admin_approved").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists())
                                        {
                                           Boolean admin_approved =(Boolean) dataSnapshot.getValue();
                                           if (admin_approved)
                                           {
                                               Toast.makeText(MainActivity.this, "לא ניתן לעדכן מידע",Toast.LENGTH_SHORT ).show();
                                               return;
                                           }
                                        }
                                        Log.d(TAG,"profile: " + ia.getProfile());
                                        mDatabase.child(this_delivery_guy.getIndex_string()).setValue(ia);
                                        Toast.makeText(MainActivity.this, "המידע עודכן",Toast.LENGTH_SHORT ).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    final AlertDialog dialog = builder.create();

                    dialog.show();

                    dLayout.closeDrawers();
                }
                else if (itemId == R.id.nav_15)
                {
                    Intent intent = new Intent(MainActivity.this, HistoryDeliveries.class);
                    startActivity(intent);
                    dLayout.closeDrawers();
                }
                else if (itemId == R.id.nav_8)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_send_shifts, null);
                    List<String> options = new ArrayList<String>();
                    options.add("ללא");
                    options.add("בוקר");
                    options.add("ערב");
                    options.add("כפולה");
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(), R.layout.spiner_item_modified, options);
                    final Spinner sunday = v.findViewById(R.id.sunday);
                    setOnClickSpinner(sunday,0);
                    sunday.setAdapter(adapter);
                    final  Spinner monday = v.findViewById(R.id.monday);
                    monday.setAdapter(adapter);
                    setOnClickSpinner(monday,1);
                    final Spinner tuesday = v.findViewById(R.id.tuesday);
                    tuesday.setAdapter(adapter);
                    setOnClickSpinner(tuesday,2);
                    final Spinner wednesday = v.findViewById(R.id.wednesday);
                    wednesday.setAdapter(adapter);
                    setOnClickSpinner(wednesday,3);
                    final Spinner thursday = v.findViewById(R.id.thursday);
                    thursday.setAdapter(adapter);
                    setOnClickSpinner(thursday,4);
                    final Spinner friday = v.findViewById(R.id.friday);
                    friday.setAdapter(adapter);
                    setOnClickSpinner(friday,5);
                    final Spinner saturday = v.findViewById(R.id.saturday);
                    saturday.setAdapter(adapter);
                    setOnClickSpinner(saturday,6);
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                    c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                    //next sunday
                    c.add(Calendar.DATE,7);
                    //last sunday/today
                    final String sunday_date =  df.format(c.getTime());
                    c.add(Calendar.DATE,1);
                    final String monday_date = df.format(c.getTime());
                    c.add(Calendar.DATE,1);
                    final String tuesday_date = df.format(c.getTime());
                    c.add(Calendar.DATE,1);
                    final String wednesday_date = df.format(c.getTime());
                    c.add(Calendar.DATE,1);
                    final String thursday_date = df.format(c.getTime());
                    c.add(Calendar.DATE,1);
                    final String friday_date = df.format(c.getTime());
                    c.add(Calendar.DATE,1);
                    final String saturday_date = df.format(c.getTime());

                    TextView sund = v.findViewById(R.id.text1);

                    sund.setText(sunday_date + " יום ראשון:");

                    TextView mond = v.findViewById(R.id.text2);

                    mond.setText(monday_date + " יום שני:");

                    TextView tues = v.findViewById(R.id.text3);

                    tues.setText(tuesday_date + " יום שלישי:");
                    TextView wedn = v.findViewById(R.id.text4);

                    wedn.setText(wednesday_date + " יום רביעי:");
                    TextView thurs = v.findViewById(R.id.text5);

                    thurs.setText(thursday_date + " יום חמישי:");
                    TextView frid = v.findViewById(R.id.text6);

                    frid.setText(friday_date + " יום שישי:");
                    TextView satu = v.findViewById(R.id.text7);

                    satu.setText(saturday_date + " יום שבת:");



                    builder.setView(v);
                    builder.setTitle("הגשת משמרות לשבוע הקרוב:" + sunday_date);
                    builder.setPositiveButton("שלח", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String dateToday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            int total_morning = 0;
                            int total_evening = 0;
                            for (DeliveryGuysShift.KindShift k : chosen_shifts)
                            {
                                switch (k)
                                {
                                    case NONE:
                                        break;
                                    case DOUBLE:
                                        total_evening++;
                                        total_morning++;
                                        break;
                                    case EVENING:
                                        total_evening++;
                                        break;
                                    case MORNING:
                                        total_morning++;
                                        break;
                                    default:
                                        break;
                                }
                            }
                            DeliveryGuysShift dgs = new DeliveryGuysShift(MainActivity.this_delivery_guy.getIndex(),MainActivity.this_delivery_guy.getIndex_string(),
                                    MainActivity.this_delivery_guy.getName(),dateToday,
                                    chosen_shifts[0],chosen_shifts[1],chosen_shifts[2],chosen_shifts[3],chosen_shifts[4],chosen_shifts[5],chosen_shifts[6],
                                    total_morning,total_evening,sunday_date,false);
                            DatabaseReference  mDatabase  = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Delivery_Guys_Shifts").child(MainActivity.this_delivery_guy.getIndex_string()).child(sunday_date)
                                    .setValue(dgs);
//                            mDatabase.child("Delivery_Guys_Shifts_Aproved").child(MainActivity.this_delivery_guy.getIndex_string()).child(sunday_date)
//                                    .setValue(dgs);
                            Toast.makeText(MainActivity.this, "משמרות נשלחו בהצלחה", Toast.LENGTH_SHORT).show();

                        }
                    });
                    builder.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    final AlertDialog dialog = builder.create();

                    dialog.show();
                    dLayout.closeDrawers();
                }
                else if (itemId == R.id.nav_9)
                {

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                    c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                    //next sunday
                    c.add(Calendar.DATE,7);
                    //last sunday/today
                    final String sunday_date =  df.format(c.getTime());
                  DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Delivery_Guys_Shifts_Aproved");
                  mDatabase.child(this_delivery_guy.getIndex_string()).child(sunday_date).addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          if (dataSnapshot.exists()) {
                              DeliveryGuysShift dgs = new DeliveryGuysShift(dataSnapshot.getValue(DeliveryGuysShift.class));
                              AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                              View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_send_shifts, null);
                              List<String> options = new ArrayList<String>();
                              options.add("ללא");
                              options.add("בוקר");
                              options.add("ערב");
                              options.add("כפולה");
                              ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(), R.layout.spiner_item_modified, options);
                              final Spinner sunday = v.findViewById(R.id.sunday);
                              //  setOnClickSpinner(sunday,0);
                              sunday.setClickable(false);
                              sunday.setEnabled(false);
                              sunday.setAdapter(adapter);
                              sunday.setSelection(DeliveryGuysShift.KindShift.valueOf(dgs.getSunday(),false));


                              final  Spinner monday = v.findViewById(R.id.monday);
                              monday.setClickable(false);
                              monday.setEnabled(false);
                              monday.setAdapter(adapter);
                              monday.setSelection(DeliveryGuysShift.KindShift.valueOf(dgs.getMonday(),false));

                              //    setOnClickSpinner(monday,1);
                              final Spinner tuesday = v.findViewById(R.id.tuesday);
                              tuesday.setClickable(false);
                              tuesday.setEnabled(false);
                              tuesday.setAdapter(adapter);
                              tuesday.setSelection(DeliveryGuysShift.KindShift.valueOf(dgs.getTuesday(),false));

                              //   setOnClickSpinner(tuesday,2);
                              final Spinner wednesday = v.findViewById(R.id.wednesday);
                              wednesday.setClickable(false);
                              wednesday.setEnabled(false);
                              wednesday.setAdapter(adapter);
                              wednesday.setSelection(DeliveryGuysShift.KindShift.valueOf(dgs.getWednesday(),false));

                              //   setOnClickSpinner(wednesday,3);
                              final Spinner thursday = v.findViewById(R.id.thursday);
                              thursday.setClickable(false);
                              thursday.setEnabled(false);
                              thursday.setAdapter(adapter);
                              thursday.setSelection(DeliveryGuysShift.KindShift.valueOf(dgs.getThursday(),false));

                              //   setOnClickSpinner(thursday,4);
                              final Spinner friday = v.findViewById(R.id.friday);
                              friday.setClickable(false);
                              friday.setEnabled(false);
                              friday.setAdapter(adapter);
                              friday.setSelection(DeliveryGuysShift.KindShift.valueOf(dgs.getFriday(),false));

                              //   setOnClickSpinner(friday,5);
                              final Spinner saturday = v.findViewById(R.id.saturday);
                              saturday.setClickable(false);
                              saturday.setEnabled(false);
                              saturday.setAdapter(adapter);
                              saturday.setSelection(DeliveryGuysShift.KindShift.valueOf(dgs.getSaturday(),false));

                              //  setOnClickSpinner(saturday,6);




                              builder.setView(v);
                              builder.setTitle("המשמרות שלך לשבוע הקרוב:");
                              builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialogInterface, int i) {

                                      Toast.makeText(MainActivity.this, "משמרות נשלחו בהצלחה", Toast.LENGTH_SHORT).show();

                                  }
                              });
                              final AlertDialog dialog = builder.create();

                              dialog.show();

                          }
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {

                      }
                  });
                    dLayout.closeDrawers();
                }



                return false;
            }
        });
    }
    public void setOnClickSpinner(Spinner s,final int day)
    {
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosen_shifts[day] = valueOf(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void set_pick_info(ImageView camera,ImageView galery,final int act_result_code_cam,final int act_result_code_gal)
    {
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file_photo = Uri.fromFile(getOutputMediaFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, file_photo);
                startActivityForResult(intent, act_result_code_cam);
            }
        });

        galery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, act_result_code_gal);
            }
        });

    }
    static Marker m;
    public void onAdressReady()
    {
        if (m != null)
        {
            m.remove();
        }

        Destination first = array_dest.get(0);
        LatLng  loc = new LatLng(first.getLatitude(),first.getLongitude());
        Log.d(TAG,"location: " + loc);
        MarkerOptions guy = new MarkerOptions();
        guy.position(loc);
        guy.title(first.getBusiness_name());
        //use m after
         m = mMap.addMarker(guy);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(10);
        mMap.setMaxZoomPreference(16);
        LatLng modiin = new LatLng(31.8903, 35.0104);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(modiin));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        mMapView.onResume();

        if (this_delivery_guy.getDestinations() != null && !this_delivery_guy.getDestinations().isEmpty()) {
            onAdressReady();
        }
        Log.d(TAG,"onMapReady");
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }

    public String getDirectionsUrl(LatLng origin, LatLng dest){

// Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

// Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

// Sensor enabled
        String sensor = "sensor=false";

        String mode = "mode=driving";

// Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor + "&" + mode;

// Output format
        String output = "json";

        String key = "&key=" + "AIzaSyDoRFQVaeh76RYJnlfQpCpuATe2xqmfcAI";

// Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+key;
        Log.d(TAG,"url: " + url);

        return url;
    }
}
