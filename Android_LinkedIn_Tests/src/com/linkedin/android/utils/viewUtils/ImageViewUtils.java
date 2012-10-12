package com.linkedin.android.utils.viewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.View;
import android.widget.ImageView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.BitmapUtils;

/**
 * The class contains util methods for ImageView.
 * 
 * @author vasily.gancharov
 * @created Aug 21, 2012 18:04:37 PM
 */
public final class ImageViewUtils {

    /**
     * Gets image view with specified {@code imageViewIndex} from current image
     * view list
     * 
     * @param imageViewIndex
     *            index of required image view
     * @return {@code ImageView} with specified index or <b>null</b> if there is
     *         no such image view
     */
    public static ImageView getImageViewByIndex(int imageViewIndex) {
        Solo solo = DataProvider.getInstance().getSolo();
        List<ImageView> currrentImageViews = solo.getCurrentImageViews();
        if (null == currrentImageViews || currrentImageViews.size() <= imageViewIndex) {
            return null;
        }
        ImageView requiredImageView = currrentImageViews.get(imageViewIndex);
        return requiredImageView;
    }

    /**
     * Verifies that {@code imageView} contains specified points with colors
     * 
     * @param imageView
     *            imageView that must contain {@code point_colors}
     * @param point_colors
     *            point - color pairs
     * @return <b>true</b> if {@code imageView} contains specified points with
     *         colors <b>false</b> otherwise
     */
    public static boolean isImageViewContainsPointsWithColors(ImageView imageView,
            Map<Point, Integer> point_colors) {
        Bitmap imageViewBitmap = getImageViewBitmap(imageView);

        if (null == imageViewBitmap) {
            return false;
        }

        Set<Point> imageViewPoints = point_colors.keySet();
        for (Point point : imageViewPoints) {
            int colorAtThePoint = point_colors.get(point);
            if (!BitmapUtils.isBitmapContainPointWithColor(imageViewBitmap, point, colorAtThePoint)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies {@code Bitmap} of specified {@code imageView}
     * 
     * @param imageView
     *            image view to get {@code Bitmap}
     * @return {@code Bitmap} of specified {@code imageView} or <b>null</b> if
     *         {@code imageView} is null
     */
    public static Bitmap getImageViewBitmap(ImageView imageView) {
        if (null == imageView) {
            return null;
        }

        imageView.buildDrawingCache();
        Bitmap imageViewBitmap = imageView.getDrawingCache();
        return imageViewBitmap;
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
}
