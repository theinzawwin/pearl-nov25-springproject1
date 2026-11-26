package com.pearl.nov25.springproj1.controllers;

import com.pearl.nov25.springproj1.services.SpringSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/spring-scheduler")
public class SpringSchedulerController {

    @Autowired
    private SpringSchedulerService springSchedulerService;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSchedulerStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("timestamp", LocalDateTime.now());
            status.put("schedulerType", "Spring @Scheduled");
            status.put("status", "RUNNING");
            
            // Add task statistics
            ConcurrentHashMap<String, AtomicInteger> executionCounts = springSchedulerService.getTaskExecutionCounts();
            ConcurrentHashMap<String, LocalDateTime> lastExecutions = springSchedulerService.getTaskLastExecutions();
            
            Map<String, Object> taskStats = new HashMap<>();
            executionCounts.forEach((taskName, count) -> {
                Map<String, Object> taskInfo = new HashMap<>();
                taskInfo.put("executionCount", count.get());
                taskInfo.put("lastExecution", lastExecutions.get(taskName));
                taskInfo.put("status", springSchedulerService.getTaskStatus(taskName));
                taskStats.put(taskName, taskInfo);
            });
            
            status.put("tasks", taskStats);
            status.put("totalTasks", executionCounts.size());
            
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to get scheduler status", "message", e.getMessage()));
        }
    }

    @GetMapping("/tasks")
    public ResponseEntity<Map<String, Object>> getAllTasks() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDateTime.now());
            
            ConcurrentHashMap<String, AtomicInteger> executionCounts = springSchedulerService.getTaskExecutionCounts();
            ConcurrentHashMap<String, LocalDateTime> lastExecutions = springSchedulerService.getTaskLastExecutions();
            
            Map<String, Object> tasks = new HashMap<>();
            executionCounts.forEach((taskName, count) -> {
                Map<String, Object> taskDetails = new HashMap<>();
                taskDetails.put("taskName", taskName);
                taskDetails.put("executionCount", count.get());
                taskDetails.put("lastExecution", lastExecutions.get(taskName));
                taskDetails.put("isRunning", springSchedulerService.isTaskRunning(taskName));
                taskDetails.put("status", springSchedulerService.getTaskStatus(taskName));
                tasks.put(taskName, taskDetails);
            });
            
            response.put("tasks", tasks);
            response.put("totalTasks", tasks.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to get tasks", "message", e.getMessage()));
        }
    }

    @GetMapping("/task/{taskName}")
    public ResponseEntity<Map<String, Object>> getTaskDetails(@PathVariable String taskName) {
        try {
            Map<String, Object> taskDetails = new HashMap<>();
            taskDetails.put("taskName", taskName);
            taskDetails.put("isRunning", springSchedulerService.isTaskRunning(taskName));
            taskDetails.put("status", springSchedulerService.getTaskStatus(taskName));
            
            ConcurrentHashMap<String, AtomicInteger> executionCounts = springSchedulerService.getTaskExecutionCounts();
            ConcurrentHashMap<String, LocalDateTime> lastExecutions = springSchedulerService.getTaskLastExecutions();
            
            if (executionCounts.containsKey(taskName)) {
                taskDetails.put("executionCount", executionCounts.get(taskName).get());
                taskDetails.put("lastExecution", lastExecutions.get(taskName));
            } else {
                taskDetails.put("executionCount", 0);
                taskDetails.put("lastExecution", null);
                taskDetails.put("status", "NOT_FOUND");
            }
            
            return ResponseEntity.ok(taskDetails);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to get task details", "message", e.getMessage()));
        }
    }

    @PostMapping("/reset/{taskName}")
    public ResponseEntity<Map<String, Object>> resetTaskStatistics(@PathVariable String taskName) {
        try {
            springSchedulerService.resetTaskStatistics(taskName);
            return ResponseEntity.ok(Map.of(
                "taskName", taskName,
                "message", "Task statistics reset successfully",
                "timestamp", LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to reset task statistics", "message", e.getMessage()));
        }
    }

    @PostMapping("/reset-all")
    public ResponseEntity<Map<String, Object>> resetAllStatistics() {
        try {
            springSchedulerService.resetAllStatistics();
            return ResponseEntity.ok(Map.of(
                "message", "All task statistics reset successfully",
                "timestamp", LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to reset all statistics", "message", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        try {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "UP");
            health.put("scheduler", "Spring @Scheduled");
            health.put("timestamp", LocalDateTime.now());
            health.put("activeTasks", springSchedulerService.getTaskExecutionCounts().size());
            
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("status", "DOWN", "error", e.getMessage()));
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("timestamp", LocalDateTime.now());
            
            ConcurrentHashMap<String, AtomicInteger> executionCounts = springSchedulerService.getTaskExecutionCounts();
            ConcurrentHashMap<String, LocalDateTime> lastExecutions = springSchedulerService.getTaskLastExecutions();
            
            stats.put("totalTasks", executionCounts.size());
            stats.put("totalExecutions", executionCounts.values().stream()
                .mapToInt(AtomicInteger::get).sum());
            
            Map<String, Integer> taskCounts = new HashMap<>();
            executionCounts.forEach((taskName, count) -> {
                taskCounts.put(taskName, count.get());
            });
            stats.put("taskExecutionCounts", taskCounts);
            
            Map<String, LocalDateTime> taskLastRuns = new HashMap<>();
            lastExecutions.forEach((taskName, lastRun) -> {
                taskLastRuns.put(taskName, lastRun);
            });
            stats.put("taskLastExecutions", taskLastRuns);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to get statistics", "message", e.getMessage()));
        }
    }
}
