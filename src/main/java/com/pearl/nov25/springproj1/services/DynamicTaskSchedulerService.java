package com.pearl.nov25.springproj1.services;

import com.pearl.nov25.springproj1.dtos.SpringSchedulerInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class DynamicTaskSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(DynamicTaskSchedulerService.class);
    
    private final TaskScheduler taskScheduler;
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final Map<String, SpringSchedulerInput> taskConfigs = new ConcurrentHashMap<>();
    private final Map<String, Integer> taskExecutionCounts = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> taskLastExecutions = new ConcurrentHashMap<>();

    public DynamicTaskSchedulerService(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public boolean scheduleTask(SpringSchedulerInput schedulerInput) {
        try {
            String taskName = schedulerInput.taskName();
            
            // Cancel existing task if any
            cancelTask(taskName);
            
            Runnable task = createTask(schedulerInput);
            ScheduledFuture<?> scheduledFuture = null;
            
            switch (schedulerInput.scheduleType().toUpperCase()) {
                case "FIXED_RATE":
                    long rate = Long.parseLong(schedulerInput.scheduleExpression());
                    scheduledFuture = taskScheduler.scheduleAtFixedRate(task, Duration.ofMillis(rate));
                    break;
                    
                case "FIXED_DELAY":
                    long delay = Long.parseLong(schedulerInput.scheduleExpression());
                    scheduledFuture = taskScheduler.scheduleWithFixedDelay(task, Duration.ofMillis(delay));
                    break;
                    
                case "CRON":
                    CronTrigger cronTrigger = new CronTrigger(schedulerInput.scheduleExpression());
                    scheduledFuture = taskScheduler.schedule(task, cronTrigger);
                    break;
                    
                case "INITIAL_DELAY":
                    long initialDelay = schedulerInput.initialDelay() != null ? 
                        schedulerInput.initialDelay() : Long.parseLong(schedulerInput.scheduleExpression());
                    scheduledFuture = taskScheduler.scheduleAtFixedRate(
                        task, 
                        Instant.now().plusMillis(initialDelay),
                        Duration.ofMillis(Long.parseLong(schedulerInput.scheduleExpression()))
                    );
                    break;
                    
                default:
                    logger.error("Unsupported schedule type: {}", schedulerInput.scheduleType());
                    return false;
            }
            
            if (scheduledFuture != null) {
                scheduledTasks.put(taskName, scheduledFuture);
                taskConfigs.put(taskName, schedulerInput);
                taskExecutionCounts.put(taskName, 0);
                logger.info("Task '{}' scheduled successfully with type: {}", taskName, schedulerInput.scheduleType());
                return true;
            }
            
        } catch (Exception e) {
            logger.error("Failed to schedule task '{}': {}", schedulerInput.taskName(), e.getMessage());
        }
        return false;
    }

    public boolean cancelTask(String taskName) {
        try {
            ScheduledFuture<?> scheduledFuture = scheduledTasks.get(taskName);
            if (scheduledFuture != null) {
                boolean cancelled = scheduledFuture.cancel(false);
                scheduledTasks.remove(taskName);
                taskConfigs.remove(taskName);
                logger.info("Task '{}' cancelled: {}", taskName, cancelled);
                return cancelled;
            }
        } catch (Exception e) {
            logger.error("Failed to cancel task '{}': {}", taskName, e.getMessage());
        }
        return false;
    }

    public boolean pauseTask(String taskName) {
        try {
            ScheduledFuture<?> scheduledFuture = scheduledTasks.get(taskName);
            if (scheduledFuture != null) {
                boolean cancelled = scheduledFuture.cancel(false);
                if (cancelled) {
                    // Store the task config for resuming
                    SpringSchedulerInput config = taskConfigs.get(taskName);
                    if (config != null) {
                        taskConfigs.put(taskName + "_PAUSED", config);
                    }
                    logger.info("Task '{}' paused", taskName);
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to pause task '{}': {}", taskName, e.getMessage());
        }
        return false;
    }

    public boolean resumeTask(String taskName) {
        try {
            String pausedKey = taskName + "_PAUSED";
            SpringSchedulerInput config = taskConfigs.get(pausedKey);
            if (config != null) {
                boolean scheduled = scheduleTask(config);
                if (scheduled) {
                    taskConfigs.remove(pausedKey);
                    logger.info("Task '{}' resumed", taskName);
                }
                return scheduled;
            }
        } catch (Exception e) {
            logger.error("Failed to resume task '{}': {}", taskName, e.getMessage());
        }
        return false;
    }

    public boolean restartTask(String taskName) {
        try {
            SpringSchedulerInput config = taskConfigs.get(taskName);
            if (config != null) {
                cancelTask(taskName);
                return scheduleTask(config);
            }
        } catch (Exception e) {
            logger.error("Failed to restart task '{}': {}", taskName, e.getMessage());
        }
        return false;
    }

    public Map<String, Object> getTaskStatus(String taskName) {
        Map<String, Object> status = new ConcurrentHashMap<>();
        status.put("taskName", taskName);
        status.put("isScheduled", scheduledTasks.containsKey(taskName));
        status.put("executionCount", taskExecutionCounts.getOrDefault(taskName, 0));
        status.put("lastExecution", taskLastExecutions.get(taskName));
        
        SpringSchedulerInput config = taskConfigs.get(taskName);
        if (config != null) {
            status.put("config", Map.of(
                "description", config.description(),
                "scheduleType", config.scheduleType(),
                "scheduleExpression", config.scheduleExpression(),
                "enabled", config.enabled()
            ));
        }
        
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(taskName);
        if (scheduledFuture != null) {
            status.put("isCancelled", scheduledFuture.isCancelled());
            status.put("isDone", scheduledFuture.isDone());
        }
        
        return status;
    }

    public Map<String, Object> getAllTasksStatus() {
        Map<String, Object> allStatus = new ConcurrentHashMap<>();
        allStatus.put("totalTasks", scheduledTasks.size());
        allStatus.put("timestamp", LocalDateTime.now());
        
        Map<String, Object> tasks = new ConcurrentHashMap<>();
        scheduledTasks.keySet().forEach(taskName -> {
            tasks.put(taskName, getTaskStatus(taskName));
        });
        allStatus.put("tasks", tasks);
        
        return allStatus;
    }

    public void cancelAllTasks() {
        scheduledTasks.forEach((taskName, scheduledFuture) -> {
            try {
                scheduledFuture.cancel(false);
                logger.info("Cancelled task: {}", taskName);
            } catch (Exception e) {
                logger.error("Failed to cancel task '{}': {}", taskName, e.getMessage());
            }
        });
        scheduledTasks.clear();
        taskConfigs.clear();
        taskExecutionCounts.clear();
        taskLastExecutions.clear();
    }

    private Runnable createTask(SpringSchedulerInput schedulerInput) {
        return () -> {
            try {
                String taskName = schedulerInput.taskName();
                logger.info("Executing dynamic task: {} - {}", taskName, schedulerInput.description());
                
                // Update execution statistics
                taskExecutionCounts.merge(taskName, 1, Integer::sum);
                taskLastExecutions.put(taskName, LocalDateTime.now());
                
                // Simulate task execution (replace with actual business logic)
                if (schedulerInput.parameters() != null && !schedulerInput.parameters().isEmpty()) {
                    logger.info("Task parameters: {}", schedulerInput.parameters());
                }
                
                // Add your custom business logic here
                Thread.sleep(500); // Simulate work
                
                logger.info("Completed dynamic task: {} - Execution count: {}", 
                    taskName, taskExecutionCounts.get(taskName));
                    
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Dynamic task {} was interrupted", schedulerInput.taskName());
            } catch (Exception e) {
                logger.error("Error executing dynamic task {}: {}", 
                    schedulerInput.taskName(), e.getMessage());
            }
        };
    }
}
