package com.linkedin.android.tests;

import java.lang.reflect.Method;

import android.os.Build;

import com.linkedin.android.tests.utils.Tag;
import com.linkedin.android.utils.Logger;

/**
 * Class for run tests from Tags and from custom methods.
 * 
 * @author alexander.makarov
 * @created Jan 18, 2013 5:20:21 PM
 */
public class ActionSuiteTest extends BaseTestCase {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------
    // Tag object with nformation about test.
    private final Tag tag;

    private Class<? extends BaseTestCase> customClass;
    private Method customMethod;

    // CONSTRUCTORS ---------------------------------------------------------
    /**
     * Constructor for test from {@code Tag}.
     * 
     * @param tag
     *            {@code Tag} object with test data.
     */
    public ActionSuiteTest(Tag tag) {
        super(tag.getName());
        this.tag = tag;
        /*
         * Android till to 2.3 version doesn't support test name without the
         * same method name. Android 2.3 do support it. In both cases runTest()
         * method will be called.
         */
        if (Build.VERSION.SDK_INT > 8)
            setName(tag.getName());
        else
            setName("testActions");
    }

    /**
     * Constructor for test from Class and Method.
     * 
     * @param customClass
     *            is class object to invoke method
     * @param sustomMethod
     *            method to invoke
     */
    public ActionSuiteTest(Class<? extends BaseTestCase> customClass, Method customMethod) {
        super(customMethod.getName());
        this.tag = null;
        this.customClass = customClass;
        this.customMethod = customMethod;
        if (Build.VERSION.SDK_INT > 8)
            setName(customMethod.getName());
        else
            setName("testActions");
    }

    // METHODS --------------------------------------------------------------
    /**
     * Dummy for JUnit on Android till to 2.3 version (if not exist then JUnit
     * crash).
     */
    public void testActions() {
    }

    /**
     * Method that called actually.
     */
    @Override
    protected void runTest() throws Throwable {
        if (tag != null)
            runActions();
        else
            testMethod();
    }

    /**
     * Run all actions in tag.
     * 
     * @throws Throwable
     */
    public void runActions() throws Throwable {
        startTest(tag.getId(), tag.getName());

        for (int i = 0; i < tag.countOfActions(); i++) {
            tag.processNextAction();
        }

        passTest();
    }

    /**
     * Method for run real method-test.
     * 
     * @throws Throwable
     */
    public void testMethod() throws Throwable {
        try {
            customMethod.invoke(customClass.newInstance());
        } catch (Exception exc) {
            Throwable e = exc.getCause();
            if (e == null) {
                e = exc;
            }
            Logger.e("", e);
            throw e;
        }
    }
}
