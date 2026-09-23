package auto;


public class Automobile {

    private final String vin;
    private String make;
    private String model;
    private int price;

    public Automobile(String vin, String make, String model, int price)
            throws NullParameterException, InvalidValueException {

        if (vin == null) {
            throw new NullParameterException("VIN cannot be null.");
        }

        if (make == null) {
            throw new NullParameterException("Make cannot be null.");
        }

        if (model == null) {
            throw new NullParameterException("Model cannot be null.");
        }

        if (vin.isBlank()) {
            throw new InvalidValueException("VIN cannot be blank.");
        }

        if (make.isBlank()) {
            throw new InvalidValueException("Make cannot be blank.");
        }

        if (model.isBlank()) {
            throw new InvalidValueException("Model cannot be blank.");
        }

        if (price < 0) {
            throw new InvalidValueException("Price cannot be negative.");
        }

        if (!isValidVin(vin)) {
            throw new InvalidValueException("Invalid VIN.");
        }

        this.vin = vin;
        this.make = make;
        this.model = model;
        this.price = price;
    }

    public String getVin() {
        return vin;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) throws InvalidValueException {
        if (price < 0) {
            throw new InvalidValueException("Price cannot be negative.");
        }

        this.price = price;
    }

    private boolean isValidVin(String vin) {

        if (vin.length() != 17) {
            return false;
        }

        String allowedLetters = "ABCDEFGHJKLMNPRSTUVWXYZ";

        for (int i = 0; i < vin.length(); i++) {
            char character = vin.charAt(i);

            if (Character.isDigit(character)) {
                continue;
            }

            if (!allowedLetters.contains(String.valueOf(character))) {
                return false;
            }
        }

        return calculateCheckDigit(vin);
    }

    private boolean calculateCheckDigit(String vin) {

        int[] weights = {
                8, 7, 6, 5, 4, 3, 2, 10, 0,
                9, 8, 7, 6, 5, 4, 3, 2
        };

        int sum = 0;

        for (int i = 0; i < 17; i++) {
            char character = vin.charAt(i);
            int value;

            if (Character.isDigit(character)) {
                value = character - '0';
            } else {
                switch (character) {
                    case 'A': value = 1; break;
                    case 'B': value = 2; break;
                    case 'C': value = 3; break;
                    case 'D': value = 4; break;
                    case 'E': value = 5; break;
                    case 'F': value = 6; break;
                    case 'G': value = 7; break;
                    case 'H': value = 8; break;
                    case 'J': value = 1; break;
                    case 'K': value = 2; break;
                    case 'L': value = 3; break;
                    case 'M': value = 4; break;
                    case 'N': value = 5; break;
                    case 'P': value = 7; break;
                    case 'R': value = 9; break;
                    case 'S': value = 2; break;
                    case 'T': value = 3; break;
                    case 'U': value = 4; break;
                    case 'V': value = 5; break;
                    case 'W': value = 6; break;
                    case 'X': value = 7; break;
                    case 'Y': value = 8; break;
                    case 'Z': value = 9; break;
                    default: return false;
                }
            }

            sum += value * weights[i];
        }

        int remainder = sum % 11;

        char expectedCheckDigit;

        if (remainder == 10) {
            expectedCheckDigit = 'X';
        } else {
            expectedCheckDigit = (char) ('0' + remainder);
        }

        return vin.charAt(8) == expectedCheckDigit;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Automobile)) {
            return false;
        }

        Automobile other = (Automobile) obj;

        return this.vin.equals(other.vin);
    }

    @Override
    public int hashCode() {
        return vin.hashCode();
    }
}

