package com.linkedin.android.utils.viewUtils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import android.graphics.Point;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.StringUtils;

/**
 * Class for interactions with views.
 * 
 * @author evgeny.agapov
 * @created Aug 8, 2012 1:34:01 PM
 */
public final class ViewUtils {
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
     * Searches for the {@code TextView} object with {@code text} in
     * {@code parentView}. If {@code parentView} is <b>null</b> the method will
     * search {@code TextView} object with {@code text} from list of TextViews
     * that currently on screen.
     * 
     * @param text
     *            is string for searching.
     * @param parentView
     *            is layout for search, <b>null</b> for search in all visible
     *            part of screen.
     * @param isSctrictEqual
     *            if <b>true</b> returns {@code TextView} object with text that
     *            equals current text, else returns {@code TextView} object with
     *            text that contains current text.
     * @return {@code TextView} object with current text.
     */
    public static TextView searchTextViewInLayout(String text, View parentView,
            boolean isSctrictEqual) {
        Solo solo = DataProvider.getInstance().getSolo();

        for (TextView view : solo.getCurrentTextViews(parentView)) {
            String textOfView = view.getText().toString();
            if (textOfView.indexOf(text) != -1) {
                if (isSctrictEqual) {
                    if (textOfView.equals(text)) {
                        return view;
                    }
                } else {
                    return view;
                }
            }
        }

        return null;
    }

    /**
     * Searches for the all {@code TextView} objects with {@code text} in
     * {@code parentView}. If {@code parentView} is <b>null</b> the method will
     * search {@code TextView} object with {@code text} from list of TextViews
     * that currently on screen.
     * 
     * @param text
     *            is string for searching.
     * @param parentView
     *            is layout for search, <b>null</b> for search in all visible
     *            part of screen.
     * @param isSctrictEqual
     *            if <b>true</b> returns {@code TextView} object with text that
     *            equals current text, else returns {@code TextView} object with
     *            text that contains current text.
     * @return {@code ArrayList} of {@code TextView} objects with {@code text}.
     */
    public static ArrayList<TextView> getTextViewsInLayout(String text, View parentView,
            boolean isStrictEqual) {
        ArrayList<TextView> foundTextViews = new ArrayList<TextView>();
        Solo solo = DataProvider.getInstance().getSolo();

        for (TextView currentTextView : solo.getCurrentTextViews(parentView)) {

            String currentTextViewValue = currentTextView.getText().toString();
            if (StringUtils.isStringsEquals(currentTextViewValue, text, isStrictEqual)) {
                foundTextViews.add(currentTextView);
            }
        }
        return foundTextViews;
    }

    /**
     * Gets {@code ImageView} from it's {@code parentView} layout by
     * {@code imageViewIndex}. If {@code parentView} is <b>null</b> the method
     * will get {@code ImageView} from list of {@code ImageView}s that currently
     * on screen.
     * 
     * @param parentView
     *            parent view that must contain {@code ImageView} with required
     *            {@code imageViewIndex}
     * @param imageViewIndex
     *            index of {@code ImageView} on {@code parentView}
     * @return {@code ImageView} with specified {@code imageViewIndex} from
     *         {@code parentView} <b>null</b> otherwise
     */
    public static ImageView getImageViewFromLayoutByIndex(View parentView, int imageViewIndex) {
        Solo solo = DataProvider.getInstance().getSolo();

        ArrayList<ImageView> allChildImageViews = solo.getCurrentImageViews(parentView);
        return getViewByIndex(allChildImageViews, imageViewIndex);
    }

    /**
     * Gets {@code View} from {@code listOfViews} by {@code requiredViewIndex}.
     * If {@code listOfViews} is <b>null</b> or {@code requiredViewIndex} is out
     * of {@code listOfViews} range method will return <b>null</b>
     * 
     * @param listOfViews
     *            list of views to select from
     * @param requiredViewIndex
     *            index of required view in {@code listOfViews}
     * @return {@code View} from {@code listOfViews} with specified
     *         {@code requiredViewIndex}, or <b>null</b> if {@code listOfViews}
     *         is <b>null</b> or {@code requiredViewIndex} is out of
     *         {@code listOfViews} range
     */
    public static <T extends View> T getViewByIndex(List<T> listOfViews, int requiredViewIndex) {
        if (null == listOfViews || listOfViews.size() <= requiredViewIndex || requiredViewIndex < 0) {
            return null;
        }
        return listOfViews.get(requiredViewIndex);
    }

    /**
     * Gets count of {@code ImageView}s from {@code parentView}. If
     * {@code parentView} is <b>null</b> the method will count all
     * {@code ImageView}s that currently on screen.
     * 
     * @param parentView
     *            parent layout of {@code ImageView}s
     * @return count of {@code ImageView}s from {@code parentView} or <b>-1</b>
     *         if there is on {@code ImageView}s on {@code parentView}
     */
    public static int getImageViewsFromLayoutCount(View parentView) {
        final int noImageViewsOnLayoutValue = -1;

        Solo solo = DataProvider.getInstance().getSolo();
        ArrayList<ImageView> allChildImageViews = solo.getCurrentImageViews(parentView);
        if (null == allChildImageViews) {
            return noImageViewsOnLayoutValue;
        }
        return allChildImageViews.size();
    }

    // TODO move all TextView util methods to TextViewUtils class
    /**
     * Searches for the {@code TextView} object with {@code text} in current
     * activity
     * 
     * @param text
     *            string for searching.
     * @param isStrictEqual
     *            if <b>true</b> returns {@code TextView} object with text that
     *            equals specified text, else returns {@code TextView} object
     *            with text that contains specified text.
     * @return {@code TextView} object with specified text.
     */
    public static TextView searchTextViewInActivity(String text, boolean isStrictEqual) {
        return searchTextViewInActivity(text, null, isStrictEqual);
    }

    /**
     * Searches for the {@code TextView} object with {@code text} in current
     * activity
     * 
     * @param text
     *            string for searching.
     * @param exceptions
     *            {@code List} of {@code TextView} that must not be returned by
     *            the method
     * @param isStrictEqual
     *            if <b>true</b> returns {@code TextView} object with text that
     *            equals specified text, else returns {@code TextView} object
     *            with text that contains specified text.
     * @return {@code TextView} object with specified text.
     */
    public static TextView searchTextViewInActivity(String text, List<TextView> exceptions,
            boolean isStrictEqual) {
        Solo solo = DataProvider.getInstance().getSolo();
        List<View> allActivityViews = solo.getViews();
        if (null == allActivityViews) {
            return null;
        }

        for (View view : allActivityViews) {

            if (!(view instanceof TextView) || (null != exceptions && exceptions.contains(view))) {
                continue;
            }

            TextView currentTextView = (TextView) view;
            String currentTextViewValue = currentTextView.getText().toString();
            if (StringUtils.isStringsEquals(currentTextViewValue, text, isStrictEqual)) {
                return currentTextView;
            }
        }

        return null;
    }

    /**
     * Taps on specified {@code view}.
     * Logs "Tapping on <i>viewName</i>"
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
     * Gets view with specified {@code viewId} from current screen
     * 
     * @deprecated use Id.getViewByName() instead.
     * @param viewId
     *            id of required view
     * @param viewType
     *            type of required view
     * @return view with specified {@code viewId} and {@code viewType} or
     *         <b>null</b> if there is no such view
     */
    @Deprecated
    public static <T extends View> T getViewById(int viewId, Class<T> viewType) {
        Solo solo = DataProvider.getInstance().getSolo();
        View viewWithSpecifiedId = solo.getView(viewId);

        if (null == viewWithSpecifiedId || !viewType.isInstance(viewWithSpecifiedId)) {
            return null;
        }
        @SuppressWarnings("unchecked")
        T requiredView = (T) viewWithSpecifiedId;
        return requiredView;
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
}
