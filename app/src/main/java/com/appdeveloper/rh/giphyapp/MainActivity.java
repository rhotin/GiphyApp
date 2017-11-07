package com.appdeveloper.rh.giphyapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    FragmentManager fm;
    TrendingFragment trendingFragment;
    FavouriteFragment favFragment;
    TabLayout tabLayout;
    TabItem trendingTab;
    TabItem favTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = prefs.edit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        trendingTab = (TabItem) findViewById(R.id.trendingTab);
        favTab = (TabItem) findViewById(R.id.favTab);

        fm = getFragmentManager();
        trendingFragment = new TrendingFragment();
        favFragment = new FavouriteFragment();

        fragmentReplace(fm, trendingFragment);

        if (prefs.getString("tab", "Trending").equals("Trending")){
            tabLayout.getTabAt(0).select();
            fragmentReplace(fm, trendingFragment);
        }else if (prefs.getString("tab", "Trending").equals("My Favorite")){
            tabLayout.getTabAt(1).select();
            fragmentReplace(fm, favFragment);
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    if (tab.getText().toString().equals("Trending")) {
                        editor.putString("tab", tab.getText().toString());
                        editor.commit();
                        Toast.makeText(getBaseContext(), "Trend", Toast.LENGTH_SHORT).show();
                        fragmentReplace(fm, trendingFragment);
                    } else if (tab.getText().toString().equals("My Favorite")) {
                        editor.putString("tab", tab.getText().toString());
                        editor.commit();
                        Toast.makeText(getBaseContext(), "Fav", Toast.LENGTH_SHORT).show();
                        fragmentReplace(fm, favFragment);
                    }
                }catch(NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void fragmentReplace(FragmentManager FM, Fragment fragment) {
        try {
            FragmentTransaction transaction = FM.beginTransaction();

            if (fragment.getClass() != TrendingFragment.class) {
                Fragment temp;
                try {
                    temp = fragment.getClass().newInstance();
                    transaction.remove(fragment);
                    transaction.replace(R.id.fragment_holder, temp);
                    transaction.commit();
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    transaction.replace(R.id.fragment_holder, fragment);
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            FM.executePendingTransactions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            // Handle the logout action
            //Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
            editor.putString("tab", "");
            editor.putString("user", "");
            editor.putString("pass", "");
            editor.commit();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
