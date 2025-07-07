package com.social.social_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {
    private ResponseDetail response;

    public ErrorResponse(String message, String action) {
        this.response = new ResponseDetail(message, action);
    }

    public ResponseDetail getResponse() {
        return response;
    }

    public void setResponse(ResponseDetail response) {
        this.response = response;
    }

    public static class ResponseDetail {
        private String message;
        private String action;

        public ResponseDetail(String message, String action) {
            this.message = message;
            this.action = action;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }
} 