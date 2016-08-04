package com.dthoffman.tomcatmock

import spock.lang.Specification

class TomcatResponseSpec extends Specification {

    def "builder"() {
        when:
        TomcatResponse response = TomcatResponse.status(200).body("foo".bytes).contentType("text/plain").header("foo", "bar").build()

        then:
        response.status == 200
        response.body == "foo".bytes
        response.contentType == "text/plain"
        response.headers["foo"] == "bar"
    }
}
