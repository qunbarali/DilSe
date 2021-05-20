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

public class CardsActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar cardsToolbar;
    RecyclerView cardsContainer;
    ArrayList<ProductDetails> cardsList;
    FirebaseFirestore firebaseFirestore;
    ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        cardsToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.cardsToolbar);
        setSupportActionBar(cardsToolbar);
        cardsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cardsContainer = (RecyclerView) findViewById(R.id.cardsContainer);
        cardsContainer.setLayoutManager(new LinearLayoutManager(this));
        cardsList = new ArrayList<>();
        productsAdapter = new ProductsAdapter(cardsList);
        cardsContainer.setAdapter(productsAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("cards").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(CardsActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                        } else {
                            List<DocumentSnapshot> allCards = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot index : allCards){
                                ProductDetails productDetails = index.toObject(ProductDetails.class);
                                cardsList.add(productDetails);
                            }
                            productsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}