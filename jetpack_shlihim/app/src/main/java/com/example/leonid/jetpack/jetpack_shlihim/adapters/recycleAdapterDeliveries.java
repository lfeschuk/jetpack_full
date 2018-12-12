package com.example.leonid.jetpack.jetpack_shlihim.adapters;



import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.leonid.jetpack.jetpack_shlihim.Objects.Delivery;
import com.example.leonid.jetpack.jetpack_shlihim.R;

import java.util.ArrayList;



public class recycleAdapterDeliveries extends RecyclerView.Adapter<recycleAdapterDeliveries.ViewHolder> {


    private ArrayList<Delivery> deliveries;
    private ItemClickListener itemClickListener;

    public recycleAdapterDeliveries(ArrayList<Delivery> objects, @NonNull ItemClickListener itemClickListener) {
        this.deliveries = objects;
        this.itemClickListener = itemClickListener;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        Context context = viewGroup.getContext();
        View parent = LayoutInflater.from(context).inflate(R.layout.adapter_history_deliveries, viewGroup, false);
        return ViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Delivery delivery = deliveries.get(position);

            viewHolder.setIndex(delivery.getIndexString());
            viewHolder.setRestorauntName(delivery.getBusiness_name());
            viewHolder.setDateTime(delivery.getDate(),delivery.getTimeDeliver());
            viewHolder.setAddresses(delivery.getAdressTo());
            viewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.itemClicked(delivery);
                }
            });


    }

    @Override
    public int getItemCount() {
        return deliveries.size();
    }
    @Override
    public long getItemId(int position) {
        return deliveries.get(position).getIndex();
    }


    public void moveItem(int start, int end) {
        int max = Math.max(start, end);
        int min = Math.min(start, end);
        if (min >= 0 && max < deliveries.size()) {
            Delivery item = deliveries.remove(min);
            deliveries.add(max, item);
            notifyItemMoved(min, max);
        }
    }

    public int getPositionForId(long id) {
        for (int i = 0; i < deliveries.size(); i++) {
            if (deliveries.get(i).getIndex() == id) {
                return i;
            }
        }
        return -1;
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final View parent;
        private final TextView index_delivery;
        private final TextView rest_name;
        private final TextView dateTime;
        private final TextView adress;


        public static ViewHolder newInstance(View parent) {

            TextView index_delivery = (TextView) parent.findViewById(R.id.line_1);
            TextView rest_name =  (TextView) parent.findViewById(R.id.line_2);
            TextView dateTime =  (TextView) parent.findViewById(R.id.line_3);
            TextView adress = (TextView) parent.findViewById(R.id.line_4);

            return new ViewHolder(parent, index_delivery, rest_name, dateTime,adress);
        }

        private ViewHolder(View parent,  TextView index_delivery, TextView rest_name, TextView dateTime,TextView adress) {
            super(parent);
            this.parent = parent;
            this.index_delivery = index_delivery;
            this.rest_name = rest_name;
            this.dateTime = dateTime;
            this.adress = adress;
        }

        public void setIndex(CharSequence text) {
            index_delivery.setText("הזמנה # " + text);
            index_delivery.setTextColor(Color.GREEN);
        }

        public void setRestorauntName(CharSequence text) {
            rest_name.setText(  text);
        }
        public void setDateTime(CharSequence date,CharSequence time) {
            dateTime.setText(  date + " " + time);
        }

        public void setAddresses(CharSequence adresss) {
            adress.setText( adresss );
        }


        public void setOnClickListener(View.OnClickListener listener) {
            parent.setOnClickListener(listener);
        }
    }

    public interface ItemClickListener {
        void itemClicked(Delivery delivery);
    }
}