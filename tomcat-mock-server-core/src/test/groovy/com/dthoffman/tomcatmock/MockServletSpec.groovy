package com.dthoffman.tomcatmock

import spock.lang.Specification

import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class MockServletSpec extends Specification {


    TomcatMock tomcatMock = Mock(TomcatMock)
    MockServlet mockServlet = new MockServlet(tomcatMock: tomcatMock)

    HttpServletRequest mockServletRequest = Mock(HttpServletRequest)
    HttpServletResponse mockServletResponse = Mock(HttpServletResponse)

    def "relays request to mock"() {
        setup:
        ServletOutputStream mockOutputStream = Mock(ServletOutputStream)

        when:
        mockServlet.doMock(HttpMethod.GET, mockServletRequest, mockServletResponse)

        then:
        1 * mockServletRequest.getRequestURI() >> '/path'
        1 * tomcatMock.method(HttpMethod.GET, '/path', mockServletRequest) >> TomcatResponse.status(200).body("foo".bytes).contentType('text/plain').header('header', 'value').build()
        1 * mockServletResponse.setStatus(200)
        1 * mockServletResponse.setContentType('text/plain')
        1 * mockServletResponse.setContentLength("foo".length())
        1 * mockServletResponse.getOutputStream() >> mockOutputStream
        1 * mockOutputStream.write("foo".bytes)
    }

    def "handles no body content-type and body"() {
        setup:
        HttpServletRequest mockServletRequest = Mock(HttpServletRequest)
        HttpServletResponse mockServletResponse = Mock(HttpServletResponse)

        when:
        mockServlet.doMock(HttpMethod.GET, mockServletRequest, mockServletResponse)

        then:
        1 * mockServletRequest.getRequestURI() >> '/path'
        1 * tomcatMock.method(HttpMethod.GET, '/path', mockServletRequest) >> TomcatResponse.status(200).body(body).contentType(contentType).header('header', 'value').build()
        1 * mockServletResponse.setStatus(200)
        0 * mockServletResponse.setContentType(_)
        0 * mockServletResponse.getOutputStream()
        0 * mockServletResponse.setContentLength(_ as int)


        where:
        contentType  | body
        null         | null
        'text/plain' | null


    }
}
