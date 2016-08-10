package com.dthoffman.tomcatmock;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;

/**
 * Created by dhoffman on 7/29/16.
 */
public class TomcatMockServer {
    int port;

    Tomcat tomcat;

    MockServlet mockServlet = new MockServlet();

    public void start() {
        tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setBaseDir(createTempContextDirectory());
        tomcat.addContext("/", createTempContextDirectory());
        tomcat.addServlet("/", "mockServlet", mockServlet).addMapping("/");
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }

    private static String createTempContextDirectory() {
        try {
            File dir = File.createTempFile("tomcat-webapp-context", "");
            if (dir.delete() && dir.mkdirs()) {
                return dir.getAbsolutePath();
            } else {
                throw new RuntimeException("unable to create tomcat context directory: " + dir.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTomcatMock(TomcatMock tomcatMock) {
        mockServlet.tomcatMock = tomcatMock;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
