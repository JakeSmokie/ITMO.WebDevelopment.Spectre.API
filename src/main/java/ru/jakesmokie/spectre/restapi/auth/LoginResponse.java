package ru.jakesmokie.spectre.restapi.auth;

import com.google.gson.Gson;
import lombok.Data;

@Data
public class LoginResponse {
    private static final Gson gson = new Gson();
    private final String token;

    public static LoginResponse fromJson(String json) {
        return gson.fromJson(json, LoginResponse.class);
    }

    public static String toJson(LoginResponse response) {
        return gson.toJson(response);
    }
}
