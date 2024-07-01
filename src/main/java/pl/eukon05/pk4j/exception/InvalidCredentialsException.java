package pl.eukon05.pk4j.exception;

public final class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String username) {
        super(String.format("Authentication failed for user %s, are the login details correct?", username));
    }
}
