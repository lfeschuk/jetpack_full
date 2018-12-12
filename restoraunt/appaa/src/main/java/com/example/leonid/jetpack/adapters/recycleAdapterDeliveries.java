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
        View parent = LayoutInflater.from(context).inflate(R.layout.adapter_layout_deliveries, viewGroup, false);
        return ViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Delivery delivery = deliveries.get(position);
        if (!delivery.getIs_gas_sta()) {
            viewHolder.setIndex(delivery.getIndexString());
            viewHolder.setTime_of_order(delivery.getTimeTaken());
            viewHolder.setTime_to_prepare(delivery.getPrepare_time());
            viewHolder.setStatus(delivery.getStatus());
            viewHolder.setAddresses(delivery.getAdressTo() + "-" + delivery.getAdressFrom());
            viewHolder.setTime_taken(delivery.getTimeTaken());
            viewHolder.setDelta_taken("+2");
            viewHolder.setTime_arrived(delivery.getTimeDeliver());
            viewHolder.setDelta_arrived("-2");
            viewHolder.setComment(delivery.getComment());
            viewHolder.setNum_of_packages(delivery.getNum_of_packets());
            if (!delivery.getStatus().equals("A")) {
                viewHolder.setName_of_delivery_guy(delivery.getDeliveryGuyName());
            }
            viewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.itemClicked(delivery);
                }
            });
        }
        else
        {
            viewHolder.setTime_of_order(delivery.getTimeTaken());
            viewHolder.setName_of_delivery_guy(delivery.getDeliveryGuyName());
            viewHolder.setAddresses(delivery.getGasStation().getName());
        }
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
        private final TextView time_of_order;
        private final TextView time_to_prepare;
        private final TextView status;
        private final TextView addresses;
        private final TextView time_taken;
        private final TextView delta_taken;
        private final TextView time_arrived;
        private final TextView delta_arrived;
        private final TextView comment;
        private final TextView num_of_packages;
        private final TextView name_of_delivery_guy;

        public static ViewHolder newInstance(View parent) {

            TextView index_delivery = (TextView) parent.findViewById(R.id.index_delivery);
            TextView time_of_order =  (TextView) parent.findViewById(R.id.time_of_order);
            TextView time_to_prepare =  (TextView) parent.findViewById(R.id.time_to_prepare);
            TextView status = (TextView) parent.findViewById(R.id.status);
            TextView addresses = (TextView) parent.findViewById(R.id.addresses);
            TextView time_taken = (TextView) parent.findViewById(R.id.time_taken);
            TextView delta_taken = (TextView) parent.findViewById(R.id.delta_taken);
            TextView time_arrived = (TextView) parent.findViewById(R.id.time_arrived);
            TextView delta_arrived = (TextView) parent.findViewById(R.id.delta_arrived);
            TextView comment = (TextView) parent.findViewById(R.id.comment);
            TextView num_of_packages = (TextView) parent.findViewById(R.id.num_of_packages);
            TextView name_of_delivery_guy = (TextView) parent.findViewById(R.id.name_of_delivery_guy);
            return new ViewHolder(parent, index_delivery, time_of_order, time_to_prepare,status,addresses,time_taken,
                    delta_taken,time_arrived,delta_arrived,comment,num_of_packages,name_of_delivery_guy);
        }

        private ViewHolder(View parent,  TextView index_delivery, TextView time_of_order, TextView time_to_prepare,TextView status,
                           TextView addresses,TextView time_taken,  TextView delta_taken,TextView time_arrived ,TextView delta_arrived,
                           TextView comment,TextView num_of_packages, TextView name_of_delivery_guy) {
            super(parent);
            this.parent = parent;
           this.index_delivery = index_delivery;
           this.time_of_order = time_of_order;
           this.time_to_prepare = time_to_prepare;
           this.status = status;
           this.addresses = addresses;
           this.time_taken = time_taken;
           this.delta_taken = delta_taken;
           this.time_arrived = time_arrived;
           this.delta_arrived = delta_arrived;
           this.comment = comment;
           this.num_of_packages = num_of_packages;
           this.name_of_delivery_guy = name_of_delivery_guy;
        }


        public void setIndex(CharSequence text) {
            index_delivery.setText(text);
        }

        public void setTime_of_order(CharSequence text) {
            time_of_order.setText(text);
        }

        public void setTime_to_prepare(CharSequence text) {
            time_to_prepare.setText(text);
        }
        public void setStatus(CharSequence text) {
            status.setText(text);
        }

        public void setAddresses(CharSequence text) {
            addresses.setText(text);
        }

        public void setTime_taken(CharSequence text) {
            time_taken.setText(text);
        }
        public void setDelta_taken(CharSequence text) {
            delta_taken.setText(text);
        }

        public void setTime_arrived(CharSequence text) {
            time_arrived.setText(text);
        }
        public void setDelta_arrived(CharSequence text) {
            delta_arrived.setText(text);
        }

        public void setComment(CharSequence text) {
            comment.setText(text);
        }
        public void setNum_of_packages(CharSequence text) {
            num_of_packages.setText(text);
        }

        public void setName_of_delivery_guy(CharSequence text) {
            name_of_delivery_guy.setText(text);
            name_of_delivery_guy.setVisibility(View.VISIBLE);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            parent.setOnClickListener(listener);
        }
    }

    public interface ItemClickListener {
        void itemClicked(Delivery delivery);
    }
}