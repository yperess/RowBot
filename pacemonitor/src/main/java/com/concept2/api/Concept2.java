package com.concept2.api;

import com.concept2.api.impl.LogbookImpl;
import com.concept2.api.impl.VirtualPaceMonitorImpl;
import com.concept2.api.model.LogbookApi;
import com.concept2.api.model.VirtualPaceMonitorApi;

public final class Concept2 {

    public static final VirtualPaceMonitorApi PaceMonitor = new VirtualPaceMonitorImpl();

    public static final LogbookApi Logbook = new LogbookImpl();
}
