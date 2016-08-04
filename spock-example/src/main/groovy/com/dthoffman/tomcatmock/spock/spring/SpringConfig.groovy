package com.dthoffman.tomcatmock.spock.spring

import org.springframework.context.annotation.*
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
}
