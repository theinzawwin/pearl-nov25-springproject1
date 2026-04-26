package com.pearl.nov25.springproj1.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SpringSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(SpringSchedulerService.class);
    private final ConcurrentHashMap<String, AtomicInteger> taskExecutionCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> taskLastExecutions = new ConcurrentHashMap<>();

    // Fixed rate scheduling - executes at fixed intervals
    @Scheduled(fixedRate = 5000) // Every 5 seconds
    public void fixedRateTask() {
        executeTask("fixed-rate-task", "Fixed Rate Task (every 5 seconds)");
    }

    // Fixed delay scheduling - waits for completion before starting delay
    @Scheduled(fixedDelay = 10000) // Every 10 seconds after completion
    public void fixedDelayTask() {
        executeTask("fixed-delay-task", "Fixed Delay Task (10 seconds after completion)");
    }

    // Cron expression scheduling
    @Scheduled(cron = "0 */2 * * * *") // Every 2 minutes
    public void cronTask() {
        executeTask("cron-task", "Cron Task (every 2 minutes)");
    }

    // Initial delay with fixed rate
    @Scheduled(initialDelay = 15000, fixedRate = 20000) // Start after 15 seconds, then every 20 seconds
    public void initialDelayTask() {
        executeTask("initial-delay-task", "Initial Delay Task (start after 15s, then every 20s)");
    }

    // Daily cleanup task
    @Scheduled(cron = "0 0 2 * * *") // Every day at 2 AM
    public void dailyCleanupTask() {
        executeTask("daily-cleanup", "Daily Cleanup Task (2 AM daily)");
    }

    // Business hours task
    @Scheduled(cron = "0 0/30 9-17 * * MON-FRI") // Every 30 minutes during business hours (9 AM - 5 PM, Mon-Fri)
    public void businessHoursTask() {
        executeTask("business-hours", "Business Hours Task (every 30 min, 9 AM - 5 PM, weekdays)");
    }

    // Sample data generation task
    @Scheduled(fixedRate = 30000) // Every 30 seconds
    public void dataGenerationTask() {
        executeTask("data-generation", "Data Generation Task (every 30 seconds)");
    }

    // Health check task
    @Scheduled(fixedRate = 60000) // Every minute
    public void healthCheckTask() {
        executeTask("health-check", "Health Check Task (every minute)");
    }

    private void executeTask(String taskName, String description) {
        try {
            logger.info("Executing task: {} - {}", taskName, description);
            
            // Update execution statistics
            taskExecutionCounts.computeIfAbsent(taskName, k -> new AtomicInteger(0)).incrementAndGet();
            taskLastExecutions.put(taskName, LocalDateTime.now());
            
            // Simulate task execution (replace with actual business logic)
            Thread.sleep(1000); // Simulate 1 second of work
            
            logger.info("Completed task: {} - Execution count: {}", 
                taskName, taskExecutionCounts.get(taskName).get());
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Task {} was interrupted", taskName);
        } catch (Exception e) {
            logger.error("Error executing task {}: {}", taskName, e.getMessage());
        }
    }

    public ConcurrentHashMap<String, AtomicInteger> getTaskExecutionCounts() {
        return taskExecutionCounts;
    }

    public ConcurrentHashMap<String, LocalDateTime> getTaskLastExecutions() {
        return taskLastExecutions;
    }

    public void resetTaskStatistics(String taskName) {
        if (taskName != null) {
            taskExecutionCounts.remove(taskName);
            taskLastExecutions.remove(taskName);
        }
    }

    public void resetAllStatistics() {
        taskExecutionCounts.clear();
        taskLastExecutions.clear();
    }

    public boolean isTaskRunning(String taskName) {
        return taskExecutionCounts.containsKey(taskName);
    }

    public String getTaskStatus(String taskName) {
        if (!taskExecutionCounts.containsKey(taskName)) {
            return "NOT_FOUND";
        }
        
        LocalDateTime lastExecution = taskLastExecutions.get(taskName);
        if (lastExecution == null) {
            return "SCHEDULED";
        }
        
        return "RUNNING - Last execution: " + lastExecution + 
               " - Count: " + taskExecutionCounts.get(taskName).get();
    }
}
