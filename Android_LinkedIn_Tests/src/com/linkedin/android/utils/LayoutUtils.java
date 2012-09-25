/**
 * 
 */
package com.linkedin.android.utils;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;

/**
 * Class for actions with screen layouts.
 * 
 * @author evgeny.agapov
 * @created Aug 13, 2012 3:49:07 PM
 */
public final class LayoutUtils {

    public static final float screenWidth = ScreenResolution.getScreenWidth()
            / ScreenResolution.getScreenDensity();
    public static final float screenHeight = ScreenResolution.getScreenHeight()
            / ScreenResolution.getScreenDensity();

    public static final Rect2DP UPPER_LEFT_BUTTON_LAYOUT = new Rect2DP(0, 0, 55.0f, 82.0f);
    public static final Rect2DP UPPER_RIGHT_BUTTON_LAYOUT = new Rect2DP(screenWidth - 59.0f, 0,
            59.0f, 82.0f);
    public static final Rect2DP SEARCH_BAR_LAYOUT = new Rect2DP(54.0f, 0, screenWidth - 2 * 54.0f,
            82.0f);

    public static final Rect2DP LEFT_BUTTON_LAYOUT = new Rect2DP(0, 0, screenWidth / 2,
            screenHeight);
    public static final Rect2DP RIGHT_BUTTON_LAYOUT = new Rect2DP(screenWidth / 2, 0,
            screenWidth / 2, screenHeight);

    public static final Rect2DP LOWER_LEFT_OF_2_BUTTONS_LAYOUT = new Rect2DP(0,
            screenHeight - 150.0f, screenWidth / 2, 151.0f);
    public static final Rect2DP LOWER_RIGHT_OF_2_BUTTONS_LAYOUT = new Rect2DP(screenWidth / 2,
            screenHeight - 150.0f, screenWidth / 2, 151.0f);

    public static final Rect2DP CENTER_BIG_IMAGE_LAYOUT = new Rect2DP(89.0f, 100.0f, 142.0f,
            screenHeight);

    public static final Rect2DP LOWER_LEFT_OF_3_BUTTONS_LAYOUT = new Rect2DP(0,
            screenHeight - 150.0f, screenWidth / 3, 151.0f);
    public static final Rect2DP LOWER_CENTER_OF_3_BUTTONS_LAYOUT = new Rect2DP(screenWidth / 3,
            screenHeight - 150.0f, screenWidth / 3, 151.0f);
    public static final Rect2DP LOWER_RIGHT_OF_3_BUTTONS_LAYOUT = new Rect2DP(screenWidth * 2 / 3,
            screenHeight - 150.0f, screenWidth / 3, 151.0f);

    public static final Rect2DP LOWER_LEFT_OF_4_BUTTONS_LAYOUT = new Rect2DP(0,
            screenHeight - 150.0f, screenWidth / 4, 151.0f);
    public static final Rect2DP LOWER_LEFT_CENTER_OF_4_BUTTONS_LAYOUT = new Rect2DP(
            screenWidth / 4, screenHeight - 150.0f, screenWidth / 4, 151.0f);
    public static final Rect2DP LOWER_RIGHT_CENTER_OF_4_BUTTONS_LAYOUT = new Rect2DP(
            screenWidth / 2, screenHeight - 150.0f, screenWidth / 4, 151.0f);
    public static final Rect2DP LOWER_RIGHT_OF_4_BUTTONS_LAYOUT = new Rect2DP(screenWidth * 3 / 4,
            screenHeight - 150.0f, screenWidth / 4, 151.0f);

    public static final Rect2DP CALENDAR_IN_HERO_SLOT_LAYOUT = new Rect2DP(screenWidth / 2, 80.0f,
            screenWidth / 2, screenHeight / 4);

    public static final Rect2DP NEWS_BIG_IMAGE_LAYOUT = new Rect2DP(0, 0, screenWidth,
            screenHeight * 2 / 3);

    public static final Rect2DP UP_ARROW_BUTTON_LAYOUT = new Rect2DP(screenWidth * 0.7f, 23,
            screenWidth * 0.3f, 60);
    public static final Rect2DP DOWN_ARROW_BUTTON_LAYOUT = new Rect2DP(screenWidth * 0.83f, 23,
            screenWidth * 0.17f, 60);

    /**
     * Checks {@code view} placed in {@code layout}
     * 
     * @param view
     *            is {@code View} object for checking.
     * @param layout
     *            is {@code Rect2DP} object for checking.
     * @return <b>true</b> if {@code view} placed in {@code layout}, else
     *         <b>false</b>
     */
    public static boolean isViewPlacedInLayout(View view, Rect2DP layout) {
        if (null == view || null == layout) {
            return false;
        }
        Rect2DP viewRect = new Rect2DP(view);
        return ((viewRect.x >= layout.x) && (viewRect.y >= layout.y)
                && (viewRect.x + viewRect.width <= layout.x + layout.width) && (viewRect.y
                + viewRect.height <= layout.y + layout.height));
    }

    /**
     * Gets list of views placed in {@code layout}.
     * 
     * @param layout
     *            is {@code Rect2DP} object specified layout.
     * @return list of views placed in {@code layout}.
     */
    public static List<View> getListOfViewsPlacedInLayout(Rect2DP layout) {
        List<View> viewsList = new ArrayList<View>();
        List<View> allViewsList = DataProvider.getInstance().getSolo().getViews();
        for (View view : allViewsList) {
            if (isViewPlacedInLayout(view, layout)) {
                viewsList.add(view);
            }
        }
        return viewsList;
    }

    /***
     * Checks does {@code view} have specified {@code width} and {@code height}
     * 
     * @param view
     *            {@code view} to check
     * @param width
     *            target width
     * @param height
     *            target height
     * @param accuracyOfComparing
     *            accuracy of comparing
     * @return <b>true</b> if view has specified {@code width} and
     *         {@code height}, <b>false</b> otherwise
     */
    public static boolean isViewSpecificlySized(View view, float width, float height,
            float accuracyOfComparing) {
        Rect2DP viewRect = new Rect2DP(view);
        boolean doesViewHasSpecifiedSize = viewRect.isSizeEqual(width, height, accuracyOfComparing);
        return doesViewHasSpecifiedSize;
    }

    /***
     * Gets imageView with specified {@code width}, {@code height} that placed
     * on {@code layout}
     * 
     * @param layout
     *            target layout
     * @param width
     *            target width
     * @param height
     *            target height
     * @param accuracyOfComparing
     *            accuracy of comparing width and height
     * @return ImageView that placed on {@code layout} and has specified
     *         {@code width}, {@code height}, or <b>null</b> if there is no such
     *         ImageView
     */
    public static ImageView getImageViewByItsLayoutAndSize(Rect2DP layout, float width,
            float height, float accuracyOfComparing) {
        Solo solo = DataProvider.getInstance().getSolo();
        List<ImageView> currentImageViews = solo.getCurrentImageViews();
        return getViewByItsLayoutAndSize(layout, width, height, accuracyOfComparing,
                currentImageViews);
    }

    /***
     * Gets editText with specified {@code width}, {@code height} that placed on
     * {@code layout}
     * 
     * @param layout
     *            target layout
     * @param width
     *            target width
     * @param height
     *            target height
     * @param accuracyOfComparing
     *            accuracy of comparing width and height
     * @return EditText that placed on {@code layout} and has specified
     *         {@code width}, {@code height}, or <b>null</b> if there is no such
     *         ImageView
     */
    public static EditText getEditTextByItsLayoutAndSize(Rect2DP layout, float width, float height,
            float accuracyOfComparing) {
        Solo solo = DataProvider.getInstance().getSolo();
        List<EditText> currentImageViews = solo.getCurrentEditTexts();
        return getViewByItsLayoutAndSize(layout, width, height, accuracyOfComparing,
                currentImageViews);
    }

    /**
     * Gets view from {@code viewsToSelectFrom} with specified {@code width},
     * {@code height} that placed on {@code layout}
     * 
     * @param layout
     *            target layout
     * @param width
     *            target width
     * @param height
     *            target height
     * @param accuracyOfComparing
     *            accuracy of comparing width and height
     * @param viewsToSelectFrom
     *            list of views from which view will be selected
     * @return view from {@code viewsToSelectFrom} that placed on {@code layout}
     *         and has specified {@code width}, {@code height}, or <b>null</b>
     *         if there is no such view
     */
    private static <T extends View> T getViewByItsLayoutAndSize(Rect2DP layout, float width,
            float height, float accuracyOfComparing, List<T> viewsToSelectFrom) {

        if (null == viewsToSelectFrom) {
            return null;
        }

        for (T view : viewsToSelectFrom) {
            boolean doesViewPlacedInSpecifiedLayoutWithSize = isViewPlacedInLayout(view, layout)
                    && isViewSpecificlySized(view, width, height, accuracyOfComparing);
            if (doesViewPlacedInSpecifiedLayoutWithSize) {
                return view;
            }
        }
        return null;
    }
}
