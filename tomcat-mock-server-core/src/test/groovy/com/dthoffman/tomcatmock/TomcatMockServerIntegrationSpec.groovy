package com.dthoffman.tomcatmock

import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.Method
import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

/**
 * Created by dhoffman on 8/4/16.
 */
class TomcatMockServerIntegrationSpec extends Specification {
    @Shared
    TomcatMockServer tomcatMockServer

    TomcatMock tomcatMock = Mock(TomcatMock)

    RESTClient restClient = new RESTClient("http://localhost:8888")

    def setupSpec() {
        tomcatMockServer = new TomcatMockServer(port: 8888)
        tomcatMockServer.start()
    }

    def setup() {
        tomcatMockServer.tomcatMock = tomcatMock
    }

    def "mock server handles get, put, post, delete"() {
        when:
        HttpResponseDecorator responseDecorator = restClient.request(httpMethod) {
            uri.path = "/test-path"
            uri.query = ["foo": "bar"]
        }

        then:
        responseDecorator.status == 200
        responseDecorator.data.text == "body"
        responseDecorator.contentType == "text/plain"
        1 * tomcatMock.method(mockMethod, "/test-path", { HttpServletRequest req -> req.getParameter("foo") == "bar" } as HttpServletRequest) >> TomcatResponse.status(200).body("body".bytes).contentType("text/plain").build()

        where:
        httpMethod    | mockMethod
        Method.GET    | HttpMethod.GET
        Method.POST   | HttpMethod.POST
        Method.PUT    | HttpMethod.PUT
        Method.DELETE | HttpMethod.DELETE
    }

    def "mock server handles head"() {
        when:
        HttpResponseDecorator responseDecorator = restClient.request(Method.HEAD) {
            uri.path = "/test-path"
            uri.query = ["foo": "bar"]
        }

        then:
        responseDecorator.status == 200
        responseDecorator.data == null
        responseDecorator.getFirstHeader("FOO").value == "BAR"
        1 * tomcatMock.method(HttpMethod.HEAD, "/test-path", { HttpServletRequest req -> req.getParameter("foo") == "bar" } as HttpServletRequest) >> TomcatResponse.status(200).header("FOO", "BAR").build()
    }

    def "mock server handles options"() {
        when:
        HttpResponseDecorator responseDecorator = restClient.options(path: "/foo-path")

        then:
        responseDecorator.status == 200
        responseDecorator.data == null
        responseDecorator.getFirstHeader("Allow").value == "PUT"
        1 * tomcatMock.method(HttpMethod.OPTIONS, "/foo-path", _ as HttpServletRequest) >> TomcatResponse.status(200).header("Allow", "PUT").build()
    }

}
