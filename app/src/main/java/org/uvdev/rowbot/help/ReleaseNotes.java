package org.uvdev.rowbot.help;
import android.content.res.Resources;

import org.uvdev.rowbot.common.data.Version;
import org.uvdev.rowbot.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ReleaseNotes {

    public static final ArrayList<Version> sReleaseVersions = new ArrayList<>();
    private static final HashMap<Version, String> sReleaseNotes = new HashMap<>();

    public static void init(Resources res) {
        // Only init once
        if (!sReleaseVersions.isEmpty()) return;

        // Add Release notes here.
        addReleaseNotes(new Version("A.1.0", 1), res.getString(R.string.release_notes_a_1_0));
        addReleaseNotes(new Version("A.1.1", 2), res.getString(R.string.release_notes_a_1_1));
        addReleaseNotes(new Version("A.1.2", 3), res.getString(R.string.release_notes_a_1_2));
        addReleaseNotes(new Version("A.1.3", 4), res.getString(R.string.release_notes_a_1_3));

        // Sort the release note versions in descending order.
        Collections.sort(sReleaseVersions, new Comparator<Version>() {
            @Override
            public int compare(Version lhs, Version rhs) {
                return rhs.compareTo(lhs);
            }
        });
    }

    private static void addReleaseNotes(Version version, String notes) {
        sReleaseVersions.add(version);
        sReleaseNotes.put(version, notes);
    }

    public static String getReleaseNotes(Version version) {
        return sReleaseNotes.get(version);
    }
}
