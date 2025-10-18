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

/**
 * Example 1: Basic Virtual Thread Creation
 * Demonstrates the simplest way to create and use virtual threads in Java 21.
 */
public class BasicVirtualThreadExample {

    /**
     * Creates a platform thread (traditional approach).
     */
    public static void createPlatformThread() {
        Thread platformThread = new Thread(() -> {
            System.out.println("Running on a platform thread: " + Thread.currentThread());
        });
        platformThread.start();
        try {
            platformThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Creates a virtual thread using Thread.startVirtualThread().
     */
    public static void createVirtualThread() throws InterruptedException {
        Thread virtualThread = Thread.startVirtualThread(() -> {
            System.out.println("Running on a virtual thread: " + Thread.currentThread());
        });
        virtualThread.join();
    }

    /**
     * Creates a virtual thread using Thread.ofVirtual() API.
     */
    public static void createNamedVirtualThread() throws InterruptedException {
        Thread vt2 = Thread.ofVirtual()
                .name("my-virtual-thread")
                .start(() -> {
                    System.out.println("Named virtual thread: " + Thread.currentThread());
                });
        vt2.join();
    }

    /**
     * Demonstrates memory comparison between platform and virtual threads.
     */
    public static void memoryComparison() {
        System.out.println("Platform thread: ~1MB stack");
        System.out.println("Virtual thread: ~few KB stack");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Basic Virtual Thread Examples ===\n");

        System.out.println("1. Platform Thread:");
        createPlatformThread();

        System.out.println("\n2. Virtual Thread:");
        createVirtualThread();

        System.out.println("\n3. Named Virtual Thread:");
        createNamedVirtualThread();

        System.out.println("\n4. Memory Comparison:");
        memoryComparison();
    }
}
