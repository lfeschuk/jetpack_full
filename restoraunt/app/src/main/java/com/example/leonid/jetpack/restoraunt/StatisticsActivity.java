package com.example.leonid.jetpack.restoraunt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonid.jetpack.restoraunt.UIClasses.MyDatePickerFragment;
import com.example.leonid.jetpack.restoraunt.adapters.recycleAdapterHistoryDeliveries;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Objects.Delivery;

public class StatisticsActivity extends AppCompatActivity {

    Query mDatabase;
    StatisticsActivity this_context = this;
    private Toolbar toolbar;

    int total_delivery_from_arrived = 0;
    int total_delivery_from_inserted = 0;

    int total_delivery_from_arrived_1 = 0;
    int total_delivery_from_inserted_1 = 0;

    int total_delivery_from_arrived_2 = 0;
    int total_delivery_from_inserted_2 = 0;

    int total_delivery_from_arrived_3 = 0;
    int total_delivery_from_inserted_3 = 0;

    int total_delivery_from_arrived_4 = 0;
    int total_delivery_from_inserted_4 = 0;

    int total_delivery_from_arrived_5 = 0;
    int total_delivery_from_inserted_5 = 0;

    int total_delivery_from_arrived_6 = 0;
    int total_delivery_from_inserted_6 = 0;

    int total_delivery_from_arrived_7 = 0;
    int total_delivery_from_inserted_7 = 0;
    private ArrayList<Delivery> array = new ArrayList<>();
    ArrayList<String> array_insert_time = new ArrayList<>();
    //10:00 -0   24:00 -14
   int[] histogram_hours = new int[15];
    String date_start = "";
    String date_end = "";
    //    PendingDeliveriesForGuyActivity this_context = this;
//    Delivery to_assign_delivery = null;
//    DeliveryGuys chosen_delivery_guy = null;
    //  ArrayList<DistanceDuration> durations_list = new ArrayList<>();
    final static String TAG = "StatisticsActivity";
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);

        Log.d(TAG,"send to Db");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.main_title_text);
        title.setText("סטטיסטיקה");



        mDatabase =  FirebaseDatabase.getInstance().getReference("Deliveries").orderByChild("restoraunt_key").equalTo(MainActivity.this_restoraunt.getKey());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long total_inserted = 0;
                long total_arrived = 0;
                int total_sunday = 0;
                int total_monday = 0;
                int total_tuesday = 0;
                int total_wednesday = 0;
                int total_thursday = 0;
                int total_friday = 0;
                int total_saturday = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Delivery temp = new Delivery(ds.getValue(Delivery.class));

                    if (!temp.getIs_deleted() && temp.getStatus().equals("D"))
                    {
                        array.add(temp);
                        array_insert_time.add(temp.getTimeInserted());

                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(temp.getDate());
                            String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday


                            Date delivered =  new SimpleDateFormat("HH:mm").parse(temp.getTimeDeliver());
                            Date inserted = new SimpleDateFormat("HH:mm").parse(temp.getTimeInserted());
                            Date arrived = new SimpleDateFormat("HH:mm").parse(temp.getTimeTaken());
                            //in minutes
                            long diff_inserted = (delivered.getTime() - inserted.getTime())/(1000*60);
                            long diff_arrived = (delivered.getTime() - arrived.getTime())/(1000*60);

                            switch (dayOfTheWeek)
                            {
                                case "Sunday":
                                    total_delivery_from_arrived_1 += diff_arrived;
                                    total_delivery_from_inserted_1 += diff_inserted;
                                    total_sunday++;
                                    break;
                                case "Monday":
                                    total_delivery_from_arrived_2 += diff_arrived;
                                    total_delivery_from_inserted_2 += diff_inserted;
                                    total_monday++;
                                    break;
                                case "Tuesday":
                                    total_delivery_from_arrived_3 += diff_arrived;
                                    total_delivery_from_inserted_3 += diff_inserted;
                                    total_tuesday++;
                                    break;
                                case "Wednesday":
                                    total_delivery_from_arrived_4 += diff_arrived;
                                    total_delivery_from_inserted_4 += diff_inserted;
                                    total_wednesday++;
                                    break;
                                case "Thursday":
                                    total_delivery_from_arrived_5 += diff_arrived;
                                    total_delivery_from_inserted_5 += diff_inserted;
                                    total_thursday++;
                                    break;
                                case "Friday":
                                    total_delivery_from_arrived_6 += diff_arrived;
                                    total_delivery_from_inserted_6 += diff_inserted;
                                    total_friday++;
                                    break;
                                case "Saturday":
                                    total_delivery_from_arrived_7 += diff_arrived;
                                    total_delivery_from_inserted_7 += diff_inserted;
                                    total_saturday++;
                                    break;
                            }

                           // Log.d(TAG,"delivered time: " + temp.getTimeDeliver() + " inserted time: " + temp.getTimeInserted() + " diff: " + diff_inserted) ;
                            total_arrived += diff_arrived;
                            total_inserted += diff_inserted;

                        } catch (ParseException e) {
                            e.printStackTrace();
                           // Log.d(TAG,"fffffffffff");
                        }

                    }

                }
                if (!array.isEmpty()) {
                    total_delivery_from_arrived = (int) (total_arrived / array.size());
                    total_delivery_from_inserted = (int) total_inserted / array.size();
                }

                if (total_sunday != 0)
                {
                    total_delivery_from_arrived_1 /= total_sunday;
                    total_delivery_from_inserted_1 /= total_sunday;
                }
                if (total_monday != 0)
                {
                    total_delivery_from_arrived_2 /= total_monday;
                    total_delivery_from_inserted_2 /= total_monday;
                }
                if (total_tuesday != 0)
                {
                    total_delivery_from_arrived_3 /= total_tuesday;
                    total_delivery_from_inserted_3 /= total_tuesday;
                }
                if (total_wednesday != 0)
                {
                    total_delivery_from_arrived_4 /= total_wednesday;
                    total_delivery_from_inserted_4 /= total_wednesday;
                }
                if (total_thursday != 0)
                {
                    total_delivery_from_arrived_5 /= total_thursday;
                    total_delivery_from_inserted_5 /= total_thursday;
                }
                if (total_friday != 0)
                {
                    total_delivery_from_arrived_6 /= total_friday;
                    total_delivery_from_inserted_6 /= total_friday;
                }
                if (total_saturday != 0)
                {
                    total_delivery_from_arrived_7 /= total_saturday;
                    total_delivery_from_inserted_7 /= total_saturday;
                }



                setUI();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });







//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
    }
    public void setUI()
    {
        TextView arrived = (TextView)findViewById(R.id.text_amount_delivery);
        Log.d(TAG,"arrived " + arrived + " total:" + total_delivery_from_arrived);
        arrived.setText("" + total_delivery_from_arrived);

        TextView inserted = findViewById(R.id.text_amount_delivery2);
        inserted.setText("" + total_delivery_from_inserted);

        //days

        TextView arrived1 = findViewById(R.id.text_amount_sunday);
        arrived1.setText("" + total_delivery_from_arrived_1);

        TextView inserted1 = findViewById(R.id.text_amount2_sunday);
        inserted1.setText("" + total_delivery_from_inserted_1);

        TextView arrived2 = findViewById(R.id.text_amount_monday);
        arrived2.setText("" + total_delivery_from_arrived_2);

        TextView inserted2 = findViewById(R.id.text_amount2_monday);
        inserted2.setText("" + total_delivery_from_inserted_2);

        TextView arrived3 = findViewById(R.id.text_amount_tuesday);
        arrived3.setText("" + total_delivery_from_arrived_3);

        TextView inserted3 = findViewById(R.id.text_amount2_tuesday);
        inserted3.setText("" + total_delivery_from_inserted_3);

        TextView arrived4 = findViewById(R.id.text_amount_wednesday);
        arrived4.setText("" + total_delivery_from_arrived_4);

        TextView inserted4 = findViewById(R.id.text_amount2_wednesday);
        inserted4.setText("" + total_delivery_from_inserted_4);

        TextView arrived5 = findViewById(R.id.text_amount_thursday);
        arrived5.setText("" + total_delivery_from_arrived_5);

        TextView inserted5 = findViewById(R.id.text_amount2_thursday);
        inserted5.setText("" + total_delivery_from_inserted_5);


        TextView arrived6 = findViewById(R.id.text_amount_friday);
        arrived6.setText("" + total_delivery_from_arrived_6);

        TextView inserted6 = findViewById(R.id.text_amount2_friday);
        inserted6.setText("" + total_delivery_from_inserted_6);

        TextView arrived7 = findViewById(R.id.text_amount_saturday);
        arrived7.setText("" + total_delivery_from_arrived_7);

        TextView inserted7 = findViewById(R.id.text_amount2_saturday);
        inserted7.setText("" + total_delivery_from_inserted_7);


        try {
            fill_histogramm();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        display_data();
        ImageButton maps = findViewById(R.id.searchImageButton);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatisticsActivity.this, MapsActivityNew.class);
                Bundle b = new Bundle();
//                b.putString("phone",costumer_phone);
//                intent.putExtras(b); //Put yo
                String arrayAsString = new Gson().toJson(array);
                b.putString("array", arrayAsString);
                intent.putExtras(b);

//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });




    }
    public void fill_histogramm() throws ParseException {
        for (String s: array_insert_time)
        {
           int  index_insert = 0;
            Date d =  new SimpleDateFormat("HH:mm").parse(s);
            String Hour  = (String) DateFormat.format("HH",  d);
            String Minutes  = (String) DateFormat.format("mm",   d);
            int hour_idx = Integer.valueOf(Hour);
            int min_int = Integer.valueOf(Minutes);
            if (hour_idx >= 0 && hour_idx < 3 ) //assume not in the night
            {
                index_insert = 14;
            }
            else if (hour_idx <10 )
            {
                index_insert = 0;
            }
            else
            {
                index_insert = hour_idx - 10;
                if (min_int > 30)
                {
                    index_insert++;
                }

            }
            histogram_hours[index_insert]++;
        }
    }

    public void display_data()
    {


//        ArrayList<String> labels = new ArrayList<>();
//        labels.add("January");
//        labels.add("February");
//        labels.add("March");
//        labels.add("April");
//        labels.add("May");
//        labels.add("June");
        LineChart chart;

        chart = (LineChart) findViewById(R.id.chart_line);


        chart.setVisibility(View.VISIBLE);
//        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
//                int chosen = (int)e.getX();
//                Toast.makeText(getApplicationContext(), "בחרת בלקוח: "+cotumer_names.get(chosen), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(CostumerListActivity.this, HistoryActivity.class);
//                Bundle b = new Bundle();
//                b.putString("costumer_name",costumer_info.get(chosen).getName());
//                b.putString("costumer_phone",costumer_info.get(chosen).getPhone());
//
//                intent.putExtras(b); //Put your id to your
//                startActivity(intent);
//            }
//
//            @Override
//            public void onNothingSelected() {
//
//            }
//        });


        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);

        ArrayList<Entry> entries = new ArrayList<>();
        for(int i = 0 ;i<15;i++)
        {
            entries.add(new Entry(i+10, histogram_hours[i]));
        }



        LineDataSet dataSet = new LineDataSet(entries, "Customized values");
        dataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSet.setDrawValues(false);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        chart.getLegend().setEnabled(false);
       // dataSet.setValueTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        //****
        // Controlling X axis
        XAxis xAxis = chart.getXAxis();
        // Set the xAxis position to bottom. Default is top
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //Customizing x axis value
//        final String[] hours = new String[]{"10","11", "12","13", "14", "15","16","17","18","19","20","21","22","23","00"};
//
//        IAxisValueFormatter formatter = new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return hours[(int) value];
//            }
//        };
//        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
//        xAxis.setValueFormatter(formatter);

        //***
        // Controlling right side of y axis
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setEnabled(false);

        //***
        // Controlling left side of y axis
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setGranularity(1f);

        // Setting Data
        LineData data = new LineData(dataSet);
        chart.setData(data);
        chart.animateX(10);
        //refresh
        chart.invalidate();


//        HorizontalBarChart  mChart;
//        if (is_price)
//        {
//            mChart = (HorizontalBarChart) findViewById(R.id.chart_price);
//        }
//        else
//        {
//            mChart = (HorizontalBarChart) findViewById(R.id.chart_amount);
//        }
//        mChart.setVisibility(View.VISIBLE);
//        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
//                int chosen = (int)e.getX();
//                Toast.makeText(getApplicationContext(), "בחרת בלקוח: "+cotumer_names.get(chosen), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(CostumerListActivity.this, HistoryActivity.class);
//                Bundle b = new Bundle();
//                b.putString("costumer_name",costumer_info.get(chosen).getName());
//                b.putString("costumer_phone",costumer_info.get(chosen).getPhone());
//
//                intent.putExtras(b); //Put your id to your
//                startActivity(intent);
//            }
//
//            @Override
//            public void onNothingSelected() {
//
//            }
//        });
//        mChart.setDrawBarShadow(false);
//        mChart.setDrawValueAboveBar(true);
//        //mChart.getDescription().setEnabled(false);
//        mChart.setPinchZoom(false);
//        mChart.setDrawGridBackground(false);
//
//
//        XAxis xl = mChart.getXAxis();
//        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xl.setDrawAxisLine(true);
//        xl.setDrawGridLines(false);
//        CostumerListActivity.CategoryBarChartXaxisFormatter xaxisFormatter = new CostumerListActivity.CategoryBarChartXaxisFormatter(cotumer_names);
//        xl.setValueFormatter(xaxisFormatter);
//        xl.setGranularity(1);
//
//        YAxis yl = mChart.getAxisLeft();
//        yl.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//        yl.setDrawGridLines(false);
//        yl.setEnabled(false);
//        yl.setAxisMinimum(0f);
//
//        YAxis yr = mChart.getAxisRight();
//        yr.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//        yr.setDrawGridLines(false);
//        yr.setAxisMinimum(0f);
//
//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//        for (int i = 0; i < cotumer_names.size(); i++) {
//            yVals1.add(new BarEntry(i, is_price?prices.get(i):amounts.get(i)));
//        }
//
//
//        BarDataSet set1;
//        set1 = new BarDataSet(yVals1, "DataSet 1");
//        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//        dataSets.add(set1);
//        BarData data = new BarData(dataSets);
//        data.setValueTextSize(10f);
//        data.setBarWidth(.9f);
//        mChart.setData(data);
//        Description d = new Description();
//        if(is_price)
//        {
//            d.setText("סכום הזמנה");
//        }
//        else
//        {
//            d.setText("כמות הזמנות");
//        }
//        d.setTextColor(Color.RED);
//        mChart.setDescription(d);
//        mChart.getLegend().setEnabled(false);
//        mChart.getLayoutParams().height = 80*cotumer_names.size();
//        mChart.invalidate();



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
}
