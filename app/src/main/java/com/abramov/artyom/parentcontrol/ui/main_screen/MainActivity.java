package com.abramov.artyom.parentcontrol.ui.main_screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.abramov.artyom.parentcontrol.MyApplication;
import com.abramov.artyom.parentcontrol.R;
import com.abramov.artyom.parentcontrol.interfaces.Constants;
import com.abramov.artyom.parentcontrol.interfaces.ScreenUtils;
import com.abramov.artyom.parentcontrol.services.DataService;
import com.abramov.artyom.parentcontrol.ui.calls.CallsFragment;
import com.abramov.artyom.parentcontrol.ui.map.MapFragment;
import com.abramov.artyom.parentcontrol.ui.sms.SmsFragment;
import com.abramov.artyom.parentcontrol.ui.welcome_screen.WelcomeActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements ScreenUtils, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.drawer_navigation)
    NavigationView mNavigationView;

    private Fragment mCurrentFragment;
    private ActionBarDrawerToggle mDrawerToggle;

    @Inject
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_layout);
        ButterKnife.bind(this);
        ((MyApplication) getApplication()).getInjector().inject(this);

        if (!mSharedPreferences.getBoolean(Constants.ALREADY_LAUNCHED, false)) {
            overridePendingTransition(0, 0);
            Intent startIntent = new Intent(this, WelcomeActivity.class);
            startActivity(startIntent);
            finish();
        }

        setSupportActionBar(mToolbar);
        mNavigationView.setNavigationItemSelectedListener(this);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, null,
                R.string.nav_drawer_open, R.string.nav_drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        changeFragment(new MapFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.nav_drawer_menu, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.drawer_content, fragment, fragment.getClass().getName())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_location) {
            mCurrentFragment = getSupportFragmentManager().findFragmentByTag(MapFragment.class.getName());
            changeFragment(mCurrentFragment == null ? MapFragment.getInstance() : mCurrentFragment);
            setTitle(getString(R.string.map_title));
        } else if (id == R.id.nav_sms) {
            mCurrentFragment = getSupportFragmentManager().findFragmentByTag(SmsFragment.class.getName());
            changeFragment(mCurrentFragment == null ? SmsFragment.getInstance() : mCurrentFragment);
            setTitle(getString(R.string.sms_title));
        } else if (id == R.id.nav_calls) {
            mCurrentFragment = getSupportFragmentManager().findFragmentByTag(CallsFragment.class.getName());
            changeFragment(mCurrentFragment == null ? CallsFragment.getInstance() : mCurrentFragment);
            setTitle(getString(R.string.calls_title));
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(this, DataService.class);
            intent.setAction(Constants.ACTION_CALLS);
            startService(intent);

            intent = new Intent(this, DataService.class);
            intent.setAction(Constants.ACTION_SMS);
            startService(intent);
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(this, DataService.class);
            intent.setAction(Constants.ACTION_LOCATION);
            startService(intent);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
