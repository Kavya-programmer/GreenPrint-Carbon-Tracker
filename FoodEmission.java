
public class FoodEmission extends EmissionSource {

    private String mealType;
    private int numberOfMeals;

    public FoodEmission(String sourceID, String date, String userName,
                         int numberOfMeals, String mealType) {
        super(sourceID, "Food", date, userName);
        this.mealType = mealType;
        this.numberOfMeals = numberOfMeals;
    }

    @Override
    public double calculateEmission() {
        double factor;
        if (mealType.equals("Vegan")) {
            factor = 0.5;
        } else if (mealType.equals("Vegetarian")) {
            factor = 1.1;
        } else if (mealType.equals("Poultry")) {
            factor = 1.7;
        } else if (mealType.equals("Beef")) {
            factor = 3.3;
        } else {
            factor = 1.0;
        }
        return numberOfMeals * factor;
    }

    @Override
    public String toString() {
        double emission = Math.round(calculateEmission() * 100.0) / 100.0;
        return super.toString() + ", Meal Type: " + mealType + ", Meals: " + numberOfMeals + ", Emission: " + emission + " kg CO2e";
    }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public int getNumberOfMeals() { return numberOfMeals; }
    public void setNumberOfMeals(int numberOfMeals) { this.numberOfMeals = numberOfMeals; }
}
