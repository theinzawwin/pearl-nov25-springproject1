package com.pearl.nov25.springproj1.controllers;

import com.pearl.nov25.springproj1.dtos.SpringSchedulerInput;
import com.pearl.nov25.springproj1.services.DynamicTaskSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/dynamic-scheduler")
public class DynamicSchedulerController {

    @Autowired
    private DynamicTaskSchedulerService dynamicTaskSchedulerService;

    @PostMapping("/schedule")
    public ResponseEntity<Map<String, Object>> scheduleTask(@RequestBody SpringSchedulerInput schedulerInput) {
        try {
            boolean success = dynamicTaskSchedulerService.scheduleTask(schedulerInput);
            Map<String, Object> response = Map.of(
                "success", success,
                "taskName", schedulerInput.taskName(),
                "message", success ? "Task scheduled successfully" : "Failed to schedule task",
                "timestamp", LocalDateTime.now()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "error", e.getMessage(), "timestamp", LocalDateTime.now()));
        }
    }

    @PostMapping("/cancel/{taskName}")
    public ResponseEntity<Map<String, Object>> cancelTask(@PathVariable String taskName) {
        try {
            boolean success = dynamicTaskSchedulerService.cancelTask(taskName);
            Map<String, Object> response = Map.of(
                "success", success,
                "taskName", taskName,
                "message", success ? "Task cancelled successfully" : "Task not found or already cancelled",
                "timestamp", LocalDateTime.now()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "error", e.getMessage(), "timestamp", LocalDateTime.now()));
        }
    }

    @PostMapping("/pause/{taskName}")
    public ResponseEntity<Map<String, Object>> pauseTask(@PathVariable String taskName) {
        try {
            boolean success = dynamicTaskSchedulerService.pauseTask(taskName);
            Map<String, Object> response = Map.of(
                "success", success,
                "taskName", taskName,
                "message", success ? "Task paused successfully" : "Failed to pause task",
                "timestamp", LocalDateTime.now()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "error", e.getMessage(), "timestamp", LocalDateTime.now()));
        }
    }

    @PostMapping("/resume/{taskName}")
    public ResponseEntity<Map<String, Object>> resumeTask(@PathVariable String taskName) {
        try {
            boolean success = dynamicTaskSchedulerService.resumeTask(taskName);
            Map<String, Object> response = Map.of(
                "success", success,
                "taskName", taskName,
                "message", success ? "Task resumed successfully" : "Failed to resume task",
                "timestamp", LocalDateTime.now()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "error", e.getMessage(), "timestamp", LocalDateTime.now()));
        }
    }

    @PostMapping("/restart/{taskName}")
    public ResponseEntity<Map<String, Object>> restartTask(@PathVariable String taskName) {
        try {
            boolean success = dynamicTaskSchedulerService.restartTask(taskName);
            Map<String, Object> response = Map.of(
                "success", success,
                "taskName", taskName,
                "message", success ? "Task restarted successfully" : "Failed to restart task",
                "timestamp", LocalDateTime.now()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "error", e.getMessage(), "timestamp", LocalDateTime.now()));
        }
    }

    @GetMapping("/task/{taskName}")
    public ResponseEntity<Map<String, Object>> getTaskStatus(@PathVariable String taskName) {
        try {
            Map<String, Object> status = dynamicTaskSchedulerService.getTaskStatus(taskName);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to get task status", "message", e.getMessage()));
        }
    }

    @GetMapping("/tasks")
    public ResponseEntity<Map<String, Object>> getAllTasksStatus() {
        try {
            Map<String, Object> allStatus = dynamicTaskSchedulerService.getAllTasksStatus();
            return ResponseEntity.ok(allStatus);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to get all tasks status", "message", e.getMessage()));
        }
    }

    @PostMapping("/cancel-all")
    public ResponseEntity<Map<String, Object>> cancelAllTasks() {
        try {
            dynamicTaskSchedulerService.cancelAllTasks();
            Map<String, Object> response = Map.of(
                "message", "All tasks cancelled successfully",
                "timestamp", LocalDateTime.now()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to cancel all tasks", "message", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        try {
            Map<String, Object> health = Map.of(
                "status", "UP",
                "scheduler", "Dynamic Task Scheduler",
                "timestamp", LocalDateTime.now()
            );
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("status", "DOWN", "error", e.getMessage()));
        }
    }
}
