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

public class EarphonesActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar earphonesToolbar;
    RecyclerView earphonesContainer;
    ArrayList<ProductDetails> earphonesList;
    FirebaseFirestore firebaseFirestore;
    ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earphones);

        earphonesToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.earphonesToolbar);
        setSupportActionBar(earphonesToolbar);
        earphonesToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        earphonesContainer = (RecyclerView) findViewById(R.id.earphonesContainer);
        earphonesContainer.setLayoutManager(new LinearLayoutManager(this));
        earphonesList = new ArrayList<>();
        productsAdapter = new ProductsAdapter(earphonesList);
        earphonesContainer.setAdapter(productsAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("earphones").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(EarphonesActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                        } else {
                            List<DocumentSnapshot> allEarphones = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot index : allEarphones){
                                ProductDetails productDetails = index.toObject(ProductDetails.class);
                                earphonesList.add(productDetails);
                            }
                            productsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}