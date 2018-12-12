package com.example.leonid.jetpack.restoraunt;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonid.jetpack.restoraunt.Service.SoundService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Objects.Costumer;
import Objects.DataBaseManager;
import Objects.DelayedDelivery;
import Objects.Delivery;
import Objects.Restoraunt;

public class MainActivity extends AppCompatActivity {
    DrawerLayout dLayout;
  //  private Toolbar toolbar;
  //  public static UserAdmin current_user;
    private static TabLayout tabLayout;
    private static ViewPager viewPager;
   // public static  ViewPagerAdapter adapter;
    Fragment deliveries_fragment;
    Fragment delivery_guys_fragment;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    public static final String TAG = "MainActivity";
    public static String email_login = "val25@live.com";
    public static Restoraunt this_restoraunt = null;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
   public static MediaPlayer  player ;
    DataBaseManager dbm = new DataBaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG,"send to Db");
      //  toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setNavigationDrawer();
//        String address, String comment, String phone, String city, String floor, String entrance, String building, String street, String apartment,
//        double source_cord_lat, double source_cord_long ,String name
//        Costumer c = new Costumer("this is comment","0525410912","מודיעין","7","1","4","עמק  החולה",
//                "78",31.913139,35.006592, "יוסי","","");
//        dbm.writeCostumer(c);
//        Costumer c2 = new Costumer("this is comment 2","0525410912","מודיעין","2","1","18","עמק  זבולון",
//                "5",31.913139,35.006592, "יוסי","","");
//        dbm.writeCostumer(c2);

   //     Alerts.register(this);
        //Alerts.displayError(getApplicationContext(), "test Error");

//        player.setLooping(true); // Set looping
//        player.setVolume(100,100);
        Alerts.register(this);

     //   startService(new Intent(MainActivity.this, SoundService.class));
        mDatabase =  FirebaseDatabase.getInstance().getReference("Restoraunt");
        mDatabase.orderByChild("email").equalTo(email_login).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        Restoraunt temp = new Restoraunt(ds.getValue(Restoraunt.class));
                        this_restoraunt = temp;
                     //   Log.d(TAG,"ttt");
                        final Query mDatabase_ = FirebaseDatabase.getInstance().getReference("Deliveries");
                        mDatabase_.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                Log.d(TAG,"bbb");
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                Log.d(TAG, "s: " + dataSnapshot.getValue());
                                if (dataSnapshot.child("just_assigned_deliv").getValue(Boolean.class).equals(true) &&
                                        dataSnapshot.child("restoraunt_key").getValue(String.class).equals(MainActivity.this_restoraunt.getKey())) {

                                    Log.d(TAG, "child changed: " + dataSnapshot.child("indexString").getValue(String.class));
                                    Alerts.displayDialog(getApplicationContext(), dataSnapshot.child("indexString").getValue(String.class));

                                    Delivery d = dataSnapshot.getValue(Delivery.class);
                                    DatabaseReference mDatab = FirebaseDatabase.getInstance().getReference("Deliveries");
                                    mDatab.child(d.getKey()).child("just_assigned_deliv").setValue(false);


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


//                     Query mDatabase_ = FirebaseDatabase.getInstance().getReference("Deliveries").orderByChild("restoraunt_key").equalTo(MainActivity.this_restoraunt.getKey());
//                        mDatabase_.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                //Log.d(TAG, "fff");
//                                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                                    Delivery temp = new Delivery(ds.getValue(Delivery.class));
//                                    if (temp.getStatus().equals("B")) {
//                                       // Log.d(TAG, "kkkk");
//                                        Alerts.displayDialog(getApplicationContext(),temp.getIndexString());
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                        Log.d(TAG,"key is " +this_restoraunt.getKey());
//                        Log.d(TAG,"key is " +temp.getKey());
//                        Log.d(TAG,"key is " +temp);
//                        Log.d(TAG,"key is " +temp.getName());
                    }

//                    Delivery d = new Delivery();
//                    d.setStatus("D");
//                    d.setRestoraunt_key(this_restoraunt.getKey());
//                    d.setPrice("30");
//                    d.setCostumerName("יוסי כהן");
//                    d.setCostumer_phone("0525410912");
//                    d.setIs_cash(true);
//                    d.setCity("מודיעין");
//                    d.setStreet("עמק האלה");
//                    d.setBuilding("51");
//                    d.setApartment("6");
//                    d.setIndexString("-1");
//                    dbm.writeDelivery(d);
//
//                    dbm.writeDelivery(d);
//                    d.setPrice("49");
//                    d.setCostumerName("יוסי כהן");
//                    d.setCostumer_phone("0525410912");
//                    d.setIs_cash(false);
//                    d.setCity("מודיעין");
//                    d.setStreet("עמק האלה");
//                    d.setBuilding("51");
//                    d.setApartment("6");
//                    dbm.writeDelivery(d);
//                    d.setPrice("105");
//                    d.setCostumerName("יוסי");
//                    d.setCostumer_phone("0524560912");
//                    d.setIs_cash(true);
//                    d.setCity("מודיעין");
//                    d.setStreet("עמק האלה");
//                    d.setBuilding("51");
//                    d.setApartment("7");
//                    dbm.writeDelivery(d);
//                    d.setPrice("108.5");
//                    d.setCostumerName("יוסי");
//                    d.setCostumer_phone("0524560912");
//                    d.setIs_cash(false);
//                    d.setCity("מודיעין");
//                    d.setStreet("עמק האלה");
//                    d.setBuilding("51");
//                    d.setApartment("8");
//                    dbm.writeDelivery(d);
//                    d.setPrice("13.58");
//                    d.setCostumerName("יוסי");
//                    d.setCostumer_phone("0524560912");
//                    d.setIs_cash(false);
//                    d.setCity("מודיעין");
//                    d.setStreet("עמק האל");
//                    d.setBuilding("51");
//                    d.setApartment("8");
//                    dbm.writeDelivery(d);
//                    d.setPrice("13.58");
//                    d.setCostumerName("דוד עובידה");
//                    d.setCostumer_phone("0524560912");
//                    d.setIs_cash(true);
//                    d.setCity("מודיעין");
//                    d.setStreet("עמק האל");
//                    d.setBuilding("51");
//                    d.setApartment("8");
//                    dbm.writeDelivery(d);
//                    d.setPrice("13.58");
//                    d.setCostumerName("דוד יוסי");
//                    d.setCostumer_phone("05245421242");
//                    d.setIs_cash(true);
//                    d.setCity("מודיעין");
//                    d.setStreet("חננאל");
//                    d.setBuilding("1");
//                    d.setApartment("3");
//                    dbm.writeDelivery(d);

//                    Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
//                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "משהוא רע קרה", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Alerts.register(this);

    }

    @Override
    protected void onPause()
    {

        Alerts.unregister(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Alerts.unregister(this);
        super.onStop();
    }
    //        Log.d(TAG,"got  here");
//            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//         final   Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//        // r.play();
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        LayoutInflater li = LayoutInflater.from(MainActivity.this);
//        final View myView = li.inflate(R.layout.found_delivery_guy_dialog, null);
//
//        TextView index_text = myView.findViewById(R.id.textViewId);
//        index_text.setText("#" + index);
//        final AlertDialog dialog = builder.create();
//        Button b = myView.findViewById(R.id.button2);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              //  r.stop();
//                dialog.cancel();
//            }
//        });
//        builder.setView(myView);
//
//
//        Log.d(TAG,"got  here2");
//        dialog.show();
//    }
//    catch (Exception e) {
//    e.printStackTrace();
//    }
//}
//    private void setupViewPager(ViewPager viewPager) {
//        adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        deliveries_fragment = new FragmentDeliveries();
//        delivery_guys_fragment = new FragmentDeliveryGuys();
//
//        adapter.addFragment(deliveries_fragment, "משלוחים");
//        adapter.addFragment(delivery_guys_fragment, "שליחים");
//        viewPager.setAdapter(adapter);
//    }

//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }

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
                //insert delivery
                if (itemId == R.id.nav_1) {
                    frag = null;
                    //deliveries oin road
                } else if (itemId == R.id.nav_2) {
                    frag = null;
                    Log.d(TAG, "maps checked");
                    //costumer list
                } else if (itemId == R.id.nav_3) {
                    frag = null;
                    //statistics
                } else if (itemId == R.id.nav_4) {
                    frag = null;
                }
                else if (itemId == R.id.nav_5) {
                    frag = null;
                }
                else if (itemId == R.id.nav_6) {
                    frag = null;
                }
                    //presence time
//                } else if (itemId == R.id.nav_9) {
//                    frag = new ThirdFragment();
//                } else if (itemId == R.id.nav_10) {
//                    frag = new ThirdFragment();
//                    //shifts
//                } else if (itemId == R.id.nav_11) {
//                    frag = null;
//                } else if (itemId == R.id.nav_12) {
//                    frag = new ThirdFragment();
//                } else if (itemId == R.id.nav_13) {
//                    frag = new ThirdFragment();
//                } else if (itemId == R.id.nav_14) {
//                    frag = new ThirdFragment();
//                }
// display a toast message with menu item's title

                Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                    if (frag != null) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, frag); // replace a Fragment with Frame Layout
                        //  transaction.addToBackStack(frag.getClass().getSimpleName()); //maybe null instead
                        //  transaction.addToBackStack(deliveries_fragment.getClass().getSimpleName());
                        transaction.remove(deliveries_fragment);
                        //    transaction.addToBackStack(BACK_STACK_ROOT_TAG); //maybe null instead
                        transaction.commit(); // commit the changes
                        dLayout.closeDrawers(); // close the all open Drawer Views
                        return true;
                    }
                    else if (itemId == R.id.nav_1)
                    {
                        dLayout.closeDrawers(); // close the all open Drawer Views
                        Intent intent = new Intent(MainActivity.this, InsertDeliveryActivity.class);
                        Bundle b = new Bundle();
                        b.putBoolean("anotherAdress",false);
                        intent.putExtras(b); //Put your id to your
                        startActivity(intent);

                    }
                    //delayed
                    else if (itemId == R.id.nav_2)
                    {
                        dLayout.closeDrawers(); // close the all open Drawer Views
                        Intent intent = new Intent(MainActivity.this, DelayedDeliveryActivity.class);
                        startActivity(intent);
                    }
                    //another_adress
                    else if (itemId == R.id.nav_3)
                    {
                        dLayout.closeDrawers(); // close the all open Drawer Views
                        Intent intent = new Intent(MainActivity.this, InsertDeliveryActivity.class);
                        Bundle b = new Bundle();
                        b.putBoolean("anotherAdress",true);
                        intent.putExtras(b); //Put your id to your
                        startActivity(intent);
                    }
                    else if (itemId == R.id.nav_4)
                    {
                        dLayout.closeDrawers(); // close the all open Drawer Views
                        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);

                        startActivity(intent);
                    }
                    else if (itemId == R.id.nav_5)
                    {
                        dLayout.closeDrawers(); // close the all open Drawer Views
                        Intent intent = new Intent(MainActivity.this, CostumerListActivity.class);
                        startActivity(intent);
                    }
                    else if (itemId == R.id.nav_6)
                    {
                        dLayout.closeDrawers(); // close the all open Drawer Views
                        Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
                        startActivity(intent);
                    }

//

                    return false;
                }
        });
    }
}
