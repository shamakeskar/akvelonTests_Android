package com.linkedin.android.utils.viewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.Assert;
import android.graphics.Point;
import android.text.Layout;
import android.text.SpannedString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.RegexpUtils;
import com.linkedin.android.utils.StringUtils;

/**
 * The class contains util methods for {@code TextView}.
 * 
 * @author vasily.gancharov
 * @created Sep 10, 2012 14:25:31 PM
 */
public class TextViewUtils {
    /**
     * Returns {@code TextView} with specified text (equal). Unlike
     * <i>getSolo().getText(text)</i> not throw Exception if TextView not found.
     * 
     * @param text
     *            text to search.
     * @return {@code TextView} object with text equal to <b>text</b>.
     */
    public static TextView getTextViewByText(String text) {
        ArrayList<TextView> textViews = DataProvider.getInstance().getSolo()
                .getCurrentTextViews(null);
        for (TextView textView : textViews) {
            if (textView.getText().equals(text)) {
                return textView;
            }
        }
        return null;
    }

    /**
     * Returns {@code TextView} with text that contain specified text.
     * 
     * @param partialText
     *            partial text to search.
     * @return {@code TextView} object that contain <b>partialText</b>.
     */
    public static TextView getTextViewByPartialText(String partialText) {
        ArrayList<TextView> textViews = DataProvider.getInstance().getSolo()
                .getCurrentTextViews(null);
        for (TextView textView : textViews) {
            if (textView.getText().toString().indexOf(partialText) != -1) {
                return textView;
            }
        }
        return null;
    }

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
     * Searches for the {@code TextView} object with {@code text} in
     * {@code parentView}. If {@code parentView} is <b>null</b> the method will
     * search {@code TextView} object with {@code text} from list of TextViews
     * that currently on screen.
     * 
     * @param text
     *            is string for searching (only visible).
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
     *            is string for searching (only visible).
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
     * Returns all {@code LinkTextView} on current screen.
     * 
     * @return ArrayList<TextView> with all {@code LinkTextView} on current
     *         screen.
     */
    public static ArrayList<TextView> getLinkTextViews() {
        ArrayList<TextView> linkTextViews = new ArrayList<TextView>();
        ArrayList<TextView> textViews = DataProvider.getInstance().getSolo()
                .getCurrentTextViews(null);
        for (TextView textView : textViews) {
            if (textView.getClass().getSimpleName()
                    .equals(ANDROID_VIEW_NAMES.LINK_TEXT_VIEW.getTypeValue()))
                linkTextViews.add(textView);
        }
        return linkTextViews;
    }

    /**
     * Scroll to up, after scroll down while textViev is visible.
     * 
     * @param text
     *            - text in texView.
     * @param maxCountScroll
     *            - max count scroll down.
     * @return true if textView is visible.
     */
    public static boolean searchAndScrollToVisibleText(String text, int maxCountScroll) {
        Solo solo = DataProvider.getInstance().getSolo();
        HardwareActions.scrollUp();
        do {
            if (solo.searchText(text, true))
                break;
            solo.scrollDown();
            maxCountScroll--;
        } while (maxCountScroll > 0);
        if (solo.searchText(text, true))
            return true;
        return false;
    }

    /**
     * Scroll to up, after scroll down while textViev is visible (Max count
     * scroll down 10 count).
     * 
     * @param text
     *            - text in texView.
     * @return true if textView is visible.
     */
    public static boolean searchAndScrollToVisibleText(String text) {
        return searchAndScrollToVisibleText(text, 10);
    }
    
    /**
     * Tap on link in textView.
     * 
     * @param textView
     *            is allusive.
     * @param textLink
     *            - link text.
     */
    public static void tapOnLinkInTextView(final TextView textView, final String textLink) {
        Assert.assertTrue("TextView or text link equal null", textLink != null && textView != null);
        int beginIndex = textView.getText().toString().indexOf(textLink);
        int endIndex = beginIndex + textLink.length();
        Assert.assertTrue("Text link is not found", beginIndex >= 0);
        SpannedString stringClickableElements = new SpannedString(textView.getText());
        final ClickableSpan[] links = stringClickableElements.getSpans(beginIndex, endIndex, ClickableSpan.class);
        Assert.assertTrue("Clickable link is not found", links.length > 0);
        Assert.assertNotNull("Clickable link is not found", links[0]);
        Logger.i("Tapping on link: '" + textLink + "'");
        DataProvider.getInstance().getSolo().getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                links[0].onClick(textView);
            }
        });
    }
}
