package com.portfolio.apple.exception.itemFile;

import com.portfolio.apple.exception.NotFoundByApiException;

public class UploadFileNotFoundException extends NotFoundByApiException {
    public UploadFileNotFoundException(String message) {
        super(message);
    }
}
