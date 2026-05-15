package spacemerchant.exception;

public class NotEnoughCreditsException extends Exception {
    public NotEnoughCreditsException(String message) {
        super(message);
    }
}