package com.linkedin.android.tests.data;

import java.util.HashMap;

import junit.framework.Assert;

/**
 * Class for get Class name for action name.
 * 
 * @author alexander.makarov
 * @created Dec 29, 2012 3:17:05 PM
 */
public class AccordingActionNameAndScreenClassName {
    // CONSTANTS ------------------------------------------------------------
    // HashMap with accordance action name - class name.
    private static final HashMap<String, String> mapActionToClass;
    static {
        mapActionToClass = new HashMap<String, String>();
        mapActionToClass.put("login", "com.linkedin.android.screens.common.ScreenLogin");
        mapActionToClass.put("expose", "com.linkedin.android.screens.common.ScreenExpose");
        mapActionToClass.put("jobs_home", "com.linkedin.android.screens.more.ScreenJobs");
    };

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Returns class name from action name.
     * 
     * @param actionName
     *            string with action name to parse.
     * @return class name that contain this action.
     */
    public static String getClassNameFromActionName(String actionName) {
        String className = null;

        // Check case if it "got_to" action.
        if (actionName.startsWith("go_to_")) {
            // If action name starts from "go_to_" then clean screen name after
            // 6th symbol.
            className = mapActionToClass.get(actionName.substring(6));
        } else {
            // Check case if action name equal to screen name.
            className = mapActionToClass.get(actionName);
            if (className != null)
                return className;

            // Check other cases.
            String stringForCheck = actionName;
            String[] words = actionName.split("_");
            for (int i = words.length - 1; i >= 0; i--) {
                // Check string = actionName - last word.
                stringForCheck = stringForCheck.substring(0,
                        stringForCheck.length() - words[i].length() - 1);
                className = mapActionToClass.get(stringForCheck);
                if (className != null)
                    break;
            }

        }
        Assert.assertNotNull("Cannot get class name from action '" + actionName + "'.", className);
        return className;
    }
}
