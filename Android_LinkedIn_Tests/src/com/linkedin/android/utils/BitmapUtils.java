package com.linkedin.android.utils;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * The class contains util methods for {@code Bitmap}
 * 
 * @author vasily.gancharov
 * @created Aug 23, 2012 15:04:37 PM
 */
public final class BitmapUtils {

    /**
     * Verifies if specified {@code bitmap} contains {@code point} with
     * {@code color}
     * 
     * @param bitmap
     *            bitmap that must contain {@code point} with {@code color}
     * @param point
     *            point of {@code bitmap} that must has specified {@code color}
     * @param color
     *            color that must be at specified {@code point} of
     *            {@code bitmap}
     * @return <b>true</b> if {@code bitmap} contains {@code point} with
     *         {@code color} <b>false</b> otherwise
     */
    public static boolean isBitmapContainPointWithColor(Bitmap bitmap, Point point, int color) {
        if (null == bitmap || null == point) {
            return false;
        }

        if (!isBitmapContainPoint(bitmap, point)) {
            return false;
        }
        int colorAtSpecifiedPoint = bitmap.getPixel(point.x, point.y);
        return colorAtSpecifiedPoint == color;
    }

    /**
     * Verifies if specified {@code bitmap} contains {@code point}
     * 
     * @param bitmap
     *            bitmap that must contain {@code point}
     * @param point
     *            prospective point of {@code bitmap}
     * @return <b>true</b> if {@code bitmap} contains {@code point} <b>false</b>
     *         otherwise
     */
    public static boolean isBitmapContainPoint(Bitmap bitmap, Point point) {
        if (null == bitmap || null == point) {
            return false;
        }

        int pointX = point.x;
        int pointY = point.y;
        boolean isBitmapContainPoint = pointX >= 0 && pointX < bitmap.getWidth() && pointY >= 0
                && pointY < bitmap.getHeight();
        return isBitmapContainPoint;
    }
}
