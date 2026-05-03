package com.pearl.nov25.springproj1.controllers;

import com.pearl.nov25.springproj1.services.CronJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/cron-jobs")
public class CronJobController {

    @Autowired
    private CronJobService cronJobService;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getCronJobsStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("timestamp", LocalDateTime.now());
            status.put("schedulerType", "Spring @Scheduled Cron Jobs");
            status.put("status", "RUNNING");
            
            // Add job statistics
            ConcurrentHashMap<String, AtomicInteger> executionCounts = cronJobService.getJobExecutionCounts();
            ConcurrentHashMap<String, LocalDateTime> lastExecutions = cronJobService.getJobLastExecutions();
            ConcurrentHashMap<String, Boolean> jobStatus = cronJobService.getJobStatus();
            
            Map<String, Object> jobStats = new HashMap<>();
            executionCounts.forEach((jobName, count) -> {
                Map<String, Object> jobInfo = new HashMap<>();
                jobInfo.put("executionCount", count.get());
                jobInfo.put("lastExecution", lastExecutions.get(jobName));
                jobInfo.put("isEnabled", jobStatus.getOrDefault(jobName, false));
                jobInfo.put("status", cronJobService.getJobStatusDetails(jobName));
                jobStats.put(jobName, jobInfo);
            });
            
            status.put("jobs", jobStats);
            status.put("totalJobs", executionCounts.size());
            status.put("enabledJobs", (int) jobStatus.values().stream().mapToInt(enabled -> enabled ? 1 : 0).sum());
            
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to get cron jobs status", "message", e.getMessage()));
        }
    }

    @GetMapping("/jobs")
    public ResponseEntity<Map<String, Object>> getAllCronJobs() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDateTime.now());
            
            ConcurrentHashMap<String, AtomicInteger> executionCounts = cronJobService.getJobExecutionCounts();
            ConcurrentHashMap<String, LocalDateTime> lastExecutions = cronJobService.getJobLastExecutions();
            ConcurrentHashMap<String, Boolean> jobStatus = cronJobService.getJobStatus();
            
            Map<String, Object> jobs = new HashMap<>();
            executionCounts.forEach((jobName, count) -> {
                Map<String, Object> jobDetails = new HashMap<>();
                jobDetails.put("jobName", jobName);
                jobDetails.put("executionCount", count.get());
                jobDetails.put("lastExecution", lastExecutions.get(jobName));
                jobDetails.put("isEnabled", jobStatus.getOrDefault(jobName, false));
                jobDetails.put("isRunning", cronJobService.isJobRunning(jobName));
                jobDetails.put("status", cronJobService.getJobStatusDetails(jobName));
                jobs.put(jobName, jobDetails);
            });
            
            response.put("jobs", jobs);
            response.put("totalJobs", jobs.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to get cron jobs", "message", e.getMessage()));
        }
    }

    @GetMapping("/job/{jobName}")
    public ResponseEntity<Map<String, Object>> getCronJobDetails(@PathVariable String jobName) {
        try {
            Map<String, Object> jobDetails = new HashMap<>();
            jobDetails.put("jobName", jobName);
            jobDetails.put("isRunning", cronJobService.isJobRunning(jobName));
            jobDetails.put("status", cronJobService.getJobStatusDetails(jobName));
            
            ConcurrentHashMap<String, AtomicInteger> executionCounts = cronJobService.getJobExecutionCounts();
            ConcurrentHashMap<String, LocalDateTime> lastExecutions = cronJobService.getJobLastExecutions();
            ConcurrentHashMap<String, Boolean> jobStatus = cronJobService.getJobStatus();
            
            if (executionCounts.containsKey(jobName)) {
                jobDetails.put("executionCount", executionCounts.get(jobName).get());
                jobDetails.put("lastExecution", lastExecutions.get(jobName));
                jobDetails.put("isEnabled", jobStatus.getOrDefault(jobName, false));
            } else {
                jobDetails.put("executionCount", 0);
                jobDetails.put("lastExecution", null);
                jobDetails.put("isEnabled", false);
                jobDetails.put("status", "NOT_FOUND");
            }
            
            return ResponseEntity.ok(jobDetails);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to get cron job details", "message", e.getMessage()));
        }
    }

    @PostMapping("/enable/{jobName}")
    public ResponseEntity<Map<String, Object>> enableCronJob(@PathVariable String jobName) {
        try {
            boolean success = cronJobService.enableJob(jobName);
            return ResponseEntity.ok(Map.of(
                "jobName", jobName,
                "success", success,
                "message", success ? "Cron job enabled successfully" : "Failed to enable cron job",
                "timestamp", LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to enable cron job", "message", e.getMessage()));
        }
    }

    @PostMapping("/disable/{jobName}")
    public ResponseEntity<Map<String, Object>> disableCronJob(@PathVariable String jobName) {
        try {
            boolean success = cronJobService.disableJob(jobName);
            return ResponseEntity.ok(Map.of(
                "jobName", jobName,
                "success", success,
                "message", success ? "Cron job disabled successfully" : "Failed to disable cron job",
                "timestamp", LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to disable cron job", "message", e.getMessage()));
        }
    }

    @PostMapping("/reset/{jobName}")
    public ResponseEntity<Map<String, Object>> resetCronJobStatistics(@PathVariable String jobName) {
        try {
            cronJobService.resetJobStatistics(jobName);
            return ResponseEntity.ok(Map.of(
                "jobName", jobName,
                "message", "Cron job statistics reset successfully",
                "timestamp", LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to reset cron job statistics", "message", e.getMessage()));
        }
    }

    @PostMapping("/reset-all")
    public ResponseEntity<Map<String, Object>> resetAllCronJobStatistics() {
        try {
            cronJobService.resetAllStatistics();
            return ResponseEntity.ok(Map.of(
                "message", "All cron job statistics reset successfully",
                "timestamp", LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to reset all cron job statistics", "message", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        try {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "UP");
            health.put("scheduler", "Spring @Scheduled Cron Jobs");
            health.put("timestamp", LocalDateTime.now());
            health.put("activeJobs", cronJobService.getJobExecutionCounts().size());
            health.put("enabledJobs", (int) cronJobService.getJobStatus().values().stream().mapToInt(enabled -> enabled ? 1 : 0).sum());
            
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("status", "DOWN", "error", e.getMessage()));
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getCronJobStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("timestamp", LocalDateTime.now());
            
            ConcurrentHashMap<String, AtomicInteger> executionCounts = cronJobService.getJobExecutionCounts();
            ConcurrentHashMap<String, LocalDateTime> lastExecutions = cronJobService.getJobLastExecutions();
            ConcurrentHashMap<String, Boolean> jobStatus = cronJobService.getJobStatus();
            
            stats.put("totalJobs", executionCounts.size());
            stats.put("totalExecutions", executionCounts.values().stream()
                .mapToInt(AtomicInteger::get).sum());
            stats.put("enabledJobs", (int) jobStatus.values().stream().mapToInt(enabled -> enabled ? 1 : 0).sum());
            stats.put("disabledJobs", (int) jobStatus.values().stream().mapToInt(enabled -> enabled ? 0 : 1).sum());
            
            Map<String, Integer> jobCounts = new HashMap<>();
            executionCounts.forEach((jobName, count) -> {
                jobCounts.put(jobName, count.get());
            });
            stats.put("jobExecutionCounts", jobCounts);
            
            Map<String, LocalDateTime> jobLastRuns = new HashMap<>();
            lastExecutions.forEach((jobName, lastRun) -> {
                jobLastRuns.put(jobName, lastRun);
            });
            stats.put("jobLastExecutions", jobLastRuns);
            
            Map<String, Boolean> jobStatusMap = new HashMap<>();
            jobStatus.forEach((jobName, status) -> {
                jobStatusMap.put(jobName, status);
            });
            stats.put("jobStatus", jobStatusMap);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to get cron job statistics", "message", e.getMessage()));
        }
    }

    @GetMapping("/available-jobs")
    public ResponseEntity<Map<String, Object>> getAvailableCronJobs() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDateTime.now());
            
            Map<String, String> availableJobs = new HashMap<>();
            availableJobs.put("every-minute-job", "Every Minute Job (0 seconds of every minute)");
            availableJobs.put("every-five-minutes-job", "Every 5 Minutes Job");
            availableJobs.put("every-hour-job", "Every Hour Job (at minute 0)");
            availableJobs.put("daily-midnight-job", "Daily Midnight Job");
            availableJobs.put("daily-9am-job", "Daily 9 AM Job");
            availableJobs.put("weekly-monday-job", "Weekly Monday 8 AM Job");
            availableJobs.put("monthly-first-day-job", "Monthly First Day Job");
            availableJobs.put("weekdays-9am-job", "Weekdays 9 AM Job (Mon-Fri)");
            availableJobs.put("weekends-10am-job", "Weekends 10 AM Job (Sat-Sun)");
            availableJobs.put("business-hours-30s-job", "Business Hours 30 Seconds Job (9 AM-5 PM, Mon-Fri)");
            availableJobs.put("business-hours-15m-job", "Business Hours 15 Minutes Job (9 AM-5 PM, Mon-Fri)");
            availableJobs.put("custom-every-2-hours-job", "Custom Every 2 Hours Job (8 AM-8 PM)");
            availableJobs.put("data-backup-job", "Data Backup Job (Daily at 2 AM)");
            availableJobs.put("system-cleanup-job", "System Cleanup Job (Every Sunday at 3 AM)");
            availableJobs.put("report-generation-job", "Report Generation Job (Weekdays at 5 PM)");
            availableJobs.put("health-check-job", "Health Check Job (Every 10 minutes)");
            
            response.put("availableJobs", availableJobs);
            response.put("totalAvailableJobs", availableJobs.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to get available cron jobs", "message", e.getMessage()));
        }
    }
}
