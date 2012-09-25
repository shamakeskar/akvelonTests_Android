package com.linkedin.android.utils.viewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.Assert;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.RegexpUtils;
import com.linkedin.android.utils.StringUtils;

import android.graphics.Point;
import android.text.Layout;
import android.view.View;
import android.widget.TextView;

/**
 * The class contains util methods for {@code TextView}.
 * 
 * @author vasily.gancharov
 * @created Sep 10, 2012 14:25:31 PM
 */
public class TextViewUtils {

    /**
     * Searches all child {@code TextView}s of {@code parentView} that contain
     * {@code textPattern}. NOTE: if {@code parentView} is <b>null</b> than
     * child {@code TextView}s will be searched in all currently visible
     * {@code TextView}s.
     * 
     * @param textPattern
     *            {@code Pattern} to find {@code TextView}s in
     *            {@code parentView}
     * @param parentView
     *            parent {@code View} to find child {@code TextView}s
     * @return {@code List} of all child {@code TextView}s of {@code parentView}
     *         that contain {@code textPattern}.
     */
    public static List<TextView> searchChildTextViews(Pattern textPattern, View parentView) {
        List<TextView> result = new ArrayList<TextView>();
        Solo solo = DataProvider.getInstance().getSolo();
        ArrayList<TextView> allChildTextViews = solo.getCurrentTextViews(parentView);
        if (null == allChildTextViews) {
            return null;
        }

        for (TextView currentTextView : allChildTextViews) {
            String currentTextViewValue = currentTextView.getText().toString();
            if (RegexpUtils.isCanBeFound(currentTextViewValue, textPattern)) {
                result.add(currentTextView);
            }
        }

        return result;
    }

    /**
     * Clicks on {@code textView} text substring
     * 
     * @param textView
     *            {@code TextView} which substring must be clicked
     * @param substring
     *            {@code String} to click on
     */
    public static void clickOnTextViewSubstring(TextView textView, String substring) {
        Point substringPositionOnScreen = getSubstringPositionOnScreen(textView, substring);
        Assert.assertNotNull("There is no such substring " + substring + " in specified TextView",
                substringPositionOnScreen);
        Solo solo = DataProvider.getInstance().getSolo();
        solo.clickOnScreen(substringPositionOnScreen.x, substringPositionOnScreen.y);
    }

    /**
     * Gets position at screen ({@code Point}) of {@code textView} text
     * substring. NOTE: calculated x coordinate points to the middle of the
     * substring.
     * 
     * @param textView
     *            {@code TextView} which text substring position must be
     *            calculated
     * @param substring
     *            {@code textView} text substring
     * @return position at screen ({@code Point}) of {@code textView} text
     *         substring or <b>null</b> if there is no such substring
     */
    private static Point getSubstringPositionOnScreen(TextView textView, String substring) {

        final int stringNotFoundIndex = -1;

        if (null == textView || StringUtils.isNullOrEmpty(substring)) {
            return null;
        }

        String baseText = textView.getText().toString();
        int substringStartOffset = baseText.indexOf(substring);
        int substringEndOffset = substringStartOffset + substring.length();

        if (stringNotFoundIndex == substringStartOffset) {
            return null;
        }

        Layout textViewLayout = textView.getLayout();
        float textLineHeight = textViewLayout.getHeight() / textViewLayout.getLineCount();
        int substringLine = textViewLayout.getLineForOffset(substringStartOffset) + 1;
        Point textViewCoordinates = ViewUtils.getViewCoordinatesOnScreen(textView);

        int textViewCoordinateX = textViewCoordinates.x;
        int substringXStart = calculateStringOffsetPositionX(textViewLayout, substringStartOffset,
                textViewCoordinateX);
        int substringXEnd = calculateStringOffsetPositionX(textViewLayout, substringEndOffset,
                textViewCoordinateX);
        int endOfLineX = textViewCoordinateX + textView.getWidth();
        int substringX = substringXEnd > substringXStart ? (substringXStart + substringXEnd) / 2
                : (substringXStart + endOfLineX) / 2;

        int substringY = (int) (substringLine * textLineHeight) + textViewCoordinates.y;

        Point substringCoordinates = new Point(substringX, substringY);
        return substringCoordinates;
    }

    /**
     * Calculates x coordinate of specified string {@code offset} relatively
     * screen start
     * 
     * @param textViewLayout
     *            it will be calculated x coordinate of {@code offset} in text
     *            that is value of {@code TextView}. {@code textViewLayout} is
     *            {@code Layout} of this {@code TextView}.
     * @param offset
     *            offset in {@code TextView} text
     * @param textViewPositionX
     *            position on screen of {@code textViewLayout}
     * @return x coordinate of specified string {@code offset} relatively screen
     *         start or <b>-1</b> if there is wrong parameters passed.
     */
    private static int calculateStringOffsetPositionX(Layout textViewLayout, int offset,
            int textViewPositionX) {
        final int defaultValue = -1;

        if (null == textViewLayout || offset < 0 || textViewPositionX < 0) {
            return defaultValue;
        }

        float horizontalOffsetRelativelyTextView = textViewLayout.getPrimaryHorizontal(offset);
        int horizontalOffsetRelativelyScreenStart = (int) (horizontalOffsetRelativelyTextView + textViewPositionX);
        return horizontalOffsetRelativelyScreenStart;
    }

}
