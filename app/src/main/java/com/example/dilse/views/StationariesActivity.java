package com.example.dilse.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dilse.R;
import com.example.dilse.adapters.ProductsAdapter;
import com.example.dilse.models.ProductDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StationariesActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar stationariesToolbar;
    RecyclerView stationariesContainer;
    ArrayList<ProductDetails> stationariesList;
    FirebaseFirestore firebaseFirestore;
    ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stationaries);

        stationariesToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.stationariesToolbar);
        setSupportActionBar(stationariesToolbar);
        stationariesToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        stationariesContainer = (RecyclerView) findViewById(R.id.stationariesContainer);
        stationariesContainer.setLayoutManager(new LinearLayoutManager(this));
        stationariesList = new ArrayList<>();
        productsAdapter = new ProductsAdapter(stationariesList);
        stationariesContainer.setAdapter(productsAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("stationery").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(StationariesActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                        } else {
                            List<DocumentSnapshot> allstationaries = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot index : allstationaries){
                                ProductDetails productDetails = index.toObject(ProductDetails.class);
                                stationariesList.add(productDetails);
                            }
                            productsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}