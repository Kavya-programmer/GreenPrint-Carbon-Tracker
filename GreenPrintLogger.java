 

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class GreenPrintLogger {

    public static void logOperations(String type, String details) {
        try {
            // Open file in append mode — never deletes old logs
            FileWriter file = new FileWriter("greenprint_log.txt", true);

            // Get current date and time using Date — no extra classes needed
            String now = new Date().toString();

            // Build the log line
            String logLine = now + " | " + type + " | " + details + "\n";

            // Write and close
            file.write(logLine);
            file.close();

        } catch (IOException e) {
            System.out.println("Error writing log");
        }
    }
}
