 

public class TransportationEmission extends EmissionSource {

    private double distanceKm;
    private String transportMode;

    public TransportationEmission(String sourceID, String date, String userName,
                                   double distanceKm, String transportMode) {
        super(sourceID, "Transportation", date, userName);
        this.distanceKm = distanceKm;
        this.transportMode = transportMode;
    }

    @Override
    public double calculateEmission() {
        double factor;
        if (transportMode.equals("Car")) {
            factor = 0.21;
        } else if (transportMode.equals("Bus")) {
            factor = 0.089;
        } else if (transportMode.equals("Train")) {
            factor = 0.041;
        } else if (transportMode.equals("Bicycle")) {
            factor = 0.0;
        } else {
            factor = 0.2;
        }
        return distanceKm * factor;
    }

    @Override
    public String toString() {
        double emission = Math.round(calculateEmission() * 100.0) / 100.0;
        return super.toString() + ", Distance: " + distanceKm + " km, Mode: " + transportMode + ", Emission: " + emission + " kg CO2e";
    }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
    public String getTransportMode() { return transportMode; }
    public void setTransportMode(String transportMode) { this.transportMode = transportMode; }
}
