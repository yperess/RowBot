package com.concept2.api.pacemonitor;

import com.concept2.api.Result;

public interface PaceMonitorResult extends Result {
    PaceMonitorStatus getPaceMonitorStatus();
}
