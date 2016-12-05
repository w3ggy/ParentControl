package com.abramov.artyom.parentcontrol.ui.main_screen;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
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
import android.widget.Toast;

import com.abramov.artyom.parentcontrol.MyApplication;
import com.abramov.artyom.parentcontrol.R;
import com.abramov.artyom.parentcontrol.interfaces.Constants;
import com.abramov.artyom.parentcontrol.interfaces.ScreenUtils;
import com.abramov.artyom.parentcontrol.services.DataService;
import com.abramov.artyom.parentcontrol.services.LocationService;
import com.abramov.artyom.parentcontrol.ui.calls.CallsFragment;
import com.abramov.artyom.parentcontrol.ui.map.MapFragment;
import com.abramov.artyom.parentcontrol.ui.sms.SmsFragment;
import com.abramov.artyom.parentcontrol.ui.welcome_screen.WelcomeActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements ScreenUtils, NavigationView.OnNavigationItemSelectedListener {

    private DevicePolicyManager mDPM;
    private ComponentName mDeviceAdminSample;
    private static final int REQUEST_CODE_ENABLE_ADMIN = 1;
    private final static int REQUEST_DRAW_OVERLAYS = 101;

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

        mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDeviceAdminSample = new ComponentName(this, MainActivity.DeviceAdminSampleReceiver.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
        startService(new Intent(this, LocationService.class));
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
            checkAdminPermission();
        } else if (id == R.id.nav_send) {

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getData() {
        Intent intent = new Intent(this, DataService.class);
        intent.setAction(Constants.ACTION_ALL_DATA);
        startService(intent);
    }

    public void checkDrawOverlayPermission() {
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this))) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_DRAW_OVERLAYS);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ENABLE_ADMIN) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                Toast.makeText(this, "Provisioning done.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Provisioning failed.", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkAdminPermission() {
        if (getResources().getBoolean(R.bool.isTablet) && !mDPM.isAdminActive(mDeviceAdminSample)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "U r really want to become admin?");
            startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
        }
    }

    public static class DeviceAdminSampleReceiver extends DeviceAdminReceiver {

        public DeviceAdminSampleReceiver() {
            super();
        }

        void showToast(Context context, String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEnabled(Context context, Intent intent) {
            showToast(context, "onEnabled!");
        }

        @Override
        public CharSequence onDisableRequested(Context context, Intent intent) {
            return "U r really want to disable it?";
        }

        @Override
        public void onDisabled(Context context, Intent intent) {
            showToast(context, "onDisabled!");
        }

        @Override
        public void onPasswordChanged(Context context, Intent intent) {
            showToast(context, "onPasswordChanged");
        }
    }
}
