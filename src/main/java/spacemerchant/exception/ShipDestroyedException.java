package spacemerchant.exception;

public class ShipDestroyedException extends RuntimeException {
    public ShipDestroyedException(String message) {
        super(message);
    }
}