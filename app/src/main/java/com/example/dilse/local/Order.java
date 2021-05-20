package com.example.dilse.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Order {

    @PrimaryKey(autoGenerate = true)
    public int oid;

    @ColumnInfo(name = "image")
    public String order_img;

    @ColumnInfo(name = "item")
    public String order_item;

    @ColumnInfo(name = "brand")
    public String order_brand;

    @ColumnInfo(name = "price")
    public int order_price;

    @ColumnInfo(name = "quantity")
    public int order_quantity;

    @ColumnInfo(name = "total")
    public int order_total;
}
