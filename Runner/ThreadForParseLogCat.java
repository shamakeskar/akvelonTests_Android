import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ThreadForParseLogCat extends Thread {
    // CONSTANTS ------------------------------------------------------------
    private static final String CMD_CLEAR_LOGCAT = "logcat -c";
    private static final String STRING_TAG_FOR_LOGCAT_FILTER = "LinkedIn_Android_Tests";
    private static final String CMD_START_LOGCAT = "logcat -v long " + STRING_TAG_FOR_LOGCAT_FILTER
            + ":V *:S";
    private static final String STRING_START_TEST = "Start test '";
    private static final String STRING_END_TEST = "Test over.-------------------------------------------------------------------";
    // private static final String STRING_PASS_TEST = "Pass test ";
    // private static final String STRING_FAIL_TEST = "Fail in test ";

    // PROPERTIES -----------------------------------------------------------
    // Log from logcat for currebt test.
    private StringBuilder logcat = new StringBuilder();
    // String (dirty) with test name.
    private String currentTestNameString;
    // Process of 'logcat' command.
    private Process process;

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    @Override
    public void run() {
        try {
            int exitValue = Integer.MAX_VALUE;
            // Clear logcat.
            exitValue = Runner.runCommandInRuntime(Runner.getStartAdbCommand()
                    .append(CMD_CLEAR_LOGCAT).toString());
            if (exitValue != 0) {
                Runner.logError("Cannot clear logcat!");
            }

            // Start logcat for parse.
            Runtime runtime = Runtime.getRuntime();
            String command = Runner.getStartAdbCommand().append(CMD_START_LOGCAT).toString();
            Runner.logAndOutput("Running command for logcat parse: '" + command + "':");
            try {
                process = runtime.exec(command);
            } catch (Exception e) {
                Runner.logError("While running '" + command + "' throws exception: "
                        + e.getMessage());
            }
            BufferedReader stdOutput = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(
                    process.getErrorStream()));
            String line = null;
            boolean isTestStarted = false;
            try {
                // This cycle will be stopped by killing 'process'.
                while (process != null) {
                    // Try get line.
                    line = stdOutput.readLine();
                    if (line == null)
                        line = stdError.readLine();
                    if (line == null)
                        break;// End of stream reached.

                    // Handle each line.
                    if (line.length() == 0) {
                        continue;
                    } else if (line.startsWith(STRING_START_TEST)) {
                        isTestStarted = true;
                        currentTestNameString = line.substring(STRING_START_TEST.length() - 1);
                    } else if (line.startsWith(STRING_END_TEST)) {
                        isTestStarted = false;
                    }
                    if (isTestStarted) {
                        logcat.append(line).append('\n');
                    }
                    Runner.log("# " + line);
                }
            } catch (Exception e) {
                Runner.logError("While running '" + command + "'throws exception: "
                        + e.getMessage());
            }
        } catch (Exception e1) {
            Runner.logError("Exception while parsing logcat: " + e1.getMessage());
        }
    }

    /**
     * Stops parsing.
     * 
     * @return <b>true</b> if start stops and <b>false</b> if not runned.
     */
    public boolean stopParsing() {
        if (process != null) {
            process.destroy();
            return true;
        }
        return false;
    }

    /**
     * Returns name of current test.
     * 
     * @return name of current test.
     */
    public synchronized String getCurrentTestName() {
        Matcher matcher = Pattern.compile("'.*'").matcher(currentTestNameString);
        if (matcher.find()) {
            String name = matcher.group(0);
            if (name.length() > 2) {
                name = name.substring(1, name.length() - 1);
                return name;
            }
        }
        return currentTestNameString;
    }

    /**
     * Returns id of current test.
     * 
     * @return id of current test (as String) or <b>null</b> if cannot found.
     */
    public synchronized String getCurrentTestId() {
        Integer id = Runner.convertStringToIntegerSafely(currentTestNameString);
        return id != null ? id.toString() : null;
    }

    /**
     * Returns logcat for current test.
     * 
     * @return logcat for current test.
     */
    public synchronized String getLogcatForCurrentTest() {
        return logcat.toString();
    }
}
