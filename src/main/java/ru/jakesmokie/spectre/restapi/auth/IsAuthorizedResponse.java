package ru.jakesmokie.spectre.restapi.auth;

import lombok.Data;

@Data
public class IsAuthorizedResponse {
    private final Boolean isAuthorized;
}
