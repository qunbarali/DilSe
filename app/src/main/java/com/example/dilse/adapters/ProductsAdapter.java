package com.example.dilse.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dilse.R;
import com.example.dilse.models.ProductDetails;
import com.example.dilse.views.ProductDetailsActivity;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.productsViewHolder> {

    ArrayList<ProductDetails> productsList;

    public ProductsAdapter(ArrayList<ProductDetails> flowersList) {
        this.productsList = flowersList;
    }

    @NonNull
    @Override
    public productsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product, parent, false);
        return new productsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productsViewHolder holder, int position) {
        holder.prodTitle.setText(productsList.get(position).getTitle());
        holder.prodBrand.setText(productsList.get(position).getBrand());
        holder.prodPrice.setText(Integer.toString(productsList.get(position).getPrice()));

        if (productsList.get(position).isInStock()){
            holder.inStock.setText("In Stock");
            holder.inStock.setTextColor(ContextCompat.getColor(holder.inStock.getContext(), R.color.white));
            holder.inStock.setBackgroundColor(ContextCompat.getColor(holder.inStock.getContext(), R.color.green));
        } else {
            holder.inStock.setText("Out of Stock");
            holder.inStock.setTextColor(ContextCompat.getColor(holder.inStock.getContext(), R.color.white));
            holder.inStock.setBackgroundColor(ContextCompat.getColor(holder.inStock.getContext(), R.color.red));
        }

        Glide.with(holder.prodBrand.getContext()).load(productsList.get(position).getImgUrl()).into(holder.prodImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(holder.inStock.getContext(), ProductDetailsActivity.class);
                detailIntent.putExtra("title", productsList.get(position).getTitle());
                detailIntent.putExtra("subtitle", productsList.get(position).getSubtitle());
                detailIntent.putExtra("inStock", Boolean.toString(productsList.get(position).isInStock()));
                detailIntent.putExtra("price", Integer.toString(productsList.get(position).getPrice()));
                detailIntent.putExtra("brand", productsList.get(position).getBrand());
                detailIntent.putExtra("imgUrl", productsList.get(position).getImgUrl());
                detailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.prodBrand.getContext().startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class productsViewHolder extends RecyclerView.ViewHolder {

        TextView prodTitle, prodBrand, prodPrice, inStock;
        ImageView prodImg;

        public productsViewHolder(@NonNull View itemView) {
            super(itemView);
            prodTitle = itemView.findViewById(R.id.prodTitle);
            prodBrand = itemView.findViewById(R.id.prodBrand);
            prodPrice = itemView.findViewById(R.id.prodPrice);
            inStock = itemView.findViewById(R.id.inStock);
            prodImg = itemView.findViewById(R.id.prodImg);

        }
    }

    public void filterList(ArrayList<ProductDetails> filteredList) {
        productsList = filteredList;
        notifyDataSetChanged();
    }
}
