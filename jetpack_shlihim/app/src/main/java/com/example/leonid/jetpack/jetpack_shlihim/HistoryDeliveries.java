package com.example.leonid.jetpack.jetpack_shlihim;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonid.jetpack.jetpack_shlihim.Interfaces.MyDialogCloseListener;
import com.example.leonid.jetpack.jetpack_shlihim.Objects.Delivery;
import com.example.leonid.jetpack.jetpack_shlihim.UIClasses.MyDatePickerFragment;
import com.example.leonid.jetpack.jetpack_shlihim.adapters.recycleAdapterDeliveries;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryDeliveries extends AppCompatActivity implements recycleAdapterDeliveries.ItemClickListener,MyDialogCloseListener {
    private Toolbar toolbar;
    HistoryDeliveries this_context = this;
    String date_from = null;
    String date_to = null;
    ImageButton date;
    TextView display_date;
    Boolean click_delivery = false;
    Boolean clicked_package = false;


    private ArrayList<Delivery> array = new ArrayList<>();
    public static final String TAG = "SortDeliveriesActivity";
    private RecyclerView recyclerView;
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"on create start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
        Log.d(TAG,"on intent");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
//        Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.horizontal_divider);
//        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);
        date = findViewById(R.id.calendar);
        display_date = findViewById(R.id.text_id1);
        set_other_UI();



        TextView title = findViewById(R.id.title);
        title.setText("היסטוריית משלוחים");
        Log.d(TAG,"after intent");





    }


    public void set_other_UI()
    {

        final Button b = findViewById(R.id.search);
        final ImageButton deliv = findViewById(R.id.pizza);
        deliv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click_delivery)
                {
                    deliv.setBackgroundResource(0);
                    click_delivery = false;
                }
                else
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        deliv.setBackground(getDrawable(R.drawable.my_button_bg));
                    }
                    click_delivery = true;
                }

            }
        });

        final ImageButton pack = findViewById(R.id.box);
        pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked_package)
                {
                    pack.setBackgroundResource(0);
                    clicked_package = false;
                }
                else
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        pack.setBackground(getDrawable(R.drawable.my_button_bg));
                    }
                    clicked_package = true;
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(true);
            }
        });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                do_database_query_and_display();
            }
        });
    }
    public void showDatePicker(Boolean first_date ) {
        MyDatePickerFragment newFragment = new MyDatePickerFragment();
        Bundle args = new Bundle();
        args.putBoolean("is_first", first_date);
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "date picker");
    }

    public void do_database_query_and_display()
    {
        Query mDatabase = FirebaseDatabase.getInstance().getReference("Deliveries").orderByChild("delivery_guy_index_assigned").equalTo(MainActivity.this_delivery_guy.getIndex_string());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                array.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Delivery temp = new Delivery(ds.getValue(Delivery.class));
                    if(!temp.getStatus().equals("D"))
                    {
                        continue;
                    }
                    //todo add if package or delivery
                    else if((date_to != null && temp.getDate().compareTo(date_from) >= 0 && temp.getDate().compareTo(date_to) <= 0)
                            )
                    {
                        array.add(temp);
                    }
                    else
                    {
                        Log.d(TAG,"error in do_database_query_and_display :  " + temp);
                    }
                }
                Log.d(TAG,"Done retrieving Deliveries " + array.size());
                if (array.isEmpty())
                {
                    Toast.makeText(HistoryDeliveries.this, "לא נמצאו משלוחים", Toast.LENGTH_LONG).show();
                }
                recycleAdapterDeliveries adapter = new recycleAdapterDeliveries(array, this_context);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void itemClicked(Delivery delivery) {

    }

    @Override
    public void handleDialogClose(Boolean is_first,String d) {
        if(d != null)
        {
            if ( is_first)
            {
                date_from = d;
                showDatePicker(false);
            }
            else
            {
                date_to = d;
                display_date.setText( "התאריכים שנבחרו: " + date_from + " - " + date_to);
                display_date.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    date.setBackground(getDrawable(R.drawable.my_button_bg));

                }

            }
        }
    }


}
