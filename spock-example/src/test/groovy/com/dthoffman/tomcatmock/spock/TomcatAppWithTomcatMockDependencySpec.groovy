package com.dthoffman.tomcatmock.spock

import com.dthoffman.TomcatAppServer
import com.dthoffman.tomcatmock.TomcatMock
import com.dthoffman.tomcatmock.TomcatMockServer
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import groovyx.net.http.RESTClient
import org.springframework.http.HttpMethod
import org.springframework.web.SpringServletContainerInitializer
import spock.lang.Specification

/**
 * Created by dhoffman on 7/29/16.
 */
class TomcatAppWithTomcatMockDependencySpec extends Specification {

    TomcatAppServer tomcatAppServer

    TomcatMockServer tomcatMockServer

    TomcatMock tomcatMock = Mock(TomcatMock)

    def "test tomcat app server"() {
        setup:
        tomcatAppServer = new TomcatAppServer(port: 9999, servletInitializerClass: SpringServletContainerInitializer)
        tomcatMockServer = new TomcatMockServer(port: 8888)
        RESTClient restClient = new RESTClient("http://localhost:9999")

        when:
        restClient.request(Method.GET, ContentType.ANY) {
            req.path = "/downstream/get"
        }

        then:
        true

    }


}
