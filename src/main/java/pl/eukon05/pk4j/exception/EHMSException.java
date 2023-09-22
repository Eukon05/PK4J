package pl.eukon05.pk4j.exception;

public class EHMSException extends RuntimeException {

    public EHMSException(int statusCode) {
        super(String.format("EHMS returned an unexpected status code: %d", statusCode));
    }

}
