package com.dthoffman.tomcatmock.spock

import com.dthoffman.TomcatAppServer
import com.dthoffman.tomcatmock.TomcatMock
import com.dthoffman.tomcatmock.TomcatMockServer
import com.dthoffman.tomcatmock.TomcatResponse
import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.Method
import groovyx.net.http.RESTClient
import groovyx.net.http.URIBuilder
import org.apache.http.client.methods.HttpGet
import org.springframework.web.SpringServletContainerInitializer
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

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
        tomcatAppServer.start()
        tomcatMockServer = new TomcatMockServer(port: 8888)
        tomcatMockServer.start()
        tomcatMockServer.tomcatMock = tomcatMock
        RESTClient restClient = new RESTClient("http://localhost:9999")

        when:
        HttpResponseDecorator response = restClient.request(Method.GET, ContentType.ANY) {
            uri.path = "/downstream/get"
            ((URIBuilder)uri).setQuery([path: "mock-server-path"])
            ((HttpGet)request).setHeader("TEST-HEADER", "test-header-value")
        }

        then:
        response.data.text == "response-body-text"
        1 * tomcatMock.get("/mock-server-path", _ as HttpServletRequest) >> { String path, HttpServletRequest request ->
            assert request.getHeader("TEST-HEADER") == "test-header-value"
            return new TomcatResponse(status: 200, body: "response-body-text".getBytes("UTF-8"))
        }

    }


}
