package com.dthoffman.tomcatmock.junit.spring;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;


public class WebAppInit implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext annotationConfigApplicationContext = new AnnotationConfigWebApplicationContext();
        annotationConfigApplicationContext.register(JokeAppSpringConfig.class);
        DispatcherServlet dispatcherServlet = new DispatcherServlet(annotationConfigApplicationContext);
        servletContext.addServlet("dispatcherServlet", dispatcherServlet).addMapping("/*");
    }


}
