package org.uvdev.rowbot.concept2api.rowbot.logbook;

import java.util.Date;
import java.util.List;

public interface Workout extends Snapshot {
    List<Interval> getIntervals();
    long getTotalRestTime();
    int getTotalRestDistance();
    Date getDate();
}