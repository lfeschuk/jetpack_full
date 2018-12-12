package com.example.leonid.jetpack.restoraunt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonid.jetpack.restoraunt.UIClasses.MyDatePickerFragment;
import com.example.leonid.jetpack.restoraunt.UIClasses.MyDialogCloseListener;
import com.example.leonid.jetpack.restoraunt.adapters.recycleAdapterHistoryDeliveries;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Objects.Costumer;
import Objects.Delivery;
import Objects.Map_Key_Address_Name;
import Objects.Map_Value_Price_Amount;

public class CostumerListActivity extends AppCompatActivity implements MyDialogCloseListener {
    public String costumer_phone = null;
    public CostumerListActivity(){}
    CostumerListActivity this_context = this;
    private Toolbar toolbar;
    private ArrayList<Delivery> array = new ArrayList<>();
    private ArrayList<String> cotumer_names = new ArrayList<>();
    private ArrayList<Float> prices = new ArrayList<>();
    private ArrayList<Map_Key_Address_Name> costumer_info = new ArrayList<>();
    private  ArrayList<Integer> amounts = new ArrayList<>();
    HorizontalBarChart  chart_price;
    HorizontalBarChart  chart_amount;
    Map<Map_Key_Address_Name, Map_Value_Price_Amount> map2graph = new HashMap<>();


//    private ArrayList<Delivery> array_sorted = new ArrayList<>();
    private Query mDatabase = FirebaseDatabase.getInstance().getReference();
    TextView text_num_of_deliveries;
    TextView text_total_order;
    TextView text_total_cash;
    TextView text_total_credit;
    float total_price = 0;
    float total_cash = 0;
    float total_credit = 0;

    String date_start = "";
    String date_end = "";
    //    PendingDeliveriesForGuyActivity this_context = this;
//    Delivery to_assign_delivery = null;
//    DeliveryGuys chosen_delivery_guy = null;
    //  ArrayList<DistanceDuration> durations_list = new ArrayList<>();
    private RecyclerView recyclerView;
    recycleAdapterHistoryDeliveries adapter;
    final static String TAG = "CostumerListActivity";
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.costumer_list_activity);

        Log.d(TAG,"send to Db");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.main_title_text);
        title.setText("היסטוריה");




        text_num_of_deliveries = findViewById(R.id.text_num_of_deliveries);
        text_num_of_deliveries.setBackgroundColor(Color.YELLOW);

        text_total_cash = findViewById(R.id.text_total_cash);
        text_total_cash.setBackgroundColor(Color.GRAY);
        text_total_credit = findViewById(R.id.text_total_credit);
        text_total_credit.setBackgroundColor(Color.GRAY);
        text_total_order = findViewById(R.id.text_total_order);
        text_total_order.setBackgroundColor(Color.GRAY);

        chart_price = (HorizontalBarChart) findViewById(R.id.chart_price);
        chart_amount = (HorizontalBarChart) findViewById(R.id.chart_amount);
        chart_amount.setVisibility(View.GONE);
        chart_price.setVisibility(View.GONE);



        mDatabase =  FirebaseDatabase.getInstance().getReference("Deliveries").orderByChild("restoraunt_key").equalTo(MainActivity.this_restoraunt.getKey());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Delivery temp = new Delivery(ds.getValue(Delivery.class));

                    if (!temp.getIs_deleted() && temp.getStatus().equals("D"))
                    {
                        array.add(temp);

                        total_price += Float.valueOf(temp.getPrice());
                        if (temp.getIs_cash())
                        {
                            total_cash += Float.valueOf(temp.getPrice());
                        }
                        else
                        {
                            total_credit += Float.valueOf(temp.getPrice());
                        }
                    }

                }
                text_num_of_deliveries.setText("סה''כ משלוחים: " + array.size());
                text_total_order.setText( "סך כל ההזמנות: " + "₪"  +String.valueOf(total_price));
                text_total_credit.setText( "סך אשראי: " + "₪" + total_credit);
                text_total_cash.setText( "סך מזומן: " + "₪" +total_cash );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        Button get_info = findViewById(R.id.button);
        get_info.setOnClickListener(new View.OnClickListener() {
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





//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
    }
//
//    private List<IBarDataSet> getDataSet() {
//        ArrayList<IBarDataSet> dataSets = null;
//
//        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
//        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
//        valueSet1.add(v1e1);
//        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
//        valueSet1.add(v1e2);
//        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
//        valueSet1.add(v1e3);
//        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
//        valueSet1.add(v1e4);
//        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
//        valueSet1.add(v1e5);
//        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
//        valueSet1.add(v1e6);
//
////        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
////        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
////        valueSet2.add(v2e1);
////        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
////        valueSet2.add(v2e2);
////        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
////        valueSet2.add(v2e3);
////        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
////        valueSet2.add(v2e4);
////        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
////        valueSet2.add(v2e5);
////        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
////        valueSet2.add(v2e6);
//
//        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
//        barDataSet1.setColors( ColorTemplate.COLORFUL_COLORS);
//      //  barDataSet1.setColor(Color.rgb(0, 155, 0));
////        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
////        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        dataSets = new ArrayList<>();
//
//        dataSets.add(barDataSet1);
//       // dataSets.add(barDataSet2);
//        return dataSets;
//    }
//
//    private ArrayList<String> getXAxisValues() {
//        ArrayList<String> xAxis = new ArrayList<>();
//        xAxis.add("JAN");
//        xAxis.add("FEB");
//        xAxis.add("MAR");
//        xAxis.add("APR");
//        xAxis.add("MAY");
//        xAxis.add("JUN");
//        return xAxis;
//    }
    public void display_data(Boolean is_price)
    {


//        ArrayList<String> labels = new ArrayList<>();
//        labels.add("January");
//        labels.add("February");
//        labels.add("March");
//        labels.add("April");
//        labels.add("May");
//        labels.add("June");
        HorizontalBarChart  mChart;
        if (is_price)
        {
            mChart = (HorizontalBarChart) findViewById(R.id.chart_price);
        }
        else
        {
            mChart = (HorizontalBarChart) findViewById(R.id.chart_amount);
        }
        mChart.setVisibility(View.VISIBLE);
    mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {
            int chosen = (int)e.getX();
            Toast.makeText(getApplicationContext(), "בחרת בלקוח: "+cotumer_names.get(chosen), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CostumerListActivity.this, HistoryActivity.class);
            Bundle b = new Bundle();
            b.putString("costumer_name",costumer_info.get(chosen).getName());
            b.putString("costumer_phone",costumer_info.get(chosen).getPhone());

            intent.putExtras(b); //Put your id to your
            startActivity(intent);
        }

        @Override
        public void onNothingSelected() {

        }
    });
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        //mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);


        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        CategoryBarChartXaxisFormatter xaxisFormatter = new CategoryBarChartXaxisFormatter(cotumer_names);
        xl.setValueFormatter(xaxisFormatter);
        xl.setGranularity(1);

        YAxis yl = mChart.getAxisLeft();
        yl.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yl.setDrawGridLines(false);
        yl.setEnabled(false);
        yl.setAxisMinimum(0f);

        YAxis yr = mChart.getAxisRight();
        yr.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < cotumer_names.size(); i++) {
            yVals1.add(new BarEntry(i, is_price?prices.get(i):amounts.get(i)));
        }


        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "DataSet 1");
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(.9f);
        mChart.setData(data);
        Description d = new Description();
        if(is_price)
        {
            d.setText("סכום הזמנה");
        }
        else
        {
            d.setText("כמות הזמנות");
        }
        d.setTextColor(Color.RED);
        mChart.setDescription(d);
        mChart.getLegend().setEnabled(false);
        mChart.getLayoutParams().height = 80*cotumer_names.size();
        mChart.invalidate();




    }
    public class CategoryBarChartXaxisFormatter implements IAxisValueFormatter {

        ArrayList<String> mValues;

        public CategoryBarChartXaxisFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            int val = (int) value;
            String label = "";
            if (val >= 0 && val < mValues.size()) {
                label = mValues.get(val);
            } else {
                label = "";
            }
            return label;
        }
    }

    private class AsyncTaskcreateGraph extends AsyncTask<Void, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(Void... params) {
            map2graph.clear();
           for(Delivery temp : array)
           {
               if((temp.getDate().compareTo(date_start) >= 0 ) && (temp.getDate().compareTo(date_end) < 0))
               {
                   Map_Key_Address_Name temp_key = new Map_Key_Address_Name(temp.getCostumerName(),temp.getCostumer_phone());
                   if (map2graph.containsKey(temp_key)) {
                       Map_Value_Price_Amount extracted = map2graph.get(temp_key);
                       extracted.addAmount();
                       extracted.addPrice(Float.valueOf(temp.getPrice()));
                   } else {
                       map2graph.put(temp_key, new Map_Value_Price_Amount(Float.valueOf(temp.getPrice()), 1));
                   }
               }
           }
            return "";
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
        cotumer_names.clear();
        amounts.clear();
        prices.clear();
            for(Map_Key_Address_Name key: map2graph.keySet())
            {
                cotumer_names.add(key.getName());
                costumer_info.add(key);
            }
            for(Map_Value_Price_Amount val: map2graph.values())
            {
                amounts.add(val.getAmout());
                prices.add(val.getPrice());
            }
            progressDialog.dismiss();
            if(cotumer_names.isEmpty())
            {

               TextView hidden_text = findViewById(R.id.hidden_text);
               hidden_text.setVisibility(View.VISIBLE);
                chart_price.setVisibility(View.GONE);
                chart_amount.setVisibility(View.GONE);
            }
            else
            {
                TextView hidden_text = findViewById(R.id.hidden_text);
                hidden_text.setVisibility(View.GONE);
                display_data(true);
                display_data(false);
            }



        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CostumerListActivity.this,
                    "ProgressDialog",
                    "המתן לתוצאות המיון ");
        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
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
                Toast.makeText(CostumerListActivity.this, "לא נבחר תאריך",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CostumerListActivity.this, "לא נבחר תאריך",Toast.LENGTH_SHORT).show();
                return;
            }
            AsyncTaskcreateGraph graph = new AsyncTaskcreateGraph();
            graph.execute();

        }
    }
}
