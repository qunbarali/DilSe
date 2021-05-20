package com.example.dilse.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dilse.R;
import com.example.dilse.adapters.OrdersAdapter;
import com.example.dilse.local.Order;
import com.example.dilse.local.OrderDb;

import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar myOrdersToolbar;
    RecyclerView myOrdersContainer;
    OrdersAdapter ordersAdapter;
    List<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        myOrdersToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.myOrdersToolbar);
        setSupportActionBar(myOrdersToolbar);
        myOrdersToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myOrdersContainer = (RecyclerView) findViewById(R.id.myOrdersContainer);
        myOrdersContainer.setLayoutManager(new LinearLayoutManager(this));
        OrderDb orderDb = OrderDb.getDbInstance(this);
        orders = orderDb.orderDao().getAllOrders();
        ordersAdapter = new OrdersAdapter(orders);
        myOrdersContainer.setAdapter(ordersAdapter);
    }
}