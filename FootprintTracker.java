
import java.util.ArrayList;

public class FootprintTracker {

    private String trackerName;
    private ArrayList<EmissionSource> entries;

    public FootprintTracker() {
        this.trackerName = "RIT GreenPrint 2026";
        entries = new ArrayList<>();
    }

    public void addEntry(EmissionSource entry) {
        entries.add(entry);
    }

    // Returns the full list of entries — needed by the GUI tabs
    public ArrayList<EmissionSource> getEntries() {
        return entries;
    }

    public double getTotalEmissions() {
        double total = 0;
        for (EmissionSource e : entries) {
            total += e.calculateEmission();
        }
        return total;
    }

    public double getTotalEmissionsForUser(String userName) {
        double total = 0;
        for (EmissionSource e : entries) {
            if (e.getUserName().equalsIgnoreCase(userName)) {
                total += e.calculateEmission();
            }
        }
        return total;
    }

    // Returns the name of the user with the highest total emissions
    // Used by DashboardTab to show the top emitter in the summary bar
    public String getUserWithHighestFootprint() {
        if (entries.isEmpty()) {
            return "";
        }

        String topUser = "";
        double highest = 0;

        for (EmissionSource e : entries) {
            String userName = e.getUserName();
            double userTotal = getTotalEmissionsForUser(userName);
            if (userTotal > highest) {
                highest = userTotal;
                topUser = userName;
            }
        }
        return topUser;
    }

    public void generateDailyReport() {
        System.out.println("=== RIT GreenPrint 2026 -- Daily Report ===");
        System.out.println();

        ArrayList<String> users = new ArrayList<>();

        for (EmissionSource e : entries) {
            boolean userPresent = false;
            for (String user : users) {
                if (e.getUserName().equals(user)) {
                    userPresent = true;
                    break;
                }
            }
            if (!userPresent) {
                String currentName = e.getUserName();
                System.out.println("---> User: " + currentName);
                users.add(currentName);

                String result = "";
                for (EmissionSource entry : entries) {
                    if (entry.getUserName().equals(currentName)) {
                        result += " " + entry.toString() + "\n";
                    }
                }
                System.out.println(result);
                System.out.println("Subtotal: " + getTotalEmissionsForUser(currentName));
                System.out.println("-------------------------------------------");
            }
        }
        System.out.println("Grand Total: " + getTotalEmissions());
    }
}
