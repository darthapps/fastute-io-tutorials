/**
 * ******************************************************************************
 *                                                                              *
 *    Copyright (c) 2025 FastuteIO (FIO). All Rights Reserved.                 *
 *                                                                              *
 *    IT IS STRICTLY PROHIBITED TO USE, COPY, MODIFY, OR DISTRIBUTE THIS       *
 *    SOFTWARE IN SOURCE OR BINARY FORMS FOR ANY PURPOSE WITHOUT EXPRESS       *
 *    WRITTEN PERMISSION OF FIO.                                               *
 *                                                                              *
 *    IN NO EVENT SHALL FIO BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,       *
 *    SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,   *
 *    ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF   *
 *    FIO HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.                  *
 *                                                                              *
 *    FIO SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT            *
 *    LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR    *
 *    A PARTICULAR PURPOSE. FIO HAS NO OBLIGATION TO PROVIDE MAINTENANCE,      *
 *    SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.                        *
 *                                                                              *
 * ******************************************************************************
 */

package io.fastute.tutorials.virtualthreads;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Example 4: Scalability Demonstration
 * Shows the difference in scalability between platform and virtual threads.
 */
public class ScalabilityDemo {

    /**
     * Creates many virtual threads to demonstrate scalability.
     */
    public static void createManyVirtualThreads(int count) throws InterruptedException {
        Instant start = Instant.now();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < count; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    try {
                        // Simulate I/O operation
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }

        Duration duration = Duration.between(start, Instant.now());
        System.out.println("Created and executed " + count + " virtual threads in " + duration.toMillis() + "ms");
    }

    /**
     * Creates many platform threads (limited by resources).
     */
    public static void createManyPlatformThreads(int count) throws InterruptedException {
        Instant start = Instant.now();

        try (ExecutorService executor = Executors.newFixedThreadPool(100)) {
            for (int i = 0; i < count; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    try {
                        // Simulate I/O operation
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }

        Duration duration = Duration.between(start, Instant.now());
        System.out.println("Created and executed " + count + " tasks with platform thread pool in " + duration.toMillis() + "ms");
    }

    /**
     * Demonstrates memory efficiency of virtual threads.
     */
    public static void memoryEfficiencyDemo() {
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("Memory before creating threads: " + (memoryBefore / 1024 / 1024) + " MB");

        // Create 10,000 virtual threads
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 10000; i++) {
                executor.submit(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
            System.out.println("Memory after creating 10,000 virtual threads: " + (memoryAfter / 1024 / 1024) + " MB");
            System.out.println("Memory increase: " + ((memoryAfter - memoryBefore) / 1024 / 1024) + " MB");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Scalability Demonstration ===\n");

        System.out.println("1. Creating 1,000 Virtual Threads:");
        createManyVirtualThreads(1000);

        System.out.println("\n2. Creating 1,000 Tasks with Platform Thread Pool:");
        createManyPlatformThreads(1000);

        System.out.println("\n3. Memory Efficiency:");
        memoryEfficiencyDemo();
    }
}
