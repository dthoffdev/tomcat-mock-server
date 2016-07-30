package com.dthoffman.tomcatmock;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhoffman on 7/29/16.
 */
public class TomcatResponse {
    private int status = 200;
    private byte[] body = new byte[] {};
    private String contentType = "application/octet-stream";
    private Map<String,String> headers = new HashMap<String,String>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Map<String,String> getHeaders() {
        return headers;
    }
    public void setHeaders(Map<String,String> headers) {
        this.headers = headers;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
