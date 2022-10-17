package com.portfolio.apple.exception.itemFile;

import java.io.IOException;

public class UploadFileException extends RuntimeException {
    public UploadFileException(String message) {
        super(message);
    }

    public UploadFileException(IOException e) {
        super(e);
    }
}
