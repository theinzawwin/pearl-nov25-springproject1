package com.pearl.nov25.springproj1.services;

import com.pearl.nov25.springproj1.dtos.SchedulerInput;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SchedulerService {

    @Autowired
    private Scheduler scheduler;

    public boolean scheduleJob(SchedulerInput schedulerInput) {
        try {
            JobDetail jobDetail = JobBuilder.newJob()
                    .withIdentity(schedulerInput.jobName(), schedulerInput.jobGroup())
                    .withDescription(schedulerInput.description())
                    .storeDurably()
                    .build();

            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            jobDataMap.put("className", schedulerInput.className());
            jobDataMap.put("methodName", schedulerInput.methodName());
            jobDataMap.put("parameters", schedulerInput.parameters());

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(schedulerInput.jobName() + "_trigger", schedulerInput.jobGroup())
                    .withDescription(schedulerInput.description())
                    .withSchedule(CronScheduleBuilder.cronSchedule(schedulerInput.cronExpression()))
                    .startAt(convertToDate(schedulerInput.startTime()))
                    .endAt(convertToDate(schedulerInput.endTime()))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to schedule job: " + e.getMessage(), e);
        }
    }

    public boolean pauseJob(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            scheduler.pauseJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to pause job: " + e.getMessage(), e);
        }
    }

    public boolean resumeJob(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            scheduler.resumeJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to resume job: " + e.getMessage(), e);
        }
    }

    public boolean deleteJob(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            scheduler.deleteJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to delete job: " + e.getMessage(), e);
        }
    }

    public boolean triggerJob(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            scheduler.triggerJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to trigger job: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> getAllJobs() {
        List<Map<String, Object>> jobs = new ArrayList<>();
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.groupEquals(groupName))) {
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    
                    Map<String, Object> jobInfo = Map.of(
                        "jobName", jobKey.getName(),
                        "jobGroup", jobKey.getGroup(),
                        "description", jobDetail.getDescription() != null ? jobDetail.getDescription() : "",
                        "className", jobDetail.getJobDataMap().getString("className"),
                        "methodName", jobDetail.getJobDataMap().getString("methodName"),
                        "parameters", jobDetail.getJobDataMap().get("parameters"),
                        "triggers", triggers.stream().map(trigger -> {
                            try {
                                return Map.of(
                                    "triggerName", trigger.getKey().getName(),
                                    "triggerGroup", trigger.getKey().getGroup(),
                                    "startTime", trigger.getStartTime(),
                                    "endTime", trigger.getEndTime(),
                                    "nextFireTime", trigger.getNextFireTime(),
                                    "previousFireTime", trigger.getPreviousFireTime(),
                                    "status", scheduler.getTriggerState(trigger.getKey()).name()
                                );
                            } catch (SchedulerException e) {
                                return Map.of(
                                    "triggerName", trigger.getKey().getName(),
                                    "triggerGroup", trigger.getKey().getGroup(),
                                    "startTime", trigger.getStartTime(),
                                    "endTime", trigger.getEndTime(),
                                    "nextFireTime", trigger.getNextFireTime(),
                                    "previousFireTime", trigger.getPreviousFireTime(),
                                    "status", "ERROR"
                                );
                            }
                        }).toList()
                    );
                    jobs.add(jobInfo);
                }
            }
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to get all jobs: " + e.getMessage(), e);
        }
        return jobs;
    }

    public boolean updateJobSchedule(String jobName, String jobGroup, String newCronExpression) {
        try {
            TriggerKey triggerKey = new TriggerKey(jobName + "_trigger", jobGroup);
            
            Trigger newTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(newCronExpression))
                    .build();

            scheduler.rescheduleJob(triggerKey, newTrigger);
            return true;
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to update job schedule: " + e.getMessage(), e);
        }
    }

    private Date convertToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
