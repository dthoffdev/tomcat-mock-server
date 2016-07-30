package com.dthoffman.tomcatmock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by dhoffman on 7/29/16.
 */
public class MockServlet extends HttpServlet {
    TomcatMock tomcatMock;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TomcatResponse tomcatResponse = tomcatMock.get(req.getRequestURI(), req);
        resp.setStatus(tomcatResponse.getStatus());
        for (Map.Entry<String, String> header : tomcatResponse.getHeaders().entrySet()) {
            resp.setHeader(header.getKey(), header.getValue());
        }
        resp.setContentType(tomcatResponse.getContentType());
        resp.setContentLength(tomcatResponse.getBody().length);
        resp.getOutputStream().write(tomcatResponse.getBody());
    }
}
