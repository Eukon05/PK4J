package pl.eukon05.pk4j.exception;

public final class RateLimitExceededException extends RuntimeException {

    public RateLimitExceededException(String userID) {
        super(String.format("User %s got rate-limited! Please wait or solve the captcha on another device before trying to log in again!", userID));
    }

}
