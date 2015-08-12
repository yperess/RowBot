package com.rowbot.model;

import java.util.List;

public interface Interval extends Snapshot {

    List<Snapshot> getSnapshots();
    long getRestTime();
    int getRestDistance();
}
