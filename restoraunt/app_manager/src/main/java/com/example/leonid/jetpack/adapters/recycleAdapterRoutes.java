package com.example.leonid.jetpack.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.leonid.jetpack.R;

import java.util.ArrayList;

import Objects.Delivery;
import Objects.Destination;

public class recycleAdapterRoutes extends RecyclerView.Adapter<recycleAdapterRoutes.ViewHolder>{


    private ArrayList<Destination> destinations;
    private ItemClickListener itemClickListener;

    public recycleAdapterRoutes(ArrayList<Destination> objects, @NonNull ItemClickListener itemClickListener) {
        this.destinations = objects;
        this.itemClickListener = itemClickListener;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        Context context = viewGroup.getContext();
        View parent = LayoutInflater.from(context).inflate(R.layout.adapter_layout_delivery_routes, viewGroup, false);
        return ViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Destination d = destinations.get(position);
        viewHolder.setIndex(d.getIndex_string());
        viewHolder.setTime_of_order(d.getTimeInserted());
        viewHolder.setTime_to_destination(d.getTimeDeliver());
        viewHolder.setDeliveryAndAdresses(d.getTo_costumer(),d.getName_costumer(),d.getAdressTo(),d.getBusiness_name(),d.getAdressFrom());

        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClicked(d);
            }
        });
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }
    @Override
    public long getItemId(int position) {
        return destinations.get(position).getIndex();
    }

    public void moveItem(int start, int end) {
        int max = Math.max(start, end);
        int min = Math.min(start, end);
        if (min >= 0 && max < destinations.size()) {
            Destination item = destinations.remove(min);
            destinations.add(max, item);
            notifyItemMoved(min, max);
        }
    }

    public int getPositionForId(long id) {
        for (int i = 0; i < destinations.size(); i++) {
            if (destinations.get(i).getIndex() == id) {
                return i;
            }
        }
        return -1;
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final View parent;
        private final TextView index_delivery;
        private final TextView time_of_order;
        private final TextView time_to_destination;
        private final TextView addresses;
        private final TextView from_where_the_delivery;

        public static ViewHolder newInstance(View parent) {

            TextView index_delivery = (TextView) parent.findViewById(R.id.index_delivery_routes);
            TextView time_of_order =  (TextView) parent.findViewById(R.id.time_of_order_routes);
            TextView time_to_destination =  (TextView) parent.findViewById(R.id.time_to_destination_routes);
            TextView addresses = (TextView) parent.findViewById(R.id.addresses_routes);
            TextView from_where_the_delivery = (TextView) parent.findViewById(R.id.from_where_the_delivery_routes);

            return new ViewHolder(parent, index_delivery, time_of_order, time_to_destination,from_where_the_delivery,addresses);
        }

        private ViewHolder(View parent,  TextView index_delivery, TextView time_of_order, TextView time_to_destination,TextView from_where_the_delivery,
                           TextView addresses) {
            super(parent);
            this.parent = parent;
            this.index_delivery = index_delivery;
            this.time_of_order = time_of_order;
            this.time_to_destination = time_to_destination;
            this.addresses = addresses;
            this.from_where_the_delivery = from_where_the_delivery;
        }


        public void setIndex(CharSequence text) {
            index_delivery.setText(text);
        }

        public void setTime_of_order(CharSequence text) {
            time_of_order.setText(text);
        }

        public void setTime_to_destination(CharSequence text) {
            time_to_destination.setText(text);
        }
        public void setDeliveryAndAdresses(Boolean to_costumer,String costumer_name,String adressTo,String businessName,String adressFrom) {
            if (to_costumer)
            {
                String adress_or_cost_name = "(" + costumer_name + ")";
                addresses.setText(adressTo + adress_or_cost_name);
                from_where_the_delivery.setText("משלוח מ:" + businessName);
                from_where_the_delivery.setVisibility(View.VISIBLE);


            }
            else
            {
                String adress_or_cost_name = "(" + adressFrom + ")";
                addresses.setText(businessName + adress_or_cost_name);
            }
        }





        public void setOnClickListener(View.OnClickListener listener) {
            parent.setOnClickListener(listener);
        }
    }

    public interface ItemClickListener {
        void itemClicked(Destination d);
    }
}