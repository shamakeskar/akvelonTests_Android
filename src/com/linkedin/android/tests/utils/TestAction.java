package com.linkedin.android.tests.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for specify test actions.
 * 
 * @author alexander.makarov
 * @created Jan 5, 2013 1:10:43 PM
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAction {
    /**
     * returns unique String value associated with this action.
     * 
     * @return unique String value associated with this action.
     */
    String value();

    /**
     * Returns name of screen from which action must be called.
     * 
     * @return
     */
    String screen() default "";
}
