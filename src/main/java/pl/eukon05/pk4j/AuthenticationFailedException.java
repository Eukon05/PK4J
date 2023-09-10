package pl.eukon05.pk4j;

public class AuthenticationFailedException extends Exception {
    public AuthenticationFailedException() {
        super("Authentication failed - Verify your login details or refresh the session");
    }
}
