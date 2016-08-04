package com.dthoffman.tomcatmock.spock

import com.dthoffman.TomcatAppServer
import com.dthoffman.tomcatmock.HttpMethod
import com.dthoffman.tomcatmock.TomcatMock
import com.dthoffman.tomcatmock.TomcatMockServer
import com.dthoffman.tomcatmock.TomcatResponse
import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.Method
import groovyx.net.http.RESTClient
import org.springframework.web.SpringServletContainerInitializer
import spock.lang.Shared
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

/**
 * Created by dhoffman on 7/29/16.
 */
class TomcatAppWithTomcatMockDependencySpec extends Specification {

    @Shared
    TomcatAppServer tomcatAppServer

    @Shared
    TomcatMockServer tomcatMockServer

    TomcatMock tomcatMock = Mock(TomcatMock)

    RESTClient restClient = new RESTClient("http://localhost:9999")

    def setupSpec() {
        tomcatAppServer = new TomcatAppServer(port: 9999, servletInitializerClass: SpringServletContainerInitializer)
        tomcatAppServer.start()
        tomcatMockServer = new TomcatMockServer(port: 8888)
        tomcatMockServer.start()
    }

    def setup() {
        tomcatMockServer.tomcatMock = tomcatMock
    }

    def "app under test calls downstream mock server"() {
        when:
        HttpResponseDecorator response = restClient.request(Method.GET, ContentType.ANY) {
            uri.path = "/downstream/get"
            uri.query = [path: "mock-server-path"]
            request.setHeader("TEST-HEADER", "test-header-value")
        }

        then:
        response.data.text == "response-body-text"
        response.headers["TEST-HEADER"].value == "response-header-value"
        1 * tomcatMock.method(HttpMethod.GET, "/mock-server-path", _ as HttpServletRequest) >> { HttpMethod method, String path, HttpServletRequest request ->
            assert request.getHeader("TEST-HEADER") == "test-header-value"
            return TomcatResponse.status(200).body("response-body-text".getBytes("UTF-8")).header("TEST-HEADER", "response-header-value").build()
        }
    }


}
