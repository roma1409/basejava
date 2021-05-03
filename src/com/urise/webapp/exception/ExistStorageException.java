package com.urise.webapp.exception;

public class ExistStorageException extends StorageException {
    public ExistStorageException(String uuid) {
        super(String.format("Resume %s already exist.", uuid), uuid);
    }
}
