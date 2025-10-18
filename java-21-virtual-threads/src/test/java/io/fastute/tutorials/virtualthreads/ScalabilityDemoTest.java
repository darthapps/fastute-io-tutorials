/*
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * #                                                                             #
 * #   Copyright (c) 2025 FastuteIO (FIO). All Rights Reserved.                 #
 * #                                                                             #
 * #   IT IS STRICTLY PROHIBITED TO USE, COPY, MODIFY, OR DISTRIBUTE THIS       #
 * #   SOFTWARE IN SOURCE OR BINARY FORMS FOR ANY PURPOSE WITHOUT EXPRESS       #
 * #   WRITTEN PERMISSION OF FIO.                                               #
 * #                                                                             #
 * #   IN NO EVENT SHALL FIO BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,       #
 * #   SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,   #
 * #   ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF   #
 * #   FIO HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.                  #
 * #                                                                             #
 * #   FIO SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT            #
 * #   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR    #
 * #   A PARTICULAR PURPOSE. FIO HAS NO OBLIGATION TO PROVIDE MAINTENANCE,      #
 * #   SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.                        #
 * #                                                                             #
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 */

package io.fastute.tutorials.virtualthreads;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ScalabilityDemo.
 */
class ScalabilityDemoTest {

    @Test
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testCreateManyVirtualThreads() throws InterruptedException {
        int threadCount = 1000;
        AtomicInteger completedTasks = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(threadCount);

        long startTime = System.currentTimeMillis();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        Thread.sleep(10);
                        completedTasks.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        boolean completed = latch.await(10, TimeUnit.SECONDS);
        long duration = System.currentTimeMillis() - startTime;

        assertThat(completed).isTrue();
        assertThat(completedTasks.get()).isEqualTo(threadCount);
        assertThat(duration).isLessThan(5000); // Should complete quickly
    }

    @Test
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testVirtualThreadsHandleIOBoundTasks() throws InterruptedException {
        int taskCount = 500;
        AtomicInteger completedTasks = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(taskCount);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    try {
                        // Simulate I/O operation
                        Thread.sleep(50);
                        completedTasks.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        boolean completed = latch.await(10, TimeUnit.SECONDS);
        
        assertThat(completed).isTrue();
        assertThat(completedTasks.get()).isEqualTo(taskCount);
    }

    @Test
    void testVirtualThreadCreationSpeed() {
        int threadCount = 10000;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            Thread.startVirtualThread(() -> {
                // Minimal work
            });
        }

        long duration = System.currentTimeMillis() - startTime;
        
        // Creating 10,000 virtual threads should be very fast
        assertThat(duration).isLessThan(1000); // Less than 1 second
        System.out.println("Created " + threadCount + " virtual threads in " + duration + "ms");
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testConcurrentExecution() throws InterruptedException {
        int taskCount = 100;
        AtomicInteger concurrentCount = new AtomicInteger(0);
        AtomicInteger maxConcurrent = new AtomicInteger(0);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(taskCount);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    try {
                        startLatch.await(); // Wait for all threads to be ready
                        
                        int current = concurrentCount.incrementAndGet();
                        maxConcurrent.updateAndGet(max -> Math.max(max, current));
                        
                        Thread.sleep(100); // Simulate work
                        
                        concurrentCount.decrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        completionLatch.countDown();
                    }
                });
            }
            
            Thread.sleep(100); // Let all threads start
            startLatch.countDown(); // Release all threads
        }

        boolean completed = completionLatch.await(5, TimeUnit.SECONDS);
        
        assertThat(completed).isTrue();
        assertThat(maxConcurrent.get()).isGreaterThan(10); // Should have high concurrency
        System.out.println("Max concurrent virtual threads: " + maxConcurrent.get());
    }

    @Test
    void testMemoryEfficiency() {
        Runtime runtime = Runtime.getRuntime();
        System.gc(); // Suggest garbage collection
        
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        int threadCount = 1000;
        CountDownLatch latch = new CountDownLatch(threadCount);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            
            long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
            long memoryIncrease = (memoryAfter - memoryBefore) / 1024 / 1024;
            
            System.out.println("Memory increase for " + threadCount + " virtual threads: " + memoryIncrease + " MB");
            
            // Virtual threads should use much less memory than platform threads
            // Platform threads would use ~1GB for 1000 threads, virtual threads should use much less
            assertThat(memoryIncrease).isLessThan(500); // Less than 500MB
        }
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testScalabilityComparison() throws InterruptedException {
        int taskCount = 1000;
        
        // Test with virtual threads
        long virtualStart = System.currentTimeMillis();
        CountDownLatch virtualLatch = new CountDownLatch(taskCount);
        
        try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                virtualExecutor.submit(() -> {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        virtualLatch.countDown();
                    }
                });
            }
        }
        virtualLatch.await();
        long virtualDuration = System.currentTimeMillis() - virtualStart;

        // Test with platform thread pool
        long platformStart = System.currentTimeMillis();
        CountDownLatch platformLatch = new CountDownLatch(taskCount);
        
        try (ExecutorService platformExecutor = Executors.newFixedThreadPool(100)) {
            for (int i = 0; i < taskCount; i++) {
                platformExecutor.submit(() -> {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        platformLatch.countDown();
                    }
                });
            }
        }
        platformLatch.await();
        long platformDuration = System.currentTimeMillis() - platformStart;

        System.out.println("Virtual threads: " + virtualDuration + "ms");
        System.out.println("Platform thread pool: " + platformDuration + "ms");
        System.out.println("Speedup: " + ((double) platformDuration / virtualDuration) + "x");

        // Virtual threads should be faster or comparable
        assertThat(virtualDuration).isLessThanOrEqualTo(platformDuration * 2);
    }
}
