package com.dthoffman

import spock.lang.Specification

/**
 * Created by dhoffman on 8/11/16.
 */
class TomcatAppServerSpec extends Specification {
    def "TomcatAppServer builds server uri"() {
        when:
        String url = tomcatAppServer.getURL()

        then:
        url == "http://localhost:8080"

        where:
        tomcatAppServer << [new TomcatAppServer(), new TomcatAppServer(port:8080)]
    }
}
