package com.example.laundry254.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SignupCustActivity extends AppCompatActivity {

    private EditText email, password, confPassword;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private Button signUp;
    private TextView login;
    private FirebaseAuth auth;
    private String mName, mEmail, mPassword,mConfpassword, mPhonenumber, mUsertype;
    FirebaseDatabase database =  FirebaseDatabase.getInstance();
    //ProgressBar
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_cust);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");

        //eneble back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //Init

        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);

        confPassword = findViewById(R.id.input_password2);
        signUp = findViewById(R.id.btn_signup);
        login = findViewById(R.id.link_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User....");
        radioGroup =  findViewById(R.id.radio);



        auth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get data from edit text
                mEmail = email.getText().toString().trim();
                mPassword = password.getText().toString().trim();
                mConfpassword = confPassword.getText().toString().trim();





                //Validate

                //checking if email and passwords are empty
                if(!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
                    email.setError("Invalid Email");
                    email.setFocusable(true);
                }

               else if(TextUtils.isEmpty(mPassword)){
                    Toast.makeText(SignupCustActivity.this,"Please enter password",Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    registerUser(mEmail,mPassword); //register user

                }

            }

        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(SignupCustActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });


    }


    private void registerUser(String email, String password){
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();
                            //Get  User Email and Uid form auth

                            String email = user.getEmail();
                            String uid = user.getUid();
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            radioButton =  findViewById(selectedId);

                            mUsertype = radioButton.getText().toString().trim();

                            //firebase database instance
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            HashMap<Object, String> hashMap = new HashMap<>();
                                //put infor  in hashmap
                                hashMap.put("email",  email);
                                hashMap.put("uid",  uid);
                                hashMap.put("usertype", mUsertype);
                                hashMap.put("name",  "");
                                hashMap.put("phone",  "");
                                hashMap.put("image",  "");
                                hashMap.put("cover",  "");
                                //path to store user data named users
                                DatabaseReference reference = database.getReference("Users");
                                //put data within hashmap in dataabase
                                reference.child(uid).setValue(hashMap);


//                            if(mUsertype.equals("Customer")){
//                                //Using HashMap to store user data
//                                HashMap<Object, String> hashMap = new HashMap<>();
//                                //put infor  in hashmap
//                                hashMap.put("email",  email);
//                                hashMap.put("uid",  uid);
//                                hashMap.put("usertype", "Customer");
//                                hashMap.put("name",  "");
//                                hashMap.put("phone",  "");
//                                hashMap.put("image",  "");
//                                hashMap.put("cover",  "");
//                                //path to store user data named users
//                                DatabaseReference reference = database.getReference("Users");
//                                //put data within hashmap in dataabase
//                                reference.child("Customer").child(uid).setValue(hashMap);
//
//                            }else{
//
//                                //Using HashMap to store user data
//                                HashMap<Object, String> hashMap = new HashMap<>();
//                                //put infor  in hashmap
//                                hashMap.put("email",  email);
//                                hashMap.put("uid",  uid);
//                                hashMap.put("usertype",  "Provider");
//                                hashMap.put("name",  "");
//                                hashMap.put("phone",  "");
//                                hashMap.put("image",  "");
//                                hashMap.put("cover",  "");
//                                //path to store user data named users
//                                DatabaseReference reference = database.getReference("Users");
//                                //put data within hashmap in dataabase
//                                reference.child("Provider").child(uid).setValue(hashMap);
//
//                            }



                            Toast.makeText(SignupCustActivity.this, "Registered...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupCustActivity.this, LoginActivity.class));
                            finish();

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(SignupCustActivity.this ,"Authentication failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignupCustActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed(); // go to prvous activity
        return super.onSupportNavigateUp();
    }

}
