package com.talosi.superfight.utils.exception;

import org.springframework.http.HttpStatus;

import java.util.Optional;

public class NotFoundException extends HttpRequestException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
        Optional.empty().orElseThrow(() -> new NotFoundException("")); // to avoid error about no default constructor
    }
}
