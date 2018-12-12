package com.example.leonid.jetpack.restoraunt;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonid.jetpack.restoraunt.UIClasses.MyDatePickerFragment;
import com.example.leonid.jetpack.restoraunt.UIClasses.MyDialogCloseListener;
import com.example.leonid.jetpack.restoraunt.adapters.recycleAdapterHistoryDeliveries;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import Objects.Delivery;

public class HistoryActivity extends AppCompatActivity  implements recycleAdapterHistoryDeliveries.ItemClickListener,MyDialogCloseListener {
    public String costumer_phone = null;
    public HistoryActivity(){}
    HistoryActivity this_context = this;
    private Toolbar toolbar;
    private ArrayList<Delivery> array = new ArrayList<>();
    private ArrayList<Delivery> array_sorted = new ArrayList<>();
    private Query mDatabase = FirebaseDatabase.getInstance().getReference();
    //    PendingDeliveriesForGuyActivity this_context = this;
//    Delivery to_assign_delivery = null;
//    DeliveryGuys chosen_delivery_guy = null;
    //  ArrayList<DistanceDuration> durations_list = new ArrayList<>();
    private RecyclerView recyclerView;
    recycleAdapterHistoryDeliveries adapter;
    final static String TAG = "HistoryActivity";
    private FloatingActionButton fab;



    enum SortBy {NONE,ADRESS,INDEX,NAME};
    String date_start = "";
    String date_end = "";
    String sort_city = "";
    String sort_street = "";
    String sort_building = "";
    String sort_apartment = "";
    String sort_index = "";
    String sort_name = "";
    Boolean input_is_inserted = false;
    String sort_string = "";
    String out_sort_string = "";
    Boolean date_is_picked = false;
     TextView text_num_of_deliveries;
    Boolean is_from_costumer_list = false;
    String costumer_phone_num = null;

    public SortBy current_sort = SortBy.NONE;


    //  private DataBaseManager dbm = new DataBaseManager();
    //  private ArrayList<Destination> array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        Log.d(TAG,"send to Db");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.main_title_text);
        title.setText("היסטוריה");

        recyclerView = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);
        fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        set_fab();


      text_num_of_deliveries = findViewById(R.id.text_num_of_deliveries);
        text_num_of_deliveries.setBackgroundColor(Color.YELLOW);

//todo add to layout if from costumer
        Bundle b = getIntent().getExtras();
        if (b != null)
        {
           costumer_phone_num = b.getString("costumer_phone");
            is_from_costumer_list = (costumer_phone_num != null);
        }
        else
        {
            is_from_costumer_list = false;
        }

        if (!is_from_costumer_list) {
            mDatabase = FirebaseDatabase.getInstance().getReference("Deliveries").orderByChild("restoraunt_key").equalTo(MainActivity.this_restoraunt.getKey());
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Delivery temp = new Delivery(ds.getValue(Delivery.class));
                        array.add(temp);
                    }

                    adapter = new recycleAdapterHistoryDeliveries(array, this_context);
                    recyclerView.setAdapter(adapter);
                    text_num_of_deliveries.setText("סה''כ משלוחים: " + array.size());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
        else
        {
            mDatabase = FirebaseDatabase.getInstance().getReference("Deliveries").orderByChild("restoraunt_key").equalTo(MainActivity.this_restoraunt.getKey());
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Delivery temp = new Delivery(ds.getValue(Delivery.class));
                        if(temp.getCostumer_phone().equals(costumer_phone_num)) {
                            array.add(temp);
                        }
                    }

                    adapter = new recycleAdapterHistoryDeliveries(array, this_context);
                    recyclerView.setAdapter(adapter);
                    text_num_of_deliveries.setText("סה''כ משלוחים: " + array.size());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }





//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
    }

    public void set_fab()
    {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(HistoryActivity.this,R.style.Theme_Dialog);
                // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                //  LayoutInflater li = LayoutInflater.from(getActivity());
                //    final View myView = li.inflate(R.layout.checkbox_sort_history, null);
                dialog.setContentView(R.layout.checkbox_sort_history);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);


                Button sort_by_address = dialog.findViewById(R.id.sort_by_address);
                sort_by_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDialog(SortBy.ADRESS);
                        dialog.dismiss();
                        fab.setVisibility(View.VISIBLE);
                    }
                });
                Button sort_by_costumer = dialog.findViewById(R.id.sort_by_costumer);
                sort_by_costumer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDialog(SortBy.NAME);
                        dialog.dismiss();
                        fab.setVisibility(View.VISIBLE);
                    }
                });
                Button sort_by_num_order = dialog.findViewById(R.id.sort_by_num_order);
                sort_by_num_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDialog(SortBy.INDEX);
                        dialog.dismiss();
                        fab.setVisibility(View.VISIBLE);
                    }
                });
                Button sort_by_dates = dialog.findViewById(R.id.sort_by_dates);
                sort_by_dates.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    MyDatePickerFragment newFragment = new MyDatePickerFragment();
                    Bundle args = new Bundle();
                    //doesnt matter here
                    args.putBoolean("is_first", true);
                    newFragment.setArguments(args);
                    newFragment.show(getSupportFragmentManager(), "date picker");

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
    public void setDialog(final SortBy sort)
    {
        input_is_inserted = false;
        AlertDialog.Builder builder =  new AlertDialog.Builder(HistoryActivity.this);
        LayoutInflater li = LayoutInflater.from(HistoryActivity.this);
        final View myView = li.inflate(R.layout.history_sort_dialog, null);
        //    final Dialog dialog = new Dialog(context,R.style.Dialog);
        EditText et = myView.findViewById(R.id.edit_text_insert);
        EditText et_street = myView.findViewById(R.id.edit_text_insert_street);
        EditText et_build = myView.findViewById(R.id.edit_text_building);
        EditText et_apart = myView.findViewById(R.id.edit_text_apartment);

        TextView tv = myView.findViewById(R.id.text_insert);
        switch (sort)
        {
            case NAME:
                sort_string = "שם לקוח";
                tv.setText("אנא בחר שם לקוח");
                et.setHint("שם לקוח");
                setEdit_text(et,"name");
                break;
            case ADRESS:
                sort_string = "כתובת לקוח";
                tv.setText("אנא בחר כתובת");
                et.setHint("בחר עיר");
                setEdit_text(et,"city");
                setEdit_text(et_street,"street");
                setEdit_text(et_build,"building");
                setEdit_text(et_apart,"apartment");
                break;
            case INDEX:
                sort_string = "מס הזמנה";
                tv.setText("אנא בחר מספר הזמנה");
                et.setHint("מס הזמנה");
                setEdit_text(et,"index");
                break;
            default:
                    break;
        }
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
                if (input_is_inserted)
                {
                    out_sort_string = "";
                    switch (sort)
                    {
                        case NAME:
                            if(sort_name.equals(""))
                            {
                                Toast.makeText(HistoryActivity.this, "לא הוכנס שם לקוח",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            out_sort_string = sort_name;
                            break;
                        case ADRESS:
                            if(sort_city.equals(""))
                            {
                                Toast.makeText(HistoryActivity.this, "לא הוכנסה עיר ",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            out_sort_string = sort_city;
                            if(!sort_street.equals(""))
                            {
                                out_sort_string += ",";
                                out_sort_string += sort_street;
                                if(!sort_building.equals(""))
                                {
                                    out_sort_string += " ";
                                    out_sort_string += sort_building;
                                    if(!sort_apartment.equals(""))
                                    {
                                        out_sort_string += "/";
                                        out_sort_string += sort_apartment;
                                    }
                                }
                            }
                            break;
                        case INDEX:
                            if(sort_index.equals(""))
                            {
                                Toast.makeText(HistoryActivity.this, "לא הוכנסה מס הזמנה",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            out_sort_string = sort_index;
                            break;
                    }
                    Toast.makeText(HistoryActivity.this, "מיון לפי"+" " + sort_string + ":" + out_sort_string ,Toast.LENGTH_SHORT).show();
                    current_sort = sort;
                    display_data_sorted();
                }
                else
                {
                    Toast.makeText(HistoryActivity.this, "לא הוכנס קלט" + costumer_phone, Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
        final  AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void setEdit_text(EditText et,final String what)
    {
        et.setVisibility(View.VISIBLE);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                switch (what)
                {
                    case "index":
                        sort_index = editable.toString();
                        break;
                    case "city":
                        sort_city = editable.toString();
                        break;
                    case "street":
                        sort_street = editable.toString();
                        break;
                    case "building":
                        sort_building = editable.toString();
                        break;
                    case "apartment":
                        sort_apartment = editable.toString();
                        break;
                    case "name":
                        sort_name = editable.toString();
                        break;

                }
                input_is_inserted = true;
            }
        });
    }
    private class AsyncTaskSort extends AsyncTask<Void, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(Void... params) {
            array_sorted.clear();
            if (date_is_picked)
            {
                switch (current_sort)
                {
                    case INDEX:
                        for(Delivery d: array)
                        {
                            if ( (d.getIndexString().equals(sort_index)) && (d.getDate().compareTo(date_start) >= 0 ) && (d.getDate().compareTo(date_end) < 0))
                            {
                                array_sorted.add(d);
                            }
                        }
                        break;
                    case ADRESS:
                        for(Delivery d: array)
                        {
                            if ( (d.getCity().equals(sort_city)) && (d.getDate().compareTo(date_start) >= 0 ) && (d.getDate().compareTo(date_end) < 0))
                            {
                                if (!sort_street.equals(""))
                                {
                                    if (d.getStreet().equals(sort_street))
                                    {
                                        if (!sort_building.equals(""))
                                        {
                                            if (d.getBuilding().equals(sort_building))
                                            {
                                                if (!sort_apartment.equals(""))
                                                {
                                                    if (d.getApartment().equals(sort_apartment))
                                                    {
                                                        array_sorted.add(d);
                                                    }
                                                }
                                                else
                                                {
                                                    array_sorted.add(d);
                                                }
                                            }
                                        }
                                        else
                                        {
                                            array_sorted.add(d);
                                        }
                                    }
                                }
                                else
                                {
                                    array_sorted.add(d);
                                }

                            }
                        }
                        break;
                    case NAME:
                        for(Delivery d: array)
                        {
                            if ( (d.getCostumerName().equals(sort_name)) && (d.getDate().compareTo(date_start) >= 0 ) && (d.getDate().compareTo(date_end) < 0))
                            {
                                array_sorted.add(d);
                            }
                        }
                        break;
                    case NONE:
                        for(Delivery d: array)
                        {
                            if ((d.getDate().compareTo(date_start) >= 0 ) && (d.getDate().compareTo(date_end) < 0))
                            {
                                array_sorted.add(d);
                            }
                        }
                        break;

                }
            }
            //date is not picked
            else
            {
                switch (current_sort)
                {
                    case INDEX:
                        for(Delivery d: array)
                        {
                            if ( (d.getIndexString().equals(sort_index)))
                            {
                                array_sorted.add(d);
                            }
                        }
                        break;
                    case ADRESS:
                        for(Delivery d: array)
                        {
                            if ( (d.getCity().equals(sort_city)))
                            {
                                if (!sort_street.equals(""))
                                {
                                    if (d.getStreet().equals(sort_street))
                                    {
                                        if (!sort_building.equals(""))
                                        {
                                            if (d.getBuilding().equals(sort_building))
                                            {
                                                if (!sort_apartment.equals(""))
                                                {
                                                    if (d.getApartment().equals(sort_apartment))
                                                    {
                                                        array_sorted.add(d);
                                                    }
                                                }
                                                else
                                                {
                                                    array_sorted.add(d);
                                                }
                                            }
                                        }
                                        else
                                        {
                                            array_sorted.add(d);
                                        }
                                    }
                                }
                                else
                                {
                                    array_sorted.add(d);
                                }

                            }
                        }
                        break;
                    case NAME:
                        for(Delivery d: array)
                        {
                            if ( (d.getCostumerName().equals(sort_name)))
                            {
                                array_sorted.add(d);
                            }
                        }
                        break;
                    case NONE:
                        Toast.makeText(HistoryActivity.this, "לא הוכנס קלט" + costumer_phone, Toast.LENGTH_SHORT).show();
                        break;

                }
            }

            //the array is full;

          //  publishProgress("מחשב..."); // Calls onProgressUpdate()
            try {
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            adapter = new recycleAdapterHistoryDeliveries(array_sorted, this_context);
            recyclerView.setAdapter(adapter);
            Toast.makeText(HistoryActivity.this, "מיון לפי"+" " + sort_string + ":" + out_sort_string ,Toast.LENGTH_SHORT).show();
            if (sort_string.equals(""))
            {
                text_num_of_deliveries.setText("מיון לפי "+" " + "תאריכים" + ":" + date_start + "-" +date_end + " -סה''כ " + array_sorted.size());
            }
            else if (date_is_picked)
            {

                text_num_of_deliveries.setText("מיון לפי " + " " + sort_string + ":" + out_sort_string + " ותאריכים: " + date_start + "-" +date_end + " -סה''כ " + array_sorted.size());
            }
            //no date
            else
            {
                text_num_of_deliveries.setText("מיון לפי " + " " + sort_string + ":" + out_sort_string + " -סה''כ " + array_sorted.size());
            }
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(HistoryActivity.this,
                    "ProgressDialog",
                    "המתן לתוצאות המיון ");
        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
    public void display_data_sorted()
    {
        AsyncTaskSort sort = new AsyncTaskSort();
        sort.execute();
    }
    @Override
    public void handleDialogClose(Boolean is_first, String date, Boolean is_date) {
        if(date == null)
        {
            return;
        }
        if (is_first)
        {
            date_start = date;
            if (date_start.equals(""))
            {
                Toast.makeText(HistoryActivity.this, "לא נבחר תאריך",Toast.LENGTH_SHORT).show();
                return;
            }
            MyDatePickerFragment newFragment = new MyDatePickerFragment();
            Bundle args = new Bundle();
            //doesnt matter here
            args.putBoolean("is_first", false);
            newFragment.setArguments(args);
            newFragment.show(getSupportFragmentManager(), "date picker");
        }
        else
        {
            date_end = date;
            if (date_end.equals(""))
            {
                Toast.makeText(HistoryActivity.this, "לא נבחר תאריך",Toast.LENGTH_SHORT).show();
                return;
            }
            date_is_picked = true;
            display_data_sorted();
        }
    }
    @Override
    public void itemClicked(Delivery delivery) {
        Log.d(TAG,"item clicked: " +delivery.getIndexString());
        AlertDialog.Builder builder =  new AlertDialog.Builder(HistoryActivity.this);
        LayoutInflater li = LayoutInflater.from(HistoryActivity.this);
        final View myView = li.inflate(R.layout.on_click_dialog_history_element, null);
        //    final Dialog dialog = new Dialog(context,R.style.Dialog);
        TextView title = myView.findViewById(R.id.title);
        if(delivery.getIs_deleted())
        {
            title.setText("המשלוח בוטל");
            title.setTextColor(Color.RED);
        }
        else
        {
            title.setText("המשלוח נמסר ללקוח");
            title.setTextColor(Color.GREEN);
        }
        Spanned text;
        TextView date = myView.findViewById(R.id.date);
       // text = Html.fromHtml( "<font color=\"#FF0000\">" +"תאריך: " + "</font>");
        //worked
        text = Html.fromHtml( "<b>" +"תאריך: " + "</b>" + delivery.getDate());
//        myTextView.setText(Html.fromHtml(text + "<font color=\"#FFFFFF\">" + CepVizyon.getPhoneCode() + "</font><br><br>"
//                + getText(R.string.currentversion) + CepVizyon.getLicenseText()));
       // date.setText( text + delivery.getDate());
        date.setText( text );

        TextView delivery_guy = myView.findViewById(R.id.delivery_guy);
//        myTextView.setText(Html.fromHtml(text + "<font color=\"#FFFFFF\">" + CepVizyon.getPhoneCode() + "</font><br><br>"
//                + getText(R.string.currentversion) + CepVizyon.getLicenseText()));
        text = Html.fromHtml( "<b>" +"שליח: " + "</b>" + delivery.getDeliveryGuyName());
        delivery_guy.setText(  text );

        TextView phone_deliv = myView.findViewById(R.id.phone_deliv);
//        myTextView.setText(Html.fromHtml(text + "<font color=\"#FFFFFF\">" + CepVizyon.getPhoneCode() + "</font><br><br>"
//                + getText(R.string.currentversion) + CepVizyon.getLicenseText()));
        text = Html.fromHtml( "<b>" +"טלפון: " + "</b>" + delivery.getDeliveryGuyPhone());
        phone_deliv.setText(text);

        TextView title_delivery_to = myView.findViewById(R.id.title_delivery_to);
//        myTextView.setText(Html.fromHtml(text + "<font color=\"#FFFFFF\">" + CepVizyon.getPhoneCode() + "</font><br><br>"
//                + getText(R.string.currentversion) + CepVizyon.getLicenseText()));
        text = Html.fromHtml( "<b>" +"משלוח אל: " + "</b>" );
        title_delivery_to.setText(text);


        TextView address = myView.findViewById(R.id.address);
//        myTextView.setText(Html.fromHtml(text + "<font color=\"#FFFFFF\">" + CepVizyon.getPhoneCode() + "</font><br><br>"
//                + getText(R.string.currentversion) + CepVizyon.getLicenseText()));
        text = Html.fromHtml( "<b>" +"כתובת: " + "</b>" + delivery.getAdressTo() );
        address.setText(text);

        TextView costumer_name = myView.findViewById(R.id.costumer_name);
//        myTextView.setText(Html.fromHtml(text + "<font color=\"#FFFFFF\">" + CepVizyon.getPhoneCode() + "</font><br><br>"
//                + getText(R.string.currentversion) + CepVizyon.getLicenseText()));
        text = Html.fromHtml( "<b>" +"שם לקוח: " + "</b>" + delivery.getCostumerName() );
        costumer_name.setText(text);

        TextView phone_costumer = myView.findViewById(R.id.phone_costumer);
//        myTextView.setText(Html.fromHtml(text + "<font color=\"#FFFFFF\">" + CepVizyon.getPhoneCode() + "</font><br><br>"
//                + getText(R.string.currentversion) + CepVizyon.getLicenseText()));
        text = Html.fromHtml( "<b>" +"טלפון: " + "</b>" + delivery.getCostumer_phone() );
        phone_costumer.setText( text);

        TextView time_of_order = myView.findViewById(R.id.time_of_order);
//        myTextView.setText(Html.fromHtml(text + "<font color=\"#FFFFFF\">" + CepVizyon.getPhoneCode() + "</font><br><br>"
//                + getText(R.string.currentversion) + CepVizyon.getLicenseText()));
        text = Html.fromHtml( "<b>" +"שעת הזמנה: " + "</b>" + delivery.getTimeInserted() );
        time_of_order.setText( text);

        TextView time_picked = myView.findViewById(R.id.time_picked);
//        myTextView.setText(Html.fromHtml(text + "<font color=\"#FFFFFF\">" + CepVizyon.getPhoneCode() + "</font><br><br>"
//                + getText(R.string.currentversion) + CepVizyon.getLicenseText()));
        text = Html.fromHtml( "<b>" +"שעת איסוף: " + "</b>" + delivery.getTimeTaken() );
        time_picked.setText(text);

        TextView time_to_prepare = myView.findViewById(R.id.time_to_prepare);
//        myTextView.setText(Html.fromHtml(text + "<font color=\"#FFFFFF\">" + CepVizyon.getPhoneCode() + "</font><br><br>"
//                + getText(R.string.currentversion) + CepVizyon.getLicenseText()));
        text = Html.fromHtml( "<b>" +"שעת הגעת שליח: " + "</b>" + delivery.getTimeArriveToRestoraunt() );
        time_to_prepare.setText(text);

        TextView time_delivered = myView.findViewById(R.id.time_delivered);
//        myTextView.setText(Html.fromHtml(text + "<font color=\"#FFFFFF\">" + CepVizyon.getPhoneCode() + "</font><br><br>"
//                + getText(R.string.currentversion) + CepVizyon.getLicenseText()));
        text = Html.fromHtml( "<b>" +"שעת סיום: " + "</b>" + delivery.getTimeDeliver() );
        time_delivered.setText(text);

        TextView price = myView.findViewById(R.id.price);
//        myTextView.setText(Html.fromHtml(text + "<font color=\"#FFFFFF\">" + CepVizyon.getPhoneCode() + "</font><br><br>"
//                + getText(R.string.currentversion) + CepVizyon.getLicenseText()));
        text = Html.fromHtml( "<b>" +"סכום: " + "</b>" + delivery.getPrice() + "₪" );
        price.setText(text);

        TextView comment = myView.findViewById(R.id.comment);
//        myTextView.setText(Html.fromHtml(text + "<font color=\"#FFFFFF\">" + CepVizyon.getPhoneCode() + "</font><br><br>"
//                + getText(R.string.currentversion) + CepVizyon.getLicenseText()));
        text = Html.fromHtml( "<b>" +"הערה: " + "</b>" + delivery.getComment());
            comment.setText(text);


        builder.setView(myView);
//                builder.setTitle(shift.getName());
        builder.setNeutralButton("סיום", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        final  AlertDialog dialog = builder.create();

        dialog.show();
    }
}
