package com.falalurahman.sacapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.falalurahman.sacapp.Fragments.NewsFeedFragment;
import com.falalurahman.sacapp.Fragments.StoreFeedFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment newsfeedFragment;
    private Fragment storefeedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
        displaySelectedScreen(R.id.nav_home);

        SharedPreferences sharedPreferences = getSharedPreferences("SACNITC", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Username", "Falalu Rahman");
        editor.putString("RollNo", "B140347CS");

        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@Nullable MenuItem item) {
        if (item != null) {
            displaySelectedScreen(item.getItemId());
            return true;
        } else {
            return false;
        }
    }

    private void displaySelectedScreen(int itemId) {
        Fragment fragment = null;

        switch (itemId) {
            case R.id.nav_home:
                if (newsfeedFragment == null) {
                    newsfeedFragment = new NewsFeedFragment();
                }
                fragment = newsfeedFragment;
                break;
            case R.id.nav_store:
                if (storefeedFragment == null) {
                    storefeedFragment = new StoreFeedFragment();
                }
                fragment = storefeedFragment;
                break;
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.contentFrame, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}
