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

public class SoftToysActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar softToysToolbar;
    RecyclerView softToysContainer;
    ArrayList<ProductDetails> softToysList;
    FirebaseFirestore firebaseFirestore;
    ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_toys);

        softToysToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.softToysToolbar);
        setSupportActionBar(softToysToolbar);
        softToysToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        softToysContainer = (RecyclerView) findViewById(R.id.softToysContainer);
        softToysContainer.setLayoutManager(new LinearLayoutManager(this));
        softToysList = new ArrayList<>();
        productsAdapter = new ProductsAdapter(softToysList);
        softToysContainer.setAdapter(productsAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("softToys").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(SoftToysActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                        } else {
                            List<DocumentSnapshot> allsoftToys = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot index : allsoftToys){
                                ProductDetails productDetails = index.toObject(ProductDetails.class);
                                softToysList.add(productDetails);
                            }
                            productsAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }
}