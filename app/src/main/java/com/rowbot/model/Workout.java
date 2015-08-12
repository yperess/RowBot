package com.rowbot.model;

import java.util.Date;
import java.util.List;

public interface Workout extends Snapshot {
    List<Interval> getIntervals();
    long getTotalRestTime();
    int getTotalRestDistance();
    Date getDate();
}
