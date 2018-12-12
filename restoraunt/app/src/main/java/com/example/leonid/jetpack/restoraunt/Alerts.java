package com.example.leonid.jetpack.restoraunt;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Alerts {
    public static void register(Activity activity) {
        AlertReceiver.register(activity);
    }

    public static void unregister(Activity activity) {
        AlertReceiver.unregister(activity);
    }

    public static ArrayList<String> index_arr = new ArrayList<>();
    public static void displayDialog(Context context, String index) {
        Intent intent = new Intent("MyApplication.DisplayDialog");
        intent.putExtra(Intent.EXTRA_TEXT, index);
        context.sendOrderedBroadcast(intent, null);
    }

    private static void showAlertDialog(Context context, String index) {
        final Dialog dialog = new Dialog(context,R.style.Theme_Dialog);
Log.d("Alerts", " show alert new");
        index_arr.add(index);
        // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //  LayoutInflater li = LayoutInflater.from(getActivity());
        //    final View myView = li.inflate(R.layout.checkbox_sort_deliveries, null);
       // MainActivity.player.reset();
        MainActivity.player =  MediaPlayer.create(context,  R.raw.vibra);
        MainActivity.player.setLooping(true); // Set looping
        MainActivity. player.setVolume(100,100);
        MainActivity.player.start();

//        try {
//            MainActivity.player.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        dialog.setContentView(R.layout.found_delivery_guy_dialog);
        Button b  =dialog.findViewById(R.id.button2);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
               // MainActivity.player.stop();
            }
        });

        TextView tv = dialog.findViewById(R.id.textViewId);
        tv.setText("#" + index);

        InsertDeliveryActivity.notifyArrayChanged();
        dialog.show();
    }

    private static class AlertReceiver extends BroadcastReceiver {
        private static HashMap<Activity, AlertReceiver> registrations;

        static {
            registrations = new HashMap<Activity, AlertReceiver>();
        }

        public static Context activityContext;

        private AlertReceiver(Activity activity) {
            activityContext = activity;
        }

        static void register(Activity activity) {
            Log.d("Alert" ," activity reg: " + activity);
            AlertReceiver receiver = new AlertReceiver(activity);
            activity.registerReceiver(receiver, new IntentFilter("MyApplication.DisplayDialog"));
            registrations.put(activity, receiver);
        }

        static void unregister(Activity activity) {
            Log.d("Alert" ," activity unreg: " + activity);
            AlertReceiver receiver = registrations.get(activity);
            if (receiver != null) {
                activity.unregisterReceiver(receiver);
                registrations.remove(activity);
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            Log.d("Alert" ,"  recieve: " + activityContext);
            String msg = intent.getStringExtra(Intent.EXTRA_TEXT);
            showAlertDialog(activityContext, msg);
        }
    }

}