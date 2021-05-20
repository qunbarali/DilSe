package com.example.dilse.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dilse.R;

public class ProductDetailsActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar productDetailsToolbar;
    TextView singleTitle, singleBrand, singlePrice, singleSubtitle, singleInStock;
    ImageView singleImg;
    Button buyNow;
    Spinner singleQuantity;
    int q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productDetailsToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.productDetailsToolbar);
        setSupportActionBar(productDetailsToolbar);
        productDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        singleTitle = (TextView) findViewById(R.id.singleTitle);
        singleBrand = (TextView) findViewById(R.id.singleBrand);
        singlePrice = (TextView) findViewById(R.id.singlePrice);
        singleSubtitle = (TextView) findViewById(R.id.singleSubtitle);
        singleInStock = (TextView) findViewById(R.id.singleInStock);
        singleImg = (ImageView) findViewById(R.id.singleImg);
        buyNow = (Button) findViewById(R.id.buyNow);
        buyNow.setEnabled(true);
        singleQuantity = (Spinner) findViewById(R.id.singleQuantity);

        singleTitle.setText(getIntent().getStringExtra("title"));
        singleBrand.setText(getIntent().getStringExtra("brand"));
        String prodImg = getIntent().getStringExtra("imgUrl");
        Glide.with(ProductDetailsActivity.this).load(prodImg).into(singleImg);
        singlePrice.setText(getIntent().getStringExtra("price"));
        singleSubtitle.setText(getIntent().getStringExtra("subtitle"));

        if (getIntent().getStringExtra("inStock").equals("true")){
            singleInStock.setText("In Stock");
            singleInStock.setTextColor(ContextCompat.getColor(this, R.color.white));
            singleInStock.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        } else {
            singleInStock.setText("Out of Stock");
            singleInStock.setTextColor(ContextCompat.getColor(this, R.color.white));
            singleInStock.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            buyNow.setEnabled(false);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, R.array.quantity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        singleQuantity.setAdapter(adapter);
        singleQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                q = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent confirmIntent = new Intent(ProductDetailsActivity.this, OrderConfirmActivity.class);
                confirmIntent.putExtra("productTitle", getIntent().getStringExtra("title"));
                confirmIntent.putExtra("productBrand", getIntent().getStringExtra("brand"));
                confirmIntent.putExtra("productImage", getIntent().getStringExtra("imgUrl"));
                confirmIntent.putExtra("productPrice", getIntent().getStringExtra("price"));
                confirmIntent.putExtra("productQuantity", Integer.toString(q));
                startActivity(confirmIntent);
            }
        });
    }
}