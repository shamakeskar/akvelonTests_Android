import java.util.ArrayList;

public class Report {
    // CONSTANTS ------------------------------------------------------------
    private static final String STRING_OK = "OK (";
    private static final String STRING_FAILURES = "FAILURES!!!";

    // PROPERTIES -----------------------------------------------------------
    // Global instrument status.
    public String instrumentStatus = null;
    // Counters.
    private int countTests = 0, countPass = 0, countFail = 0;
    // Array of results.
    private ArrayList<TestResult> results = new ArrayList<TestResult>();

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Report current state.
     */
    public void report() {
        StringBuilder builder;
        // Check report.
        if (instrumentStatus == null) {
            Runner.logError("Instrument status is null!!!");
        }
        if (countTests != countPass + countFail) {
            builder = new StringBuilder().append("Report wrong: countTests=").append(countTests)
                    .append(", countPass=").append(countPass).append(", countFail=")
                    .append(countFail).append("!");
            Runner.logError(builder.toString());
        }
        if (countPass > results.size()) {
            builder = new StringBuilder().append("Report wrong: countPass = ").append(countPass)
                    .append(" but have only  ").append(results.size()).append(" results!");
            Runner.logError(builder.toString());
        }
        if (countFail > results.size()) {
            builder = new StringBuilder().append("Report wrong: countFail = ").append(countFail)
                    .append(" but have only  ").append(results.size()).append(" results!");
            Runner.logError(builder.toString());
        }

        // Log total.
        builder = new StringBuilder();
        if (countFail > 0) {
            builder.append("<<<<<<<<<< Fails ").append(countFail).append(" test(s) of ")
                    .append(countTests).append(". >>>>>>>>>>");
        } else {
            builder.append("<<<<<<<<<< Passed ").append(countTests).append(" test(s). >>>>>>>>>>");
        }
        Runner.logAndOutput(builder.append('\n').toString());

        // Log instruments status.
        builder = new StringBuilder("Instrument status: ").append(getInstrumentsStatusOneLine())
                .append('\n');
        Runner.logAndOutput(builder.toString());

        // Load passes.
        builder = new StringBuilder("PASSED (").append(countPass).append("):\n");
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).isPassed)
                builder.append(results.get(i).name).append('\n');
        }
        Runner.logAndOutput(builder.append('\n').toString());

        // Load fails if need.
        if (countFail > 0) {
            builder = new StringBuilder("FAILED (").append(countFail).append("):\n");
            for (int i = 0; i < results.size(); i++) {
                TestResult test = results.get(i);
                if (!test.isPassed) {
                    builder.append(test.name).append(": ").append(test.getCause()).append('\n');
                }
            }
            Runner.logAndOutput(builder.append('\n').toString());
        }
    }

    /**
     * Sets instrumentStatus
     * 
     * @param instrumentStatus
     *            line with instrument status
     */
    public synchronized void setInstrumentStatus(String instrumentStatus) {
        this.instrumentStatus = instrumentStatus;
    }

    /**
     * Returns instruments status in one line.
     * 
     * @return instruments status in one line.
     */
    public String getInstrumentsStatusOneLine() {
        if (instrumentStatus != null && instrumentStatus.length() > 0) {
            String[] lines = instrumentStatus.split("\n");
            boolean isNextStringResult = false;
            for (String string : lines) {
                if (isNextStringResult || string.startsWith(STRING_OK))
                    return string;
                else if (string.startsWith(STRING_FAILURES))
                    isNextStringResult = true;
            }
        }
        return instrumentStatus;
    }

    /**
     * Adds one more passes test.
     * 
     * @param name
     *            name of test
     * @param id
     *            id of test
     * @param instrumentLog
     *            log from 'instruments' command
     * @param logcat
     *            logcat for this test
     * @return TestResult object that has been added.
     */
    public synchronized TestResult addPassTest(String name, String id, String instrumentLog,
            String logcat) {
        TestResult test = new TestResult(name, id, instrumentLog, logcat, true);
        results.add(test);
        countTests++;
        countPass++;
        return test;
    }

    /**
     * Adds one more failed test.
     * 
     * @param name
     *            name of test
     * @param id
     *            id of test
     * @param instrumentLog
     *            log from 'instruments' command
     * @param logcat
     *            logcat for this test
     * @return TestResult object that has been added.
     */
    public synchronized TestResult addFailTest(String name, String id, String instrumentLog,
            String logcat) {
        TestResult test = new TestResult(name, id, instrumentLog, logcat, false);
        results.add(test);
        countTests++;
        countFail++;
        return test;
    }

    /**
     * Logs all data in results.
     * 
     * @param isLogInOutput
     *            if <b>true</b> then log in output too.
     */
    public void logAllResults(boolean isLogInOutput) {
        if (isLogInOutput) {
            Runner.logAndOutput("All results:");
            for (int i = 0; i < results.size(); i++) {
                results.get(i).logAllInfo(true);
            }
        } else {
            Runner.log("All results:");
            for (int i = 0; i < results.size(); i++) {
                results.get(i).logAllInfo(false);
            }
        }
    }

    /**
     * Class for save data about test running.
     * 
     * @author alexander.makarov
     * @created Jan 28, 2013 11:57:25 AM
     */
    public class TestResult {
        final String name;
        final String id;
        final boolean isPassed;
        final String instrumentLog;
        final String logcat;

        /**
         * Constructor for TestResult.
         * 
         * @param name
         *            name of test
         * @param id
         *            ID of test
         * @param instrumentLog
         *            log from 'instruments' command
         * @param isPassed
         *            flag that it test passed.
         */
        public TestResult(String name, String id, String instrumentLog, String logcat,
                boolean isPassed) {
            this.name = name;
            this.id = id;
            this.instrumentLog = instrumentLog;
            this.logcat = logcat;
            this.isPassed = isPassed;
        }

        /**
         * Returns one line with cause of fail.
         * 
         * @return one line with cause of fail.
         */
        public String getCause() {
            String cause = null;
            if (!isPassed && instrumentLog != null && instrumentLog.length() > 0) {
                String[] lines = instrumentLog.split("\n");
                boolean isNextLineCause = false;
                for (String string : lines) {
                    if (isNextLineCause)
                        return string;
                    isNextLineCause = string.startsWith("Failure in ");
                }
            }
            return cause;
        }

        /**
         * Logs all info via Runner.log.
         * 
         * @param isLogInOutput
         *            if <b>true</b> then log in output too.
         */
        public void logAllInfo(boolean isLogInOutput) {
            StringBuilder builder = new StringBuilder();
            builder.append("NAME: ").append(name).append('\n');
            builder.append("ID: ").append(id).append('\n');
            builder.append("IS PASS: ").append(isPassed).append('\n');
            builder.append("INSTRUMENT LOG: ").append(instrumentLog).append('\n');
            builder.append("LOGCAT: ").append(logcat)
                    .append("\n-----------------------------------");
            if (isLogInOutput)
                Runner.logAndOutput(builder.toString());
            else
                Runner.log(builder.toString());
        }
    }
}
