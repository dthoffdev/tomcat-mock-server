package com.dthoffman.tomcatmock.junit.spring;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class JokeAppController {

    @Autowired
    HttpClient httpClient;

    @Value("${joke.baseUri}")
    String jokeBaseUri;

    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping(path = "/joke", method = RequestMethod.GET)
    ResponseEntity<String> getMeAJoke() throws URISyntaxException, IOException {
        HttpGet get = new HttpGet(new URIBuilder(jokeBaseUri).setPath("/jokes/random/").build());
        HttpResponse response = httpClient.execute(get);
        JokeResponse jokeResponse = objectMapper.readValue(response.getEntity().getContent(), JokeResponse.class);
        return ResponseEntity.status(200).body(jokeResponse.getValue().getJoke());
    }
}
