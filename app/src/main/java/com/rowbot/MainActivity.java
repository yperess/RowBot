package com.rowbot;

import android.content.SharedPreferences;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.concept2.api.common.Constants;
import com.concept2.api.common.data.Version;
import com.rowbot.R;
import com.rowbot.ui.dialogs.WelcomeDialogFragment;
import com.rowbot.model.RowBotActivity;
import com.rowbot.ui.adapters.NavDrawerAdapter;
import com.rowbot.ui.fragments.HomePagerFragment;

public class MainActivity extends AppCompatActivity implements RowBotActivity,
        WelcomeDialogFragment.WelcomeDialogListener {

    private static final String TAG = "RowBotActivity";

    private SharedPreferences mPreferences;
    private Version mLastRunVersion;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavDrawerAdapter mNavDrawerAdapter;

    /** Instance of the {@link WelcomeDialogFragment} that is currently showing. */
    private WelcomeDialogFragment mWelcomeDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mPreferences = getSharedPreferences(Constants.SHARED_PREF_FILE, MODE_PRIVATE);
        String lastRunVersion = mPreferences.getString(Constants.SHARED_PREF_LAST_RUN_VERSION,
                null);
        mLastRunVersion = lastRunVersion == null ? null : new Version(lastRunVersion);

        if (getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE) != null) {
            // Launched from a device connection.
//            Concept2.PaceMonitor.start(this);
        }

        initNavigationDrawer();
        initActionBar();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, new HomePagerFragment(), "HOME")
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLastRunVersion == null || mLastRunVersion.compareTo(Constants.VERSION) < 0) {
            // Either never run or an update was issued. Show the dialog.
            showWelcomeDialog();
        }
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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setActionBarTitle(int resId) {
        getSupportActionBar().setTitle(resId);
    }

    @Override
    public boolean isSharingData() {
        return mPreferences.getBoolean(Constants.SHARED_PREF_SHARING_DATA,
                Constants.VERSION.isBeta());
    }

    @Override
    public void setSharingData(boolean sharing) {
        mPreferences.edit()
                .putBoolean(Constants.SHARED_PREF_SHARING_DATA, sharing)
                .apply();
    }

    @Override
    public String getUserName() {
        return mPreferences.getString(Constants.SHARED_PREF_USER_NAME, null);
    }

    @Override
    public void setUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            mPreferences.edit().remove(Constants.SHARED_PREF_USER_NAME).apply();
        } else {
            mPreferences.edit().putString(Constants.SHARED_PREF_USER_NAME, userName).apply();
        }
    }

    @Override
    public String getClubName() {
        return mPreferences.getString(Constants.SHARED_PREF_CLUB_NAME, null);
    }

    @Override
    public void setClubName(String clubName) {
        if (TextUtils.isEmpty(clubName)) {
            mPreferences.edit().remove(Constants.SHARED_PREF_CLUB_NAME).apply();
        } else {
            mPreferences.edit().putString(Constants.SHARED_PREF_CLUB_NAME, clubName).apply();
        }
    }

    @Override
    public void onBackPressed() {
        if (!mDrawerToggle.isDrawerIndicatorEnabled()) {
            mDrawerToggle.setDrawerIndicatorEnabled(true);
        }
        super.onBackPressed();
    }

    public void showFragment(Fragment fragment, boolean hasNavDrawer) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();
        mDrawerToggle.setDrawerIndicatorEnabled(hasNavDrawer);
    }

    private void initNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNavDrawerAdapter = new NavDrawerAdapter(this);
        ListView drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(mNavDrawerAdapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mNavDrawerAdapter.setSelected(position);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    private void initActionBar() {
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void showWelcomeDialog() {
        mWelcomeDialog = new WelcomeDialogFragment();
        mWelcomeDialog.show(getSupportFragmentManager(), "WelcomeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.d(TAG, "User accepted welcome dialog");
        if (dialog == mWelcomeDialog) {
            mWelcomeDialog.getDialog().dismiss();
            mWelcomeDialog = null;
            mLastRunVersion = Constants.VERSION;
            mPreferences.edit()
                    .putString(Constants.SHARED_PREF_LAST_RUN_VERSION, mLastRunVersion.toString())
                    .apply();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.d(TAG, "User rejected welcome dialog");
        if (dialog == mWelcomeDialog) {
            mWelcomeDialog.getDialog().cancel();
            mWelcomeDialog = null;
            finish();
        }
    }
}
