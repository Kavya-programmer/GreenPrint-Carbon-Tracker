
public class EnergyEmission extends EmissionSource {

    private double kWhUsed;
    private String energySource;

    public EnergyEmission(String sourceID, String date, String userName,
                           double kWhUsed, String energySource) {
        super(sourceID, "Energy", date, userName);
        this.kWhUsed = kWhUsed;
        this.energySource = energySource;
    }

    @Override
    public double calculateEmission() {
        double factor;
        if (energySource.equals("Grid")) {
            factor = 0.42;
        } else if (energySource.equals("Solar")) {
            factor = 0.05;
        } else if (energySource.equals("Wind")) {
            factor = 0.02;
        } else {
            factor = 0.4;
        }
        return kWhUsed * factor;
    }

    @Override
    public String toString() {
        double emission = Math.round(calculateEmission() * 100.0) / 100.0;
        return super.toString() + ", kWh Used: " + kWhUsed + ", Source: " + energySource + ", Emission: " + emission + " kg CO2e";
    }

    public double getkWhUsed() { return kWhUsed; }
    public void setkWhUsed(double kWhUsed) { this.kWhUsed = kWhUsed; }
    public String getEnergySource() { return energySource; }
    public void setEnergySource(String energySource) { this.energySource = energySource; }
}
