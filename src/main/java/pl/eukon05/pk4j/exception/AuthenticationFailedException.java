package pl.eukon05.pk4j.exception;

public class AuthenticationFailedException extends Exception {
    public AuthenticationFailedException() {
        super("Authentication failed - Verify your login details or refresh the session");
    }
}
