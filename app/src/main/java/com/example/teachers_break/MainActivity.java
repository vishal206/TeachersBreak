package com.example.teachers_break;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView=findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.player);
    }
    playerFragment playerF=new playerFragment();
    profileFragment profileF=new profileFragment();
    favouriteFragment favouriteF=new favouriteFragment();

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.player:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, playerF).commit();
                return true;

            case R.id.favourite:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, favouriteF).commit();
                return true;

            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileF).commit();
                return true;
        }
        return false;
    }
}