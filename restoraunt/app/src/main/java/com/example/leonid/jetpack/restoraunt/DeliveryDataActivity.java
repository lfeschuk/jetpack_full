package com.example.leonid.jetpack.restoraunt;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonid.jetpack.restoraunt.Interfaces.LocationFound;
import com.example.leonid.jetpack.restoraunt.UIClasses.MyDatePickerFragment;
import com.example.leonid.jetpack.restoraunt.UIClasses.MyDialogCloseListener;
import com.example.leonid.jetpack.restoraunt.UIClasses.TimePickerFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Objects.Costumer;
import Objects.DataBaseManager;
import Objects.DelayedDelivery;
import Objects.Delivery;

public class DeliveryDataActivity extends AppCompatActivity implements MyDialogCloseListener, LocationFound
{
    DataBaseManager dbm = new DataBaseManager();
    protected  Toolbar toolbar;
    public String costumer_phone = "";
    public Costumer found_costumer = null;
    public ArrayList<Costumer> found_costumer_arr = new ArrayList<>();
    public ArrayList<String> found_costumer_adress_arr = new ArrayList<>();
    public Costumer costumer_create = new Costumer();
    private Delivery d_out = new Delivery();
    String different_street = "";
    String different_city = "";
    String different_floor = "";
    String different_apartment = "";
    String different_building = "";
    String different_entrance = "";
    String different_address = "";
    String delayed_date = "";
    String delayed_hour = "";

    Boolean is_delayed = false;
    Boolean is_from_another_adress = false;

    TextView different_address_text;
    TextView delayed_delivery;
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
        costumer_phone = b.getString("phone");
        is_delayed = b.getBoolean("is_delayed");
        if (is_delayed == null)
        {
            is_delayed = false;
        }
        is_from_another_adress = b.getBoolean("is_from_another_adress");
        if (is_from_another_adress == null)
        {
            is_from_another_adress = false;
        }
        Log.d(TAG,"after intent");
        mDatabase =  FirebaseDatabase.getInstance().getReference("Costumers");
        mDatabase.child(costumer_phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        Costumer temp = new Costumer(ds.getValue(Costumer.class));
                        found_costumer_arr.add(temp);
                        found_costumer_adress_arr.add(temp.getCity() +" " + temp.getStreet() + " " + temp.getApartment() + "/" + temp.getBuilding());
                    }
                    if (found_costumer_arr.size() == 1)
                    {
                        found_costumer = found_costumer_arr.get(0);
                        set_delivery_table(true);
                        set_buttons();
                    }
                    //more than 1 option
                    else
                    {
                        AlertDialog.Builder builder =  new AlertDialog.Builder(DeliveryDataActivity.this);
                        LayoutInflater li = LayoutInflater.from(DeliveryDataActivity.this);
                        final View myView = li.inflate(R.layout.choose_adress_client_dialog, null);
                        //    final Dialog dialog = new Dialog(context,R.style.Dialog);
                        AppCompatSpinner spinner = myView.findViewById(R.id.spinner);
                        ArrayAdapter adapter = new ArrayAdapter<>(DeliveryDataActivity.this, R.layout.spinner_item,found_costumer_adress_arr );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int index_pressed, long lon) {
                                 found_costumer = found_costumer_arr.get(index_pressed);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                found_costumer = found_costumer_arr.get(0);
                            }
                        });
                        builder.setView(myView);
//                builder.setTitle(shift.getName());
                        builder.setPositiveButton("המשך", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                set_delivery_table(true);
                                set_buttons();
                            }
                        });
                        builder.setNegativeButton("הזן כתובת חדשה", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                found_costumer = null;
                                set_delivery_table(false);
                                set_buttons();
                            }
                        });
                        final  AlertDialog dialog = builder.create();

                        dialog.show();
                    }
                }
                else
                {
                    set_delivery_table(false);
                    set_buttons();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        different_address_text = findViewById(R.id.different_address);
        different_address_text.setVisibility(View.GONE);
        delayed_delivery = findViewById(R.id.delayed_delivery);
        delayed_delivery.setVisibility(View.GONE);




    }
    void set_delivery_table(Boolean exist)
    {
        EditText time_prepare_edit = findViewById(R.id.time_prepare_edit); //do something later
        time_prepare_edit.setText("15");
        d_out.setPrepare_time("15");
        time_prepare_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d_out.setPrepare_time(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText num_of_pack = findViewById(R.id.num_of_pack_edit_text);
        num_of_pack.setText("1");
        d_out.setNum_of_packets("1");
        num_of_pack.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d_out.setNum_of_packets(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText comment = findViewById(R.id.comment_edit_text);
        if (exist)
        {
            comment.setText(found_costumer.getComment());
            d_out.setComment(found_costumer.getComment());
        }
        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d_out.setComment(charSequence.toString());
                costumer_create.setComment(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText another_phone_edit_text = findViewById(R.id.another_phone_edit_text);
        if (exist)
        {
            another_phone_edit_text.setText(found_costumer.getAnother_phone());
            d_out.setCostumer_another_phone(found_costumer.getAnother_phone());
        }
        another_phone_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d_out.setCostumer_another_phone(charSequence.toString());
                costumer_create.setAnother_phone(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText intercume_code_edit_text = findViewById(R.id.intercume_code_edit_text);
        if (exist)
        {
            intercume_code_edit_text.setText(found_costumer.getIntercum_num());
            d_out.setIntercum_num(found_costumer.getIntercum_num());
        }
        intercume_code_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d_out.setIntercum_num(charSequence.toString());
                costumer_create.setIntercum_num(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        EditText phone_costumer_edit_text = findViewById(R.id.phone_costumer_edit_text);
        if(exist)
        {
            phone_costumer_edit_text.setText(found_costumer.getPhone());
            d_out.setCostumer_phone(found_costumer.getPhone());
        }
        else
        {
            phone_costumer_edit_text.setText(costumer_phone);
            d_out.setCostumer_phone(costumer_phone);
            costumer_create.setPhone(costumer_phone);
        }
        phone_costumer_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d_out.setCostumer_phone(charSequence.toString());
                costumer_create.setPhone(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText costumer_name_edit = findViewById(R.id.costumer_name_edit);
       if(exist)
       {
           costumer_name_edit.setText(found_costumer.getName());
           d_out.setCostumerName(found_costumer.getName());
       }
        costumer_name_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d_out.setCostumerName(charSequence.toString());
                costumer_create.setName(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText costumer_city_edit = findViewById(R.id.costumer_city_edit);
        if(exist)
        {
            costumer_city_edit.setText(found_costumer.getCity());
            d_out.setCity(found_costumer.getCity());
        }
        costumer_city_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d_out.setCity(charSequence.toString());
                costumer_create.setCity(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText costumer_street_edit = findViewById(R.id.costumer_street_edit);
        if(exist)
        {
            costumer_street_edit.setText(found_costumer.getStreet());
            d_out.setStreet(found_costumer.getStreet());
        }
        costumer_street_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d_out.setStreet(charSequence.toString());
                costumer_create.setStreet(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText costumer_floor_edit = findViewById(R.id.costumer_floor_edit);
        if(exist)
        {
            costumer_floor_edit.setText(found_costumer.getFloor());
            d_out.setFloor(found_costumer.getFloor());
        }
        costumer_floor_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d_out.setFloor(charSequence.toString());
                costumer_create.setFloor(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText costumer_apartment_edit = findViewById(R.id.costumer_apartment_edit);
        if(exist)
        {
            costumer_apartment_edit.setText(found_costumer.getApartment());
            d_out.setApartment(found_costumer.getApartment());
        }
        costumer_apartment_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d_out.setApartment(charSequence.toString());
                costumer_create.setApartment(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText costumer_building_edit = findViewById(R.id.costumer_building_edit);
        if(exist)
        {
            costumer_building_edit.setText(found_costumer.getBuilding());
            d_out.setBuilding(found_costumer.getBuilding());
        }
        costumer_building_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d_out.setBuilding(charSequence.toString());
                costumer_create.setBuilding(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText costumer_entrance_edit = findViewById(R.id.costumer_entrance_edit);
        if(exist)
        {
            costumer_entrance_edit.setText(found_costumer.getEntrance());
            d_out.setEntrance(found_costumer.getEntrance());
        }
        costumer_entrance_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d_out.setEntrance(charSequence.toString());
                costumer_create.setEntrance(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText pay_amount_edit_text = findViewById(R.id.pay_amount_edit_text);
        pay_amount_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                d_out.setPrice(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
    public void update_ui_if_delayed_or_another_addr()
    {
        if (is_from_another_adress)
        {
            AlertDialog.Builder builder =  new AlertDialog.Builder(DeliveryDataActivity.this);
            LayoutInflater li = LayoutInflater.from(DeliveryDataActivity.this);
            final View myView = li.inflate(R.layout.choose_different_address_dialog, null);
            //    final Dialog dialog = new Dialog(context,R.style.Dialog);
            EditText costumer_city_edit = myView.findViewById(R.id.costumer_city_edit);
            costumer_city_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    different_city = editable.toString();
                }
            });
            EditText costumer_street_edit = myView.findViewById(R.id.costumer_street_edit);
            costumer_street_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    different_street = editable.toString();
                }
            });
            EditText costumer_floor_edit = myView.findViewById(R.id.costumer_floor_edit);
            costumer_floor_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    different_floor = editable.toString();
                }
            });
            EditText costumer_apartment_edit = myView.findViewById(R.id.costumer_apartment_edit);
            costumer_apartment_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    different_apartment = editable.toString();
                }
            });
            EditText costumer_building_edit = myView.findViewById(R.id.costumer_building_edit);
            costumer_building_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    different_building = editable.toString();
                }
            });
            EditText costumer_entrance_edit = myView.findViewById(R.id.costumer_entrance_edit);
            costumer_entrance_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    different_entrance = editable.toString();
                }
            });
            builder.setView(myView);
//                builder.setTitle(shift.getName());
            builder.setNeutralButton("ביטול", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setPositiveButton("סיום", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(different_city.equals("") || different_street.equals("") || different_building.equals("") )
                    {
                        Toast.makeText(DeliveryDataActivity.this, "חסרים פרטים, לא נקלטה כתובת חלופית" ,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        different_address = different_city + " " + different_street + " " +different_building;
                        if(!different_apartment.equals(""))
                        {
                            different_address += "/" + different_apartment;
                        }
                        different_address_text.setVisibility(View.VISIBLE);
                        different_address_text.setText("נבחרה כתובת חלופית:" + " " + different_address);
                    }
                }

            });
            final  AlertDialog dialog = builder.create();

            dialog.show();
        }

        if (is_delayed)
        {
            showDatePicker();
        }


    }
    public void set_buttons()
    {


        Button assign_button = findViewById(R.id.button_assign);
        assign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (d_out.getCostumer_phone().equals("") || d_out.getCostumerName().equals("") || d_out.getCity().equals("") || d_out.getStreet().equals("")
                || d_out.getBuilding().equals("") || d_out.getApartment().equals("")
                || d_out.getNum_of_packets().equals("") || d_out.getPrepare_time().equals("") )
                {
                    Toast.makeText(getApplicationContext(), "אנא סמן את השדות החסרים", Toast.LENGTH_SHORT).show();
                    return;
                }
                //time now
                Calendar cal = Calendar.getInstance();
                Date curr_iter_date = cal.getTime();
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                String time_now = df.format(curr_iter_date);
                df = new SimpleDateFormat("yyyy-MM-dd");
                String date_now = df.format(curr_iter_date);

                RadioGroup cash_or_credit = (RadioGroup) findViewById(R.id.radioButton);
                //1 for credit
                Boolean is_cash = cash_or_credit.getCheckedRadioButtonId() == 0;

                d_out.setBusiness_name(MainActivity.this_restoraunt.getName());
                d_out.setDate(date_now);
                d_out.setTimeInserted(time_now);
                d_out.setAdressTo(d_out.getCity() +" " + d_out.getStreet() + " " +d_out.getBuilding() + "/" + d_out.getApartment() );
                d_out.setAdressFrom(MainActivity.this_restoraunt.getAdress());
                d_out.setIs_cash(is_cash);
                d_out.setIndexString("-1");
                d_out.setRestoraunt_key(MainActivity.this_restoraunt.getKey());
                d_out.setDifferent_address(different_address);
                if (different_address != null && !different_address.equals(""))
                {
                    d_out.setIs_different_adress(true);
                }
                d_out.setRestoraunt_phone(MainActivity.this_restoraunt.getPhone());
                d_out.setTime_max_to_costumer(MainActivity.this_restoraunt.getTime_to_costumer());
                d_out.setSource_cord_long(MainActivity.this_restoraunt.getLongt());
                d_out.setSource_cord_lat(MainActivity.this_restoraunt.getLat());
                //this function will callback and write the delivery
                getLocationFromAddress(DeliveryDataActivity.this,d_out.getAdressTo(),d_out.getDifferent_address());


            }
        });
        update_ui_if_delayed_or_another_addr();


    }


    public LatLng getLocationFromAddress(Context context, String strAddress,String different_address_arg) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        List<Address> different_address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (different_address_arg != null)
            {
                different_address = coder.getFromLocationName(different_address_arg, 5);
                if (different_address == null || different_address.isEmpty())
                {
                    Log.d(TAG,"not found different adress");
                    ((LocationFound)this).handleLocationFound(null,false);
                    return null;
                }
                else
                {
                    Address location = different_address.get(0);
                    LatLng l = new LatLng(location.getLatitude(),location.getLongitude());
                    Log.d(TAG,"location:" + location + " adress: " + different_address.get(0));
                    Log.d(TAG,"strADres: " + strAddress);

                    ((LocationFound)this).handleLocationFound(l,true);
                    if (address == null || address.isEmpty()) {
                        Log.d(TAG,"not found adress");
                        ((LocationFound)this).handleLocationFound(null,false);
                        return null;
                    }

                    Address location2 = address.get(0);
                    LatLng l2 = new LatLng(location2.getLatitude(),location2.getLongitude());
                    Log.d(TAG,"location:" + location2 + " adress: " + address.get(0));
                    Log.d(TAG,"strADres: " + strAddress);

                    ((LocationFound)this).handleLocationFound(l2,false);


                    Log.d(TAG,"location: " + location);


                }
            }
            else
            {
                if (address == null || address.isEmpty()) {
                    Log.d(TAG,"not found adress");
                    ((LocationFound)this).handleLocationFound(null,false);
                    return null;
                }

                Address location2 = address.get(0);
                LatLng l2 = new LatLng(location2.getLatitude(),location2.getLongitude());
                Log.d(TAG,"location:" + location2 + " adress: " + address.get(0));
                Log.d(TAG,"strADres: " + strAddress);

                ((LocationFound)this).handleLocationFound(l2,false);


                Log.d(TAG,"location: " + location2);
            }




        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    public void showDatePicker( ) {
        MyDatePickerFragment newFragment = new MyDatePickerFragment();
        Bundle args = new Bundle();
        //not needed here
        args.putBoolean("is_first", true);
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "בחר תאריך");
    }

    @Override
    public void handleDialogClose(Boolean is_first, String date,Boolean is_date) {
        if(date == null)
        {
            return;
        }
        if (is_date)
        {
            this.delayed_date = date;
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getSupportFragmentManager(),"TimePicker");
        }
        else
        {
            this.delayed_hour = date;
            Toast.makeText(getApplicationContext(), "נבחרה שעה"+date, Toast.LENGTH_SHORT).show();
            if (!this.delayed_hour.equals("") && !this.delayed_date.equals(""))
            {
                is_delayed = true;
                delayed_delivery.setVisibility(View.VISIBLE);
                delayed_delivery.setText( "משלוח דחוי לתאריך: " + this.delayed_date + " " + this.delayed_hour);
                EditText time_prepare_edit = findViewById(R.id.time_prepare_edit); //do something later
                time_prepare_edit.setText("25");
                d_out.setPrepare_time("25");
                time_prepare_edit.setClickable(false);
            }
        }


    }

    @Override
    public void handleLocationFound(LatLng loc,Boolean is_different_adress) {
       if (is_different_adress && (loc != null))
       {
           d_out.setSource_cord_lat(loc.latitude);
           d_out.setSource_cord_long(loc.longitude);
           return;
       }

        if (loc != null)
        {
            d_out.setDest_cord_long(loc.longitude);
            d_out.setDest_cord_lat(loc.latitude);
        }


        if (is_delayed)
        {
            DelayedDelivery dd = new DelayedDelivery(d_out,delayed_date,delayed_hour);
            dbm.writeDelayedDelivery(dd);
        }
        else {
            dbm.writeDelivery(d_out);
        }
        if (found_costumer == null)
        {
            Log.d(TAG,"phone: " + costumer_create.getPhone());
            dbm.writeCostumer(costumer_create);
        }
        finish();
    }
}
