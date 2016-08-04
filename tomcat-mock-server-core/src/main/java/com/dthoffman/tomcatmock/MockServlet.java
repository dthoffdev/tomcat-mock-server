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


    public void doMock(HttpMethod method, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TomcatResponse tomcatResponse = tomcatMock.method(method, req.getRequestURI(), req);
        resp.setStatus(tomcatResponse.getStatus());
        for (Map.Entry<String, String> header : tomcatResponse.getHeaders().entrySet()) {
            resp.setHeader(header.getKey(), header.getValue());
        }
        byte[] body = tomcatResponse.getBody();
        if(body != null) {
            resp.setContentType(tomcatResponse.getContentType());
            resp.setContentLength(body.length);
            resp.getOutputStream().write(body);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doMock(HttpMethod.GET, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doMock(HttpMethod.POST, req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doMock(HttpMethod.PUT, req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doMock(HttpMethod.DELETE, req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doMock(HttpMethod.HEAD, req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doMock(HttpMethod.OPTIONS, req, resp);
    }


}
