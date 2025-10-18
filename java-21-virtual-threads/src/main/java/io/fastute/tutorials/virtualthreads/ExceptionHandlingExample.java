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
 * Example 3: Exception Handling in Virtual Threads
 * Demonstrates proper exception handling patterns.
 */
public class ExceptionHandlingExample {

    /**
     * Demonstrates exception handling within a virtual thread.
     */
    public static void handleExceptionInVirtualThread() throws InterruptedException {
        Thread vt = Thread.startVirtualThread(() -> {
            try {
                throw new RuntimeException("Oops");
            } catch (Exception e) {
                System.err.println("Caught: " + e.getMessage());
            }
        });
        vt.join();
    }

    /**
     * Demonstrates using uncaught exception handler.
     */
    public static void uncaughtExceptionHandler() throws InterruptedException {
        Thread vt = Thread.ofVirtual()
            .name("exception-handler-thread")
            .uncaughtExceptionHandler((thread, throwable) -> {
                System.err.println("Uncaught exception in " + thread.getName() + ": " + throwable.getMessage());
            })
            .start(() -> {
                throw new RuntimeException("Uncaught exception example");
            });
        vt.join();
    }

    /**
     * Demonstrates proper resource cleanup with try-finally.
     */
    public static void resourceCleanup() throws InterruptedException {
        Thread vt = Thread.startVirtualThread(() -> {
            System.out.println("Acquiring resource...");
            try {
                // Simulate resource usage
                Thread.sleep(100);
                System.out.println("Using resource");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted");
            } finally {
                System.out.println("Cleaning up resource");
            }
        });
        vt.join();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Exception Handling Examples ===\n");

        System.out.println("1. Exception Handling in Virtual Thread:");
        handleExceptionInVirtualThread();

        System.out.println("\n2. Uncaught Exception Handler:");
        uncaughtExceptionHandler();

        System.out.println("\n3. Resource Cleanup:");
        resourceCleanup();
    }
}
