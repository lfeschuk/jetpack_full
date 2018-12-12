package com.example.leonid.jetpack;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.leonid.jetpack.adapters.recycleAdapterDeliveries;
import com.example.leonid.jetpack.adapters.recycleAdapterDeliveryGuys;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Objects.DataBaseManager;
import Objects.DeliveryGuys;

public class FragmentDeliveryGuys extends Fragment implements recycleAdapterDeliveryGuys.ItemClickListener {
    public FragmentDeliveryGuys(){}
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private RecyclerView recyclerView;
    FragmentDeliveryGuys this_fragment = this;
    final static String TAG = "FragmentDeliveriesGuy";
    private DataBaseManager dbm = new DataBaseManager();
    private ArrayList<DeliveryGuys> array = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout wrapper = new LinearLayout(getActivity()); // for example
        View fragment_view =  inflater.inflate(R.layout.fragment_recycle_list, wrapper, true);

        recyclerView = (RecyclerView) fragment_view.findViewById(R.id.list);
        ImageView overlay = (ImageView) fragment_view.findViewById(R.id.overlay);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);
//        MainActivity.fab.setVisibility(View.GONE);
        FloatingActionButton  fab = fragment_view.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        //no need
      //  recyclerView.addOnItemTouchListener(new DragController(recyclerView, overlay));

        mDatabase =  FirebaseDatabase.getInstance().getReference("Delivery_Guys");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    DeliveryGuys temp = new DeliveryGuys(ds.getValue(DeliveryGuys.class));
                    DeliveryGuys exist = get_delivery_guy(temp.getIndex_string());
                    if (exist == null) {
                        array.add(temp);
                        Log.d(TAG, "DeliveryGuy is :  " + temp.getName());
                    }
                    else
                    {
                        exist = temp;
                    }
                }
                MainActivity.set_title_for_adapter(1,array.size());
                Log.d(TAG,"Done retrieving DeliveryGuy " + array.size());
                recycleAdapterDeliveryGuys adapter = new recycleAdapterDeliveryGuys(array, this_fragment);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        return fragment_view;
    }
    DeliveryGuys get_delivery_guy(String index)
    {
        for(DeliveryGuys d : array)
        {
            if (d.getIndex_string().equals(index))
            {
                return d;
            }
        }
        return null;
    }
    @Override
    public void itemClicked(DeliveryGuys d) {
        Log.d(TAG,"onTouch");
        Toast.makeText(getActivity(), "Touch delivery guy: " + d.getName(), Toast.LENGTH_SHORT).show();
    }
}
