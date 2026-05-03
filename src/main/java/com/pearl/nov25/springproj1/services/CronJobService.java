package com.pearl.nov25.springproj1.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CronJobService {

    private static final Logger logger = LoggerFactory.getLogger(CronJobService.class);
    private final ConcurrentHashMap<String, AtomicInteger> jobExecutionCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> jobLastExecutions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Boolean> jobStatus = new ConcurrentHashMap<>();

    // Every minute cron job
    @Scheduled(cron = "0 * * * * *")
    public void everyMinuteJob() {
        executeCronJob("every-minute-job", "Every Minute Job (0 seconds of every minute)");
    }

    // Every 5 minutes cron job
    @Scheduled(cron = "0 */5 * * * *")
    public void everyFiveMinutesJob() {
        executeCronJob("every-five-minutes-job", "Every 5 Minutes Job");
    }

    // Every hour cron job
    @Scheduled(cron = "0 0 * * * *")
    public void everyHourJob() {
        executeCronJob("every-hour-job", "Every Hour Job (at minute 0)");
    }

    // Every day at midnight cron job
    @Scheduled(cron = "0 0 0 * * *")
    public void dailyMidnightJob() {
        executeCronJob("daily-midnight-job", "Daily Midnight Job");
    }

    // Every day at 9 AM cron job
    @Scheduled(cron = "0 0 9 * * *")
    public void daily9AMJob() {
        executeCronJob("daily-9am-job", "Daily 9 AM Job");
    }

    // Every Monday at 8 AM cron job
    @Scheduled(cron = "0 0 8 ? * MON")
    public void weeklyMondayJob() {
        executeCronJob("weekly-monday-job", "Weekly Monday 8 AM Job");
    }

    // First day of every month at midnight cron job
    @Scheduled(cron = "0 0 0 1 * ?")
    public void monthlyFirstDayJob() {
        executeCronJob("monthly-first-day-job", "Monthly First Day Job");
    }

    // Every weekday at 9 AM cron job (Monday-Friday)
    @Scheduled(cron = "0 0 9 ? * MON-FRI")
    public void weekdays9AMJob() {
        executeCronJob("weekdays-9am-job", "Weekdays 9 AM Job (Mon-Fri)");
    }

    // Every weekend at 10 AM cron job (Saturday-Sunday)
    @Scheduled(cron = "0 0 10 ? * SAT-SUN")
    public void weekends10AMJob() {
        executeCronJob("weekends-10am-job", "Weekends 10 AM Job (Sat-Sun)");
    }

    // Every 30 seconds during business hours (9 AM - 5 PM, Monday-Friday)
    @Scheduled(cron = "*/30 * 9-17 * * MON-FRI")
    public void businessHours30SecondsJob() {
        executeCronJob("business-hours-30s-job", "Business Hours 30 Seconds Job (9 AM-5 PM, Mon-Fri)");
    }

    // Every 15 minutes during business hours
    @Scheduled(cron = "0/15 * 9-17 * * MON-FRI")
    public void businessHours15MinutesJob() {
        executeCronJob("business-hours-15m-job", "Business Hours 15 Minutes Job (9 AM-5 PM, Mon-Fri)");
    }

    // Custom cron job - every 2 hours between 8 AM and 8 PM
    @Scheduled(cron = "0 0 8-20/2 * * *")
    public void customEvery2HoursJob() {
        executeCronJob("custom-every-2-hours-job", "Custom Every 2 Hours Job (8 AM-8 PM)");
    }

    // Data backup cron job - daily at 2 AM
    @Scheduled(cron = "0 0 2 * * *")
    public void dataBackupJob() {
        executeCronJob("data-backup-job", "Data Backup Job (Daily at 2 AM)");
    }

    // System cleanup cron job - every Sunday at 3 AM
    @Scheduled(cron = "0 0 3 ? * SUN")
    public void systemCleanupJob() {
        executeCronJob("system-cleanup-job", "System Cleanup Job (Every Sunday at 3 AM)");
    }

    // Report generation cron job - every weekday at 5 PM
    @Scheduled(cron = "0 0 17 ? * MON-FRI")
    public void reportGenerationJob() {
        executeCronJob("report-generation-job", "Report Generation Job (Weekdays at 5 PM)");
    }

    // Health check cron job - every 10 minutes
    @Scheduled(cron = "0 */10 * * * *")
    public void healthCheckJob() {
        executeCronJob("health-check-job", "Health Check Job (Every 10 minutes)");
    }

    private void executeCronJob(String jobName, String description) {
        try {
            logger.info("Executing cron job: {} - {}", jobName, description);
            
            // Update execution statistics
            jobExecutionCounts.computeIfAbsent(jobName, k -> new AtomicInteger(0)).incrementAndGet();
            jobLastExecutions.put(jobName, LocalDateTime.now());
            jobStatus.put(jobName, true);
            
            // Simulate job execution (replace with actual business logic)
            Thread.sleep(1000); // Simulate 1 second of work
            
            logger.info("Completed cron job: {} - Execution count: {}", 
                jobName, jobExecutionCounts.get(jobName).get());
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Cron job {} was interrupted", jobName);
            jobStatus.put(jobName, false);
        } catch (Exception e) {
            logger.error("Error executing cron job {}: {}", jobName, e.getMessage());
            jobStatus.put(jobName, false);
        }
    }

    public ConcurrentHashMap<String, AtomicInteger> getJobExecutionCounts() {
        return jobExecutionCounts;
    }

    public ConcurrentHashMap<String, LocalDateTime> getJobLastExecutions() {
        return jobLastExecutions;
    }

    public ConcurrentHashMap<String, Boolean> getJobStatus() {
        return jobStatus;
    }

    public void resetJobStatistics(String jobName) {
        if (jobName != null) {
            jobExecutionCounts.remove(jobName);
            jobLastExecutions.remove(jobName);
            jobStatus.remove(jobName);
        }
    }

    public void resetAllStatistics() {
        jobExecutionCounts.clear();
        jobLastExecutions.clear();
        jobStatus.clear();
    }

    public boolean isJobRunning(String jobName) {
        return jobStatus.getOrDefault(jobName, false);
    }

    public String getJobStatusDetails(String jobName) {
        if (!jobExecutionCounts.containsKey(jobName)) {
            return "NOT_FOUND";
        }
        
        LocalDateTime lastExecution = jobLastExecutions.get(jobName);
        Boolean status = jobStatus.get(jobName);
        Integer count = jobExecutionCounts.get(jobName).get();
        
        return String.format("Status: %s, Last Execution: %s, Count: %d", 
            status != null ? status : "UNKNOWN", 
            lastExecution != null ? lastExecution : "NEVER", 
            count != null ? count : 0);
    }

    public boolean enableJob(String jobName) {
        jobStatus.put(jobName, true);
        logger.info("Cron job {} enabled", jobName);
        return true;
    }

    public boolean disableJob(String jobName) {
        jobStatus.put(jobName, false);
        logger.info("Cron job {} disabled", jobName);
        return true;
    }
}
