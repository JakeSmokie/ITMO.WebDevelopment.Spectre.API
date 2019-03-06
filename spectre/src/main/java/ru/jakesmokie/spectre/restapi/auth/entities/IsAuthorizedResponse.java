package ru.jakesmokie.spectre.restapi.auth.entities;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class IsAuthorizedResponse {
    private Boolean isAuthorized;

    public IsAuthorizedResponse(Boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

    public IsAuthorizedResponse() {
    }
}
