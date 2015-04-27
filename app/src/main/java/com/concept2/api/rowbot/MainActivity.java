package com.concept2.api.rowbot;

import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.concept2.api.Concept2;
import com.concept2.api.rowbot.fragments.DebugFragment;
import com.concept2.api.rowbot.model.RowBotActivity;
import com.concept2.api.rowbot.ui.adapters.NavDrawerAdapter;

public class MainActivity extends FragmentActivity implements RowBotActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE) != null) {
            // Launched from a device connection.
            Concept2.PaceMonitor.start(this);
        }

        initNavigationDrawer();
        initActionBar();

        // TODO - do not default to debug fragment.
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_frame, new DebugFragment(), DebugFragment.TAG)
                .commit();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setActionBarTitle(int resId) {
        getActionBar().setTitle(resId);
    }

    private void initNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        final NavDrawerAdapter navDrawerAdapter = new NavDrawerAdapter(this);
        ListView drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(navDrawerAdapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                navDrawerAdapter.setSelected(position);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    private void initActionBar() {
        getActionBar().setTitle(R.string.app_name);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        findViewById(android.R.id.home).setVisibility(View.INVISIBLE);
    }
}
