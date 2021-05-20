package com.example.dilse.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dilse.R;
import com.example.dilse.local.Order;
import com.example.dilse.local.OrderDb;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class OrderConfirmActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar confirmOrderToolbar;
    ImageView itemImage;
    TextView itemName, itemBrand, itemPrice, itemQuantity, itemTotalPrice;
    int total;
    TextInputLayout editPinCode, editAddress, editLandmark;
    Button cancel, confirm;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Dialog confirmDialog;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);

        confirmOrderToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.confirmOrderToolbar);
        setSupportActionBar(confirmOrderToolbar);
        confirmOrderToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UID = firebaseAuth.getCurrentUser().getUid();
        itemImage = (ImageView) findViewById(R.id.itemImage);
        itemName = (TextView) findViewById(R.id.itemName);
        itemBrand = (TextView) findViewById(R.id.itemBrand);
        itemPrice = (TextView) findViewById(R.id.itemPrice);
        itemQuantity = (TextView) findViewById(R.id.itemQuantity);
        itemTotalPrice = (TextView) findViewById(R.id.itemTotalPrice);
        editPinCode = (TextInputLayout) findViewById(R.id.editPinCode);
        editAddress = (TextInputLayout) findViewById(R.id.editAddress);
        editLandmark = (TextInputLayout) findViewById(R.id.editLandmark);
        cancel = (Button) findViewById(R.id.cancel);
        confirm = (Button) findViewById(R.id.confirm);
        confirmDialog = new Dialog(this);

        String productImage = getIntent().getStringExtra("productImage");
        Glide.with(OrderConfirmActivity.this).load(productImage).into(itemImage);
        itemName.setText(getIntent().getStringExtra("productTitle"));
        itemBrand.setText(getIntent().getStringExtra("productBrand"));
        itemPrice.append(getIntent().getStringExtra("productPrice"));
        itemQuantity.setText(getIntent().getStringExtra("productQuantity"));

        //Total Amount
        int productPrice = Integer.parseInt(getIntent().getStringExtra("productPrice"));
        int productQuantity = Integer.parseInt(getIntent().getStringExtra("productQuantity"));
        total = productPrice * productQuantity;
        itemTotalPrice.append(Integer.toString(total));

        //Cancel Order
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Delivery Info
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinCode = editPinCode.getEditText().getText().toString().trim();
                String address = editAddress.getEditText().getText().toString().trim();
                String landmark = editLandmark.getEditText().getText().toString().trim();

                if (pinCode.isEmpty()){
                    editPinCode.setError("Pin Code is required");
                    editPinCode.requestFocus();
                    return;
                }
                if (pinCode.length() != 6){
                    editPinCode.setError("Pin Code must be of 6 characters");
                    editPinCode.requestFocus();
                    return;
                }
                if (address.isEmpty()){
                    editAddress.setError("Address is required");
                    editAddress.requestFocus();
                    return;
                }
                if (landmark.isEmpty()){
                    editLandmark.setError("Landmark is required");
                    editLandmark.requestFocus();
                    return;
                }
                DocumentReference userReference = firebaseFirestore.collection("users").document(UID);
                userReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        Map<String, String> order = new HashMap<>();
                        order.put("name", value.getString("name"));
                        order.put("phone", value.getString("phone"));
                        order.put("email", value.getString("email"));
                        order.put("item", getIntent().getStringExtra("productTitle"));
                        order.put("brand", getIntent().getStringExtra("productBrand"));
                        order.put("image", getIntent().getStringExtra("productImage"));
                        order.put("price", getIntent().getStringExtra("productPrice"));
                        order.put("quantity", getIntent().getStringExtra("productQuantity"));
                        order.put("total", Integer.toString(total));
                        order.put("pincode", pinCode);
                        order.put("address", address);
                        order.put("landmark", landmark);
                        firebaseFirestore.collection("orders").add(order)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (!task.isSuccessful()){
                                            Toast.makeText(OrderConfirmActivity.this, "Order Error", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //Display Confirmation Alert Dialog.
                                            OrderDb orderDb = OrderDb.getDbInstance(getApplicationContext());

                                            Order order = new Order();
                                            order.order_img = getIntent().getStringExtra("productImage");
                                            order.order_item = getIntent().getStringExtra("productTitle");
                                            order.order_brand = getIntent().getStringExtra("productBrand");
                                            order.order_price = Integer.parseInt(getIntent().getStringExtra("productPrice"));
                                            order.order_quantity = Integer.parseInt(getIntent().getStringExtra("productQuantity"));
                                            order.order_total = total;

                                            orderDb.orderDao().insertOrder(order);
                                            showConfirmDialog();
                                        }
                                    }
                                });
                    }
                });

            }
        });
    }

    public void showConfirmDialog() {
        confirmDialog.setContentView(R.layout.confirm_dialog);
        confirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmDialog.setCancelable(false);
        Button confirmOk = confirmDialog.findViewById(R.id.confirmOk);
        confirmOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirmDialog.show();
    }
}