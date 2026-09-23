import auto.Automobile;
import auto.InvalidValueException;
import auto.NullParameterException;

public class Main {

    // ---------------------------------------
    // Simple self-grading test harness
    // ---------------------------------------
    private static int pointsEarned = 0;
    private static int pointsPossible = 0;

    public static void main(String[] args) {
        System.out.println("=== Automobile Self-Grading Test Suite ===");

        // Known-valid VINs that pass format + check digit
        // Example commonly used in VIN validation demos:
        final String validVin1 = "1M8GDM9AXKP042788"; // check digit = X
        final String validVin2 = "5GZCZ43D13S812715"; // another commonly used valid VIN example

        // ----------------------------
        // POSITIVE TESTS
        // ----------------------------
        System.out.println("\n--- Positive Tests ---");

        grade("Create Automobile (valid inputs)", 5, () -> {
            Automobile a1 = new Automobile(validVin1, "Honda", "Civic", 18000);
            require(a1.getVin().equals(validVin1), "VIN mismatch");
            require(a1.getMake().equals("Honda"), "Make mismatch");
            require(a1.getModel().equals("Civic"), "Model mismatch");
            require(a1.getPrice() == 18000, "Price mismatch");
        });

        grade("Update price via setPrice()", 4, () -> {
            Automobile a1 = new Automobile(validVin1, "Honda", "Civic", 18000);
            a1.setPrice(17500);
            require(a1.getPrice() == 17500, "Price was not updated correctly");
        });

        grade("Equality: same VIN => equals() true", 5, () -> {
            Automobile a1 = new Automobile(validVin1, "Honda", "Civic", 18000);
            Automobile a2 = new Automobile(validVin1, "Toyota", "Corolla", 22000);
            require(a1.equals(a2), "Automobiles with same VIN should be equal");
        });

        grade("Equality: different VIN => equals() false", 5, () -> {
            Automobile a1 = new Automobile(validVin1, "Honda", "Civic", 18000);
            Automobile a3 = new Automobile(validVin2, "Ford", "Escape", 24000);
            require(!a1.equals(a3), "Automobiles with different VINs should not be equal");
        });

        grade("hashCode: same VIN => same hashCode", 4, () -> {
            Automobile a1 = new Automobile(validVin1, "Honda", "Civic", 18000);
            Automobile a2 = new Automobile(validVin1, "Toyota", "Corolla", 22000);
            require(a1.hashCode() == a2.hashCode(), "hashCodes should match for same VIN");
        });

        // ----------------------------
        // NEGATIVE TESTS - NULLS
        // ----------------------------
        System.out.println("\n--- Negative Tests (NullParameterException expected) ---");

        gradeExpectNull("Null VIN", 4, () ->
                new Automobile(null, "Honda", "Civic", 10000));

        gradeExpectNull("Null make", 4, () ->
                new Automobile(validVin1, null, "Civic", 10000));

        gradeExpectNull("Null model", 4, () ->
                new Automobile(validVin1, "Honda", null, 10000));

        // ----------------------------
        // NEGATIVE TESTS - INVALID VALUES
        // ----------------------------
        System.out.println("\n--- Negative Tests (InvalidValueException expected) ---");

        gradeExpectInvalid("Blank make", 3, () ->
                new Automobile(validVin1, "   ", "Civic", 10000));

        gradeExpectInvalid("Blank model", 3, () ->
                new Automobile(validVin1, "Honda", "   ", 10000));

        gradeExpectInvalid("Negative price in constructor", 4, () ->
                new Automobile(validVin1, "Honda", "Civic", -1));

        gradeExpectInvalid("VIN too short", 4, () ->
                new Automobile("123", "Honda", "Civic", 10000));

        gradeExpectInvalid("VIN contains illegal character 'I'", 4, () ->
                new Automobile("1M8GDM9AIKP042788", "Honda", "Civic", 10000));

        gradeExpectInvalid("VIN incorrect check digit", 6, () ->
                new Automobile("1M8GDM9A1KP042788", "Honda", "Civic", 10000));

        grade("setPrice negative throws InvalidValueException", 4, () -> {
            Automobile a = new Automobile(validVin1, "Honda", "Civic", 10000);
            expectInvalid(() -> {
                a.setPrice(-500);
                return null;
            });
        });

        // ----------------------------
        // FINAL SCORE
        // ----------------------------
        System.out.println("\n=== Score ===");
        System.out.println("Points Earned:   " + pointsEarned);
        System.out.println("Points Possible: " + pointsPossible);

        System.out.println("=== Done ===");
    }

    // -----------------------------------------------------------------
    // Harness utilities
    // -----------------------------------------------------------------

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    @FunctionalInterface
    private interface ThrowingFactory<T> {
        T create() throws Exception;
    }

    private static void grade(String testName, int pts, ThrowingRunnable test) {
        pointsPossible += pts;
        try {
            test.run();
            pointsEarned += pts;
            System.out.println("PASS (" + pts + " pts): " + testName);
        } catch (Exception ex) {
            System.out.println("FAIL (" + pts + " pts): " + testName
                    + " -> " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }

    private static void gradeExpectNull(String testName, int pts, ThrowingFactory<Automobile> factory) {
        pointsPossible += pts;
        try {
            factory.create();
            System.out.println("FAIL (" + pts + " pts): " + testName + " -> expected NullParameterException, got none");
        } catch (NullParameterException ex) {
            pointsEarned += pts;
            System.out.println("PASS (" + pts + " pts): " + testName + " -> NullParameterException");
        } catch (InvalidValueException ex) {
            System.out.println("FAIL (" + pts + " pts): " + testName
                    + " -> expected NullParameterException, got InvalidValueException: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("FAIL (" + pts + " pts): " + testName
                    + " -> unexpected exception: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }

    private static void gradeExpectInvalid(String testName, int pts, ThrowingFactory<Automobile> factory) {
        pointsPossible += pts;
        try {
            factory.create();
            System.out.println("FAIL (" + pts + " pts): " + testName + " -> expected InvalidValueException, got none");
        } catch (InvalidValueException ex) {
            pointsEarned += pts;
            System.out.println("PASS (" + pts + " pts): " + testName + " -> InvalidValueException");
        } catch (NullParameterException ex) {
            System.out.println("FAIL (" + pts + " pts): " + testName
                    + " -> expected InvalidValueException, got NullParameterException: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("FAIL (" + pts + " pts): " + testName
                    + " -> unexpected exception: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }

    private static void require(boolean condition, String message) throws Exception {
        if (!condition) {
            throw new Exception(message);
        }
    }

    private static void expectInvalid(ThrowingFactory<Void> action) throws Exception {
        try {
            action.create();
            throw new Exception("Expected InvalidValueException, but no exception was thrown");
        } catch (InvalidValueException ex) {
            // expected
        }
    }
}