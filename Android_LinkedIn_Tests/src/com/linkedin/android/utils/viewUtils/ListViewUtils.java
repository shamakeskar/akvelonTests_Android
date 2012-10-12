package com.linkedin.android.utils.viewUtils;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.ScreenResolution;
import com.linkedin.android.utils.WaitActions;

/**
 * The class contains util methods for ListView.
 * 
 * @author vasily.gancharov
 * @created Aug 21, 2012 14:52:37 PM
 */
public final class ListViewUtils {
    // CONSTANTS ------------------------------------------------------------
    // Default delay for wait until ListView loads after scroll (in sec).
    private static final int WAIT_LOADING_ITEMS_AFTER_SCROLL = 10;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------

    /**
     * Checks if current list of list views is not empty
     * 
     * @return <b>true</b> if current list of list views is not empty
     *         <b>false</b> otherwise
     */
    public static boolean isCurrentListViewsNotEmpty() {
        Solo solo = DataProvider.getInstance().getSolo();
        List<ListView> currentListViews = solo.getCurrentListViews();
        if (null == currentListViews) {
            return false;
        }
        return !currentListViews.isEmpty();
    }

    /**
     * Returns first list view from screen
     * 
     * @return {@code ListView} first list view from screen or null if there is
     *         no {@code ListView}
     */
    public static ListView getFirstListView() {
        final int firstScreenViewIndex = 0;

        Solo solo = DataProvider.getInstance().getSolo();
        List<ListView> currentListViews = solo.getCurrentListViews();
        if (null == currentListViews) {
            return null;
        }
        return currentListViews.get(firstScreenViewIndex);
    }

    /**
     * Checks if width of specified list view is equal to screen width
     * 
     * @param {@code ListView} list view
     * @return <b>true</b> if width of specified list view is equal to screen
     *         width <b>false</b> otherwise
     */
    public static boolean isListViewWidthEqualToScreenWidth(ListView listView) {
        if (null == listView) {
            return false;
        }
        return listView.getWidth() == ScreenResolution.getScreenWidth();
    }

    /**
     * Returns {@code ListView} by {@code listViewIndex} from list of
     * {@code ListView}s that currently on screen.
     * 
     * @param listViewIndex
     *            index of {@code ListView} on {@code parentView}
     * @return {@code ListView} with specified {@code listViewIndex} list of
     *         {@code ListView}s that currently on screen, <b>null</b> otherwise
     */
    public static ListView getListViewFromCurrentScreenByIndex(int listViewIndex) {
        Solo solo = DataProvider.getInstance().getSolo();
        ArrayList<ListView> allListViewsFromLayout = solo.getCurrentListViews();
        return ImageViewUtils.getViewByIndex(allListViewsFromLayout, listViewIndex);
    }

    /**
     * Verifies that {@code listView} is not empty
     * 
     * @param listView
     *            {@code ListView} to verify it's that not empty
     * @return <b>true</b> if {@code listView} is not empty <b>false</b>
     *         otherwise
     */
    public static boolean isListViewNotEmpty(ListView listView) {
        return null != listView && listView.getCount() > 0;
    }

    /**
     * Returns {@code ListView} child with specified {@code index}
     * 
     * @param childIndex
     *            index of {@code ListView} child
     * @return {@code ListView} child at specified {@code index} or <b>null</b>
     *         if such child does not exist
     */
    public static View getChildAtSafely(ListView listView, int childIndex) {
        if (null == listView || listView.getCount() <= childIndex) {
            return null;
        }
        return listView.getChildAt(childIndex);
    }

    /**
     * Scroll list with index <b>listIndex</b> on one screen down. After that
     * wait <b>delayForLoading</b>.
     * 
     * @param listIndex
     *            - is index of list to scroll
     * @param delayForLoading
     *            - is delay in seconds for wait after scroll
     * @return <b>true</b> if can scroll more, <b>false</b> if it end of list.
     */
    public static boolean scrollToNewItems(int listIndex, float delayForLoading) {
        Logger.i("Scrool list on one screen down");
        boolean isEnd = DataProvider.getInstance().getSolo().scrollDownList(listIndex);
        WaitActions.delay(delayForLoading);// Wait for ListView load more rows.
        return isEnd;
    }

    /**
     * Scroll list with index <b>listIndex</b> on one screen down. After that
     * wait WAIT_LOADING_ITEMS_AFTER_SCROLL.
     * 
     * @param listIndex
     *            - is index of list to scroll
     * @return <b>true</b> if can scroll more, <b>false</b> if it end of list.
     */
    public static boolean scrollToNewItems(int listIndex) {
        return scrollToNewItems(listIndex, WAIT_LOADING_ITEMS_AFTER_SCROLL);
    }

    /**
     * Scroll list with index <b>0</b> on one screen down. After that wait
     * WAIT_LOADING_ITEMS_AFTER_SCROLL.
     * 
     * @return <b>true</b> if can scroll more, <b>false</b> if it end of list.
     */
    public static boolean scrollToNewItems() {
        return scrollToNewItems(0, WAIT_LOADING_ITEMS_AFTER_SCROLL);
    }
}
