package com.declan.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkloadController {
    @GetMapping("/io-bound")
    public String ioBoundWorkload(@RequestParam(defaultValue = "100") int duration) {
        try {
            Thread.sleep(duration   * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return "I/O-bound task completed";
    }
    @GetMapping("/cpu-bound")
    public String cpuBoundWorkload(@RequestParam(defaultValue = "1000000") int iterations) {
        long result = 0;
        for (int i = 0; i < iterations; i++) {
            result += i * i;
        }
        return "CPU-bound task completed with result: " + result;
    }
    @GetMapping("/synchronized")
    public String synchronizedWorkload(){
        synchronized (this) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        return "Synchronized task completed";
    }
}
