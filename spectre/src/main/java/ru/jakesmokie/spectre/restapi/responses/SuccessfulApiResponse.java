package ru.jakesmokie.spectre.restapi.responses;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SuccessfulApiResponse extends ApiResponse {
    public SuccessfulApiResponse(Object entity) {
        setEntity(entity);
        setSuccess(true);
    }

    public SuccessfulApiResponse() {
    }
}
