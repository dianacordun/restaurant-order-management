package com.unibuc.java_project.exceptions;

public class UnavailableException extends RuntimeException {
        public UnavailableException(String message) {
            super("The resource is unavailable: " + message);
        }
}
