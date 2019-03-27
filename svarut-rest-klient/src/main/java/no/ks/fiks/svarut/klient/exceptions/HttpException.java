package no.ks.fiks.svarut.klient.exceptions;

import lombok.Data;

@Data
public class HttpException extends RuntimeException {
    private int httperror;
    private String message;

    public HttpException(int httperror, String message) {
        super("Http status: " + httperror + " " + message);
        this.httperror = httperror;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Http status: " + httperror + " " + message;
    }
}
