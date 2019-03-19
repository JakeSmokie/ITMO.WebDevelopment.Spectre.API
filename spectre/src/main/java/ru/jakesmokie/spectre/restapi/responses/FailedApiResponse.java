package ru.jakesmokie.spectre.restapi.responses;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class FailedApiResponse extends ApiResponse {
    public FailedApiResponse(String message) {
        setEntity(message);
        setSuccess(false);
    }

    public FailedApiResponse() {
    }
}
