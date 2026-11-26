package com.pearl.nov25.springproj1.controllers;

import com.pearl.nov25.springproj1.dtos.SchedulerInput;
import com.pearl.nov25.springproj1.services.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

    @Autowired
    private SchedulerService schedulerService;

    @PostMapping("/schedule")
    public ResponseEntity<Boolean> scheduleJob(@RequestBody SchedulerInput schedulerInput) {
        try {
            boolean result = schedulerService.scheduleJob(schedulerInput);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(false);
        }
    }

    @PostMapping("/pause/{jobName}/{jobGroup}")
    public ResponseEntity<Boolean> pauseJob(@PathVariable String jobName, @PathVariable String jobGroup) {
        try {
            boolean result = schedulerService.pauseJob(jobName, jobGroup);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(false);
        }
    }

    @PostMapping("/resume/{jobName}/{jobGroup}")
    public ResponseEntity<Boolean> resumeJob(@PathVariable String jobName, @PathVariable String jobGroup) {
        try {
            boolean result = schedulerService.resumeJob(jobName, jobGroup);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(false);
        }
    }

    @DeleteMapping("/delete/{jobName}/{jobGroup}")
    public ResponseEntity<Boolean> deleteJob(@PathVariable String jobName, @PathVariable String jobGroup) {
        try {
            boolean result = schedulerService.deleteJob(jobName, jobGroup);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(false);
        }
    }

    @PostMapping("/trigger/{jobName}/{jobGroup}")
    public ResponseEntity<Boolean> triggerJob(@PathVariable String jobName, @PathVariable String jobGroup) {
        try {
            boolean result = schedulerService.triggerJob(jobName, jobGroup);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(false);
        }
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<Map<String, Object>>> getAllJobs() {
        try {
            List<Map<String, Object>> jobs = schedulerService.getAllJobs();
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/update-schedule/{jobName}/{jobGroup}")
    public ResponseEntity<Boolean> updateJobSchedule(
            @PathVariable String jobName, 
            @PathVariable String jobGroup,
            @RequestParam String newCronExpression) {
        try {
            boolean result = schedulerService.updateJobSchedule(jobName, jobGroup, newCronExpression);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(false);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Scheduler API is running");
    }
}
