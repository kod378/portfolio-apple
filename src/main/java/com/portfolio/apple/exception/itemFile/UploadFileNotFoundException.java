package com.portfolio.apple.exception.itemFile;

import com.portfolio.apple.exception.ApiEntityNotFoundException;

public class UploadFileNotFoundException extends ApiEntityNotFoundException {
    public UploadFileNotFoundException(String message) {
        super(message);
    }
}
