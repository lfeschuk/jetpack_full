package com.example.leonid.jetpack.restoraunt;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonid.jetpack.restoraunt.adapters.recycleAdapterDeliveries;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import Objects.Delivery;

public class DelayedDeliveryActivity extends AppCompatActivity implements recycleAdapterDeliveries.ItemClickListener{
    public String costumer_phone = null;
    public DelayedDeliveryActivity(){}
    DelayedDeliveryActivity this_context = this;
    private Toolbar toolbar;
    private ArrayList<Delivery> array = new ArrayList<>();
    private Query mDatabase = FirebaseDatabase.getInstance().getReference();
    //    PendingDeliveriesForGuyActivity this_context = this;
//    Delivery to_assign_delivery = null;
//    DeliveryGuys chosen_delivery_guy = null;
    //  ArrayList<DistanceDuration> durations_list = new ArrayList<>();
    private RecyclerView recyclerView;
    public static recycleAdapterDeliveries adapter;
    final static String TAG = "DelayedDeliveryActivity";

    //  private DataBaseManager dbm = new DataBaseManager();
    //  private ArrayList<Destination> array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delayed_delivery_activity);

        Log.d(TAG,"send to Db");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);

        TextView title = findViewById(R.id.main_title_text);
        title.setText("משלוחים דחויים");

        adapter = new recycleAdapterDeliveries(array, this_context);
        recyclerView.setAdapter(adapter);
        mDatabase =  FirebaseDatabase.getInstance().getReference("Delayed_Delivery").orderByChild("restoraunt_key").equalTo(MainActivity.this_restoraunt.getKey());
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Delivery temp = new Delivery(dataSnapshot.getValue(Delivery.class));
                Log.d(TAG,"fff:" + temp.getIndexString() + "key " + temp.getKey());
                array.add(temp);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Delivery temp = new Delivery(dataSnapshot.getValue(Delivery.class));
                Log.d(TAG,"onchiled changed:" + temp.getIndexString() + "key " + temp.getKey());
                add_element_to_array(temp);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                //todo

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        set_ui();
    }

    public static void notifyArrayChanged()
    {
        if (adapter!=null)
        {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG,"register alerts");
        Alerts.register(this);

    }

    @Override
    protected void onPause()
    {
        Log.d(TAG,"unregister alerts");
        Alerts.unregister(this);
        super.onPause();
    }
    public void add_element_to_array(Delivery temp)
    {

        for(int i=0;i<array.size();i++)
        {
            //    Log.d(TAG,"index old : "  + array.get(i).getIndexString() + " index enw:"  + temp.getIndexString() + " old key: " +array.get(i).getKey() + " new key: " + temp.getKey() );
            if (array.get(i).getIndexString().equals(temp.getIndexString()) || array.get(i).getIndexString().equals("-1") && array.get(i).getKey().equals("") && !temp.getKey().equals(""))
            {
                // Log.d(TAG,"index: " + temp.getIndexString());
                array.set(i,temp);
                return;
            }
        }


    }


    public void set_ui()
    {
        Button b = findViewById(R.id.button_assign_pending);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG,"set_ui on button click");
                set_button_insert_delivery(true);
            }
        });
//        Button b2 = findViewById(R.id.button_assign_from_other_address);
//        b2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                set_button_insert_delivery(false);
//            }
//        });
    }
    public void set_button_insert_delivery(final Boolean from_restoraunt)
    {
        Log.d(TAG,"set_button_insert_delivery");
        AlertDialog.Builder builder =  new AlertDialog.Builder(DelayedDeliveryActivity.this);
        LayoutInflater li = LayoutInflater.from(DelayedDeliveryActivity.this);
        final View myView = li.inflate(R.layout.insert_delivery_dialog, null);
        //    final Dialog dialog = new Dialog(context,R.style.Dialog);
        EditText et = myView.findViewById(R.id.edit_text_insert_phone);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                costumer_phone = editable.toString();
            }
        });
        builder.setView(myView);
//                builder.setTitle(shift.getName());
        builder.setNeutralButton("ביטול", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("חפש", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (costumer_phone == null)
                {
                    Toast.makeText(DelayedDeliveryActivity.this, "הכנס בבקשה מספר טלפון" ,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(DelayedDeliveryActivity.this, "costumer phone: " + costumer_phone, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DelayedDeliveryActivity.this, DeliveryDataActivity.class);
                    Bundle b = new Bundle();
                    b.putString("phone",costumer_phone);
                    b.putBoolean("is_delayed",true);
                    intent.putExtras(b); //Put your id to your next Intent
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });
        final  AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    public void itemClicked(Delivery delivery) {

    }
}
