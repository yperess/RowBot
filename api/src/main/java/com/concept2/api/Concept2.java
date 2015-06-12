package com.concept2.api;

import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.internal.PaceMonitorImpl;

/**
 * Concept2 API main class.
 */
public class Concept2 {

    // Not instantiable.
    private Concept2() {}

    /** APIs for communicating with the Concept2 Pace Monitor. */
    public static final PaceMonitor PaceMonitor = new PaceMonitorImpl();
}
