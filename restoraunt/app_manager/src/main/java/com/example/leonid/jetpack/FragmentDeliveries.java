package com.example.leonid.jetpack;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.leonid.jetpack.adapters.recycleAdapterConst;
import com.example.leonid.jetpack.adapters.recycleAdapterDeliveries;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Objects.DataBaseManager;
import Objects.Delivery;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;


public class FragmentDeliveries extends Fragment implements recycleAdapterDeliveries.ItemClickListener{

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//    private ListAdapter adapter=null;
    final static String TAG = "FragmentDeliveries";
    private DataBaseManager dbm = new DataBaseManager();
    private ArrayList<Delivery> array = new ArrayList<>();
    private RecyclerView recyclerView;
    private  CoordinatorLayout cl;
    private FloatingActionButton fab;
    Boolean is_show_a = true;
    Boolean is_show_b = true;
    Boolean is_show_c = true;
    Boolean is_show_d = false;
    Boolean is_show_changed = false;
    FragmentDeliveries this_fragment = this;
    public FragmentDeliveries(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        LinearLayout wrapper = new LinearLayout(getActivity()); // for example
        View fragment_view =  inflater.inflate(R.layout.fragment_recycle_list, wrapper, true);

        recyclerView = (RecyclerView) fragment_view.findViewById(R.id.list);
        ImageView overlay = (ImageView) fragment_view.findViewById(R.id.overlay);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(new DragController(recyclerView, overlay, recycleAdapterConst.AdapterList.DELIVERIES));
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);
        cl = fragment_view.findViewById(R.id.recycle_list_coordinator);
         fab = fragment_view.findViewById(R.id.fab);
        set_fab();


        mDatabase =  FirebaseDatabase.getInstance().getReference("Deliveries");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                array.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Delivery temp = new Delivery(ds.getValue(Delivery.class));
                    if (!temp.getIs_gas_sta())
                    {
                        array.add(temp);
                    }

                    Log.d(TAG,"Delivery is :  " + temp);
                }
                MainActivity.set_title_for_adapter(0,array.size());
                Log.d(TAG,"Done retrieving Deliveries " + array.size());
                display_data();
               // tlv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return fragment_view;

    }

 public void set_fab()
 {
     fab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             final Dialog dialog = new Dialog(getActivity(),R.style.Theme_Dialog);
             // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

             //  LayoutInflater li = LayoutInflater.from(getActivity());
             //    final View myView = li.inflate(R.layout.checkbox_sort_deliveries, null);
             dialog.setContentView(R.layout.checkbox_sort_deliveries);
             dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//                builder.setView(myView);
//                builder.setNeutralButton("סיום", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
             //  AlertDialog dialog = builder.create();
             //       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
             Button b = dialog.findViewById(R.id.button_end);
             b.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     if (is_show_changed)
                     {
                         display_data();
                     }
                     dialog.dismiss();
                     fab.setVisibility(View.VISIBLE);
                 }
             });
             CheckBox sort_by_a = dialog.findViewById(R.id.checkbox_sort_a);
             sort_by_a.setChecked(is_show_a);
             sort_by_a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                     is_show_a = b;
                     is_show_changed = true;
                 }
             });
             CheckBox sort_by_b = dialog.findViewById(R.id.checkbox_sort_b);
             sort_by_b.setChecked(is_show_b);
             sort_by_b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                     is_show_b = b;
                     is_show_changed = true;
                 }
             });
             CheckBox sort_by_c = dialog.findViewById(R.id.checkbox_sort_c);
             sort_by_c.setChecked(is_show_c);
             sort_by_c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                     is_show_c = b;
                     is_show_changed = true;
                 }
             });
             CheckBox sort_by_d = dialog.findViewById(R.id.checkbox_sort_d);
             sort_by_d.setChecked(is_show_d);
             sort_by_d.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                     is_show_d = b;
                     is_show_changed = true;
                 }
             });


             WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();


             wmlp.gravity = Gravity.BOTTOM | Gravity.RIGHT;


             fab.setVisibility(View.GONE);
             dialog.show();
             //  dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
         }
     });

 }
 public void display_data()
 {
     ArrayList<Delivery> to_display = null;
     if (is_show_a && is_show_b && is_show_c)
     {
        to_display = array;
     }
     else
     {
         to_display = new ArrayList<>();
         for (Delivery d: array)
         {
             if ((d.getStatus().equals("A") && is_show_a) || (d.getStatus().equals("B") && is_show_b) || (d.getStatus().equals("C") && is_show_c) || (d.getStatus().equals("D") && is_show_d))
             {
                 to_display.add(d);
             }
         }
     }
     recycleAdapterDeliveries adapter = new recycleAdapterDeliveries(to_display, this_fragment);
     recyclerView.setAdapter(adapter);
 }

    @Override
    public void itemClicked(Delivery d) {
        Toast.makeText(recyclerView.getContext(),"hhh", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onTouch");
        Toast.makeText(getActivity(), "Touch delivery guy: " + d.getIndexString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), DeliveryDataActivity.class);
        Bundle b = new Bundle();
        Log.d(TAG,"passing index string " + d.getIndexString());
        b.putString("Delivery_Index",d.getIndexString());
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }


}
