package pl.eukon05.pk4j.exception;

public class UserAlreadyLoggedInException extends RuntimeException {

    public UserAlreadyLoggedInException(String userID) {
        super(String.format("User %s is already logged in on another device! Please logout from other devices before trying to log in again!", userID));
    }

}
