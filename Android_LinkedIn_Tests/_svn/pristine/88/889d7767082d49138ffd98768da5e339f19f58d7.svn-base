package com.linkedin.android.utils.viewUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.widget.ImageView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.BitmapUtils;

/**
 * The class contains util methods for ImageView
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

}
