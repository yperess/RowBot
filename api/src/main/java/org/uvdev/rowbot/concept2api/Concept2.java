package org.uvdev.rowbot.concept2api;

import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.PaceMonitorImpl;
import org.uvdev.rowbot.concept2api.rowbot.RowBot;
import org.uvdev.rowbot.concept2api.rowbot.internal.RowBotImpl;

/**
 * Concept2 API main class.
 */
public class Concept2 {

    // Not instantiable.
    private Concept2() {}

    /** APIs for communicating with the Concept2 Pace Monitor. */
    public static final PaceMonitor PaceMonitor = new PaceMonitorImpl();

    /** APIs for accessing and manipulating RowBot data. */
    public static final RowBot RowBot = new RowBotImpl();
}
