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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ExceptionHandlingExample.
 */
class ExceptionHandlingExampleTest {

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testExceptionHandlingInVirtualThread() throws InterruptedException {
        AtomicBoolean exceptionCaught = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);

        Thread vt = Thread.startVirtualThread(() -> {
            try {
                throw new RuntimeException("Test exception");
            } catch (Exception e) {
                exceptionCaught.set(true);
            } finally {
                latch.countDown();
            }
        });

        vt.join();
        latch.await(2, TimeUnit.SECONDS);
        
        assertThat(exceptionCaught.get()).isTrue();
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testUncaughtExceptionHandler() throws InterruptedException {
        AtomicReference<Throwable> caughtException = new AtomicReference<>();
        AtomicReference<Thread> caughtThread = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Thread vt = Thread.ofVirtual()
            .name("exception-test-thread")
            .uncaughtExceptionHandler((thread, throwable) -> {
                caughtThread.set(thread);
                caughtException.set(throwable);
                latch.countDown();
            })
            .start(() -> {
                throw new RuntimeException("Uncaught exception test");
            });

        vt.join();
        latch.await(2, TimeUnit.SECONDS);

        assertThat(caughtException.get()).isNotNull();
        assertThat(caughtException.get()).isInstanceOf(RuntimeException.class);
        assertThat(caughtException.get().getMessage()).isEqualTo("Uncaught exception test");
        assertThat(caughtThread.get()).isNotNull();
        assertThat(caughtThread.get().getName()).contains("exception-test-thread");
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testResourceCleanup() throws InterruptedException {
        AtomicBoolean resourceAcquired = new AtomicBoolean(false);
        AtomicBoolean resourceCleaned = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);

        Thread vt = Thread.startVirtualThread(() -> {
            try {
                resourceAcquired.set(true);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                resourceCleaned.set(true);
                latch.countDown();
            }
        });

        vt.join();
        latch.await(2, TimeUnit.SECONDS);

        assertThat(resourceAcquired.get()).isTrue();
        assertThat(resourceCleaned.get()).isTrue();
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testInterruptedException() throws InterruptedException {
        AtomicBoolean interrupted = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);

        Thread vt = Thread.startVirtualThread(() -> {
            try {
                Thread.sleep(5000); // Long sleep
            } catch (InterruptedException e) {
                interrupted.set(true);
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        });

        Thread.sleep(100); // Let thread start
        vt.interrupt(); // Interrupt the thread
        vt.join();
        latch.await(2, TimeUnit.SECONDS);

        assertThat(interrupted.get()).isTrue();
    }

    @Test
    void testMultipleExceptionsInDifferentVirtualThreads() throws InterruptedException {
        AtomicInteger exceptionCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            Thread.startVirtualThread(() -> {
                try {
                    throw new RuntimeException("Exception in thread");
                } catch (RuntimeException e) {
                    exceptionCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        
        assertThat(completed).isTrue();
        assertThat(exceptionCount.get()).isEqualTo(5);
    }

    @Test
    void testExceptionDoesNotAffectOtherVirtualThreads() throws InterruptedException {
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            Thread.startVirtualThread(() -> {
                try {
                    if (taskId % 2 == 0) {
                        throw new RuntimeException("Even task fails");
                    } else {
                        successCount.incrementAndGet();
                    }
                } catch (RuntimeException e) {
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        
        assertThat(completed).isTrue();
        assertThat(successCount.get()).isEqualTo(5);
        assertThat(failureCount.get()).isEqualTo(5);
    }
}
