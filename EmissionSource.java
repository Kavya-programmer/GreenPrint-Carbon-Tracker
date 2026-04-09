
public abstract class EmissionSource {

    private String sourceID;
    private String category;
    private String date;
    private String userName;

    public EmissionSource(String sourceID, String category, String date, String userName) {
        this.sourceID = sourceID;
        this.category = category;
        this.date = date;
        this.userName = userName;
    }

    public String getSourceID() { return sourceID; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getUserName() { return userName; }

    public void setSourceID(String sourceID) { this.sourceID = sourceID; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(String date) { this.date = date; }
    public void setUserName(String userName) { this.userName = userName; }

    public abstract double calculateEmission();

    @Override
    public String toString() {
        return "Source ID: " + sourceID +
               ", Category: " + category +
               ", Date: " + date +
               ", User: " + userName;
    }
}
