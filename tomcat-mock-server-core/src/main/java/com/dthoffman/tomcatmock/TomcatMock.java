package com.dthoffman.tomcatmock;

import javax.servlet.http.HttpServletRequest;

public interface TomcatMock {
    TomcatResponse get(String path, HttpServletRequest request);
    TomcatResponse head(String path, HttpServletRequest request);
    TomcatResponse put(String path, HttpServletRequest request);
    TomcatResponse post(String path, HttpServletRequest request);
    TomcatResponse delete(String path, HttpServletRequest request);
    TomcatResponse options(String path, HttpServletRequest request);
}
