package com.dthoffman.tomcatmock.spock.spring

import org.springframework.web.WebApplicationInitializer
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet

import javax.servlet.ServletContext
import javax.servlet.ServletException

/**
 * Created by dhoffman on 7/29/16.
 */
class WebAppInit implements WebApplicationInitializer {
    @Override
    void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext annotationConfigApplicationContext = new AnnotationConfigWebApplicationContext()
        annotationConfigApplicationContext.register(SpringConfig)
        DispatcherServlet dispatcherServlet = new DispatcherServlet(annotationConfigApplicationContext)
        servletContext.addServlet('dispatcherServlet', dispatcherServlet).addMapping("/*")
    }
}
