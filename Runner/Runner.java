import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Runner {
    // CONSTANTS ------------------------------------------------------------
    // Runtime command constants.
    private static final String CMD_ADB = "adb";
    private static final String CMD_ADB_S_PARAMETER = "-s";
    private static final String CMD_PUSH = "push";
    private static final String[] CMD_RUN_ALL_TESTS = new String[] { "shell", "am", "instrument",
            "-r", "-w",
            "com.linkedin.android.test/com.linkedin.android.test.AkvelonInstrumentationTestRunner" };
    private static final String CMD_INSTALL = "install";
    private static final String CMD_INSTALL_R_PARAMETER = "-r";

    // Script parameters constants.
    private static final String PARAM_FILE_WITH_TESTS = "-f";
    private static final String PARAM_CUSTOM_TESTED_APK = "--tested_apk";
    private static final String PARAM_CUSTOM_TESTS_APK = "--tests_apk";
    private static final String PARAM_CUSTOM_FOLDER_WITH_XML = "--tests_dir";
    private static final String PARAM_TESTS = "-t";
    private static final String PARAM_DEVICE_ID = "-d";
    private static final String PARAM_OUTPUT_FILE = "-r";
    private static final String PARAM_ALL_TESTS = "--all_tests";
    private static final String PARAM_HELP = "-h";
    private static final String PARAM_HELP_LONG = "--help";

    // Parse instruments output constants.
    private static final String STRING_INSTRUMENTATION_STATUS = "INSTRUMENTATION_STATUS: ";
    private static final String STRING_INSTRUMENTATION_STATUS_CODE = "INSTRUMENTATION_STATUS_CODE: ";
    private static final String STRING_INSTRUMENTATION_RESULT = "INSTRUMENTATION_RESULT: ";
    private static final String STRING_INSTRUMENTATION_CODE = "INSTRUMENTATION_CODE: ";
    private static final String STRING_IS_TEST = "INSTRUMENTATION_STATUS: test=";
    private static final String STRING_IS_NUMTESTS = "INSTRUMENTATION_STATUS: numtests=";
    // private static final String STRING_IS_CURRENT =
    // "INSTRUMENTATION_STATUS: current=";
    private static final String STRING_IS_STACK = "INSTRUMENTATION_STATUS: stack=";
    // private static final String STRING_TEST_RESULTS_FOR =
    // "Test results for ";
    // private static final String STRING_TIME = "Time: ";
    private static final String STRING_FAILURE_IN = "Failure in ";
    private static final String STRING_ERROR_IN = "Error in ";

    // Default settings constants.
    private static final String FILE_TO_PUSH = "tests.xml";
    private static final String FOLDER_WITH_XML = "../TestDefinitions";
    private static final String APK_TESTS = "../bin/LinkedIn_Android_Tests.apk";
    private static final String PATH_TO_PUSH = "/mnt/sdcard/";
    private static final String FILE_OUTPUT = "output.log";

    // Interface for parse commands output in runCommandInRuntime method.
    public interface RuntimeCommandParser {
        public void parse(String line);
    }

    // PROPERTIES -----------------------------------------------------------
    // Path to tested apk.
    private static String apkTested = null;
    // Path to tests apk.
    private static String apkTests = APK_TESTS;
    // Path to folder with XML's.
    private static String pathToXmlFolder = FOLDER_WITH_XML;
    // File for save output log.
    private static File outputFile = new File(FILE_OUTPUT);
    // FileWriter for outputFile.
    private static FileWriter outputWriter;
    // Id of android device to run tests.
    private static String deviceId = null;
    // List of tests to run.
    private static ArrayList<String> tests = new ArrayList<String>();
    // Flag that need run all tests from pathToXmlFolder.
    private static boolean isAllTests = false;
    // Custom file to push.
    private static String pathToFileWithTests = null;
    // File with tests to run.
    private static File fileWithTests = new File(FILE_TO_PUSH);
    // Report object.
    public static Report report;

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Entry point.
     * 
     * @param args
     *            array of arguments from command line.
     */
    public static void main(String[] args) {
        // Convert parameters from 'args'.
        for (int i = 0; i < args.length; i++) {
            String currentParameter = args[i];
            if (currentParameter.charAt(0) == '-') {
                if (currentParameter.equals(PARAM_DEVICE_ID)) {
                    if (isProperParameter(args[i + 1])) {
                        deviceId = args[++i];
                    } else {
                        System.err.println("Please specify Device ID!");
                        showUsage(1);
                    }
                } else if (currentParameter.equals(PARAM_TESTS)) {
                    for (i += 1; i < args.length; i++) {
                        if (isProperParameter(args[i])) {
                            tests.add(args[i]);
                        } else {
                            i--;
                            break;
                        }
                    }
                    if (tests.size() == 0) {
                        System.err.println("Please specify at least one test!");
                        showUsage(1);
                    }
                } else if (currentParameter.equals(PARAM_ALL_TESTS)) {
                    isAllTests = true;
                } else if (currentParameter.equals(PARAM_CUSTOM_TESTED_APK)) {
                    if (isProperParameter(args[i + 1])) {
                        apkTested = args[++i];
                    } else {
                        System.err.println("Please specify path to tested apk!");
                        showUsage(1);
                    }
                } else if (currentParameter.equals(PARAM_CUSTOM_TESTS_APK)) {
                    if (isProperParameter(args[i + 1])) {
                        apkTests = args[++i];
                    } else {
                        System.err.println("Please specify path to tests apk!");
                        showUsage(1);
                    }
                } else if (currentParameter.equals(PARAM_CUSTOM_FOLDER_WITH_XML)) {
                    if (isProperParameter(args[i + 1])) {
                        pathToXmlFolder = args[++i];
                    } else {
                        System.err.println("Please specify path to directory with xml!");
                        showUsage(1);
                    }
                } else if (currentParameter.equals(PARAM_FILE_WITH_TESTS)) {
                    if (isProperParameter(args[i + 1])) {
                        pathToFileWithTests = args[++i];
                    } else {
                        System.err.println("Please specify path to file with tests!");
                        showUsage(1);
                    }
                } else if (currentParameter.equals(PARAM_OUTPUT_FILE)) {
                    if (isProperParameter(args[i + 1])) {
                        outputFile = new File(args[++i]);
                    } else {
                        System.err.println("Please specify path to file for output logs.");
                        showUsage(1);
                    }
                } else if (currentParameter.equals(PARAM_HELP)
                        || currentParameter.equals(PARAM_HELP_LONG)) {
                    showUsage(0);
                } else {
                    System.err.println("Unknown parameter '" + currentParameter + "'");
                }
                continue;
            }
            System.err.println("Unknown parameter '" + currentParameter + "'");
            showUsage(1);
        }

        // Open or create log file.
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                System.err.println("IOException while trying open output file '"
                        + outputFile.getAbsolutePath() + "'");
                e.printStackTrace();
            }
        }

        // Create outputWriter for log results.
        try {
            outputWriter = new FileWriter(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Cannot create FileWriter for '" + outputFile + "'");
            System.exit(1);
        }
        log("Output of 'runner.jar'. If line starts from:");
        log("    '> ' - output of command without special parser,");
        log("    '* ' - output of instrument parser,");
        log("    '# ' - output of logcat parser");

        // If need run all tests in folder then find all xml's in
        // pathToXmlFolder.
        if (isAllTests) {
            File xmlFolder = new File(pathToXmlFolder);
            File[] listOfFiles = xmlFolder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                File file = listOfFiles[i];
                String fileName = file.getName();
                if (file.isFile() && (fileName.contains(".xml") || fileName.contains(".XML"))) {
                    tests.add(fileName.substring(0, fileName.length() - 4));
                }
            }
        }

        // Check that all XML's exist.
        for (int i = 0; i < tests.size(); i++) {
            String pathToXML = pathToXmlFolder + "/" + tests.get(i) + ".xml";
            File f = new File(pathToXML);
            if (!f.exists()) {
                logError("Cannot find XML in '" + f.getAbsolutePath() + "'");
                showUsage(1);
            }
        }

        // Log parameters.
        StringBuilder builder = new StringBuilder();
        builder.append("Android device ID: ").append(deviceId).append('\n');
        if (apkTested != null)
            builder.append("Tested APK: ").append(apkTested).append('\n');
        builder.append("Tests APK: ").append(apkTests).append('\n');
        if (pathToFileWithTests != null)
            builder.append("File with tests: ").append(pathToFileWithTests).append('\n');
        builder.append("File with output: ").append(outputFile.getAbsolutePath()).append('\n');
        builder.append("Operating system: ").append(System.getProperty("os.name")).append('\n');
        if (tests.size() > 0) {
            builder.append("Tests for run:").append('\n');
            for (int i = 0; i < tests.size(); i++) {
                builder.append("  - ").append(tests.get(i)).append('\n');
            }
        }
        logAndOutput(builder.toString());

        // Create fileWithTests or recreate if already exist.
        if (fileWithTests.exists()) {
            try {
                if (!fileWithTests.delete())
                    throw new IOException("Cannot delete file '" + fileWithTests.getAbsolutePath()
                            + "'!");
            } catch (IOException e) {
                logError(e.getMessage());
                showUsage(1);
            }
        }
        try {
            if (!fileWithTests.createNewFile())
                throw new IOException("Cannot create file '" + fileWithTests.getAbsolutePath()
                        + "'!");
            fileWithTests.setReadable(true, false);
            fileWithTests.setWritable(true, false);
        } catch (IOException e1) {
            logError(e1.getMessage());
            showUsage(1);
        }

        // Prepare file to push.
        if (pathToFileWithTests != null && pathToFileWithTests.length() > 0) {
            // If pathToFileWithTests is specified then copy it content to
            // fileWithTests.
            File file = new File(pathToFileWithTests);
            try {
                FileChannel src = new FileInputStream(file).getChannel();
                FileChannel dest = new FileOutputStream(fileWithTests).getChannel();
                dest.transferFrom(src, 0, src.size());
            } catch (Exception e) {
                logError("Cannot copy content from '" + file.getAbsolutePath() + "' to '"
                        + fileWithTests.getAbsolutePath() + "': " + e.toString());
            }
        } else {
            // Fill file to push from xml's.
            File file;
            builder = new StringBuilder();
            // Fill builder by all tests.
            for (int i = 0; i < tests.size(); i++) {
                // Get content of xml.
                String content = null;
                String pathToXML = pathToXmlFolder + "/" + tests.get(i) + ".xml";
                file = new File(pathToXML);
                try {
                    FileInputStream stream = new FileInputStream(file);
                    try {
                        FileChannel fc = stream.getChannel();
                        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                        content = Charset.defaultCharset().decode(bb).toString();
                    } catch (Exception e) {
                        logError("Cannot get data from file '" + file.getAbsolutePath() + "': "
                                + e.toString());
                    } finally {
                        stream.close();
                    }
                } catch (Exception e) {
                    logError("Cannot create stream from file '" + file.getAbsolutePath() + "': "
                            + e.toString());
                }
                // Remove first and last line from content (except first line in
                // first test and last line in last test).
                if (content != null) {
                    String[] lines = content.split("\n");
                    for (int j = 0; j < lines.length; j++) {
                        String line = lines[j];
                        if (line.length() < 2) {
                            // Skip empty lines.
                            continue;
                        } else if (j == 0 && i == 0) {
                            // It first test. Copy it first line.
                            builder.append(line).append('\n');
                        } else if (j == 0) {
                            // It first line in non-first test. Skip it.
                        } else if (j >= lines.length - 2) {
                            // It prelast line.
                            if (j == lines.length - 1 || lines[j + 1].length() < 2) {
                                // Last line empty (or it last).
                                if (i == tests.size() - 1) {
                                    // It last notempty line in last test. Copy
                                    // it.
                                    builder.append(line).append('\n');
                                }
                            } else {
                                builder.append(line).append('\n');
                                // Last line notempty.
                                line = lines[j + 1];
                                if (i == tests.size() - 1) {
                                    // It last notempty line in last test. Copy
                                    // it.
                                    builder.append(line).append('\n');
                                }
                            }
                            break;
                        } else {
                            builder.append(line).append('\n');
                        }
                    }
                }
            }

            // Write builder to fileWithTests.
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(fileWithTests));
                writer.write(builder.toString());
                writer.flush();
            } catch (Exception e) {
                logError("Cannot create or use BufferedWriter for '"
                        + fileWithTests.getAbsolutePath() + "': " + e.toString());
            } finally {
                try {
                    if (writer != null)
                        writer.close();
                } catch (IOException e) {
                    logError("Cannot close BufferedWriter for '" + fileWithTests.getAbsolutePath()
                            + "': " + e.toString());
                }
            }

            // Log content
            log("Content of file to push:");
            log("****************************");
            log(builder.toString());
            log("****************************");
        }

        // Run script for current OS.
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("win") >= 0)
            runWindowsScript();
        else if (os.indexOf("mac") >= 0)
            runMacScript();
        else if (os.indexOf("nix") >= 0)
            runUnixScript();
        else
            throw new NullPointerException("Unknown OS :" + System.getProperty("os.name"));
    }

    /**
     * Checks that string null or empty or first symbol '-'.
     * 
     * @param string
     *            string for check
     * @return <b>true</b> if string contain proper parameter.
     */
    public static boolean isProperParameter(String str) {
        return !(str == null || str.isEmpty() || str.substring(0, 1).equals("-"));
    }

    /**
     * Shows usage and exit with specified exit status.
     * 
     * @param status
     *            integer value for return to OS.
     */
    private static void showUsage(int status) {
        StringBuilder builder = new StringBuilder();
        builder.append("USAGE:\n");
        builder.append("    runner [ -d <device_id> ] [ -t <list_of_tests>]\n");
        builder.append("OPTIONS\n");
        builder.append("    -d           <device_id>      Android device/emulator ID. You may not specify this if you have only one android.\n");
        builder.append("    -t           <list_of_tests>  List of tests to run.\n");
        builder.append("    --tested_apk <path_to_apk>    Path to tested apk (if not specified then will not be installed).\n");
        builder.append("    --tests_apk  <path_to_apk>    Path to tests apk (default is '")
                .append(APK_TESTS).append("').\n");
        builder.append(
                "    --tests_dir  <path_to_dir>    Path to firectory with xml files (default is '")
                .append(FOLDER_WITH_XML).append("').\n");
        builder.append("    -f           <path_to_file>   Path to file with tests.\n");
        builder.append("    -r           <path_to_log>    Path to file to save output logs (default is 'output.log').\n");
        // builder.append("    --not_build                   Not build tests apk (for debug).\n");
        builder.append("    -h                            Show this message and quit.\n");
        System.out.println(builder.toString());
        System.exit(status);
    }

    private static void runMacScript() {
        runWindowsScript();
    }

    private static void runUnixScript() {
        runWindowsScript();
    }

    // Script for running test for OS windows.
    @SuppressWarnings("deprecation")
    private static void runWindowsScript() {
        int exitValue = Integer.MAX_VALUE;
        // Install tested apk in device.
        if (apkTested != null) {
            exitValue = runCommandInRuntime(createAdbCommand(new String[] { CMD_INSTALL,
                    CMD_INSTALL_R_PARAMETER, apkTested }));
            if (exitValue != 0) {
                logError("Cannot install tested apk on to android device.");
                System.exit(1);
            }
        }

        // Install tests apk in device.
        exitValue = runCommandInRuntime(createAdbCommand(new String[] { CMD_INSTALL,
                CMD_INSTALL_R_PARAMETER, apkTests }));
        if (exitValue != 0) {
            logError("Cannot install tests apk on to android device.");
            System.exit(1);
        }

        // Push to android file with tags.
        exitValue = runCommandInRuntime(createAdbCommand(new String[] { CMD_PUSH,
                fileWithTests.getAbsolutePath(), PATH_TO_PUSH }));
        if (exitValue != 0) {
            logError("Cannot push file '" + fileWithTests.getAbsolutePath()
                    + "' to android device.");
            System.exit(1);
        }

        // Create report.
        report = new Report();

        final ThreadForParseLogCat threadParceLogCat = new ThreadForParseLogCat();
        threadParceLogCat.start();
        try {
            Thread.sleep(1000);// For run parsing in threadParceLogCat.
        } catch (InterruptedException e) {
            logError("Cannot sleep in main thread: " + e.getMessage());
        }

        // Run tests.
        exitValue = runCommandInRuntime(createAdbCommand(CMD_RUN_ALL_TESTS),
                new RuntimeCommandParser() {
                    // Test data.
                    private String currentTestName = new String();
                    private StringBuilder instrumentLog = new StringBuilder();
                    private StringBuilder instrumentStatus = new StringBuilder();
                    // Parser manage variables.
                    private Integer numberOfTests = null;
                    private boolean isSaveNextLines = false;
                    private boolean isCopyInstrumentLog = false;
                    private boolean isCopyInstrumentStatus = false;
                    private int numberOfDoneTests = 0;

                    /**
                     * Returns String like "[##---]" for 2 of 5 passed tests.
                     * 
                     * @return
                     */
                    private String getProgressString() {
                        StringBuilder builder = new StringBuilder("[");
                        int i = 0;
                        while (i < numberOfDoneTests) {
                            builder.append('#');
                            i++;
                        }
                        while (i < numberOfTests) {
                            builder.append('-');
                            i++;
                        }
                        return builder.append(']').toString();
                    }

                    @Override
                    public void parse(String line) {
                        if (line.length() == 0) {
                            // Skip empty lines.
                            return;
                        } else if (line.startsWith(STRING_INSTRUMENTATION_STATUS)) {
                            // Strings starts from "INSTRUMENTATION_STATUS: ".
                            // Stop copy to log output.
                            isSaveNextLines = false;
                            if (line.startsWith(STRING_IS_TEST)) {
                                // Set test name (local).
                                currentTestName = line.substring(STRING_IS_TEST.length());
                            } else if (line.startsWith(STRING_IS_NUMTESTS)) {
                                // Set total count of tests (global).
                                numberOfTests = Runner.convertStringToIntegerSafely(line
                                        .substring(STRING_IS_NUMTESTS.length()));
                            } else if (line.startsWith(STRING_IS_STACK)) {
                                // Start copy error from output to log.
                                isSaveNextLines = true;
                            }
                        } else if (line.startsWith(STRING_INSTRUMENTATION_STATUS_CODE)) {
                            // Strings starts from
                            // "INSTRUMENTATION_STATUS_CODE: ".
                            // Check status code and fill report.
                            Integer statusCode = Runner.convertStringToIntegerSafely(line
                                    .substring(STRING_INSTRUMENTATION_STATUS_CODE.length()));
                            if (statusCode != null) {
                                // If statusCode = 1 then start copy
                                // instrumentLog. Else
                                // - stop copy.
                                if (statusCode == 1) {
                                    instrumentLog = new StringBuilder();
                                    isCopyInstrumentLog = true;
                                    String testName = currentTestName;
                                    if (currentTestName.equals("testActions")
                                            && numberOfDoneTests < tests.size()) {
                                        testName = tests.get(numberOfDoneTests);
                                    }
                                    Runner.logAndOutput("Started test: '" + testName + "' "
                                            + getProgressString());
                                } else {
                                    isCopyInstrumentLog = false;
                                    // Check pass/fail test state.
                                    if (statusCode == 0) {
                                        // Test pass.
                                        String name = threadParceLogCat.getCurrentTestName();
                                        if (name == null)
                                            name = currentTestName;
                                        Runner.report.addPassTest(name,
                                                threadParceLogCat.getCurrentTestId(),
                                                instrumentLog.toString(),
                                                threadParceLogCat.getLogcatForCurrentTest());
                                        numberOfDoneTests++;
                                    } else if (statusCode < 0) {
                                        // Test fail.
                                        String name = threadParceLogCat.getCurrentTestName();
                                        if (name == null)
                                            name = currentTestName;
                                        Runner.report.addFailTest(name,
                                                threadParceLogCat.getCurrentTestId(),
                                                instrumentLog.toString(),
                                                threadParceLogCat.getLogcatForCurrentTest());
                                        numberOfDoneTests++;
                                    }
                                }
                            }
                        } else if (line.startsWith(STRING_INSTRUMENTATION_RESULT)) {
                            instrumentStatus = new StringBuilder();
                            isCopyInstrumentStatus = true;
                        } else if (line.startsWith(STRING_FAILURE_IN)
                                || line.startsWith(STRING_ERROR_IN)) {
                            // Start copy error from output to log.
                            isSaveNextLines = true;
                        } else if (line.startsWith(STRING_INSTRUMENTATION_CODE)) {
                            // Strings starts from "INSTRUMENTATION_CODE: ".
                            isCopyInstrumentStatus = false;
                            instrumentStatus.append(line).append('\n');
                            Runner.report.setInstrumentStatus(instrumentStatus.toString());
                        }
                        // Fill instrumentLog.
                        if (isCopyInstrumentLog) {
                            instrumentLog.append(line).append('\n');
                        }
                        // Fill instrumentStatus.
                        if (isCopyInstrumentStatus) {
                            instrumentStatus.append(line).append('\n');
                        }

                        if (isSaveNextLines)
                            logAndOutput("Fail: " + line);
                        log("* " + line);
                    }
                });
        if (exitValue != 0) {
            logError("Instrument error: exit code = " + exitValue);
        }

        // Kill threadParceLogCat in any case.
        if (!threadParceLogCat.stopParsing()) {
            logError("LogCat thread not started yet!");
        }
        long time = System.currentTimeMillis();
        boolean isThreadStoppedNormal = false;
        while (time + 10000 > System.currentTimeMillis()) {
            if (!threadParceLogCat.isAlive()) {
                isThreadStoppedNormal = true;
                break;
            }
        }
        if (!isThreadStoppedNormal) {
            logError("LogCat thread not closed by timeout. Stop it by calling 'stop()' method.");
            threadParceLogCat.stop();
        }

        report.logAllResults(false);
        exitValue = report.report();
        System.exit(exitValue);
    }

    /**
     * Returns array with first elements "adb" or "adb -d 'deviceId'"
     * 
     * @param commandParameters
     *            parameters to adb command
     * @return adb command as List<String>
     */
    public synchronized static List<String> createAdbCommand(String[] commandParameters) {
        List<String> commandWords = new ArrayList<String>();
        commandWords.add(CMD_ADB);
        if (deviceId != null) {
            commandWords.add(CMD_ADB_S_PARAMETER);
            commandWords.add(deviceId);
        }
        for (int i = 0; i < commandParameters.length; i++) {
            commandWords.add(commandParameters[i]);
        }
        return commandWords;
    }

    /**
     * Runs specified command in runtime and returns exit value.
     * 
     * @param commandArray
     *            command for run as strings split by whitespace
     * @param parser
     *            {@code RuntimeCommandParser} object for parse this command
     *            output.
     * @return exit value.
     */
    public static int runCommandInRuntime(List<String> commandArray, RuntimeCommandParser parser) {
        StringBuilder command = new StringBuilder();
        for (int i = 0; i < commandArray.size(); i++) {
            command.append(commandArray.get(i)).append(' ');
        }
        ProcessBuilder processBuilder = new ProcessBuilder(commandArray);
        Process process = null;

        int exitValue = Integer.MAX_VALUE;
        if (parser == null) {
            logAndOutput("Running command: '" + command.toString() + "':");
        } else {
            logAndOutput("Running command with custom parser: '" + command.toString() + "'.");
        }
        try {
            process = processBuilder.start();
        } catch (Exception e) {
            logError(e.getMessage());
        }
        BufferedReader stdOutput = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));
        String line = null;
        try {
            while (true) {
                // Try get line.
                line = stdOutput.readLine();
                if (line == null)
                    line = stdError.readLine();
                if (line == null)
                    break;

                // Handle each line
                if (parser == null)
                    logAndOutput("> " + line);
                else
                    parser.parse(line);
            }
            exitValue = process.waitFor();
        } catch (Exception e) {
            logError("While running '" + command + "'throws exception: " + e.getMessage());
            e.printStackTrace();
        }
        return exitValue;
    }

    /**
     * Runs specified command in runtime and returns exit value.
     * 
     * @param commandArray
     *            command for run as strings split by whitespace
     * @return exit value.
     */
    public static int runCommandInRuntime(List<String> commandArray) {
        return runCommandInRuntime(commandArray, null);
    }

    /**
     * Logs specified string in outputFile.
     * 
     * @param log
     *            string to log
     */
    public static synchronized void log(String log) {
        try {
            outputWriter.write(log);
            outputWriter.write('\n');
            outputWriter.flush();
        } catch (IOException e) {
            System.err.println("Cannot write to output file string '" + log + "': "
                    + e.getMessage());
        }
    }

    /**
     * Logs specified string in System.out and in outputFile.
     * 
     * @param log
     *            string to log
     */
    public static synchronized void logAndOutput(String log) {
        try {
            outputWriter.write(log);
            outputWriter.write('\n');
            outputWriter.flush();
        } catch (IOException e) {
            System.err.println("Cannot write to output file string '" + log + "': "
                    + e.getMessage());
        }
        System.out.println(log);
    }

    /**
     * Logs specified string in System.err and in outputFile.
     * 
     * @param log
     *            string to log
     */
    public static synchronized void logError(String log) {
        try {
            outputWriter.write("Error: " + log + "\n");
            outputWriter.flush();
        } catch (IOException e) {
            System.err.println("Cannot write to output file string '" + log + "': "
                    + e.getMessage());
        }
        System.err.println(log);
    }

    /**
     * Cuts from string all non-digit symbols and tries to convert it in Integer
     * value.
     * 
     * @param string
     *            string with digits to convert
     * @return number or <b>null</b> if digits not found.
     */
    public static synchronized Integer convertStringToIntegerSafely(String string) {
        if (string != null && string.length() > 0) {
            string = string.replaceAll("[^0123456789-]", "");
            try {
                return Integer.parseInt(string);
            } catch (Exception e) {
                logError("Cannot find digits in string '" + string + "': " + e.getMessage());
            }
        }
        return null;
    }
}
