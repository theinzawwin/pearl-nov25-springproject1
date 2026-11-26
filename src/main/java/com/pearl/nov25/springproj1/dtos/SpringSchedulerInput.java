package com.pearl.nov25.springproj1.dtos;

import java.time.LocalDateTime;
import java.util.Map;

public record SpringSchedulerInput(
    String taskName,
    String description,
    String scheduleType, // "FIXED_RATE", "FIXED_DELAY", "CRON", "INITIAL_DELAY"
    String scheduleExpression, // rate in ms, delay in ms, or cron expression
    Long initialDelay, // for initial delay type
    LocalDateTime startTime,
    LocalDateTime endTime,
    Map<String, Object> parameters,
    boolean enabled
) {}
