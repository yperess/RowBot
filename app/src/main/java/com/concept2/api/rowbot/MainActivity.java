package com.concept2.api.rowbot;

import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.concept2.api.Concept2;
import com.concept2.api.rowbot.fragments.DebugFragment;

public class MainActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE) != null) {
            // Launched from a device connection.
            Concept2.PaceMonitor.start(this);
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new DebugFragment(), DebugFragment.TAG)
                .commit();
    }
}
