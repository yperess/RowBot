package com.concept2.api.rowbot;

import android.content.SharedPreferences;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.concept2.api.Concept2;
import com.concept2.api.common.Constants;
import com.concept2.api.common.data.Version;
import com.concept2.api.rowbot.ui.fragments.DebugFragment;
import com.concept2.api.rowbot.ui.dialogs.WelcomeDialogFragment;
import com.concept2.api.rowbot.model.RowBotActivity;
import com.concept2.api.rowbot.ui.adapters.NavDrawerAdapter;

public class MainActivity extends FragmentActivity implements RowBotActivity,
        WelcomeDialogFragment.WelcomeDialogListener {

    private static final String TAG = "RowBotActivity";

    private SharedPreferences mPreferences;
    private Version mLastRunVersion;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setActionBarTitle(int resId) {
        getActionBar().setTitle(resId);
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
