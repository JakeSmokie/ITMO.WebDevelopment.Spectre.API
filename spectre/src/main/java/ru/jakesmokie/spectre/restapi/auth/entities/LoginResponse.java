package ru.jakesmokie.spectre.restapi.auth.entities;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public LoginResponse() {
    }
}
