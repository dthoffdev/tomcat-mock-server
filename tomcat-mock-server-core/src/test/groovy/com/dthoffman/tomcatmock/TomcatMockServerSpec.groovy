package com.dthoffman.tomcatmock

import spock.lang.Specification

/**
 * Created by dhoffman on 8/11/16.
 */
class TomcatMockServerSpec extends Specification {

    def "reports url"() {
        when:
        String url = new TomcatMockServer().getURL()

        then:
        url == "http://localhost:8081"
    }
}
