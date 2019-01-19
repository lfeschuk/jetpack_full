package com.example.leonid.jetpack.jetpack_shlihim.Objects;


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
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.leonid.jetpack.jetpack_shlihim.MainActivity;
import com.example.leonid.jetpack.jetpack_shlihim.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Alerts {
    public static void register(Activity activity) {
        AlertReceiver.register(activity);
    }
    static Boolean is_dialog_set = false;

    public static void unregister(Activity activity) {
        AlertReceiver.unregister(activity);
    }

    public static ArrayList<String> index_arr = new ArrayList<>();
    public static void displayDialog(Context context) {
        Intent intent = new Intent("MyApplication.DisplayDialog");
       // intent.putExtra(Intent.EXTRA_TEXT, index);
        context.sendOrderedBroadcast(intent, null);
    }

    private static void showAlertDialog(Context context) {
        if (is_dialog_set)
        {
            return;
        }
        is_dialog_set = true;
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        Log.d("Alerts", " show alert new");
   //     index_arr.add(index);
        // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //  LayoutInflater li = LayoutInflater.from(getActivity());
        //    final View myView = li.inflate(R.layout.checkbox_sort_deliveries, null);
        // MainActivity.player.reset();
        MainActivity.player =  MediaPlayer.create(context,  R.raw.vibra);
        MainActivity.player.setLooping(true); // Set looping
        MainActivity. player.setVolume(100,100);
        MainActivity.player.start();
        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (MainActivity.player != null) {
                    MainActivity.player.stop();
                    MainActivity.player.release();
                    MainActivity.player = null;
                }
            }
        }.start();


//        try {
//            MainActivity.player.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        dialog.setContentView(R.layout.route_is_changed);
        Button b  =dialog.findViewById(R.id.button2);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.player != null) {
                    MainActivity.player.stop();
                    MainActivity.player.release();
                    MainActivity.player = null;
                }
                is_dialog_set = false;
                dialog.dismiss();
                // MainActivity.player.stop();
            }
        });
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
         //   String msg = intent.getStringExtra(Intent.EXTRA_TEXT);
            showAlertDialog(activityContext);
        }
    }

}