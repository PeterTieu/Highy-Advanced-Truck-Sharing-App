package com.tieutech.highlyadvancedtrucksharingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tieutech.highlyadvancedtrucksharingapp.R;
import com.tieutech.highlyadvancedtrucksharingapp.model.Order;
import com.tieutech.highlyadvancedtrucksharingapp.util.Util;

import java.util.List;

//ABOUT:
//RecyclerView Adapter for the Orders (to be displayed in the OrdersActivity)
//FUNCTION:
//Links the data of each item to be displayed in the RecyclerView to the RecyclerView itself
public class MyOrderRecyclerViewAdapter extends RecyclerView.Adapter<MyOrderRecyclerViewAdapter.ViewHolder> {

    //======= DEFINE VARIABLES =======
    private List<Order> orders; //Arraylist of Orders for the RecyclerView
    private Context context; //Application Context

    //Constructor for the RecyclerView Adapter
    public MyOrderRecyclerViewAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    //======= DEFINE METHODS =======
    //Upon the creation of the ViewHolder of each item in the RecyclerView
    @NonNull
    @Override
    public MyOrderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.order_row, parent, false); //Create the view of the ViewHolder
        return new ViewHolder(itemView); //Link the ViewHolder to the RecyclerView Adapter
    }

    //Modify the display of the view elements in the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull MyOrderRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.senderImageView.setImageBitmap(Util.getBitmapFromBytesArray(orders.get(position).getSenderImage()));
        holder.fromSenderUsernameTextView.setText(orders.get(position).getSenderUsername());
        holder.toReceiverUsernameTextView.setText(orders.get(position).getReceiverUsername());
        holder.goodDescriptionTextView.setText(orders.get(position).getGoodDescription());
    }

    //Return the size of the dataset
    @Override
    public int getItemCount() {
        return orders.size();
    }

    //ViewHolder for each item in the RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView senderImageView; //View for the image of the Order
        TextView fromSenderUsernameTextView;
        TextView toReceiverUsernameTextView;
        TextView goodDescriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Obtain view
            senderImageView = (ImageView) itemView.findViewById(R.id.senderImageView);
            fromSenderUsernameTextView = (TextView) itemView.findViewById(R.id.fromSenderUsernameTextView);
            toReceiverUsernameTextView = (TextView) itemView.findViewById(R.id.toReceiverUsernameTextView);
            goodDescriptionTextView = (TextView) itemView.findViewById(R.id.goodDescriptionTextView);
        }
    }
}