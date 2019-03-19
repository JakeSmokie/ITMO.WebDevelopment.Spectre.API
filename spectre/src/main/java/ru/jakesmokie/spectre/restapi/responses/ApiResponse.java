package ru.jakesmokie.spectre.restapi.responses;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApiResponse {
    private boolean success;
    private Object entity;

    public ApiResponse() {
    }

    public ApiResponse(boolean success, Object entity) {
        this.success = success;
        this.entity = entity;
    }
}
