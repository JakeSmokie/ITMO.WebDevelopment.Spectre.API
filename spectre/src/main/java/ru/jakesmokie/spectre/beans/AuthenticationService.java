package ru.jakesmokie.spectre.beans;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import javax.ejb.Singleton;
import java.io.IOException;
import java.util.Locale;

@Singleton
public class AuthenticationService {
    private final HttpClient client = HttpClients.createDefault();
    private final Locale locale = Locale.forLanguageTag("ru-RU");

    @SneakyThrows
    public JsonObject login(String login, String password) {
        val post = new HttpPost("http://jakesmokie.ru:8080/am/json/realms/root/authenticate");

        post.addHeader("Content-Type", "application/json");
        post.addHeader("X-OpenAM-Username", login);
        post.addHeader("X-OpenAM-Password", password);
        post.addHeader("Accept-API-Version", "resource=2.0, protocol=1.0");
        post.addHeader("Cache-Control", "no-cache");

        return entityToJson(client.execute(post).getEntity()).getAsJsonObject();
    }

    @SneakyThrows
    public boolean isValidToken(String token) {
        val response = doValidate(token);
        val valid = response.getAsJsonPrimitive("valid");

        return valid != null && valid.getAsBoolean();
    }

    @SneakyThrows
    public String getUid(String token) {
        val response = doValidate(token);
        return response.getAsJsonPrimitive("uid").getAsString().toLowerCase(locale);
    }

    @SneakyThrows
    public JsonObject logout(String token) {
        val post = new HttpPost("http://jakesmokie.ru:8080/am/json/sessions?_action=logout");

        post.addHeader("Content-Type", "application/json");
        post.addHeader("Accept-API-Version", "resource=3.1");
        post.setHeader(new BasicHeader("Prama", "no-cache"));
        post.setHeader(new BasicHeader("Cache-Control", "no-cache"));
        post.setEntity(new StringEntity(createKeyValueJson("tokenId", token)));

        val entity = client.execute(post).getEntity();
        return entityToJson(entity).getAsJsonObject();
    }

    @SneakyThrows
    public boolean isKeykeeper(String token) {
        if (!isValidToken(token)) {
            return false;
        }

        val properties = getSessionProperties(token, getUid(token));
        val memberOf = properties.getAsJsonArray("memberOf");

        return memberOf != null && memberOf.toString().contains("keykeepers");
    }

    @SneakyThrows
    public JsonObject getSessionProperties(String token, String user) {
        val cookieStore = new BasicCookieStore();
        val cookie = new BasicClientCookie("iPlanetDirectoryPro", token);
        cookie.setDomain(".jakesmokie.ru");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);

        @Cleanup val clientLocal = HttpClientBuilder.create()
                .setDefaultCookieStore(cookieStore)
                .build();

        val get = new HttpGet("http://jakesmokie.ru:8080/am/json/users/" + user);

        val entity = clientLocal.execute(get).getEntity();
        return entityToJson(entity).getAsJsonObject();
    }

    public JsonObject getUserProperties(String user) {
        val login = login("amAdmin", "ojiy92iohdf");
        val tokenId = login.getAsJsonPrimitive("tokenId").getAsString();

        val properties = getSessionProperties(tokenId, user);
        properties.remove("telephoneNumber");
        properties.remove("mail");

        return properties;
    }

    @SneakyThrows
    private JsonElement entityToJson(HttpEntity entity) {
        return new JsonParser().parse(EntityUtils.toString(entity));
    }

    private String createKeyValueJson(String key, String value) {
        val object = new JsonObject();
        object.add(key, new JsonPrimitive(value));

        return object.toString();
    }

    private JsonObject doValidate(String token) throws IOException {
        val post = new HttpPost("http://jakesmokie.ru:8080/am/json/sessions?_action=validate");

        post.addHeader("Content-Type", "application/json");
        post.addHeader("Accept-API-Version", "resource=3.1");
        post.setHeader(new BasicHeader("Prama", "no-cache"));
        post.setHeader(new BasicHeader("Cache-Control", "no-cache"));
        post.setEntity(new StringEntity(createKeyValueJson("tokenId", token)));

        val entity = client.execute(post).getEntity();
        return entityToJson(entity).getAsJsonObject();
    }
}
