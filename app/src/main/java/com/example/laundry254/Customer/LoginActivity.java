package com.example.laundry254.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laundry254.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    //views

    EditText mEmail, mPassword;
    TextView signup;
    Button loginBtn;

    FirebaseAuth mAuth;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");

        //eneble back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        //init

        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);


        loginBtn = findViewById(R.id.btn_login);
        signup = findViewById(R.id.link_signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging In...");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input data

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    // Invalid emaill
                    mEmail.setError("Invalid Email");
                    mEmail.setFocusable(true);
                }
                else{
                    //Valid email

                    loginUser(email, password);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupCustActivity.class));

            }
        });
    }

    private void loginUser(String email, String password) {
        //Show progress
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           progressDialog.dismiss();
                           FirebaseUser user = mAuth.getCurrentUser();

                           if(task.getResult().getAdditionalUserInfo().isNewUser()){

                           }
                           //Get  User Email and Uid form auth
//                           String email = user.getEmail();
//                           String uid = user.getUid();
//
//                           //Using HashMap to store user data
//                           HashMap<Object, String> hashMap = new HashMap<>();
//                           //put infor  in hashmap
//                           hashMap.put("email",  email);
//                           hashMap.put("uid",  uid);
//                           hashMap.put("usertype",  email);
//                           hashMap.put("name",  "");
//                           hashMap.put("phone",  "");
//                           hashMap.put("image",  "");
//                           //firebase database instance
//                           FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//                           //path to store user data named users
//                           DatabaseReference reference = database.getReference("Users");
//                           //put data within hashmap in dataabase
//                           reference.child(uid).setValue(hashMap);

                           //user log in
                           startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                           finish();
                       } else{
                           progressDialog.dismiss();

                           Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                       }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //error
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed(); // go to prvous activity
        return super.onSupportNavigateUp();
    }
}
