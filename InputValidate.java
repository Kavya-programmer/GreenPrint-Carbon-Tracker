 

public class InputValidate {

    public static boolean validSourceID(String ID) {

        // Check 1: must not be null or empty
        if (ID == null || ID.isEmpty()) {
            return false;
        }

        // Check 2: must be exactly 5 characters
        if (ID.length() != 5) {
            return false;
        }

        // Check 3: first character must be uppercase letter A to Z
        if (ID.charAt(0) < 'A' || ID.charAt(0) > 'Z') {
            return false;
        }

        // Check 4: second character must be a dash
        if (ID.charAt(1) != '-') {
            return false;
        }

        // Check 5: last 3 characters must all be digits 0 to 9
        for (int i = 2; i <= 4; i++) {
            char value = ID.charAt(i);
            if (value < '0' || value > '9') {
                return false;
            }
        }

        return true;
    }
}
