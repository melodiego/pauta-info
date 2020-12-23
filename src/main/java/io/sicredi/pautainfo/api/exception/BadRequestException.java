package io.sicredi.pautainfo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 5674013144836503315L;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Exception ex) {
        super(ex);
    }
}
