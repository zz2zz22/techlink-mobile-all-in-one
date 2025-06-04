package com.example.techlinkmobileallinone.MachineCheck;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.techlinkmobileallinone.LoginActivity;
import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.controller.SubMethods;
import com.google.android.material.navigation.NavigationView;

public class MachineCheckMainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_machine_check_main);
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
                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(MachineCheckMainActivity.this);
                switch (item.getItemId()) {

                    case R.id.mc_home:
                        returnToHome();
                        break;
                    case R.id.mc_check:
                        enableViews(true);
                        replaceFragment(new MCQRScanFragment(), true, "check", new Bundle());
                        break;
                    case R.id.mc_maintainance:
                        enableViews(true);
                        replaceFragment(new MCQRScanFragment(), true, "maintenance", new Bundle());
                        break;
                    case R.id.mc_update:
                        enableViews(true);
                        replaceFragment(new MCQRScanFragment(), true, "update", new Bundle());
                        break;
                    case R.id.mc_info:
                        enableViews(true);
                        replaceFragment(new MCQRScanFragment(), true, "information", new Bundle());
                        break;
                    case R.id.mc_logout:
                        alert.setTitle(getString(R.string.informationTitle));
                        alert.setMessage("Đăng xuất?\n登出？");
                        alert.setPositiveButton("Đồng ý 同意", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(MachineCheckMainActivity.this, LoginActivity.class));
                                overridePendingTransition(R.anim.zoom_in, R.anim.static_animation);
                                finish();
                            }
                        });
                        alert.setNegativeButton("Hủy bỏ 撤消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alert.show();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            replaceFragment(new MCHomeFragment(), false, "null", new Bundle());
            navigationView.setCheckedItem(R.id.fire_nav_home);
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

    private void getViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
    }

    public void replaceFragment(Fragment fragment, boolean needToAddBackStack, String actionType, Bundle bundle) {
        navigationView.setCheckedItem(0);
        bundle.putString("actionType", actionType);
        fragment.setArguments(bundle);
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
        replaceFragment(new MCHomeFragment(), false, "null", new Bundle());
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
}