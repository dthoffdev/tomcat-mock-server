package com.dthoffman;


import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.reflections.Reflections;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TomcatAppServer {

    private String contextDirectoryPath = createTempContextDirectory();

    private Tomcat tomcat;

    private int port;

    private Class<ServletContainerInitializer> servletInitializerClass;

    public void start() {
        try {
            tomcat = new Tomcat();
            tomcat.setPort(port);

            StandardContext ctx = (StandardContext) tomcat.addWebapp("/", contextDirectoryPath);
            System.out.println("configuring app with basedir: " + contextDirectoryPath);

            HandlesTypes onStartup = servletInitializerClass.getAnnotation(HandlesTypes.class);
            Reflections reflections = new Reflections();
            Set<Class<?>> contextIntitializerTypes = new HashSet<Class<?>>();

            for (Class<?> type : onStartup.value()) {
                for (Class<?> subType: reflections.getSubTypesOf(type)) {
                    contextIntitializerTypes.add(subType);
                }
                contextIntitializerTypes.addAll(reflections.getSubTypesOf(type));
            }
            servletInitializerClass.newInstance().onStartup(contextIntitializerTypes, ctx.getServletContext());

            tomcat.start();
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
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

    private static String createTempContextDirectory() {
        try {
            File dir = File.createTempFile("tomcat-webapp-context", "");
            if (!dir.mkdirs()) {
                throw new RuntimeException("unable to create tomcat context directory: " + dir.getAbsolutePath());
            } else {
                return dir.getAbsolutePath();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setContextDirectoryPath(String contextDirectoryPath) {
        this.contextDirectoryPath = contextDirectoryPath;
    }

    public String getContextDirectoryPath() {
        return contextDirectoryPath;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Class<ServletContainerInitializer> getServletInitializerClass() {
        return servletInitializerClass;
    }

    public void setServletInitializerClass(Class<ServletContainerInitializer> servletInitializerClass) {
        this.servletInitializerClass = servletInitializerClass;
    }

    public Tomcat getTomcat() {
        return tomcat;
    }
}
