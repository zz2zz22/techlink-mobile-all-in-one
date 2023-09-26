package com.example.techlinkmobileallinone.BigHoseCountDown;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.techlinkmobileallinone.FireSafetyEquipment.FSEHomeFragment;
import com.example.techlinkmobileallinone.FireSafetyEquipment.FSEQRScanFragment;
import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.controller.SubMethods;
import com.google.android.material.navigation.NavigationView;

public class BigHoseCountDownActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    SubMethods subMethods = new SubMethods();
    String empCode, empName;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_hose_count_down);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            empCode = extras.getString("empCode");
            empName = extras.getString("empName");
        }
        getViews();

        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderTitle = (TextView) headerView.findViewById(R.id.nav_header_title);
        TextView navHeaderCaption = (TextView) headerView.findViewById(R.id.nav_header_caption);
        navHeaderTitle.setText(empName);
        navHeaderCaption.setText(empCode);

        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bh_nav_home:
                        returnToHome();
                        break;
                    case R.id.bh_nav_settings:
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            replaceFragment(new BHCDChooseDeptFragment(), false);
            navigationView.setCheckedItem(R.id.bh_nav_home);
        }
    }
    public void enableViews(boolean enable) {
        if (enable) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            // Remove hamburger
            toggle.setDrawerIndicatorEnabled(false);
            // Show back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Doesn't have to be onBackPressed
                        onBackPressed();
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            // Remove back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            // Show hamburger
            toggle.setDrawerIndicatorEnabled(true);
            // Remove the/any drawer toggle listener
            toggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }
    }
    public void replaceFragment(Fragment fragment, boolean needToAddBackStack) {
        navigationView.setCheckedItem(0);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (needToAddBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void returnToHome()
    {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);;
        enableViews(false);
        replaceFragment(new BHCDChooseDeptFragment(), false);
    }

    public Bundle getUserData() {
        Bundle bundle = new Bundle();
        bundle.putString("empCode", empCode);
        bundle.putString("empName", empName);
        return bundle;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            if (fragmentManager.getBackStackEntryCount() == 1) {
                enableViews(false);
            }
        } else {
            super.onBackPressed();
        }
    }
    private void getViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
    }
}