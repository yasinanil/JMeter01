package utilities;

import org.junit.Assert;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

public class JMeterUtils {


    public static void RunJMX(String JMXFile) {

        String projectPath = System.getProperty("user.dir");
        String reportPath = "Report_" + LocalDateTime.now().toString().replaceAll("[^0-9]", "");
        String csvFilePath = projectPath + "/jmeter/" + reportPath + "/Outputs.csv";  // JMeter output file
        String jmeterCommand = "\"" + projectPath + "/jmeter/apache-jmeter-5.6.3/bin/jmeter.bat\""
                + " -n -t \"" + projectPath + "/" + JMXFile + ".jmx\""
                + " -l \"" + csvFilePath + "\" -e -o \"" + projectPath + "/jmeter/" + reportPath + "\"";

        // Run the JMeter command

        System.out.println("Running JMeter command: " + jmeterCommand);
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(jmeterCommand);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Wait for the process to finish
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("JMeter execution completed with exit code: " + process.exitValue());

        // Read and assert results from the CSV file
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csvFilePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line;
        boolean isFirstLine = true;

        while (true) {
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Skip the first line (header)
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }

            // Process the CSV line
            String[] columns = line.split(",");
            if (columns.length < 12) {
                System.err.println("Malformed line: " + line);
                continue;
            }

            // Validate 'success' column and log failure messages
            String success = columns[7].trim(); // 'success' column
            String failureMessage = columns[8].trim(); // 'failureMessage' column

            if ("FALSE".equalsIgnoreCase(success)) {
                String errorMessage = "Assertion failed for line: " + line;
                if (!failureMessage.isEmpty()) {
                    System.err.println("Failure message: " + failureMessage);
                    Assert.fail(errorMessage);
                }
            }
        }
        System.out.println("All tests passed.");
    }


}
