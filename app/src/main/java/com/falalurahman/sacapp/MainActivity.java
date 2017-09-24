package com.falalurahman.sacapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.falalurahman.sacapp.Helper.RecyclerDivider;
import com.falalurahman.sacapp.JavaBean.NewsFeed;
import com.falalurahman.sacapp.ViewHolder.NewsFeedHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String NEWSFEED_REF = "NewsFeed";
    private static final int DIVIDER_LENGTH = 40;
    FirebaseRecyclerAdapter<NewsFeed, NewsFeedHolder> mAdapter;
    ShimmerRecyclerView newsFeedListView;

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

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference newsFeedRef = firebaseDatabase.getReference(NEWSFEED_REF);

        newsFeedListView = findViewById(R.id.newsfeed_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        newsFeedListView.setLayoutManager(linearLayoutManager);
        RecyclerDivider recyclerDivider = new RecyclerDivider(DIVIDER_LENGTH);
        newsFeedListView.addItemDecoration(recyclerDivider);

        mAdapter =
                new FirebaseRecyclerAdapter<NewsFeed, NewsFeedHolder>(
                        NewsFeed.class,
                        R.layout.row_newsfeed,
                        NewsFeedHolder.class,
                        newsFeedRef) {
                    @Override
                    protected void populateViewHolder(NewsFeedHolder viewHolder, NewsFeed model, int position) {
                        viewHolder.setSACLogo();
                        viewHolder.setDate(model.getTimeStamp());
                        viewHolder.setStatus(model.getStatus());
                        if (model.getImageUrls() != null) {
                            viewHolder.setImages(MainActivity.this, model.getImageUrls());
                        }
                    }

                    @Override
                    public void onDataChanged() {
                        super.onDataChanged();
                        newsFeedListView.hideShimmerAdapter();
                    }
                };

        newsFeedListView.setAdapter(mAdapter);
        newsFeedListView.showShimmerAdapter();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@Nullable MenuItem item) {
        // Handle navigation view item clicks here.
        if (item != null) {
            int id = item.getItemId();

            if (id == R.id.nav_camera) {
                // Handle the camera action
            } else if (id == R.id.nav_gallery) {

            } else if (id == R.id.nav_slideshow) {

            } else if (id == R.id.nav_manage) {

            } else if (id == R.id.nav_share) {

            } else if (id == R.id.nav_send) {

            }

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }
}
