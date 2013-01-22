package com.linkedin.android.tests.utils;

/**
 * Interface for describe action
 * 
 * @author alexander.makarov
 * @created Jan 17, 2013 8:40:27 PM
 */
public interface Action {
    /**
     * Code of action for "processAction" in Registry class.
     * 
     * @param payload string parameters for code.
     * @throws Throwable
     */
    void testAction(Object ... payload) throws Throwable;
}
