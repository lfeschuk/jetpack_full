package com.example.leonid.jetpack.restoraunt.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.leonid.jetpack.restoraunt.R;

import java.util.ArrayList;

import Objects.Delivery;

public class recycleAdapterHistoryDeliveries extends RecyclerView.Adapter<recycleAdapterHistoryDeliveries.ViewHolder> {

public static final String TAG = "recycleAdapterHistory";

private ArrayList<Delivery> deliveries;
private ItemClickListener itemClickListener;

public recycleAdapterHistoryDeliveries(ArrayList<Delivery> objects, @NonNull ItemClickListener itemClickListener) {
        this.deliveries = objects;
        this.itemClickListener = itemClickListener;
        Log.d(TAG,"deliveries " + deliveries.size());
        setHasStableIds(true);
        }

@Override
public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        Context context = viewGroup.getContext();
        View parent = LayoutInflater.from(context).inflate(R.layout.list_element_history, viewGroup, false);
        return ViewHolder.newInstance(parent);
        }

    @Override
public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Delivery delivery = deliveries.get(position);
        Log.d(TAG,"position " + position);
        viewHolder.setIndex(delivery.getIndexString(),delivery.getIs_deleted());
        viewHolder.setDate(delivery.getDate(),delivery.getTimeInserted());
        viewHolder.setCostumer_adress(delivery.getAdressTo());
        viewHolder.setName_of_delivery_guy(delivery.getStatus(),delivery.getDeliveryGuyName());
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


public static final class ViewHolder extends RecyclerView.ViewHolder {
    private final View parent;
    private final TextView index_delivery;
    private final TextView name_of_delivery_guy;
    private final TextView date;
    private final TextView costumer_adress;




    public static ViewHolder newInstance(View parent) {

        TextView index_delivery = (TextView) parent.findViewById(R.id.index);
        TextView name_of_delivery_guy = (TextView) parent.findViewById(R.id.deliv_guy_name);
        TextView costumer_adress = (TextView) parent.findViewById(R.id.adress);
        TextView date = (TextView) parent.findViewById(R.id.date_of_order);
        return new ViewHolder(parent, index_delivery, costumer_adress,date,name_of_delivery_guy);
    }

    private ViewHolder(View parent,  TextView index_delivery,TextView costumer_adress, TextView name_of_delivery_guy,TextView date) {
        super(parent);
        this.parent = parent;
        this.index_delivery = index_delivery;
        this.date = date;
        this.costumer_adress = costumer_adress;
        this.name_of_delivery_guy = name_of_delivery_guy;
    }


    public void setIndex(CharSequence text,Boolean is_deleted) {
        index_delivery.setText("#" + text);
        if (is_deleted)
        {
            index_delivery.setTextColor(Color.RED);
        }
        else
        {
            index_delivery.setTextColor(Color.GREEN);
        }
    }


    public void setName_of_delivery_guy(String status,CharSequence text) {
        switch(status)
        {
            case "A":
                name_of_delivery_guy.setText("ללא שליח");
                name_of_delivery_guy.setTextColor(Color.YELLOW);
                break;
            default:
                name_of_delivery_guy.setText(text);
                name_of_delivery_guy.setTextColor(Color.MAGENTA);

        }


    }

    public void setCostumer_adress(CharSequence text) {
        costumer_adress.setText(text);
    }

    public void setDate(CharSequence date_input,CharSequence time_inserted) {
        date.setText(date_input + " " + time_inserted);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        parent.setOnClickListener(listener);
    }
}

public interface ItemClickListener {
    void itemClicked(Delivery delivery);
}
}