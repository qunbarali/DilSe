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

public class FlowersActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar flowersToolbar;
    RecyclerView flowersContainer;
    ArrayList<ProductDetails> flowersList;
    FirebaseFirestore firebaseFirestore;
    ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowers);

        //Toolbar
        flowersToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.flowersToolbar);
        setSupportActionBar(flowersToolbar);
        flowersToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        flowersContainer = (RecyclerView) findViewById(R.id.flowersContainer);
        flowersContainer.setLayoutManager(new LinearLayoutManager(this));
        flowersList = new ArrayList<>();
        productsAdapter = new ProductsAdapter(flowersList);
        flowersContainer.setAdapter(productsAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("flowers").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(FlowersActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                        } else {
                            List<DocumentSnapshot> allFlowers = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot index : allFlowers){
                                ProductDetails productDetails = index.toObject(ProductDetails.class);
                                flowersList.add(productDetails);
                            }
                            productsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}