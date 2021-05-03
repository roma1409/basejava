package com.urise.webapp.exception;

public class NotExistStorageException extends StorageException {
    public NotExistStorageException(String uuid) {
        super(String.format("Resume %s not exist.", uuid), uuid);
    }
}
