package com.example.dilse.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SearchActivity extends AppCompatActivity {

    ImageView searchBackIcon;
    RecyclerView searchContainer;
    ArrayList<ProductDetails> searchList;
    FirebaseFirestore firebaseFirestore;
    ProductsAdapter productsAdapter;
    EditText searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBackIcon = (ImageView) findViewById(R.id.searchBackIcon);
        searchBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchContainer = (RecyclerView) findViewById(R.id.searchContainer);
        searchQuery = (EditText) findViewById(R.id.searchQuery);

        searchContainer.setLayoutManager(new LinearLayoutManager(this));
        searchList = new ArrayList<>();
        productsAdapter = new ProductsAdapter(searchList);
        searchContainer.setAdapter(productsAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("products").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(SearchActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                        } else {
                            List<DocumentSnapshot> allProducts = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot index : allProducts){
                                ProductDetails productDetails = index.toObject(ProductDetails.class);
                                searchList.add(productDetails);
                            }
                            productsAdapter.notifyDataSetChanged();
                        }
                    }
                });

        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    public void filter(String query) {
        ArrayList<ProductDetails> filteredList = new ArrayList<>();
        for (ProductDetails item : searchList){
            if (item.getTitle().toLowerCase().contains(query.toLowerCase())){
                filteredList.add(item);
            }
        }
        productsAdapter.filterList(filteredList);
    }

}