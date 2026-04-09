 

import java.util.Date;

public class TransactionHandler {

    public static final double RATE = 0.015;

    public static String receiptReturn(double offset, String paymentMethod) {
        double cost = offset * RATE;
        String currentDateTime = new Date().toString();

        String receipt = "------------------------Offset Receipt---------------------------------\n"
                + "Date: " + currentDateTime + "\n"
                + "Offset: " + offset + " kg\n"
                + "Cost: $" + cost + "\n"
                + "Payment Method: " + paymentMethod + "\n"
                + "----------> PAYMENT IS SUCCESSFUL";

        return receipt;
    }
}
