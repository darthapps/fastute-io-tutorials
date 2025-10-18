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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for BasicVirtualThreadExample.
 */
class BasicVirtualThreadExampleTest {

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCreatePlatformThread() throws InterruptedException {
        AtomicBoolean executed = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);

        Thread platformThread = new Thread(() -> {
            executed.set(true);
            latch.countDown();
        });
        platformThread.start();
        
        latch.await(2, TimeUnit.SECONDS);
        assertThat(executed.get()).isTrue();
        assertThat(platformThread.isVirtual()).isFalse();
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCreateVirtualThread() throws InterruptedException {
        AtomicBoolean executed = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);

        Thread virtualThread = Thread.startVirtualThread(() -> {
            executed.set(true);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
        assertThat(executed.get()).isTrue();
        assertThat(virtualThread.isVirtual()).isTrue();
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCreateNamedVirtualThread() throws InterruptedException {
        AtomicBoolean executed = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);

        Thread vt = Thread.ofVirtual()
            .name("test-virtual-thread")
            .start(() -> {
                executed.set(true);
                latch.countDown();
            });

        latch.await(2, TimeUnit.SECONDS);
        assertThat(executed.get()).isTrue();
        assertThat(vt.isVirtual()).isTrue();
        assertThat(vt.getName()).startsWith("test-virtual-thread");
    }

    @Test
    void testVirtualThreadIsLightweight() throws InterruptedException {
        // Create 1000 virtual threads - should complete quickly
        CountDownLatch latch = new CountDownLatch(1000);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            Thread.startVirtualThread(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                latch.countDown();
            });
        }

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        long duration = System.currentTimeMillis() - startTime;

        assertThat(completed).isTrue();
        assertThat(duration).isLessThan(3000); // Should complete in less than 3 seconds
    }

    @Test
    void testVirtualThreadCharacteristics() throws InterruptedException {
        Thread vt = Thread.startVirtualThread(() -> {
            // Virtual thread task
        });
        vt.join();

        assertThat(vt.isVirtual()).isTrue();
        assertThat(vt.isDaemon()).isTrue(); // Virtual threads are daemon threads
    }
}
