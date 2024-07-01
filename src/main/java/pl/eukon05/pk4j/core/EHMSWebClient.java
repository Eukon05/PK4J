package pl.eukon05.pk4j.core;

import com.google.gson.JsonObject;
import pl.eukon05.pk4j.exception.EHMSExpiredTokenException;
import pl.eukon05.pk4j.exception.InvalidCredentialsException;

import java.io.IOException;

public interface EHMSWebClient {
    EHMSUserSession login(EHMSUser user) throws IOException, InterruptedException, InvalidCredentialsException;
    JsonObject getRequest(EHMSUserSession session, EHMSUrlEnum store) throws IOException, InterruptedException, EHMSExpiredTokenException;
    JsonObject getRequest(EHMSUserSession session, EHMSUrlEnum store, long resourceID) throws IOException, InterruptedException, EHMSExpiredTokenException;
}
