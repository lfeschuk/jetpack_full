package com.example.leonid.jetpack;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.leonid.jetpack.adapters.recycleAdapterConst;
import com.example.leonid.jetpack.adapters.recycleAdapterDeliveryGuys;
import com.example.leonid.jetpack.adapters.recycleAdapterRoutes;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Objects.DataBaseManager;
import Objects.Delivery;
import Objects.DeliveryGuys;
import Objects.Destination;
import Objects.DirectionsJSONParser;
import Objects.DistanceDuration;


public class PendingDeliveriesForGuyActivity extends AppCompatActivity implements recycleAdapterRoutes.ItemClickListener{
    public PendingDeliveriesForGuyActivity(){}
    private Toolbar toolbar;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    PendingDeliveriesForGuyActivity this_context = this;
    Delivery to_assign_delivery = null;
    DeliveryGuys chosen_delivery_guy = null;
    ArrayList<DistanceDuration> durations_list = new ArrayList<>();
    private RecyclerView recyclerView;
    final static String TAG = "PendingDeliverForGuy";
    private DataBaseManager dbm = new DataBaseManager();
    private ArrayList<Destination> array = new ArrayList<>();
    recycleAdapterRoutes adapter = new recycleAdapterRoutes(array, this_context);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_deliveries_activity);
        Log.d(TAG, "on intent");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        ImageView overlay = (ImageView) findViewById(R.id.overlay);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(new DragController(recyclerView, overlay, recycleAdapterConst.AdapterList.ROUTES));
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);

        Bundle b = getIntent().getExtras();
        final String delivery_key = b.getString("Delivery_Index");
        final String delivery_guy_key = b.getString("Delivery_Guy_Index");
        Log.d(TAG,"onCreate got from another intent Delivery_Index: " + delivery_key +" Delivery_Guy_index: " +delivery_guy_key);

        final Button assign_button = findViewById(R.id.button_assign_pending);
        final Button button_calculate_route_pending = findViewById(R.id.button_calculate_route_pending);
        Query q =  FirebaseDatabase.getInstance().getReference("Delivery_Guys").orderByChild("index_string").equalTo(delivery_guy_key);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Delivery> deliv_array = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    DeliveryGuys temp = new DeliveryGuys(ds.getValue(DeliveryGuys.class));
                    TextView title = findViewById(R.id.main_title_text);
                    title.setText( temp.getName());
                    deliv_array = temp.getDeliveries();
                    chosen_delivery_guy = temp;
                    Log.d(TAG,"DeliveryGuy is :  " + temp.getName());
                }
                //itetrate all over the guy deliveries and display them
                for(Delivery d : deliv_array)
                {
                    Destination to_business = new Destination(d,false);
                    Destination to_costumer = new Destination(d,true);
                    array.add(to_business);
                    array.add(to_costumer);
                }
                Log.d(TAG,"Done retrieving Destinations old " + array.size());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        Query q2 =  FirebaseDatabase.getInstance().getReference("Deliveries").orderByChild("indexString").equalTo(delivery_key);
        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Delivery temp = new Delivery(ds.getValue(Delivery.class));
                    //check if the clicked on delivery guy is already assigned to the delivery
                    if (temp.getDelivery_guy_index_assigned().equals(chosen_delivery_guy.getIndex_string()) == false)
                    {
                        array.add(new Destination(temp,false));
                        array.add(new Destination(temp,true));
                    }
                    to_assign_delivery = temp;
                    assign_button.setClickable(true);
                    button_calculate_route_pending.setClickable(true);
                    Log.d(TAG,"Delivery is :  " + temp.getIndexString());
                }
                Log.d(TAG,"Done retrieving Destinations new" + array.size());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        Log.d(TAG,"onCreate");

        //assign the button
        assign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onclick Status: " +to_assign_delivery.getStatus());
                if (to_assign_delivery.getStatus().equals("A"))
                {
                    to_assign_delivery.setStatus("B");
                }
                //its B
                else
                {
                    Log.d(TAG,"status B detected on assigning, removing from previous delivery guy");
                    Log.d(TAG,"DeliveryGuyPrev: " + to_assign_delivery.getDelivery_guy_index_assigned() +" DeliveryInd: " + to_assign_delivery.getIndexString());
                    dbm.remove_delivery_from_dguy(to_assign_delivery.getDelivery_guy_index_assigned(),to_assign_delivery.getIndexString());
                }

                to_assign_delivery.setDelivery_guy_index_assigned(chosen_delivery_guy.getIndex_string());
                to_assign_delivery.setDeliveryGuyName(chosen_delivery_guy.getName());
                to_assign_delivery.setDeliveryGuyPhone(chosen_delivery_guy.getPhone());
                chosen_delivery_guy.addDelivery(to_assign_delivery);
                Delivery to_write_deliv = new Delivery(to_assign_delivery);
                DeliveryGuys to_write_guy = new DeliveryGuys(chosen_delivery_guy);
                Log.d(TAG,"writing delivery " + to_assign_delivery.getIndexString() + " guy is " + to_assign_delivery.getDeliveryGuyName());
                dbm.writeDelivery(to_write_deliv);
                Log.d(TAG,"writing guy " + chosen_delivery_guy.getName());
                dbm.writeDeliveryGuy(to_write_guy);
                Intent intent = new Intent(PendingDeliveriesForGuyActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        button_calculate_route_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                durations_list.clear();
                Log.d(TAG,"on button_calculate_route_pending pressed");
                double iter_lat = chosen_delivery_guy.getLatetude();
                double iter_long = chosen_delivery_guy.getLongtitude();
                for (Destination d : array)
                {
                    DistanceDuration dd = new DistanceDuration();
                    LatLng source = new LatLng(iter_lat,iter_long);
                    LatLng dest = new LatLng(d.getLatitude(),d.getLongitude());
                    dd.start_duration_exec(source,dest,PendingDeliveriesForGuyActivity.this,array.size());
                    durations_list.add(dd);
                    iter_lat = d.getLatitude();
                    iter_long = d.getLongitude();
                }

            }
        });
    }
    public void updateUiCallback()
    {

        Calendar cal = Calendar.getInstance();
        Date curr_iter_date = cal.getTime();
        Log.d(TAG,"now is " + curr_iter_date.getHours() + ":" + curr_iter_date.getMinutes());
        Date new_date = new Date();
        for (int i=0;i<array.size();i++)
        {
            Log.d(TAG,"duration time:   " +(durations_list.get(i).getDuration()));
            Log.d(TAG,"curr iter time:   " + curr_iter_date.getTime() + " we want to add " + (durations_list.get(i).getDuration()*60*1000));
                new_date.setTime(curr_iter_date.getTime()+(durations_list.get(i).getDuration()*60*1000));
                String hour;
                String minutes;
                if (new_date.getHours() <10)
                {
                    hour = "0" + new_date.getHours();
                }
                else
                {
                    hour = "" + new_date.getHours();
                }
                if (new_date.getMinutes() <10)
                {
                    minutes = "0" + new_date.getMinutes();

                }
                else
                {
                    minutes = "" + new_date.getMinutes();
                }
                String date_deliver = hour + ":" + minutes;
            Log.d(TAG,"time is  " + date_deliver);
                array.get(i).setTimeDeliver(date_deliver);
                curr_iter_date = new_date;
        }
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void itemClicked(Destination d) {

    }

//
//    class ListAdapter extends ArrayAdapter<Destination> {
//        ListAdapter() {
//            super(PendingDeliveriesForGuyActivity.this, R.layout.adapter_layout_delivery_routes, array);
//        }
//        public View getView(int position, View convertView,
//                            ViewGroup parent) {
//            View row=convertView;
//            if (row==null) {
//                LayoutInflater inflater=getLayoutInflater();
//                row=inflater.inflate(R.layout.adapter_layout_delivery_routes, parent, false);
//            }
//            TextView index_delivery = row.findViewById(R.id.index_delivery_routes);
//            index_delivery.setText(array.get(position).getIndex_string());
//            TextView time_of_order = row.findViewById(R.id.time_of_order_routes);
//            time_of_order.setText(array.get(position).getTimeInserted());
//            TextView time_to_destination = row.findViewById(R.id.time_to_destination_routes);
//            time_to_destination.setText(array.get(position).getTimeDeliver());
//            TextView addresses = row.findViewById(R.id.addresses_routes);
//            TextView from_where_the_delivery = row.findViewById(R.id.from_where_the_delivery_routes);
//            if (array.get(position).getTo_costumer())
//            {
//                String adress_or_cost_name = "(" + array.get(position).getName_costumer() + ")";
//                addresses.setText(array.get(position).getAdressTo() + adress_or_cost_name);
//                from_where_the_delivery.setText("משלוח מ:" + array.get(position).getBusiness_name());
//                from_where_the_delivery.setVisibility(View.VISIBLE);
//
//
//            }
//            else
//            {
//                String adress_or_cost_name = "(" + array.get(position).getAdressFrom() + ")";
//                addresses.setText(array.get(position).getBusiness_name() + adress_or_cost_name);
//            }
//
//            return(row);
//        }
//    }

}
