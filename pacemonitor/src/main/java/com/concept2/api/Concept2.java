package com.concept2.api;

import com.concept2.api.logbook.impl.LogbookImpl;
import com.concept2.api.pacemonitor.impl.VirtualPaceMonitorImpl;
import com.concept2.api.logbook.LogbookApi;
import com.concept2.api.pacemonitor.VirtualPaceMonitorApi;

public final class Concept2 {

    public static final VirtualPaceMonitorApi PaceMonitor = new VirtualPaceMonitorImpl();

    public static final LogbookApi Logbook = new LogbookImpl();
}
