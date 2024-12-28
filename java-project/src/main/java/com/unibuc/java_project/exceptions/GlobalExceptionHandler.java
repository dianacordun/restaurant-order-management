//package com.unibuc.java_project.exceptions;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.validation.BindException;
//import org.springframework.validation.FieldError;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(BindException.class)
//    @ResponseBody
//    public ErrorResponse handleValidationExceptions(BindException ex) {
//        StringBuilder errorMessage = new StringBuilder();
//        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
//            errorMessage.append(fieldError.getField())
//                    .append(": ")
//                    .append(fieldError.getDefaultMessage())
//                    .append("\n");
//        }
//        return new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage.toString());
//    }
//
//    public static class ErrorResponse {
//        private HttpStatus status;
//        private String message;
//
//        public ErrorResponse(HttpStatus status, String message) {
//            this.status = status;
//            this.message = message;
//        }
//
//        public HttpStatus getStatus() {
//            return status;
//        }
//
//        public void setStatus(HttpStatus status) {
//            this.status = status;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//    }
//}
