package com.dthoffman.tomcatmock.spock.spring

import groovyx.net.http.RESTClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer

@Configuration
@ComponentScan(basePackages = ["com.dthoffman.tomcatmock.spock.controller", "com.dthoffman.tomcatmock.spock.service"])
@PropertySources(
        [
                @PropertySource('classpath:/config.properties')
        ]
)
class SpringConfig {
    @Bean
    static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer()
    }

    @Bean
    RESTClient restClient(@Value('${downstream.url}') String dowstreamUrl) {
        return new RESTClient(dowstreamUrl)
    }

}
