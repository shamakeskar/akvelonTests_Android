package com.linkedin.android.tests.data;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Point;

/**
 * The class contains "point - color" {@code Map}s to detect images
 * 
 * @author vasily.gancharov
 * @created Aug 23, 2012 15:23:47 PM
 */
public class PointsWithColorsToDetectImages {
    
    // CONTACT DEGREE IMAGES -----------------------------------------------------------------
    public static final Map<Point, Integer> CONTACT_DEGREE_1 = new HashMap<Point, Integer>() {

        private static final long serialVersionUID = 1L;

        {
            put(new Point(9, 9), -14191706);
            put(new Point(10, 9), -14257242);
            put(new Point(13, 6), -14914655);
            put(new Point(14, 6), -14914655);
            put(new Point(13, 17), -14914655);
            put(new Point(14, 17), -14914655);
        }
    };

    public static final Map<Point, Integer> CONTACT_DEGREE_2 = new HashMap<Point, Integer>() {

        private static final long serialVersionUID = 2L;

        {
            put(new Point(7, 7), -14914655);
            put(new Point(9, 6), -14914655);
            put(new Point(12, 9), -14914655);
            put(new Point(11, 12), -14914655);
            put(new Point(6, 17), -14914655);
            put(new Point(13, 17), -14914655);
        }
    };

    public static final Map<Point, Integer> CONTACT_DEGREE_3 = new HashMap<Point, Integer>() {

        private static final long serialVersionUID = 3L;

        {
            put(new Point(8, 7), -14914655);
            put(new Point(10, 6), -14914655);
            put(new Point(13, 8), -14914655);
            put(new Point(10, 11), -14914655);
            put(new Point(13, 14), -14914655);
            put(new Point(10, 17), -14914655);
        }
    };

}
