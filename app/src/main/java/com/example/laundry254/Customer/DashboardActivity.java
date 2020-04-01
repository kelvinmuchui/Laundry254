package com.example.laundry254.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.laundry254.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //ActionBar and its title
        actionBar = getSupportActionBar();
        actionBar.setTitle("Dashboard");



        //init
        firebaseAuth = FirebaseAuth.getInstance();

        // Bottom Navigation

        BottomNavigationView navigationView =findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);
        //Default
        actionBar.setTitle("Home");

        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, fragment1, "");
        ft1.commit();
    }
     private  BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
             new BottomNavigationView.OnNavigationItemSelectedListener(){

                 @Override
                 public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                     // Handle item clicks

                     switch (menuItem.getItemId()){
                         case R.id.nav_home:
                             //Home Fragment transaction

                             actionBar.setTitle("Home");

                             HomeFragment fragment1 = new HomeFragment();
                             FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                             ft1.replace(R.id.content, fragment1, "");
                             ft1.commit();
                             return true;

                         case R.id.nav_profile:
                             //Profile Fragment transaction
                             actionBar.setTitle("Profile");

                             ProfileFragment fragment2 = new ProfileFragment();
                             FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                             ft2.replace(R.id.content, fragment2, "");
                             ft2.commit();
                             return true;
                         case R.id.nav_feedback:
                             //Feedback Fragment transaction
                             actionBar.setTitle("Feedback");

                             FeedbackFragment fragment3 = new FeedbackFragment();
                             FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                             ft3.replace(R.id.content, fragment3, "");
                             ft3.commit();
                             return true;
                     }
                     return false;
                 }
             };
}
