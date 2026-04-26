package com.pearl.nov25.springproj1.dtos;

import java.time.LocalDateTime;
import java.util.Map;

public record SchedulerInput(
    String jobName,
    String jobGroup,
    String description,
    String cronExpression,
    String className,
    String methodName,
    Map<String, Object> parameters,
    LocalDateTime startTime,
    LocalDateTime endTime,
    boolean isActive
) {}
