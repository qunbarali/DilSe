package com.example.dilse.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dilse.R;
import com.example.dilse.local.Order;
import com.example.dilse.local.OrderDb;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    List<Order> orderList;

    public OrdersAdapter(List<Order> orderList){
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrdersAdapter.OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_order, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.OrdersViewHolder holder, int position) {
        Glide.with(holder.myOrderItem.getContext()).load(orderList.get(position).order_img).into(holder.myOrderImage);
        holder.myOrderItem.setText(orderList.get(position).order_item);
        holder.myOrderBrand.setText(orderList.get(position).order_brand);
        holder.myOrderPrice.setText(Integer.toString(orderList.get(position).order_price));
        holder.myOrderQuantity.setText(Integer.toString(orderList.get(position).order_quantity));
        holder.myOrderTotal.append(Integer.toString(orderList.get(position).order_total));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public Order getOrderAt(int pos){
        return orderList.get(pos);
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder{

        ImageView myOrderImage;
        TextView myOrderItem, myOrderBrand, myOrderPrice, myOrderQuantity, myOrderTotal;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            myOrderImage = itemView.findViewById(R.id.myOrderImage);
            myOrderItem = itemView.findViewById(R.id.myOrderItem);
            myOrderBrand = itemView.findViewById(R.id.myOrderBrand);
            myOrderPrice = itemView.findViewById(R.id.myOrderPrice);
            myOrderQuantity = itemView.findViewById(R.id.myOrderQuantity);
            myOrderTotal = itemView.findViewById(R.id.myOrderTotal);

        }
    }
}
