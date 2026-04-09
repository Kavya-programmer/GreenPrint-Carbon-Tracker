
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class InputTab {

    private FootprintTracker tracker;
    private DashboardTab dashboardTab;

    private TextField sourceIDText = new TextField();
    private Label validateLabel = new Label();
    private ComboBox<String> typeDropBox = new ComboBox<String>();
    private TextField userIDText = new TextField();
    private TextField dateText = new TextField();
    private Button addButton = new Button("Add Entry");

    private TextField distancekmText = new TextField();
    private ComboBox<String> modetransportDropBox = new ComboBox<String>();

    private TextField kWhText = new TextField();
    private ComboBox<String> sourceDropBox = new ComboBox<String>();

    private TextField mealsnumText = new TextField();
    private ComboBox<String> mealDropBox = new ComboBox<String>();

    private VBox sourceFields = new VBox(8);
    private Label statusLabel = new Label();

    private TextField searchField = new TextField();
    private VBox searchResult = new VBox(5);

    private VBox root;

    public InputTab(FootprintTracker tracker, DashboardTab dashboardTab) {
        this.tracker = tracker;
        this.dashboardTab = dashboardTab;
        constructLayout();
    }

    private void constructLayout() {

        Label idLabel = new Label("Source ID (e.g. T-001):");
        sourceIDText.setPromptText("Enter Source ID");

        sourceIDText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (InputValidate.validSourceID(newValue)) {
                    validateLabel.setText("Valid");
                    validateLabel.setTextFill(Color.GREEN);
                } else {
                    validateLabel.setText("Invalid");
                    validateLabel.setTextFill(Color.RED);
                }
            }
        });

        HBox idRow = new HBox(10);
        idRow.getChildren().addAll(sourceIDText, validateLabel);

        Label sourcesLabel = new Label("Select Source of Emission:");
        typeDropBox.getItems().addAll("Transportation", "Energy", "Food");
        typeDropBox.setPromptText("Select Source");

        typeDropBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sourceFieldAction(typeDropBox.getValue());
            }
        });

        Label nameLabel = new Label("Name:");
        userIDText.setPromptText("Enter Name");

        Label dateLabel = new Label("Date (YYYY-MM-DD):");
        dateText.setPromptText("Enter Date");

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addEntry();
            }
        });

        VBox addSection = new VBox(10);
        addSection.getChildren().addAll(
                idLabel, idRow,
                sourcesLabel, typeDropBox,
                nameLabel, userIDText,
                dateLabel, dateText,
                sourceFields,
                addButton, statusLabel
        );

        Label searchLabel = new Label("Search by Name:");
        searchLabel.setFont(new Font("Arial", 12));
        searchField.setPromptText("Enter Name");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchEntry();
            }
        });

        VBox searchSection = new VBox(8);
        searchSection.setPadding(new Insets(20));
        searchSection.getChildren().addAll(searchLabel, searchField, searchButton, searchResult);

        root = new VBox(10);
        root.setPadding(new Insets(30));
        root.getChildren().addAll(addSection, searchSection);
    }

    private void sourceFieldAction(String source) {
        sourceFields.getChildren().clear();

        if (source == null) return;

        if (source.equals("Transportation")) {
            distancekmText.setPromptText("Enter Distance in km");
            modetransportDropBox.getItems().clear();
            modetransportDropBox.getItems().addAll("Car", "Bus", "Train", "Bicycle");
            modetransportDropBox.setPromptText("Select Mode of Transport");
            sourceFields.getChildren().addAll(
                    new Label("Distance (km):"), distancekmText,
                    new Label("Mode of Transport:"), modetransportDropBox
            );

        } else if (source.equals("Energy")) {
            kWhText.setPromptText("Enter kWh");
            sourceDropBox.getItems().clear();
            sourceDropBox.getItems().addAll("Grid", "Solar", "Wind");
            sourceDropBox.setPromptText("Select Source of Energy");
            sourceFields.getChildren().addAll(
                    new Label("kWh:"), kWhText,
                    new Label("Source of Energy:"), sourceDropBox
            );

        } else if (source.equals("Food")) {
            mealsnumText.setPromptText("Enter number of meals");
            mealDropBox.getItems().clear();
            mealDropBox.getItems().addAll("Vegan", "Vegetarian", "Poultry", "Beef");
            mealDropBox.setPromptText("Select Meal Type");
            sourceFields.getChildren().addAll(
                    new Label("Number of Meals:"), mealsnumText,
                    new Label("Meal Type:"), mealDropBox
            );
        }
    }

    private void addEntry() {
        String sourceID = sourceIDText.getText().trim();
        String type = typeDropBox.getValue();
        String username = userIDText.getText().trim();
        String date = dateText.getText().trim();

        if (!InputValidate.validSourceID(sourceID)) {
            showError("Source ID is invalid. Format: T-001");
            return;
        }
        if (type == null) {
            showError("No Type Selected");
            return;
        }
        if (username.isEmpty()) {
            showError("Name is not entered");
            return;
        }
        if (date.isEmpty()) {
            showError("Date is empty");
            return;
        }

        EmissionSource entry = null;

        try {
            if (type.equals("Transportation")) {
                if (modetransportDropBox.getValue() == null || distancekmText.getText().trim().isEmpty()) {
                    showError("Please enter values for the transport entry");
                    return;
                }
                double distance = Double.parseDouble(distancekmText.getText().trim());
                String mode = modetransportDropBox.getValue();
                entry = new TransportationEmission(sourceID, date, username, distance, mode);

            } else if (type.equals("Energy")) {
                if (sourceDropBox.getValue() == null || kWhText.getText().trim().isEmpty()) {
                    showError("Please enter values for the energy entry");
                    return;
                }
                double kwh = Double.parseDouble(kWhText.getText().trim());
                String source = sourceDropBox.getValue();
                entry = new EnergyEmission(sourceID, date, username, kwh, source);

            } else if (type.equals("Food")) {
                if (mealDropBox.getValue() == null || mealsnumText.getText().trim().isEmpty()) {
                    showError("Please enter values for the food entry");
                    return;
                }
                int mealsnum = Integer.parseInt(mealsnumText.getText().trim());
                String mealtype = mealDropBox.getValue();
                entry = new FoodEmission(sourceID, date, username, mealsnum, mealtype);
            }

        } catch (NumberFormatException e) {
            showError("Enter valid numbers in numeric fields");
            return;
        }

        tracker.addEntry(entry);
        GreenPrintLogger.logOperations("ENTRY_ADDED", "ID: " + sourceID + " | User: " + username);
        dashboardTab.refresh();
        showSuccess("Entry added! Emission: " + String.format("%.2f", entry.calculateEmission()) + " kg CO2");
        clearFields();
    }

    private void searchEntry() {
        searchResult.getChildren().clear();
        String name = searchField.getText().trim();

        if (name.isEmpty()) {
            searchResult.getChildren().add(new Label("Please enter a name to search."));
            return;
        }

        boolean present = false;
        for (EmissionSource e : tracker.getEntries()) {
            if (e.getUserName().equalsIgnoreCase(name)) {
                present = true;
                searchResult.getChildren().add(new Label(e.toString()));
            }
        }

        if (!present) {
            searchResult.getChildren().add(new Label(name + " not found."));
        }
    }

    private void showError(String text) {
        statusLabel.setText(text);
        statusLabel.setTextFill(Color.RED);
    }

    private void showSuccess(String text) {
        statusLabel.setText(text);
        statusLabel.setTextFill(Color.GREEN);
    }

    private void clearFields() {
        sourceIDText.clear();
        typeDropBox.setValue(null);
        userIDText.clear();
        dateText.clear();
        distancekmText.clear();
        kWhText.clear();
        mealsnumText.clear();
        sourceFields.getChildren().clear();
    }

    public VBox getContent() {
        return root;
    }
}