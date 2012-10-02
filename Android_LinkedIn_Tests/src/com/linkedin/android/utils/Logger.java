package com.linkedin.android.utils;

import java.util.ArrayList;
import java.util.Formatter;

import android.app.Activity;
import android.content.res.Resources.NotFoundException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.viewUtils.ANDROID_VIEW_NAMES;

/**
 * Class with helpers for logging information.
 * 
 * @author alexander.makarov
 * @created Aug 6, 2012 3:54:04 PM
 */
public final class Logger {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Logs message as 'info' with correct tag.
     * 
     * @param message
     */
    public static void i(String message) {
        Log.i(DataProvider.getInstance().getCurrentTestTag(), message);
    }

    /**
     * Logs message as 'info' with correct tag if condition is FALSE.
     * 
     * @param message
     * @param condition
     */
    public static void i(String message, boolean condition) {
        if (!condition) {
            Log.i(DataProvider.getInstance().getCurrentTestTag(), message);
        }
    }

    /**
     * Logs message as 'debug' with correct tag.
     * 
     * @param message
     */
    public static void d(String message) {
        Log.d(DataProvider.getInstance().getCurrentTestTag(), message);
    }

    /**
     * Logs message as 'error' with correct tag.
     * 
     * @param message
     */
    public static void e(String message, Throwable tr) {
        Log.e(DataProvider.getInstance().getCurrentTestTag(), message, tr);
    }

    /**
     * Type in log elements with level and parameters.
     * 
     * @param onlyVisible
     *            if <b>true</b> then will log only visible elements
     * @param onlyShown
     *            if <b>true</b> then will log only shown elements
     * @param filter
     *            class of widgets to log. If null then log all.
     * @param inDP
     *            if <b>true</b> then write location and size in inches
     */
    public static void logElements(boolean onlyVisible, boolean onlyShown, String filter) {
        Solo solo = DataProvider.getInstance().getSolo();
        Formatter formatter;
        String type = null;
        int id = 0;
        String idName = null;
        Rect2DP rectI = null;
        String isShown = null;
        String parent = null;
        String custom = "";
        ArrayList<String> previousNames = new ArrayList<String>();
        int i = -1;
        ANDROID_VIEW_NAMES convertedType;

        formatter = new Formatter();
        if (filter != null)
            d("Settings for log: onlyVisible=" + onlyVisible + ", onlyShown=" + onlyShown
                    + ", filter=" + filter);
        d(formatter.format("%5s %15s%4s %4s %4s %4s %5s %30s %15s", "Level", "Type", "X", "Y",
                "Width", "Height", "Shown", "ID:Package:Type/Entry", ">>>Custom").toString());
        d("=========================================================================================================================");
        formatter.close();

        for (View element : solo.getViews()) {
            idName = "<noname>";
            try {
                type = element.getClass().getSimpleName();
                id = element.getId();
                isShown = ((Boolean) element.isShown()).toString();
                parent = element.getParent().getClass().getSimpleName();
                rectI = new Rect2DP(element);
                // stay it at the end, cause NotFoundException
                if (id > 0) {
                    idName = element.getResources().getResourceName(id);
                }
            } catch (NullPointerException e) {
                d("NullPointerException");
                continue;
            } catch (NotFoundException e) {
                // It's ok
            }

            custom = "";

            if (i == -1) {
                // First element.
                previousNames.add(++i, type);
            } else if (parent.equals(previousNames.get(i))) {
                // Children.
                previousNames.add(++i, type);
            } else {
                // Find parent.
                int j = i - 1;
                for (; j >= 0; j--) {
                    if (parent.equals(previousNames.get(j))) {
                        // It is parent.
                        previousNames.add(++j, type);
                        i = j;
                        break;
                    }
                }
                if (j < 0)
                    i = -1;
            }
            convertedType = ANDROID_VIEW_NAMES.getType(type);
            switch (convertedType) {
            case BUTTON:
                custom = ">>> Name='" + ((Button) element).getText() + "', isEnabled="
                        + ((Button) element).isEnabled() + "', isClickable="
                        + ((Button) element).isClickable();
                break;
            case TEXT_VIEW:
            case EDIT_TEXT:
                custom = ">>> Text='" + ((TextView) element).getText() + "'";
                break;
            case LIST_VIEW:
                custom = ">>> Count=" + ((ListView) element).getCount();
                break;
            case PROGRESS_BAR:
                custom = ">>> Progress=" + ((ProgressBar) element).getProgress();
                break;
            case IMAGE_VIEW:
                custom = ">>> isEnabled=" + ((ImageView) element).isEnabled() + "', isClickable="
                        + ((ImageView) element).isClickable();
                // break;
                // case IMAGE_BUTTON:
                // custom = "Progress=" + ((ImageButton) element).get;
                // break;
            }

            boolean isLog = true;
            if (filter != null) {
                isLog = type.equals(filter);
            }
            if (onlyVisible) {
                isLog = (element.getVisibility() == 0);
            }
            if (onlyShown && isLog) {
                isLog = element.isShown();
            }
            if (isLog) {
                formatter = new Formatter();
                d(formatter.format("%4d) %15s %4.0f %4.0f %4.0f %4.0f %6s %8d:%-50s %-1s", i, type,
                        rectI.x, rectI.y, rectI.width, rectI.height, isShown, id, idName, custom)
                        .toString());
                formatter.close();
            }
        }
    }

    /**
     * Type in log all elements with level and parameters.
     */
    public static void logElements() {
        logElements(false, false, null);
    }

    /**
     * Type in log all elements with type <b>filter</b>.
     * 
     * @param filter
     *            class of widgets to log. If null then log all.
     */
    public static void logElements(String filter) {
        logElements(false, false, filter);
    }

    /**
     * Type in log all elements with level and parameters.
     * 
     * @param onlyVisible
     *            if <b>true</b> then will log only visible elements
     * @param onlyShown
     *            if <b>true</b> then will log only shown elements
     */
    public static void logElements(boolean onlyVisible, boolean onlyShown) {
        logElements(onlyVisible, onlyShown, null);
    }

    /**
     * Logs buttons info.
     */
    public static void logButtons() {
        ArrayList<Button> buttons = DataProvider.getInstance().getSolo().getCurrentButtons();
        int[] location = new int[2];
        int i = 0;

        for (Button button : buttons) {
            button.getLocationInWindow(location);
            d("  Button(" + location[0] + ", " + location[1] + "): name=" + button.getText()
                    + ", w=" + button.getWidth() + ", h=" + button.getHeight() + ", textSize="
                    + button.getTextSize() + ", visibility="
                    + (button.getVisibility() == 0 ? "VISIBLE" : "INVISIBLE or GONE"));
            HardwareActions.takeViewScreenshot(button, i + ") " + button.getText() + "_"
                    + location[0] + "_" + location[1]
                    + (button.isShown() ? " shown" : " not shown"));
            i++;
        }
    }

    /**
     * Logs images info.
     */
    public static void logImages() {
        ArrayList<ImageView> images = DataProvider.getInstance().getSolo().getCurrentImageViews();
        int[] location = new int[2];

        for (ImageView view : images) {
            view.getLocationInWindow(location);
            d("  ImageView(" + location[0] + ", " + location[1] + "): from "
                    + view.getParent().getClass().getSimpleName() + ", w=" + view.getWidth()
                    + ", h=" + view.getHeight() + ", visibility="
                    + (view.getVisibility() == 0 ? "VISIBLE" : "INVISIBLE or GONE") + ", "
                    + (view.isShown() ? " shown" : " not shown"));
            /*
             * HardwareActions.takeViewScreenshot(view, i + ") from " +
             * view.getParent().getClass().getSimpleName() + "_" + location[0] +
             * "_" + location[1] + (view.isShown() ? " shown" : " not shown"));
             */
        }
    }

    /**
     * Log all opened activities.
     */
    public static void logAllOpenedActivities() {
        int i = 0;
        for (Activity activity : DataProvider.getInstance().getSolo().getAllOpenedActivities()) {
            d("  " + (i++) + " activity=" + activity.getClass().getName().substring(21));
        }
    }
}
