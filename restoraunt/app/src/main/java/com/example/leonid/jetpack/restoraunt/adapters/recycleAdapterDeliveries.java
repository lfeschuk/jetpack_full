package com.example.leonid.jetpack.restoraunt.adapters;



import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leonid.jetpack.restoraunt.Alerts;
import com.example.leonid.jetpack.restoraunt.MainActivity;
import com.example.leonid.jetpack.restoraunt.R;

import java.util.ArrayList;

import Objects.Delivery;

public class recycleAdapterDeliveries extends RecyclerView.Adapter<recycleAdapterDeliveries.ViewHolder> {

    public static final String TAG = "recycleAdapterliveries";

    private ArrayList<Delivery> deliveries;
    private ItemClickListener itemClickListener;

    public recycleAdapterDeliveries(ArrayList<Delivery> objects, @NonNull ItemClickListener itemClickListener) {
        this.deliveries = objects;

        this.itemClickListener = itemClickListener;
        Log.d(TAG,"deliveries " + deliveries.size());
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        Context context = viewGroup.getContext();
        View parent = LayoutInflater.from(context).inflate(R.layout.list_element_deliveries, viewGroup, false);
        return ViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Delivery delivery = deliveries.get(position);
            Log.d(TAG,"position " + position + " time:" + delivery.getTimeInserted());
            viewHolder.setIndex(delivery.getIndexString());
            viewHolder.setTime_creation(delivery.getTimeInserted());
            viewHolder.setTime_to_prepare(delivery.getPrepare_time());
            viewHolder.setStatus(delivery.getStatus());
            viewHolder.setTime_arrived_costumer(delivery.getTime_aprox_deliver());
            viewHolder.setTime_arrived_to_restoraunt(delivery.getStatus(),delivery.getTimeArriveToRestoraunt());
            viewHolder.setCostumer_name(delivery.getCostumerName());
            viewHolder.setCostumer_adress(delivery.getAdressTo());
            viewHolder.setName_of_delivery_guy(delivery.getStatus(),delivery.getDeliveryGuyName());



    }

    @Override
    public int getItemCount() {
        return deliveries.size();
    }
    @Override
    public long getItemId(int position) {
        return deliveries.get(position).getIndex();
    }


    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final View parent;
        private final TextView index_delivery;
        private final TextView time_creation;
        private final TextView time_to_prepare;
        private final TextView name_of_delivery_guy;
        private final TextView status;
        private final TextView time_arrived_costumer;
        private final TextView time_arrived_to_restoraunt;
        private final TextView costumer_name;
        private final TextView costumer_adress;
        private final ImageView smiley;


        public static ViewHolder newInstance(View parent) {

            TextView index_delivery = (TextView) parent.findViewById(R.id.index);
            TextView time_creation =  (TextView) parent.findViewById(R.id.time_creation);
            TextView time_to_prepare =  (TextView) parent.findViewById(R.id.time_prepare);
            TextView name_of_delivery_guy = (TextView) parent.findViewById(R.id.name_deliv_guy);
            TextView status = (TextView) parent.findViewById(R.id.delivery_status);
            TextView time_arrived_costumer = (TextView) parent.findViewById(R.id.time_arrive_to_costumer);
            TextView time_arrived_to_restoraunt = (TextView) parent.findViewById(R.id.time_arrive_to_restoraunt);
            TextView costumer_name = (TextView) parent.findViewById(R.id.costumer_name);
            TextView costumer_adress = (TextView) parent.findViewById(R.id.costumer_adress);
            ImageView smiley =  parent.findViewById(R.id.food_arrived_image);
            return new ViewHolder(parent, index_delivery, time_creation, time_to_prepare,status,costumer_name,time_arrived_to_restoraunt,
                    costumer_adress,time_arrived_costumer,name_of_delivery_guy,smiley);
        }

        private ViewHolder(View parent,  TextView index_delivery, TextView time_creation, TextView time_to_prepare,TextView name_of_delivery_guy,
                           TextView status,TextView time_arrived_costumer,  TextView time_arrived_to_restoraunt,TextView costumer_name ,TextView costumer_adress
        ,ImageView smiley) {
            super(parent);
            this.parent = parent;
            this.index_delivery = index_delivery;
            this.time_creation = time_creation;
            this.time_to_prepare = time_to_prepare;
            this.status = status;
            this.time_arrived_costumer = time_arrived_costumer;
            this.time_arrived_to_restoraunt = time_arrived_to_restoraunt;
            this.costumer_name = costumer_name;
            this.costumer_adress = costumer_adress;
            this.name_of_delivery_guy = name_of_delivery_guy;
            this.smiley = smiley;
        }


        public void setIndex(final CharSequence text) {
            for(String index: Alerts.index_arr)
            {
                Log.d(TAG,"index: " + index);
                if (text.toString().equals(index))
                {
                    Log.d(TAG,"smiley");
                    Drawable drawable = smiley.getDrawable();
                    if (drawable instanceof Animatable) {
                        Log.d(TAG,"smiley is drawable");
                        ((Animatable) drawable).start();
                    }
                    smiley.setVisibility(View.VISIBLE);
                    smiley.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            smiley.setVisibility(View.GONE);
                            if (MainActivity.player != null) {
                                Log.d(TAG,"smiley stop the playuer");
                                MainActivity.player.stop();
                                MainActivity.player.release();
                                MainActivity.player = null;
                            }
                            Alerts.index_arr.remove(text);
                        }
                    });

                    break;
                }
            }
            index_delivery.setText("#" + text);
        }

        public void setTime_creation(CharSequence text) {
            Log.d(TAG,"rrr: " + text);
            time_creation.setText("יצירה: " + text);
        }

        public void setTime_to_prepare(CharSequence text) {
            time_to_prepare.setText("זמן הכנה: " + text + "דק");
        }
        public void setName_of_delivery_guy(String status,CharSequence text) {
            switch(status)
            {
                case "A":
                    name_of_delivery_guy.setText("ללא שליח");
                    name_of_delivery_guy.setTextColor(Color.RED);
                    break;
                default:
                    name_of_delivery_guy.setText(text);
                    name_of_delivery_guy.setTextColor(Color.MAGENTA);

            }


        }

        public void setStatus(CharSequence text) {
            switch(text.toString())
            {
                case "A":
                    status.setText("ממתין לשליח");
                    status.setTextColor(Color.MAGENTA);
                    break;
                case "B":
                    status.setText("השליח בדרך למסעדה");
                    status.setTextColor(Color.YELLOW);
                    break;
                case "C":
                    status.setText("השליח בדרך ללקוח");
                    status.setTextColor(Color.RED);
                    break;
                case "D":
                    status.setText("השליח בבית הלקוח");
                    status.setTextColor(Color.GREEN);
                    break;
            }
        }

        public void setTime_arrived_to_restoraunt(String status,CharSequence text) {
            switch(status)
            {
                case "A":
                    time_arrived_costumer.setText("הגעה למסעדה: " + "תרם צוות שליח");
                    break;
                case "B":
                    time_arrived_costumer.setText("הגעה למסעדה: " + text);
                    break;
                default:
                    time_arrived_costumer.setVisibility(View.GONE);

            }
        }
        public void setTime_arrived_costumer(CharSequence text) {
            time_arrived_to_restoraunt.setText("שעת סיום משוערת: "+ text);
        }

        public void setCostumer_name(CharSequence text) {
            costumer_name.setText(text);
        }
        public void setCostumer_adress(CharSequence text) {
            costumer_adress.setText(text);
        }



        public void setOnClickListener(View.OnClickListener listener) {
            parent.setOnClickListener(listener);
        }
    }

    public interface ItemClickListener {
        void itemClicked(Delivery delivery);
    }
}