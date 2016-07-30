package com.dthoffman.tomcatmock.spock.controller

import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.Method
import groovyx.net.http.RESTClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(path = "/downstream")
class SimpleRestController {

    @Autowired
    RESTClient restClient

    @RequestMapping(path = "/get", method = RequestMethod.GET)
    ResponseEntity get(@RequestHeader("TEST-HEADER") header, @RequestParam("path") path) {
        HttpResponseDecorator responseDecorator = restClient.request(Method.GET, ContentType.ANY) {
            uri.path = "/${path}"
            request.setHeader("TEST-HEADER", header)
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(responseDecorator.contentType ?: MediaType.APPLICATION_OCTET_STREAM_VALUE)).body(responseDecorator.data.bytes)
    }

}
