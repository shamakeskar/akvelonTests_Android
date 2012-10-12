package com.linkedin.android.utils.viewUtils;

import junit.framework.Assert;
import android.graphics.Point;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;

/**
 * Class for interactions with views.
 * 
 * @author evgeny.agapov
 * @created Aug 8, 2012 1:34:01 PM
 */
public final class ViewUtils {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Checks whether bounds of view and rectangle are equal.
     * 
     * @param image
     *            {@code View} for comparing.
     * @param rectangle
     *            {@code Rect2DP} for comparing.
     * @return <b>true</b> if arguments are not null and is a view that has the
     *         same top-left corner, width, and height as rectangle.
     */
    public static boolean compareViewBounds(View view, Rect2DP rectangle) {
        if (view != null) {
            Rect2DP viewRect = new Rect2DP(view);

            return viewRect.equals(rectangle);
        }
        return false;
    }

    /**
     * Checks whether bounds of view and rectangle are equal with prescribed
     * accuracy {@code e}
     * 
     * @param image
     *            {@code View} for comparing.
     * @param rectangle
     *            {@code Rect2DP} for comparing.
     * @param e
     *            is accuracy of comparing.
     * @return <b>true</b> if arguments are not null and is a view that has the
     *         same top-left corner, width, and height as rectangle.
     */
    public static boolean compareViewBounds(View view, Rect2DP rectangle, float e) {
        if (view != null) {
            Rect2DP viewRect = new Rect2DP(view);

            return viewRect.equals(rectangle, e);
        }
        return false;
    }

    /**
     * Checks whether bounds of two views are equal.
     * 
     * @param viewA
     *            first {@code View} for comparing.
     * @param viewB
     *            second {@code View} for comparing.
     * @return <b>true</b> if arguments are not null and is a viewA that has the
     *         same top-left corner, width, and height as viewB.
     */
    public static boolean compareViewBounds(View viewA, View viewB) {
        if ((viewA != null) && (viewB != null)) {
            int[] posA = new int[2];
            viewA.getLocationInWindow(posA);

            int[] posB = new int[2];
            viewA.getLocationInWindow(posB);

            return (posA[0] == posB[0]) && (posA[1] == posB[1])
                    && (viewA.getWidth() == viewB.getWidth())
                    && (viewA.getHeight() == viewB.getHeight());
        }
        return false;
    }

    /**
     * Taps on specified {@code view}. Logs "Tapping on <i>viewName</i>"
     * 
     * @param view
     *            view to tap
     * @param viewName
     *            the view name
     */
    public static void tapOnView(View view, String viewName) {
        Assert.assertNotNull(viewName + " not present", view);

        Logger.i("Tapping on " + viewName);
        DataProvider.getInstance().getSolo().clickOnView(view);
    }

    /**
     * Checks if specified views placed horizontally 'in-line'
     * 
     * @param view1
     *            first view to compare
     * @param view2
     *            second view to compare
     * @return <b>true</b> if specified views placed horizontally 'in-line'
     *         <b>false</b> otherwise
     */
    public static boolean isViewsPlacedInLineHorizontally(View view1, View view2) {
        if (null == view1 || null == view2) {
            return false;
        }
        Rect2DP view1Rect = new Rect2DP(view1);
        Rect2DP view2Rect = new Rect2DP(view2);
        return (view1Rect.y - view2Rect.y) < 1f;// less that 1 pixel
    }

    /**
     * Gets {@code view} coordinates on screen
     * 
     * @param view
     *            {@code View} to get coordinates
     * @return {@code view} coordinates on screen ({@code Point}) or <b>null</b>
     *         if view is null
     */
    public static Point getViewCoordinatesOnScreen(View view) {
        final int coordinatedArraySize = 2;
        final int xCoordinateIndex = 0;
        final int yCoordinateIndex = 1;

        if (null == view) {
            return null;
        }

        int[] xy = new int[coordinatedArraySize];
        view.getLocationOnScreen(xy);
        int viewX = xy[xCoordinateIndex];
        int viewY = xy[yCoordinateIndex];

        Point viewLocation = new Point(viewX, viewY);
        return viewLocation;
    }

    /**
     * Returns first view (Android widget) with specified class.
     * 
     * @param className
     *            is name of class
     * @return first view with class <i>className</i> or <b>null</b> if not
     *         found.
     */
    public static View getViewByClassName(String className) {
        Solo solo = DataProvider.getInstance().getSolo();
        for (View view : solo.getViews()) {
            if (view.getClass().getSimpleName().equals(className))
                return view;
        }
        return null;
    }
}
