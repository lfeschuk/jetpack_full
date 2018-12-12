package com.example.leonid.jetpack.jetpack_shlihim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leonid.jetpack.jetpack_shlihim.Objects.StartShiftData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FillStartShiftActivity extends AppCompatActivity {
     byte bit_mask_checkbox = 0;
    String plate_num_edit = "";
    String gas_card_edit = "";
    String company_phone_index_edit = "";
    public final String TAG = "FillStartShift";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fill_start_shift_details);

        fill_data();


    }

    public void fill_data()
    {
        CheckBox num1 = findViewById(R.id.checkBox);
        set_check_box(num1,0);
        CheckBox num2 = findViewById(R.id.checkBox2);
        set_check_box(num2,1);
        CheckBox num3 = findViewById(R.id.checkBox3);
        set_check_box(num3,2);
        CheckBox num4 = findViewById(R.id.checkBox4);
        set_check_box(num4,3);
        CheckBox num5 = findViewById(R.id.checkBox5);
        set_check_box(num5,4);
        CheckBox num6 = findViewById(R.id.checkBox6);
        set_check_box(num6,5);
        CheckBox num7 = findViewById(R.id.checkBox7);
        set_check_box(num7,6);

        EditText plate_num = findViewById(R.id.plate_num);
        plate_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                plate_num_edit = editable.toString();
            }
        });
        EditText gas_card_num = findViewById(R.id.gas_card_num);
        gas_card_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                gas_card_edit = editable.toString();
            }
        });
        final EditText company_phone_index= findViewById(R.id.company_phone_index);
        company_phone_index.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                company_phone_index_edit = editable.toString();
            }
        });

        CheckBox approve = findViewById(R.id.check_box_approve);
        set_check_box(approve,7);
        Button send = findViewById(R.id.button_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (company_phone_index_edit.equals("") || gas_card_edit.equals("") || plate_num_edit.equals("") || bit_mask_checkbox!=-1)
                {
                  //  Log.d(TAG,"comp: " + company_phone_index_edit + " gas: " + gas_card_edit + " plate: " + plate_num_edit + " bit: " + bit_mask_checkbox);
                    Toast.makeText(getApplicationContext(), "לא הוזנו כל הנתונים", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    MainActivity.this_delivery_guy.setSent_start_shift_report(true);
                    StartShiftData ssd = new StartShiftData();
                    ssd.setCompany_phone_index(company_phone_index_edit);
                    ssd.setNum_of_gas_card(gas_card_edit);
                    ssd.setNum_of_plate(plate_num_edit);

                    Calendar cal = Calendar.getInstance();
                    Date curr_iter_date = cal.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String date_now = df.format(curr_iter_date);
                    df = new SimpleDateFormat("HH:mm");
                    String time_now = df.format(curr_iter_date);

                    ssd.setDate(date_now);
                    ssd.setTime(time_now);
                    ssd.setIndex(MainActivity.this_delivery_guy.getIndex_string());
                    ssd.setName(MainActivity.this_delivery_guy.getName());

                    mDatabase.child("Delivery_Guys").child( MainActivity.this_delivery_guy.getIndex_string()).child("sent_start_shift_report").setValue(true);
                    mDatabase.child("StartShiftData").child(MainActivity.this_delivery_guy.getIndex_string()).child(date_now).setValue(ssd);
                    finish();
                }
            }
        });


    }

    public void set_check_box(CheckBox cb,final int bit)
    {

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    bit_mask_checkbox |= (1 << bit);
                }
                else
                {
                    bit_mask_checkbox &= ~(1 << bit);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!MainActivity.this_delivery_guy.getSent_start_shift_report())
        {
            Toast.makeText(getApplicationContext(), "נא להזין כל הנתונים", Toast.LENGTH_SHORT).show();
        }
        else
        {
            super.onBackPressed();
        }
       //
    }
}
