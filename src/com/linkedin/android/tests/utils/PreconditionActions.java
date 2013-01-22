package com.linkedin.android.tests.utils;

import java.util.ArrayList;

import junit.framework.Assert;

import com.linkedin.android.screens.common.ScreenAddConnections;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenYou;
import com.linkedin.android.tests.data.StringData;
import com.linkedin.android.utils.HardwareActions;

/**
 * Class for precision action.
 * 
 * @author nikita.chehomov
 * @created Sep 10, 2012 1:06:48 PM
 */
public class PreconditionActions {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------

    /**
     * Logout and login as a specified user with senderEmail and senderPassword.
     * Send count of messages (message = messageBody, subject=messageTitle) to
     * recipient with name recepienName. After that logout.
     * 
     * @param senderEmail
     *            is email message sender.
     * @param senderPassword
     *            is password message sender.
     * @param recepienName
     *            is name of connection to send message.
     * @param messageTitle
     *            is subject of message.
     * @param messageBody
     *            is message.
     * @param count
     *            is count of message.
     * @return String body of last message.
     */
    public static String sendMessageAndLogout(String senderEmail, String senderPassword,
            String recepienName, String messageTitle, String messageBody, int count) {

        Assert.assertTrue("Parameter 'count' is zero or negative", count > 0);
        String message = null;

        LoginActions.logout();
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart(senderEmail,
                senderPassword);
        ScreenExpose screenExpose = screenUpdates.openExposeScreen();
        ScreenYou screenYou = screenExpose.openYouScreen();

        for (int i = 0; i < count; i++) {
            ScreenNewMessage screenNewMessage = screenYou.openNewMessageScreen();
            ScreenAddConnections screenAddConnections = screenNewMessage.openAddConnectionsScreen();

            screenAddConnections.chooseConnection(recepienName);
            screenAddConnections.tapOnDoneButton();
            screenNewMessage = new ScreenNewMessage();

            message = screenNewMessage.typeMessage(messageBody);
            screenNewMessage.typeSubject(messageTitle);

            screenNewMessage.sendMessage();
        }
        LoginActions.logout();
        return message;
    }

    /**
     * Logout and login as a specified user. Send count of messages (message =
     * messageBody, subject=messageTitle) to recipient with name recepienName.
     * After that logout.
     * 
     * @param recepienName
     *            is name of connection to send message.
     * @param messageTitle
     *            is subject of message.
     * @param messageBody
     *            is message.
     * @param count
     *            is count of message.
     */
    public static void sendMessageAndLogout(String recepienName, String messageTitle,
            String messageBody, int count) {
        sendMessageAndLogout(StringData.test_spammer_email, StringData.test_spammer_password,
                recepienName, messageTitle, messageBody, count);
    }

    /**
     * Logout and login as a specified user. Send count of messages to recipient
     * with name recepienName. After that logout.
     * 
     * @param recepienName
     *            is name of connection to send message.
     * @param count
     *            is count of message.
     * @return String body of last message.
     */
    public static String sendMessageAndLogout(String recepienName, int count) {
        return sendMessageAndLogout(StringData.test_spammer_email,
                StringData.test_spammer_password, recepienName, null, null, count);
    }

    /**
     * Logout and login as a specified user. Send one message to multiple
     * recipients. After that logout.
     * 
     * @param senderEmail
     *            is email message sender.
     * @param senderPassword
     *            is password message sender.
     * @param recipients
     *            are names of connections to send message.
     * @param messageTitle
     *            is subject of message.
     * @param messageBody
     *            is message.
     * @return String body of message.
     */
    public static String sendMessageToMultipleRecipientsAndLogout(String senderEmail,
            String senderPassword, ArrayList<String> recipients, String messageTitle,
            String messageBody) {
        Assert.assertNotNull("Recipient list is not present", recipients);
        Assert.assertFalse("Recipient list is empty", recipients.isEmpty());
        LoginActions.logout();
        ScreenUpdates screenUpdates;
        if (senderEmail == null || senderPassword == null) {
            screenUpdates = LoginActions.openUpdatesScreenOnStart(StringData.test_email,
                    StringData.test_password);
        } else {
            screenUpdates = LoginActions.openUpdatesScreenOnStart(senderEmail, senderPassword);
        }
        ScreenExpose screenExpose = screenUpdates.openExposeScreen();
        ScreenYou screenYou = screenExpose.openYouScreen();
        ScreenNewMessage screenNewMessage = screenYou.openNewMessageScreen();
        ScreenAddConnections screenAddConnections = screenNewMessage.openAddConnectionsScreen();
        for (String nameConnection : recipients) {
            HardwareActions.scrollUp();
            screenAddConnections.chooseConnection(nameConnection);
        }
        screenAddConnections.tapOnDoneButton();
        screenNewMessage = new ScreenNewMessage();
        if (messageTitle == null) {
            screenNewMessage.typeSubject(null);
        } else {
            screenNewMessage.typeSubject(messageTitle);
        }
        String message;
        if (messageBody == null) {
            message = screenNewMessage.typeMessage(null);
        } else {
            message = messageBody;
            screenNewMessage.typeMessage(messageBody);
        }
        screenNewMessage.sendMessage();

        LoginActions.logout();

        return message;
    }

    /**
     * Logout and login as a 'test' user. Send one message to multiple
     * recipients. After that logout.
     * 
     * @param recipients
     *            are names of connections to send message.
     * @param messageTitle
     *            is subject of message.
     * @param messageBody
     *            is message.
     * @return String body of message.
     */
    public static String sendMessageToMultipleRecipientsAndLogout(ArrayList<String> recipients,
            String messageTitle, String messageBody) {
        return sendMessageToMultipleRecipientsAndLogout(null, null, recipients, messageTitle,
                messageBody);
    }

    /**
     * Logout and login as a 'test' user. Send one message random message to
     * multiple recipients. After that logout.
     * 
     * @param recipients
     *            are names of connections to send message.
     * @return String body of message.
     */
    public static String sendMessageToMultipleRecipientsAndLogout(ArrayList<String> recipients) {
        return sendMessageToMultipleRecipientsAndLogout(null, null, recipients, null, null);
    }

}
