package com.linkedin.android.utils.viewUtils;

import junit.framework.Assert;
import android.view.View;
import android.view.ViewGroup;

/**
 * The class contains util methods for {@code ViewGroup}.
 * 
 * @author vasily.gancharov
 * @created Aug 31, 2012 17:15:37 PM
 */
public final class ViewGroupUtils {

    /**
     * Checks if specified {@code viewGroup} contains child with index
     * {@code childIndex}
     * 
     * @param viewGroup
     *            {@code ViewGroup} that must contain children with index
     *            {@code childIndex}
     * @param childIndex
     *            index of child in {@code viewGroup}
     * @return <b>true</b> if specified {@code viewGroup} contains child with
     *         index {@code childIndex} <b>false</b> otherwise
     */
    public static boolean isViewGroupContainChildWithIndex(ViewGroup viewGroup, int childIndex) {
        final int minViewGroupChildIndex = 0;
        if (null == viewGroup) {
            return false;
        }
        int viewGroupChildCount = viewGroup.getChildCount();
        return childIndex >= minViewGroupChildIndex && viewGroupChildCount > childIndex;
    }

    /**
     * Gets child view of {@code viewGroup} by {@code childIndex} and cast it to
     * {@code viewType} safely
     * 
     * @param viewGroup
     *            {@code ViewGroup} to get child from
     * @param childIndex
     *            index of child in {@code ViewGroup}
     * @param viewType
     *            type of required view
     * @return child view of {@code viewGroup} with specified {@code childIndex}
     *         casted to {@code viewType} or <b>null</b> if there is no such
     *         child
     */
    public static <T extends View> T getChildViewByIndexSafely(ViewGroup viewGroup, int childIndex,
            Class<T> viewType) {
        if (!isViewGroupContainChildWithIndex(viewGroup, childIndex)) {
            return null;
        }
        View viewGroupChild = viewGroup.getChildAt(childIndex);
        if (!viewType.isInstance(viewGroupChild)) {
            return null;
        }
        @SuppressWarnings("unchecked")
        T viewGroupChildOfTypeT = (T) viewGroupChild;
        return viewGroupChildOfTypeT;
    }

    /**
     * Taps on first {@code ImageView} or (@code TextView) in {@code layout}.
     * 
     * @param layout
     *            is layout where placed view for tap
     * @param tapOnLayout
     *            <b>true</b> for tapping on {@code layout} if there are no
     *            {@code ImageView} or (@code TextView) placed in it
     * @param viewName
     *            is name of element for tap (for logging)
     * @param errorMessage
     *            is message for logging error
     */
    public static void tapFirstViewInLayout(ViewGroup layout, boolean tapOnLayout,
            String viewName, String errorMessage) {
        int viewIdToTap = -1;
        for (int i = 0; i < layout.getChildCount(); i++) {
            String childClassName = layout.getChildAt(i).getClass().getSimpleName();
            if (childClassName.equals("ImageView") ||
                    childClassName.equals("TextView")) {
                viewIdToTap = i;
                break;
            }
        }
        
        if (!tapOnLayout) {
            Assert.assertTrue(errorMessage, viewIdToTap != -1);
        } else {
            if (viewIdToTap != -1) {
                ViewUtils.tapOnView(layout.getChildAt(viewIdToTap), viewName);
            } else {
                ViewUtils.tapOnView(layout, viewName);
            }
        }
    }
}
