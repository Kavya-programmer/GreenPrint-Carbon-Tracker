 

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.ArrayList;

public class OffsetTab {

    private FootprintTracker tracker;
    private ArrayList<String> history = new ArrayList<String>();

    public VBox root;

    public OffsetTab(FootprintTracker tracker) {
        this.tracker = tracker;
        buildLayout();
    }

    private void buildLayout() {

        Label totalLabel = new Label("Total CO2: " + String.format("%.2f", tracker.getTotalEmissions()) + " kg");

        Label amountLabel = new Label("Offset Amount (kg):");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter kg to offset");

        Label paymentLabel = new Label("Payment Method:");
        ComboBox<String> paymentBox = new ComboBox<String>();
        paymentBox.getItems().addAll("Credit Card", "Digital Wallet", "Campus Card");
        paymentBox.setPromptText("Select payment method");

        Button purchaseBtn = new Button("Purchase Offset");

        Label receiptLabel = new Label();
        Label statusLabel = new Label();

        Label historyTitle = new Label("--- Offset History ---");
        VBox historyBox = new VBox(5);

        purchaseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (amountField.getText().trim().isEmpty()) {
                    statusLabel.setText("Please enter an amount.");
                    statusLabel.setTextFill(Color.RED);
                    return;
                }
                if (paymentBox.getValue() == null) {
                    statusLabel.setText("Please select a payment method.");
                    statusLabel.setTextFill(Color.RED);
                    return;
                }

                double amount;
                try {
                    amount = Double.parseDouble(amountField.getText().trim());
                    if (amount <= 0) {
                        statusLabel.setText("Please enter a number greater than 0.");
                        statusLabel.setTextFill(Color.RED);
                        return;
                    }
                } catch (NumberFormatException e) {
                    statusLabel.setText("Please enter a valid number.");
                    statusLabel.setTextFill(Color.RED);
                    return;
                }

                purchaseBtn.setDisable(true);
                statusLabel.setText("Processing...");
                statusLabel.setTextFill(Color.ORANGE);

                final double finalAmount = amount;
                final String finalPayment = paymentBox.getValue();

                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        String receipt = TransactionHandler.receiptReturn(finalAmount, finalPayment);
                        receiptLabel.setText(receipt);
                        statusLabel.setText("Payment Successful!");
                        statusLabel.setTextFill(Color.GREEN);
                        historyBox.getChildren().add(new Label("- " + finalAmount + " kg via " + finalPayment));
                        GreenPrintLogger.logOperations("OFFSET_PURCHASED", "Amount: " + finalAmount + " kg | Payment: " + finalPayment);
                        totalLabel.setText("Total CO2: " + String.format("%.2f", tracker.getTotalEmissions()) + " kg");
                        purchaseBtn.setDisable(false);
                        amountField.clear();
                        paymentBox.setValue(null);
                    }
                });

                delay.play();
            }
        });

        root = new VBox(10,
            totalLabel,
            amountLabel, amountField,
            paymentLabel, paymentBox,
            purchaseBtn,
            statusLabel,
            new Label("Receipt:"),
            receiptLabel,
            historyTitle,
            historyBox
        );

        root.setPadding(new Insets(15));
    }
}
