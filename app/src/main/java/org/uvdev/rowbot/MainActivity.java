package org.uvdev.rowbot;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.uvdev.rowbot.concept2api.Concept2;
import org.uvdev.rowbot.concept2api.Concept2StatusCodes;
import org.uvdev.rowbot.concept2api.ResultCallback;
import com.concept2.api.common.Constants;
import org.uvdev.rowbot.concept2api.rowbot.RowBot;
import org.uvdev.rowbot.concept2api.rowbot.profile.Profile;

import org.uvdev.rowbot.R;

import org.uvdev.rowbot.ui.dialogs.ProfileEditDialogFragment;
import org.uvdev.rowbot.ui.dialogs.WelcomeDialogFragment;
import org.uvdev.rowbot.model.RowBotActivity;
import org.uvdev.rowbot.ui.adapters.NavDrawerAdapter;
import org.uvdev.rowbot.ui.fragments.HomePagerFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RowBotActivity,
        WelcomeDialogFragment.WelcomeDialogListener {

    private static final String TAG = "RowBotActivity";

    private SharedPreferences mPreferences;
    private int mLastRunVersion;

    private Toolbar mToolbar;
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
        int lastRunVersion = mPreferences.getInt(Constants.SHARED_PREF_LAST_RUN_VERSION, -1);
        mLastRunVersion = lastRunVersion;

        if (getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE) != null) {
            // Launched from a device connection.
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentVersionCode = getRowBotApplication().getCurrentVersion().getVersionCode();
        if (mLastRunVersion == -1 || mLastRunVersion < currentVersionCode) {
            // Either never run or an update was issued. Show the dialog.
            showWelcomeDialog();
        } else {
            onReady();
        }
    }

    public RowBotApplication getRowBotApplication() {
        return (RowBotApplication) getApplication();
    }

    protected void onReady() {
        Concept2.RowBot.loadProfiles(this, null /* profileId */).setResultCallback(
                new ResultCallback<RowBot.LoadProfilesResult>() {
                    @Override
                    public void onResult(RowBot.LoadProfilesResult result) {
                        if (result.getStatus() == Concept2StatusCodes.OK) {
                            if (result.getProfiles().isEmpty()) {
                                // Force the user to create a profile.
                                showCreateProfileDialog();
                            } else {
                                // TODO add freeze concept to profile then release the loaded
                                // profile.
                                NavDrawerAdapter.setProfiles(
                                        sortLoadedProfiles(result.getProfiles()));
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error loading profiles",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void showCreateProfileDialog() {
        if (getSupportFragmentManager().findFragmentByTag(ProfileEditDialogFragment.TAG) == null) {
            // Only show the fragment if not already showing.
            ProfileEditDialogFragment.createInstance(false /* isEdit */, false /* cancelable */)
                    .show(getSupportFragmentManager(), ProfileEditDialogFragment.TAG);
        }
    }

    private List<Profile> sortLoadedProfiles(List<Profile> profiles) {
        String profilesStr = mPreferences.getString(Constants.SHARED_PREF_SELECTED_PROFILE_IDS,
                null);
        if (TextUtils.isEmpty(profilesStr)) {
            return profiles;
        }
        String[] profileIds = profilesStr.split(",");
        ArrayList<Profile> sorted = new ArrayList<>(profiles);
        final int size = sorted.size();
        for (int i = 0; i < profileIds.length; ++i) {
            if (!profileIds[i].equals(sorted.get(i).getProfileId())) {
                // Find the correct profile for this slot.
                for (int j = i + 1; j < size; ++j) {
                    if (profileIds[i].equals(sorted.get(j).getProfileId())) {
                        Profile temp = sorted.get(i);
                        sorted.set(i, sorted.get(j));
                        sorted.set(j, temp);
                        break;
                    }
                }
            }
        }
        return sorted;
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
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            mDrawerToggle.setDrawerIndicatorEnabled(true);
        }
    }

    public void showFragment(Fragment fragment, boolean hasNavDrawer) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
        setHasNavDrawer(hasNavDrawer);
        closeDrawers();
    }

    public void setHasNavDrawer(boolean hasNavDrawer) {
        if (hasNavDrawer) {
            mDrawerToggle.setDrawerIndicatorEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void showHome() {
        getSupportFragmentManager().popBackStack();
        mDrawerToggle.setDrawerIndicatorEnabled(true);
    }

    public void closeDrawers() {
        mDrawerLayout.closeDrawers();
    }

    public SharedPreferences.Editor editSharedPreferences() {
        return mPreferences.edit();
    }

    private void initNavigationDrawer() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNavDrawerAdapter = NavDrawerAdapter.getInstance(this);
        RecyclerView drawerList = (RecyclerView) findViewById(R.id.left_drawer);
        drawerList.setHasFixedSize(true);
        drawerList.setAdapter(mNavDrawerAdapter);
        drawerList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
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
            mLastRunVersion = getRowBotApplication().getCurrentVersion().getVersionCode();
            mPreferences.edit()
                    .putInt(Constants.SHARED_PREF_LAST_RUN_VERSION, mLastRunVersion)
                    .apply();
            onReady();
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
