package com.dthoffman.tomcatmock.spock.controller


import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.springframework.beans.factory.annotation.Value
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

    @Value('${downstream.url}')
    String downstreamUri

    @RequestMapping(path = "/get", method = RequestMethod.GET)
    ResponseEntity get(@RequestHeader("TEST-HEADER") header, @RequestParam("path") path) {
        HttpClient httpClient = DefaultHttpClient.newInstance()
        HttpGet httpGet = new HttpGet("${downstreamUri}/${path}")
        httpGet.setHeader("TEST-HEADER", header)
        HttpResponse response = httpClient.execute(httpGet)
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(response.getFirstHeader("Content-Type")?.value ?: MediaType.APPLICATION_OCTET_STREAM_VALUE)).body(response.entity.content.bytes)
    }

}
