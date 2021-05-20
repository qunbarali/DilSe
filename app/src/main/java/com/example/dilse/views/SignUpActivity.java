package com.example.dilse.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dilse.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Swarnendu Kumar Basu
 */

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout editName, editPhone, editEmail, editPassword;
    TextView signInTxt;
    Button signUpBtn;
    ProgressBar progressSignUp;
    FirebaseAuth mAuth;
    String UserID;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editName = (TextInputLayout) findViewById(R.id.editName);
        editPhone = (TextInputLayout) findViewById(R.id.editPhone);
        editEmail = (TextInputLayout) findViewById(R.id.editEmail);
        editPassword = (TextInputLayout) findViewById(R.id.editPassword);
        signInTxt = (TextView) findViewById(R.id.signInTxt);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        progressSignUp = (ProgressBar) findViewById(R.id.progressSignUp);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        signInTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getEditText().getText().toString().trim();
                String phone = editPhone.getEditText().getText().toString().trim();
                String email = editEmail.getEditText().getText().toString().trim();
                String password = editPassword.getEditText().getText().toString().trim();
                if (name.isEmpty()){
                    editName.setError("Name is Required");
                    editName.requestFocus();
                    return;
                }
                if (phone.isEmpty()){
                    editPhone.setError("Phone No. is Required");
                    editPhone.requestFocus();
                    return;
                }
                if (phone.length() != 10){
                    editPhone.setError("Phone No. is Invalid");
                    editPhone.requestFocus();
                    return;
                }
                if (email.isEmpty()){
                    editEmail.setError("Email is Required");
                    editEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editEmail.setError("Email is Invalid");
                    editEmail.requestFocus();
                    return;
                }
                if (password.length() < 6){
                    editPassword.setError("Password should be greater than 6 characters");
                    editPassword.requestFocus();
                    return;
                }
                signUp(email, password, name, phone);
            }
        });
    }
    public void signUp(String email, String password, String name, String phone){
        progressSignUp.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressSignUp.setVisibility(View.INVISIBLE);
                    UserID = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = firebaseFirestore.collection("users").document(UserID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("id", UserID);
                    user.put("name", name);
                    user.put("email", email);
                    user.put("phone", phone);
                    documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressSignUp.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                            finish();
                        }
                    });
                } else {
                    progressSignUp.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignUpActivity.this, "Sign Up Error...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}