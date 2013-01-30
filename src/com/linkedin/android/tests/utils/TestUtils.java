package com.linkedin.android.tests.utils;

import java.lang.reflect.Method;

import junit.framework.Assert;
import android.widget.EditText;

import com.linkedin.android.tests.data.AccordingActionNameAndScreenClassName;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.ScreenResolution;

/**
 * Class for test utils.
 * 
 * @author alexander.makarov
 * @created Dec 25, 2012 3:01:06 PM
 */
public class TestUtils {
    // CONSTANTS ------------------------------------------------------------

    public static final float X_COORDINATE_SCROLL = ScreenResolution.getScreenWidth() - 5;
    public static final float Y_COORDIANTE_SCROLL_START = 84 * ScreenResolution.getScreenDensity();
    public static final float Y_COORDINATE_SCROLL_END = ScreenResolution.getScreenHeight();

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Waits WAIT_DELAY_FOR_SCREENSHOTS ms and takes screenshot with name
     * <b>screenshotName</b>.
     * 
     * @param screenshotName
     *            is name for screenshot file.
     */
    public static void delayAndCaptureScreenshot(String screenshotName) {
        DataProvider.getInstance().getSolo().sleep(DataProvider.WAIT_DELAY_FOR_SCREENSHOTS);
        HardwareActions.takeCurrentActivityScreenshot(screenshotName);
    }
    
    /**
     * Wrapper for easy find all places for post/send/comment test.
     * 
     * @param text test for return
     * @return text
     */
    public static String verifyText(String text){
        return text;
    }
    
    /**
     * Sets text in 'getSolo().getEditText(0)'.
     * 
     * @param text text to set
     * @return text that has been sets.
     */
    public static String typeTextInEditText0(String text) {
        Assert.assertNotNull("Wrong argument for typeTextInEditText0", text);
        EditText editText = DataProvider.getInstance().getSolo().getEditText(0);
        Assert.assertNotNull("Edit field is not present.", editText);
        DataProvider.getInstance().getSolo().typeText(editText, verifyText(text));
        return text;
    }
    
    /**
     * Sets text in 'getSolo().getEditText(1)'.
     * 
     * @param text text to set
     * @return text that has been sets.
     */
    public static String typeTextInEditText1(String text) {
        Assert.assertNotNull("Wrong argument for typeTextInEditText1", text);
        EditText editText = DataProvider.getInstance().getSolo().getEditText(1);
        Assert.assertNotNull("Edit field is not present.", editText);
        DataProvider.getInstance().getSolo().typeText(editText, verifyText(text));
        return text;
    }

    /**
     * Sets text in 'getSolo().getEditText(2)'.
     * 
     * @param text text to set
     * @return text that has been sets.
     */
    public static String typeTextInEditText2(String text) {
        Assert.assertNotNull("Wrong argument for typeTextInEditText2", text);
        EditText editText = DataProvider.getInstance().getSolo().getEditText(2);
        Assert.assertNotNull("Edit field is not present.", editText);
        DataProvider.getInstance().getSolo().typeText(editText, verifyText(text));
        return text;
    }

    public static void executeTag(Tag tag) {
        // Start cycle for execute actions in tag.
        for (int i = 0; i < tag.countOfActions(); i++) {
            Method method = null;
            boolean isTestAction = false;
            boolean isNeedPayload = false;
            String actionName = tag.getNextAction();
            String className = AccordingActionNameAndScreenClassName
                    .getClassNameFromActionName(actionName);

            // Try get class with action.
            Class<?> classWithAction = null;
            try {
                classWithAction = Class.forName(className);
            } catch (ClassNotFoundException e) {
                Assert.fail("Class '" + className + "' not found: " + e.toString());
            }

            // Try get method with action and check that method have TestAction
            // annotation.
            try {
                isNeedPayload = (tag.getPayloadsForCurrentAction() != null);
                if (isNeedPayload) {
                    method = classWithAction.getDeclaredMethod(actionName, String.class);
                } else {
                    method = classWithAction.getDeclaredMethod(actionName);
                }
            } catch (Exception e) {
                Assert.fail("Cannot get action '" + actionName + "' for class '"
                        + classWithAction.getName() + ": " + e.toString());
            }

            // Check that method have annotation TestAction.
            if (method.getAnnotation(TestAction.class) != null) {
                isTestAction = true;
            }

            // Log message that action start:
            String startLog = "Start ";
            if (isTestAction) {
                startLog += "test action";
            } else {
                startLog += "helper";
            }
            startLog += " '";
            if (isNeedPayload) {
                Logger.i(startLog + method.getName() + "' with payload '"
                        + tag.getPayloadsForCurrentAction() + "'");
            } else {
                Logger.i(startLog + method.getName() + "'");
            }
            // Try invoke method with action.
            try {
                if (isNeedPayload) {
                    method.invoke(null, tag.getPayloadsForCurrentAction());
                } else {
                    method.invoke(null);
                }
                // Not add InvocationTargetException to catch - it will be mask
                // tests fails!
            } catch (IllegalAccessException e) {
                Assert.fail("Cannot invoke '" + method.getName() + "' for class '"
                        + classWithAction.getName() + "': " + e.toString());
            } catch (IllegalArgumentException e) {
                Assert.fail("Cannot invoke '" + method.getName() + "' for class '"
                        + classWithAction.getName() + "': " + e.toString());
            } catch (Exception e) {
                Logger.e("Test '" + tag.getName() + "' fails on '" + method.getName() + "'",
                        e.getCause());
                Assert.fail(e.getCause().toString());
            }
        }
    }

    /**
     * Drag screen down to scrollbar is show than drag it up for scrollbar place
     * in top of screen.
     */
    public static void tapScrollBar() {
        DataProvider.getInstance().getSolo().scrollDown();
        DataProvider.getInstance().getSolo().scrollUp();

        DataProvider
                .getInstance()
                .getSolo()
                .drag(X_COORDINATE_SCROLL, X_COORDINATE_SCROLL, Y_COORDIANTE_SCROLL_START,
                        Y_COORDINATE_SCROLL_END, 150);
    }
}
