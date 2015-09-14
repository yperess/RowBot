package com.concept2.api.rowbot.logbook;

import java.util.List;

public interface Interval extends Snapshot {

    List<Snapshot> getSnapshots();
    long getRestTime();
    int getRestDistance();
}
