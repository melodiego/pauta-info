package io.sicredi.pautainfo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -8740420471401605672L;

    public RecordNotFoundException(String message) {
        super(message);
    }
}
