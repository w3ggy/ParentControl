package com.abramov.artyom.parentcontrol.ui.main_screen;

import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.abramov.artyom.parentcontrol.MyApplication;
import com.abramov.artyom.parentcontrol.R;
import com.abramov.artyom.parentcontrol.interfaces.Constants;
import com.abramov.artyom.parentcontrol.interfaces.ScreenUtils;
import com.abramov.artyom.parentcontrol.ui.map.MapFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ScreenUtils {

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.drawer_menu)
    ListView mDrawerListView;

    private Fragment mCurrentFragment;
    private ActionBarDrawerToggle mDrawerToggle;

    @Inject
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_layout);
        ButterKnife.bind(this);
        ((MyApplication) getApplication()).getInjector().inject(this);

        setSupportActionBar(mToolbar);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, new String[]{"first", "second", "third"});

        mDrawerListView.setAdapter(adapter);

        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectItem(position);
            }
        });

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        selectItem(0);
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


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerListView);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);

        return super.onPrepareOptionsMenu(menu);
    }

    private void selectItem(int position) {
        mCurrentFragment = getOrCreateFragment(position);
        changeFragment(mCurrentFragment);
        changeTitle(position);
        mDrawerListView.setItemChecked(position, true);
//        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerListView);
    }

    private Fragment getOrCreateFragment(int position) {
        Fragment fragment;
        switch (position) {
            case Constants.ITEM_MAP:
                fragment = getSupportFragmentManager().findFragmentByTag(MapFragment.class.getName());
                return fragment == null ? new MapFragment() : fragment;
            default:
                fragment = getSupportFragmentManager().findFragmentByTag(MapFragment.class.getName());
                return fragment == null ? new MapFragment() : fragment;
        }
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

    private void changeTitle(int position) {
        switch (position) {
            case Constants.ITEM_MAP:
                setTitle(getString(R.string.map_title));
                break;
            default:
                setTitle(getString(R.string.map_title));
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
