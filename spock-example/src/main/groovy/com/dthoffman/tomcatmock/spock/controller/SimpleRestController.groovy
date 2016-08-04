package com.dthoffman.tomcatmock.spock.controller

import org.apache.http.Header
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = "/downstream")
class SimpleRestController {

    @Value('${downstream.url}')
    String downstreamUri

    @RequestMapping(path = "/get", method = RequestMethod.GET)
    ResponseEntity downstream(
            @RequestHeader("TEST-HEADER") header, @RequestParam("path") path) {
        HttpClient httpClient = DefaultHttpClient.newInstance()
        HttpGet httpGet = new HttpGet("${downstreamUri}/${path}")
        httpGet.setHeader("TEST-HEADER", header)
        HttpResponse response = httpClient.execute(httpGet)
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.OK)
        response.getAllHeaders().collect { Header respHeader ->
            builder.header(respHeader.name, respHeader.value)
        }
        return builder.body(response.entity.content.bytes)
    }

}
