package com.rowbot.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rowbot.R;
import com.rowbot.help.ReleaseNotes;
import com.rowbot.ui.adapters.ReleaseNotesAdapter;

public class ReleaseNotesActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReleaseNotes.init(getResources());
        setContentView(R.layout.release_notes_activity);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ReleaseNotesAdapter());
    }
}
