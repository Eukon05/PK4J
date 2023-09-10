package pl.eukon05.pk4j;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class PK4J {
    private PK4J() {
    }

    public static final String BASE_URL = "https://ehms.pk.edu.pl/standard/";
    private static final String AUTH_CHECK = "Logowanie do systemu";

    public static Student authenticate(String login, String password) throws IOException, AuthenticationFailedException {
        Connection.Response res = Jsoup.connect(BASE_URL).execute();
        Document doc = res.parse();

        Map<String, String> cookies = res.cookies();

        Elements inputs = doc.getElementsByClass("form-control");

        String loginForm = inputs.get(0).attr("name");
        String passForm = inputs.get(1).attr("name");
        String counter = doc.select("input[type=hidden]").get(1).val();

        Map<String, String> data = new HashMap<>();
        data.put(loginForm, login);
        data.put(passForm, password);
        data.put("log_form", "yes");
        data.put("counter", counter);

        if (Jsoup.connect(BASE_URL).data(data).cookies(cookies).post().getElementsContainingText(AUTH_CHECK).isEmpty()) {
            return new Student(cookies);
        } else
            throw new AuthenticationFailedException();
    }

    public static boolean checkAuthentication(Map<String, String> cookies) throws IOException {
        return Jsoup.connect(BASE_URL).cookies(cookies).get().getElementsContainingText(AUTH_CHECK).isEmpty();
    }
}
