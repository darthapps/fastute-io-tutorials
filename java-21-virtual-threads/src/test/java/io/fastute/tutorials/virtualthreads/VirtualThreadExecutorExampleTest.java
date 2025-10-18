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
 * Unit tests for VirtualThreadExecutorExample.
 */
class VirtualThreadExecutorExampleTest {

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testVirtualThreadExecutor() throws InterruptedException {
        AtomicInteger taskCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(10);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 10; i++) {
                executor.submit(() -> {
                    taskCount.incrementAndGet();
                    latch.countDown();
                });
            }
        }

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertThat(completed).isTrue();
        assertThat(taskCount.get()).isEqualTo(10);
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testVirtualThreadExecutorWithIOOperations() throws InterruptedException {
        AtomicInteger completedTasks = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(100);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 100; i++) {
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

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertThat(completed).isTrue();
        assertThat(completedTasks.get()).isEqualTo(100);
    }

    @Test
    void testExecutorShutdown() throws InterruptedException {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        
        executor.submit(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.shutdown();
        boolean terminated = executor.awaitTermination(2, TimeUnit.SECONDS);
        
        assertThat(terminated).isTrue();
        assertThat(executor.isShutdown()).isTrue();
        assertThat(executor.isTerminated()).isTrue();
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testVirtualThreadExecutorScalability() throws InterruptedException {
        // Virtual thread executor should handle many tasks efficiently
        AtomicInteger taskCount = new AtomicInteger(0);
        int totalTasks = 10000;
        CountDownLatch latch = new CountDownLatch(totalTasks);

        long startTime = System.currentTimeMillis();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < totalTasks; i++) {
                executor.submit(() -> {
                    taskCount.incrementAndGet();
                    latch.countDown();
                });
            }
        }

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        long duration = System.currentTimeMillis() - startTime;

        assertThat(completed).isTrue();
        assertThat(taskCount.get()).isEqualTo(totalTasks);
        assertThat(duration).isLessThan(5000); // Should complete quickly
    }

    @Test
    void testCompareWithPlatformThreadPool() throws InterruptedException {
        int taskCount = 100;
        
        // Test platform thread pool
        long platformStart = System.currentTimeMillis();
        CountDownLatch platformLatch = new CountDownLatch(taskCount);
        
        try (ExecutorService platformExecutor = Executors.newFixedThreadPool(10)) {
            for (int i = 0; i < taskCount; i++) {
                platformExecutor.submit(() -> {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    platformLatch.countDown();
                });
            }
        }
        platformLatch.await();
        long platformDuration = System.currentTimeMillis() - platformStart;

        // Test virtual thread executor
        long virtualStart = System.currentTimeMillis();
        CountDownLatch virtualLatch = new CountDownLatch(taskCount);
        
        try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                virtualExecutor.submit(() -> {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    virtualLatch.countDown();
                });
            }
        }
        virtualLatch.await();
        long virtualDuration = System.currentTimeMillis() - virtualStart;

        // Virtual threads should be faster or comparable for I/O-bound tasks
        System.out.println("Platform thread pool duration: " + platformDuration + "ms");
        System.out.println("Virtual thread executor duration: " + virtualDuration + "ms");
        
        assertThat(virtualDuration).isLessThanOrEqualTo(platformDuration * 2); // Allow some variance
    }
}
