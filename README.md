# java-virtual-threads-benchmark

## Benchmark Results: Virtual Threads vs Platform Threads

Tested on JDK 21 with Spring Boot + Tomcat.  
Workloads: I/O (sleep), CPU (math loop), mixed, synchronized (pinning demo).

### Results Table

| Benchmark             | Platform Threads (ops/s) | Virtual Threads (ops/s) | Notes                                |
| --------------------- | ------------------------ | ----------------------- | ------------------------------------ |
| cpuBoundWorkload      | 679 ± 114                | 721 ± 83                | No benefit (CPU-bound)               |
| ioBoundLowConcurrency | 98 ± 1                   | 97 ± 1                  | Low concurrency – no difference      |
| ioBoundWorkload       | 9.7 ± 0.1                | 9.7 ± 0.1               | Low default concurrency – no win yet |
| mixedWorkload         | 83 ± 4                   | 82 ± 3                  | No clear benefit                     |
| synchronizedWorkload  | 0.20 ± 0.001             | 0.20 ± 0.001            | Pinning prevents gains               |

**Conclusion**  
Virtual threads provide **no improvement** in low-concurrency or CPU/synchronized workloads.  
They excel in **high I/O + high concurrency** scenarios (not fully shown here due to low JMH thread count).  
This aligns with migrations at Netflix/Stripe: big wins only when hitting thread-pool exhaustion.

**To reproduce**

- Toggle `spring.threads.virtual.enabled` in `application.properties`
- Run `mvn spring-boot:run` and `java -jar target/benchmarks.jar`
