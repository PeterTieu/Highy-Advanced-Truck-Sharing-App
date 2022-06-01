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
//RecyclerView Adapter for the Orders (to be displayed in the HomeActivity and MyOrdersActivity)
//FUNCTION:
//Links the data of each item to be displayed in the RecyclerView to the RecyclerView itself
public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {

    //======= DEFINE VARIABLES =======
    private List<Order> orders; //Arraylist of Orders for the RecyclerView
    private Context context; //Application Context
    private OnOrderListener onOrderClick; //Interface defining methods to override in the MainActivity
    private OnOrderListener onSpeakClick;
    private OnOrderListener onShareClick;

    //Constructor for the RecyclerView Adapter
    public OrderRecyclerViewAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    //Constructor for the RecyclerView Adapter
    public OrderRecyclerViewAdapter(List<Order> orders, Context context, OnOrderListener onOrderClick, OnOrderListener onSpeakClick, OnOrderListener onShareClick) {
        this.orders = orders;
        this.context = context;
        this.onOrderClick = onOrderClick;
        this.onSpeakClick = onSpeakClick;
        this.onShareClick = onShareClick;
    }

    //======= DEFINE METHODS =======
    //Upon the creation of the ViewHolder of each item in the RecyclerView
    @NonNull
    @Override
    public OrderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.order_row, parent, false); //Create the view of the ViewHolder
        return new ViewHolder(itemView, onOrderClick, onSpeakClick, onShareClick); //Link the ViewHolder to the RecyclerView Adapter
    }

    //Modify the display of the view elements in the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull OrderRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.senderImageView.setImageBitmap(Util.getBitmapFromBytesArray(orders.get(position).getSenderImage()));
        holder.fromSenderUsernameTextView.setText(orders.get(position).getSenderUsername());
        holder.toReceiverUsernameTextView.setText(orders.get(position).getReceiverUsername());
        holder.goodDescriptionTextView.setText(orders.get(position).getGoodDescription());
        holder.speakImageView.setImageResource(R.drawable.ic_speak);
        holder.shareImageView.setImageResource(R.drawable.ic_share);
    }

    //Return the size of the dataset
    @Override
    public int getItemCount() {
        return orders.size();
    }

    //ViewHolder for each item in the RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //View variables
        ImageView senderImageView;
        TextView fromSenderUsernameTextView;
        TextView toReceiverUsernameTextView;
        TextView goodDescriptionTextView;
        ImageView speakImageView;
        ImageView shareImageView;

        //Interface variables
        OnOrderListener onOrderClick;
        OnOrderListener onSpeakClick;
        OnOrderListener onShareClick;

        public ViewHolder(@NonNull View itemView, OnOrderListener onOrderClick, OnOrderListener onSpeakClick, OnOrderListener onShareClick){
            super(itemView);

            //Obtain views
            senderImageView = (ImageView) itemView.findViewById(R.id.senderImageView);
            fromSenderUsernameTextView = (TextView) itemView.findViewById(R.id.fromSenderUsernameTextView);
            toReceiverUsernameTextView = (TextView) itemView.findViewById(R.id.toReceiverUsernameTextView);
            goodDescriptionTextView = (TextView) itemView.findViewById(R.id.goodDescriptionTextView);
            speakImageView = (ImageView) itemView.findViewById(R.id.speakImageView);
            shareImageView = (ImageView) itemView.findViewById(R.id.shareImageView);

            //Define the interface variables
            this.onOrderClick = onOrderClick;
            this.onSpeakClick = onSpeakClick;
            this.onShareClick = onShareClick;

            itemView.setOnClickListener(this);
            itemView.findViewById(R.id.speakImageView).setOnClickListener(this);
            itemView.findViewById(R.id.shareImageView).setOnClickListener(this);
        }

        //OnClickListener for the ViewHolder
        @Override
        public void onClick(View view) {

            //Listener for the entire Order item
            if (view == itemView) {
                onOrderClick.onOrderClick(getAdapterPosition()); //Defined in HomeActivity and MyOrdersActivity
            }

            //Listener for the Share button of the Order item
            if (view == speakImageView) {
                onSpeakClick.onSpeakClick(getAdapterPosition()); //Defined in HomeActivity and MyOrdersActivity
            }

            //Listener for the Share button of the Order item
            if (view == shareImageView) {
                onShareClick.onShareClick(getAdapterPosition()); //Defined in HomeActivity and MyOrdersActivity
            }
        }
    }

    //Interface to be implemented by HomeActivity and MyOrdersActivity
    public interface OnOrderListener {
        void onOrderClick(int position); //Listener method to override in MyOrdersActivity
        void onSpeakClick(int position);
        void onShareClick(int position);
    }
}
