package com.linkedin.android.tests.utils;

import android.widget.ImageView;

import com.linkedin.android.tests.data.PointsWithColorsToDetectImages;
import com.linkedin.android.utils.viewUtils.ImageViewUtils;

/**
 * The class contains util methods to get info about contact
 * 
 * @author vasily.gancharov
 * @created Aug 23, 2012 15:33:27 PM
 */
public final class ContactInfoUtils {

    // CONSTANTS ------------------------------------------------------------
    public static final int CONTACT_DEGREE_UNDEFINED = -1;
    public static final int CONTACT_DEGREE_1 = 1;
    public static final int CONTACT_DEGREE_2 = 2;
    public static final int CONTACT_DEGREE_3 = 3;

    // METHODS --------------------------------------------------------------
    /**
     * Gets contact degree by it's {@code degreeImageView}
     * 
     * @param cImageView
     *            {@code ImageView} of contact degree
     * @return contact degree
     */
    public static int getContactDegree(ImageView degreeImageView) {
        if (null == degreeImageView) {
            return CONTACT_DEGREE_UNDEFINED;
        }

        if (ImageViewUtils.isImageViewContainsPointsWithColors(degreeImageView,
                PointsWithColorsToDetectImages.CONTACT_DEGREE_1)) {
            return CONTACT_DEGREE_1;
        }

        if (ImageViewUtils.isImageViewContainsPointsWithColors(degreeImageView,
                PointsWithColorsToDetectImages.CONTACT_DEGREE_2)) {
            return CONTACT_DEGREE_2;
        }

        if (ImageViewUtils.isImageViewContainsPointsWithColors(degreeImageView,
                PointsWithColorsToDetectImages.CONTACT_DEGREE_3)) {
            return CONTACT_DEGREE_3;
        }

        return CONTACT_DEGREE_UNDEFINED;
    }
}
