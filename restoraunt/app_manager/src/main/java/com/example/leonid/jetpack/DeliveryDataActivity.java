package com.example.leonid.jetpack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Objects.DataBaseManager;
import Objects.Delivery;
import Objects.DeliveryGuys;
import Objects.Destination;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class DeliveryDataActivity extends AppCompatActivity  implements PopupMenu.OnMenuItemClickListener
{
    DataBaseManager dbm = new DataBaseManager();
     public Delivery clicked_delivery = null;
     public DeliveryGuys deliveryDuyWithDeletedDelivery = null;
    protected  Toolbar toolbar;
    private Delivery d = null;
    public static final String TAG = "DeliveryDataActivity";
    protected DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"on create start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_data_activity);
        Log.d(TAG,"on intent");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle b = getIntent().getExtras();
        String delivery_key = b.getString("Delivery_Index");
        Log.d(TAG,"after intent");
        mDatabase =  FirebaseDatabase.getInstance().getReference("Deliveries");
        mDatabase.child(delivery_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Delivery temp = new Delivery(dataSnapshot.getValue(Delivery.class));
                Log.d(TAG,"the delivery is " + temp.getIndexString() + " " + temp.getAdressFrom() + temp);
                clicked_delivery = temp;
                TextView title = findViewById(R.id.main_title_text);
                title.setText("#" + clicked_delivery.getIndexString());
                d = temp;
                set_delivery_table();
                set_buttons();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    void set_delivery_table()
    {
        Button pick_hour = findViewById(R.id.pick_hour_button); //do something later

        EditText num_of_pack = findViewById(R.id.num_of_pack_edit_text);
        num_of_pack.setText(d.getNum_of_packets());
        num_of_pack.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d.setNum_of_packets(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText comment = findViewById(R.id.comment_edit_text);
        comment.setText(d.getComment());
        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d.setComment(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





        EditText phone_costumer_edit_text = findViewById(R.id.phone_costumer_edit_text);
        phone_costumer_edit_text.setText(d.getCostumer_phone());
        phone_costumer_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d.setCostumer_phone(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText costumer_name_edit = findViewById(R.id.costumer_name_edit);
        costumer_name_edit.setText(d.getCostumerName());
        costumer_name_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d.setCostumerName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText costumer_city_edit = findViewById(R.id.costumer_city_edit);
        costumer_city_edit.setText(d.getCity());
        costumer_city_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d.setCity(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText costumer_street_edit = findViewById(R.id.costumer_street_edit);
        costumer_street_edit.setText(d.getStreet());
        costumer_street_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d.setStreet(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText costumer_floor_edit = findViewById(R.id.costumer_floor_edit);
        costumer_floor_edit.setText(d.getFloor());
        costumer_floor_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d.setFloor(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText costumer_apartment_edit = findViewById(R.id.costumer_apartment_edit);
        costumer_apartment_edit.setText(d.getApartment());
        costumer_apartment_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d.setApartment(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText costumer_building_edit = findViewById(R.id.costumer_building_edit);
        costumer_building_edit.setText(d.getBuilding());
        costumer_building_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d.setBuilding(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText costumer_entrance_edit = findViewById(R.id.costumer_entrance_edit);
        costumer_entrance_edit.setText(d.getEntrance());
        costumer_entrance_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d.setEntrance(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }
    public void set_buttons()
    {
        Button end_delivery_change_button = findViewById(R.id.end_delivery_change_button);
        end_delivery_change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"send to database " + d.getApartment() +"is deleted " + clicked_delivery.getIs_deleted());
                if (deliveryDuyWithDeletedDelivery != null)
                {
                    dbm.writeDeliveryGuy(deliveryDuyWithDeletedDelivery);
                    deliveryDuyWithDeletedDelivery = null;
                }
                if(d.getIs_deleted())
                {
                    dbm.deleteDelivery(d);
                }
                else{
                    dbm.writeDelivery(d);
                }
                findViewById(R.id.success_text_buttom).setVisibility(View.VISIBLE);
                Intent intent = new Intent(DeliveryDataActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        Button change_status = findViewById(R.id.button_change_status);
        change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(DeliveryDataActivity.this, view);
                popup.setOnMenuItemClickListener(DeliveryDataActivity.this);
                popup.inflate(R.menu.change_status_button_menu);
                if (clicked_delivery.getStatus().equals("A")) {
                    popup.getMenu().findItem(R.id.change_status).setEnabled(false);
                }
                if (clicked_delivery.getIs_deleted())
                {
                    popup.getMenu().findItem(R.id.cancel_order).setTitle(R.string.restore_delivery);
                }
                else
                {
                    popup.getMenu().findItem(R.id.cancel_order).setTitle(R.string.delete_delivery);
                }
                popup.show();
            }
        });

        Button assign_button = findViewById(R.id.button_assign);
        if (d.getStatus().equals("C"))
        {
            assign_button.setVisibility(View.GONE);
        }
        assign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeliveryDataActivity.this, ActiveDeliveryGuysActivity.class);
                Bundle b = new Bundle();
                b.putString("Delivery_Index",d.getIndexString());
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.cancel_order:
                if (clicked_delivery.getIs_deleted())
                {
                    clicked_delivery.setIs_deleted(false);
                    Toast.makeText(this, "לחץ על סיום כדי להחזיר משלוח", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    clicked_delivery.setIs_deleted(true);
                    Toast.makeText(this, "לחץ על סיום כדי לבטל משלוח", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.change_status:
                clicked_delivery.setStatus("A");
                Query q =  FirebaseDatabase.getInstance().getReference("Delivery_Guys").orderByChild("index_string").equalTo(clicked_delivery.getDelivery_guy_index_assigned());
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            DeliveryGuys temp = new DeliveryGuys(ds.getValue(DeliveryGuys.class));

                           for (Delivery d : temp.getDeliveries())
                           {
                               if (d.getIndexString().equals(clicked_delivery.getIndexString()))
                               {
                                   temp.getDeliveries().remove(d);
                                   break;
                               }
                           }
                            deliveryDuyWithDeletedDelivery = temp;
                           Log.d(TAG,"remove delivery: " + clicked_delivery.getIndexString() +" from guy: " + temp.getName());
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
                clicked_delivery.setDelivery_guy_index_assigned("");
                return true;
            default:
                return false;
        }
    }
}
