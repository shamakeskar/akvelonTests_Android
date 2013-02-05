package com.linkedin.android.utils.viewUtils;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.graphics.Point;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.ScreenResolution;
import com.linkedin.android.utils.WaitActions;

/**
 * Class for interactions with views.
 * 
 * @author evgeny.agapov
 * @created Aug 8, 2012 1:34:01 PM
 */
public final class ViewUtils {
    // CONSTANTS ------------------------------------------------------------
    private static final ViewIdName NAVBAR_LAYOUT = new ViewIdName("nav_bar");
    private static final ViewIdName TOOLBAR_LAYOUT = new ViewIdName("bottom_bar");
    // private static final ViewIdName TOAST_IN_BOTTOM_OF_SCREEN_ID_NAME = new
    // ViewIdName("status_message");

    public static final int SCROLL_UP = 0;
    public static final int SCROLL_DOWN = 1;
    public static final String EXPOSE_SHORT_CLASSNAME = "ExposeActivity";

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

    /*
     * Waits until toast in bottom of screen is disappear.
     */
    public static void waitForToastDisappear() {
        WaitActions.waitForTrueInFunction("Toast bottom of the screen is not disappears",
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return TextViewUtils
                                .getTextViewByPartialText("See who you know on LinkedIn") == null;
                        // View toast =
                        // Id.getViewByViewIdName(TOAST_IN_BOTTOM_OF_SCREEN_ID_NAME);
                        // return ((toast == null) || (!toast.isShown()));
                    }
                });
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
        tapOnView(view, viewName, true);
    }

    /**
     * Taps on specified {@code view}. Logs "Tapping on <i>viewName</i>"
     * 
     * @param view
     *            view to tap
     * @param viewName
     *            the view name
     * @param waitForToastDisappear
     *            true if should wait for toast disappear
     */
    public static void tapOnView(View view, String viewName, boolean waitForToastDisappear) {
        if (waitForToastDisappear)
            waitForToastDisappear();
        Assert.assertNotNull(viewName + " is not present", view);

        Logger.i("Tapping on " + viewName);
        DataProvider.getInstance().getSolo().clickOnView(view, true);
    }

    /**
     * Taps on link from LinkTextView. Logs "Tapping on link '<i>link</i>'"
     * 
     * @param link
     *            piece of string in <b>view</b> with link for tap
     */
    public static void tapOnLink(String link) {
        Logger.i("Tapping on link '" + link + "'");
        Solo solo = DataProvider.getInstance().getSolo();
        View view = null;
        try {
            view = solo.getText(link);
        } catch (Throwable e) {
            Assert.fail("Cannot find link '" + link + "' to tap.");
        }
        Assert.assertNotNull("Cannot find link '" + link + "' to tap.");
        solo.clickOnView(view);
        try {
            solo.getText(link);
            WaitActions.waitForScreenUpdate(); // wait before tapping to avoid
                                               // tapping on toast
            ViewUtils.tapOnView(view, "'" + link + "' link", true);
        } catch (Throwable e) {
            Assert.fail("Cannot find link '" + link + "' to tap.");
        }
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
        final Solo solo = DataProvider.getInstance().getSolo();
        // Wait until getSolo().getViews() starts return views, not exception.
        WaitActions.waitForTrueInFunction("Cannot wait until application launched", new Callable<Boolean>() {
            public Boolean call() throws Exception {
                ArrayList<View> views;
                try {
                    views = solo.getViews();
                } catch (Exception ignored) {
                    return false;
                }
                return views != null;
            }
        });
        for (View view : solo.getViews()) {
            if (view.getClass().getSimpleName().equals(className))
                return view;
        }
        return null;
    }

    /**
     * Checks array of {@code View} contains visible views.
     * 
     * @param viewList
     *            is array of {@code View} for check.
     * @return <b>true</b> if array contains visible views, else <b>false</b>.
     */
    public static boolean isListContainVisibleViews(Object[] viewList) {
        for (Object view : viewList) {
            if (((View) view).isShown()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Search for first shown view in array of views and return it.
     * 
     * @param viewList
     *            is array of view in which we search for show view.
     * @return first shown <b>view</b> if not found than return <b>null</b>;
     */
    public static View getFirstShownViewInArray(ArrayList<View> viewList) {
        for (View view : viewList)
            if (view.isShown())
                return view;
        return null;
    }

    /**
     * Checks that view placed in visible part of the screen.
     * 
     * @param view
     *            - {@code View} to check.
     * @return <b>true</b> if view visible, else <b>false</b>.
     */
    public static boolean isViewVisible(View view) {
        View navbar = Id.getViewByViewIdName(NAVBAR_LAYOUT);
        float navbarHeight = ((navbar != null) && (navbar.isShown())) ? navbar.getHeight()
                / ScreenResolution.getScreenDensity() : 0;
        View toolbar = Id.getViewByViewIdName(TOOLBAR_LAYOUT);
        float toolbarHeight = ((toolbar != null) && (toolbar.isShown())) ? toolbar.getHeight()
                / ScreenResolution.getScreenDensity() : 0;
        Rect2DP screenRect = new Rect2DP(0, navbarHeight, ScreenResolution.getScreenWidthDP(),
                ScreenResolution.getScreenHeightDP() - toolbarHeight);

        return screenRect.isViewFitInThisRect(view);
    }

    /**
     * Scrolls up to view specified by its id.
     * 
     * @param viewId
     *            - {@code ViewIdName} object specifies view to scroll.
     * @param maxCountOfScrolls
     *            - max count of scrolls.
     * @param additionalCheck
     *            - function to check which must return true or false for
     *            current view, if scroll should be stopped.
     * @return necessary view if scroll successful, else <b>null</b>.
     */
    public static View scrollUpToViewById(ViewIdName viewId, int maxCountOfScrolls,
            ViewChecker additionalCheck) {
        return scrollToViewById(viewId, SCROLL_UP, maxCountOfScrolls, additionalCheck);
    }

    /**
     * Scrolls down to view specified by its id.
     * 
     * @param viewId
     *            - {@code ViewIdName} object specifies view to scroll.
     * @param maxCountOfScrolls
     *            - max count of scrolls.
     * @param additionalCheck
     *            - function to check which must return true or false for
     *            current view, if scroll should be stopped.
     * @return necessary view if scroll successful, else <b>null</b>.
     */
    public static View scrollDownToViewById(ViewIdName viewId, int maxCountOfScrolls,
            ViewChecker additionalCheck) {
        return scrollToViewById(viewId, SCROLL_DOWN, maxCountOfScrolls, additionalCheck);
    }

    /**
     * Scrolls to view specified by its id.
     * 
     * @param viewId
     *            - {@code ViewIdName} object specifies view to scroll.
     * @param scrollTo
     *            - specifies direction of scroll (ViewUtils.SCROLL_UP,
     *            ViewUtils.SCROLL_DOWN).
     * @param maxCountOfScrolls
     *            - max count of scrolls.
     * @return necessary view if scroll successful, else <b>null</b>.
     */
    public static View scrollToViewById(ViewIdName viewId, int scrollTo, int maxCountOfScrolls) {
        return scrollToViewById(viewId, scrollTo, maxCountOfScrolls, new ViewChecker() {
            @Override
            public boolean check(View view) {
                return true;
            }
        });
    }

    /**
     * Scrolls to view specified by its id.
     * 
     * @param viewId
     *            - {@code ViewIdName} object specifies view to scroll.
     * @param scrollTo
     *            - specifies direction of scroll (ViewUtils.SCROLL_UP,
     *            ViewUtils.SCROLL_DOWN).
     * @param maxCountOfScrolls
     *            - max count of scrolls.
     * @param additionalCheck
     *            - function to check which must return true or false for
     *            current view, if scroll should be stopped.
     * @return necessary view if scroll successful, else <b>null</b>.
     */
    public static View scrollToViewById(ViewIdName viewId, int scrollTo, int maxCountOfScrolls,
            ViewChecker additionalCheck) {

        for (int i = 0; i < maxCountOfScrolls; i++) {
            ArrayList<View> viewList = Id.getListOfViewByViewIdName(viewId);

            for (View targetView : viewList) {
                if ((targetView != null) && (targetView.isShown()) && (isViewVisible(targetView))
                        && (additionalCheck.check(targetView))) {
                    return targetView;
                }
            }

            switch (scrollTo) {
            case SCROLL_DOWN:
                DataProvider
                        .getInstance()
                        .getSolo()
                        .drag(0, 0, (int) (ScreenResolution.getScreenHeight() * 0.75),
                                (int) (ScreenResolution.getScreenHeight() * 0.25), 15);
                break;
            case SCROLL_UP:
                DataProvider
                        .getInstance()
                        .getSolo()
                        .drag(0, 0, (int) (ScreenResolution.getScreenHeight() * 0.25),
                                (int) (ScreenResolution.getScreenHeight() * 0.75), 15);
                break;
            }

            // Wait for animation finished.
            WaitActions.waitForScreenUpdate();
        }

        return null;
    }

    public static void goBackToExposeScreen() {
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_DEFAULT,
                "'Expose' screen is not present", new Callable<Boolean>() {
                    public Boolean call() {
                        Logger.i("Try open Expose screen");
                        if (BaseINScreen.getINButton() != null) {
                            BaseINScreen.tapOnINButton();
                        } else {
                            HardwareActions.pressBack();
                        }
                        WaitActions.waitForScreenUpdate();// To load next
                                                          // activity.
                        return ScreenExpose.isOnExposeScreen();
                    }
                });
    }
}
