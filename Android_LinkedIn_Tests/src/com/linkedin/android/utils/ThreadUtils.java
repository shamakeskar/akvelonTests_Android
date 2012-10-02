package com.linkedin.android.utils;

import junit.framework.Assert;

/**
 * Class with util methods for {@code Thread}
 * 
 * @author vasily.gancharov
 * @created Aug 28, 2012 17:34:03 PM
 */
public final class ThreadUtils {

    /**
     * Go to sleep current thread for {@code timeMs}
     * 
     * @param timeMs
     *            current thread sleep time (milliseconds)
     */
    public static void Sleep(int timeMs) {
        final int minPossibleTimeMs = 1;

        if (timeMs < minPossibleTimeMs) {
            return;
        }

        try {
            Thread.sleep(timeMs);
        } catch (InterruptedException e) {
            Assert.assertTrue(
                    "Test case stopped unexpectedly during call of Sleep method (ThreadUtils class)",
                    false);
        }
    }

    /**
     * Calls 'wait' method of specified {@code object}
     * 
     * @param objectToWait
     *            {@code Object} to call 'wait' method
     * @param objectToSynchronize
     *            {@code Object} for 'synchronized' section before 'wait' method
     *            call
     */
    public static void wait(Object objectToWait, Object objectToSynchronize) {
        synchronized (objectToSynchronize) {
            try {
                objectToWait.wait();
            } catch (InterruptedException e) {
                Assert.fail("Test case stopped unexpectedly during call of 'wait' method (ThreadUtils class)");
            }
        }
    }
}
