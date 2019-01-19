package com.example.leonid.jetpack.jetpack_shlihim.Objects;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.leonid.jetpack.jetpack_shlihim.MainActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RouteCalculation {
    Boolean update_db_after_assign;
    Delivery to_assign_delivery;
    Boolean update_delivery_guy_with_moved_delivery;
    String index_index_update_Delivery_guy;
    ArrayList<DistanceDuration> durations_list;
    DeliveryGuys chosen_delivery_guy;
    ArrayList<Destination> array;
    ArrayList<Delivery> array_deliv;
    Context context;
    public final double motorcycle_decrease = 0.85;
  //  private DataBaseManager dbm = new DataBaseManager();

    public Context getContext() {
        return context;
    }


    public RouteCalculation(Boolean update_db_after_assign, Delivery to_assign_delivery, Boolean update_delivery_guy_with_moved_delivery, String index_guy,
                            ArrayList<DistanceDuration> durations_list, DeliveryGuys chosen_delivery_guy, ArrayList<Destination> array, ArrayList<Delivery> array_deliv, Context context) {
        this.update_db_after_assign = update_db_after_assign;
        this.to_assign_delivery = to_assign_delivery;
        this.update_delivery_guy_with_moved_delivery = update_delivery_guy_with_moved_delivery;
        this.index_index_update_Delivery_guy = index_guy;
        this.durations_list = durations_list;
        this.chosen_delivery_guy = chosen_delivery_guy;
        this.array = array;
        this.array_deliv = array_deliv;
        this.context = context;
    }

    public void calculate_routes()
    {

        durations_list.clear();
        double iter_lat = chosen_delivery_guy.getLatetude();
        double iter_long = chosen_delivery_guy.getLongtitude();
        for (Destination d : array)
        {

            DistanceDuration dd = new DistanceDuration();
            LatLng source = new LatLng(iter_lat,iter_long);
            LatLng dest = new LatLng(d.getLatitude(),d.getLongitude());
            dd.start_duration_exec(source,dest,this,update_db_after_assign,array.size(),to_assign_delivery,update_delivery_guy_with_moved_delivery,index_index_update_Delivery_guy);
            durations_list.add(dd);
            iter_lat = d.getLatitude();
            iter_long = d.getLongitude();

        }
    }
    public void get_duration()
    {
        Calendar cal = Calendar.getInstance();
        Date curr_iter_date = cal.getTime();
        Date new_date = new Date();
        for (int i = 0; i < array.size(); i++) {

            new_date.setTime((long) (curr_iter_date.getTime() + (durations_list.get(i).getDuration() * 60 * 1000 * motorcycle_decrease)));
            String hour;
            String minutes;
            if (new_date.getHours() < 10) {
                hour = "0" + new_date.getHours();
            } else {
                hour = "" + new_date.getHours();
            }
            if (new_date.getMinutes() < 10) {
                minutes = "0" + new_date.getMinutes();

            } else {
                minutes = "" + new_date.getMinutes();
            }
            String date_deliver = hour + ":" + minutes;
            array.get(i).setTimeDeliver(date_deliver);
            curr_iter_date = new_date;
        }
    }
    public void callCallback(Boolean update_db_after_assign,Delivery to_assign_delivery
            ,Boolean update_delivery_guy_with_moved_delivery,String index_index_update_Delivery_guy) {

        get_duration();
//        if (context instanceof PendingDeliveriesForGuyActivity) {
//            ((PendingDeliveriesForGuyActivity) context).updateUiCallback(update_db_after_assign,to_assign_delivery,update_delivery_guy_with_moved_delivery,index_index_update_Delivery_guy);
//        }
//        if (context instanceof DeliveryDataActivity)
//        {
//            ((DeliveryDataActivity) context).updateUiCallback(array,array_deliv,chosen_delivery_guy);
//        }
        if (context instanceof MainActivity)
        {
            ((MainActivity) context).updateUiCallback(array,array_deliv,chosen_delivery_guy);
        }

    }





}
