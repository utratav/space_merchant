package spacemerchant.exception;

public class NotEnoughFuelException extends RuntimeException {
    public NotEnoughFuelException(String message) {
        super(message);
    }
}