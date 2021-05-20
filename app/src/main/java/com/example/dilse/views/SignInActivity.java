package com.example.dilse.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dilse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    TextInputLayout editEmail, editPassword;
    TextView forgotPasswordTxt, signUpTxt;
    Button signInBtn;
    ProgressBar progressBar;
    Dialog errorDialog, forgotDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        editEmail = (TextInputLayout) findViewById(R.id.editEmail);
        editPassword = (TextInputLayout) findViewById(R.id.editPassword);
        forgotPasswordTxt = (TextView) findViewById(R.id.forgotPasswordTxt);
        signUpTxt = (TextView) findViewById(R.id.signUpTxt);
        signInBtn = (Button) findViewById(R.id.signInBtn);
        errorDialog = new Dialog(this);
        forgotDialog = new Dialog(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            finish();
        }

        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getEditText().getText().toString().trim();
                String password = editPassword.getEditText().getText().toString().trim();

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
                sigIn(email, password);
            }
        });

        forgotPasswordTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgotDialog();
            }
        });
    }

    public void sigIn(String email, String password){
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignInActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                    finish();
                } else {
                    openErrorDialog();
                }
            }
        });
    }

    public void openErrorDialog() {
        progressBar.setVisibility(View.INVISIBLE);
        errorDialog.setContentView(R.layout.login_error_dialog);
        errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errorDialog.setCancelable(false);
        ImageView closeIcon = errorDialog.findViewById(R.id.signOutCloseIcon);
        Button okBtn = errorDialog.findViewById(R.id.yesSignOut);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
            }
        });
        errorDialog.show();
    }


    public void openForgotDialog() {
        forgotDialog.setContentView(R.layout.forgot_dialog);
        forgotDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        forgotDialog.setCancelable(false);
        ImageView forgotCloseIcon = forgotDialog.findViewById(R.id.forgotCloseIcon);
        TextInputLayout forgotEmail = forgotDialog.findViewById(R.id.forgotEmail);
        forgotEmail.requestFocus();
        Button sendLink = forgotDialog.findViewById(R.id.sendLink);
        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailEntered = forgotEmail.getEditText().getText().toString().trim();
                if (emailEntered.isEmpty()){
                    forgotEmail.setError("Email is required");
                    forgotEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emailEntered).matches()){
                    forgotEmail.setError("Enter a valid email");
                    forgotEmail.requestFocus();
                    return;
                }
                sendEmail(emailEntered);
                forgotDialog.dismiss();
            }
        });
        forgotCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotDialog.dismiss();
            }
        });
        forgotDialog.show();
    }

    public void sendEmail(String email){
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            forgotDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "Password reset Link sent..", Toast.LENGTH_SHORT).show();
                        } else {
                            forgotDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}