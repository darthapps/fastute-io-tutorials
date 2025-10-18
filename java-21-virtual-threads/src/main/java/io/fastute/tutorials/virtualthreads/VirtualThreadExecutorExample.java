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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Example 2: Virtual Thread Executors
 * Demonstrates using ExecutorService with virtual threads.
 */
public class VirtualThreadExecutorExample {

    /**
     * Creates a virtual thread executor and submits tasks.
     */
    public static void virtualThreadExecutor() throws InterruptedException {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        // Submit multiple tasks
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " running on: " + Thread.currentThread());
                try {
                    Thread.sleep(100); // Simulate I/O operation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * Compares platform thread pool vs virtual thread executor.
     */
    public static void compareExecutors() throws InterruptedException {
        System.out.println("Platform Thread Pool (Fixed 5 threads):");
        try (ExecutorService platformExecutor = Executors.newFixedThreadPool(5)) {
            for (int i = 0; i < 10; i++) {
                final int taskId = i;
                platformExecutor.submit(() -> {
                    System.out.println("  Platform Task " + taskId + ": " + Thread.currentThread().getName());
                });
            }
        }

        Thread.sleep(500); // Wait for platform tasks to complete

        System.out.println("\nVirtual Thread Executor (Unlimited):");
        try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 10; i++) {
                final int taskId = i;
                virtualExecutor.submit(() -> {
                    System.out.println("  Virtual Task " + taskId + ": " + Thread.currentThread());
                });
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Virtual Thread Executor Examples ===\n");

        System.out.println("1. Basic Virtual Thread Executor:");
        virtualThreadExecutor();

        System.out.println("\n2. Comparing Platform vs Virtual Thread Executors:");
        compareExecutors();
    }
}
