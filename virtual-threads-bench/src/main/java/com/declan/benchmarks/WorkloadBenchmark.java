package com.declan.benchmarks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(1)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class WorkloadBenchmark {

    private static final String BASE_URL = "http://localhost:8080/";

    private void executeGetRequest(String endpoint) throws IOException {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        connection.disconnect();

        if (responseCode != 200) {
            throw new IOException("Failed to get successful response: " + responseCode);
        }
    }

    @Benchmark
public void ioBoundWorkload() throws IOException {
    executeGetRequest("io-bound?ms=10");
}

@Benchmark
public void cpuBoundWorkload() throws IOException {
    executeGetRequest("cpu-bound?iterations=1000000");
}

@Benchmark
public void mixedWorkload() throws IOException {
    executeGetRequest("mixed?ms=10&iterations=50000");
}

@Benchmark
@Threads(10)
public void ioBoundLowConcurrency() throws IOException {
    executeGetRequest("io-bound?ms=10");
}

@Benchmark
public void synchronizedWorkload() throws IOException {
    executeGetRequest("synchronized");
}
    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(WorkloadBenchmark.class.getSimpleName())
                .build();
        new Runner(options).run();
    }
}