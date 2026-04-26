package com.pearl.nov25.springproj1.dtos;

import java.time.LocalDateTime;
import java.util.Map;

public record CronJobInput(
    String jobName,
    String description,
    String cronExpression,
    String timeZone,
    Map<String, Object> parameters,
    boolean enabled,
    LocalDateTime startTime,
    LocalDateTime endTime
) {}
