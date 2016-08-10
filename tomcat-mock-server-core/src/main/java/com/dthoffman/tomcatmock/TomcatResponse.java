package com.dthoffman.tomcatmock;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TomcatResponse {
    private int status = 200;
    private byte[] body = new byte[] {};
    private String contentType = "application/octet-stream";
    private Map<String,String> headers = new HashMap<String,String>();

    public TomcatResponse(int status, byte[] body, String contentType, Map<String, String> headers) {
        this.status = status;
        this.body = body;
        this.contentType = contentType;
        this.headers = headers;
    }

    public static TomcatResponse.Builder status(int status) {
        return new TomcatResponse.Builder().status(status);
    }

    public int getStatus() {
        return status;
    }

    public byte[] getBody() {
        return body;
    }

    public Map<String,String> getHeaders() {
        return headers;
    }

    public String getContentType() {
        return contentType;
    }

    public static class Builder {

        private int status = 200;
        private byte[] body = new byte[] {};
        private String contentType = "application/octet-stream";
        private Map<String,String> headers = new HashMap<String,String>();

        public TomcatResponse build() {
            return new TomcatResponse(status, body, contentType, headers);
        }

        public TomcatResponse.Builder status(int status) {
            this.status = status;
            return this;
        }

        public TomcatResponse.Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public TomcatResponse.Builder body(InputStream body) throws IOException {
            this.body = IOUtils.toByteArray(body);
            return this;
        }

        public TomcatResponse.Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public TomcatResponse.Builder header(String name, String value) {
            headers.put(name, value);
            return this;
        }
    }
}
