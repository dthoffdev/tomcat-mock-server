package com.dthoffman.tomcatmock;

import javax.servlet.http.HttpServletRequest;

public interface TomcatMock {
    TomcatResponse method(HttpMethod method, String path, HttpServletRequest request);
}
