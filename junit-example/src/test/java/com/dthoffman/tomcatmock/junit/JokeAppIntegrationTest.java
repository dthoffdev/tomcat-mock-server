package com.dthoffman.tomcatmock.junit;

import com.dthoffman.TomcatAppServer;
import com.dthoffman.tomcatmock.HttpMethod;
import com.dthoffman.tomcatmock.TomcatMock;
import com.dthoffman.tomcatmock.TomcatMockServer;
import com.dthoffman.tomcatmock.TomcatResponse;
import com.dthoffman.tomcatmock.junit.spring.JokeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.SpringServletContainerInitializer;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by dhoffman on 8/9/16.
 */

public class JokeAppIntegrationTest {

    ObjectMapper objectMapper = new ObjectMapper();

    private static final String JOKE_TEXT = "4 out of 5 doctors fail to recommend Chuck Norris as a solution to most problems. Also, 80% of doctors die unexplained, needlessly brutal deaths.";

    TomcatAppServer tomcatAppServer;

    TomcatMockServer tomcatMockServer;

    @Before
    public void setUp() {
        System.setProperty("environment", "ci");
        tomcatAppServer = new TomcatAppServer();
        tomcatAppServer.setPort(4831);
        tomcatAppServer.setServletInitializerClass(SpringServletContainerInitializer.class);
        tomcatAppServer.start();

        tomcatMockServer = new TomcatMockServer();
        tomcatMockServer.setPort(5831);
        tomcatMockServer.start();

    }

    @After
    public void tearDown() {
        tomcatMockServer.stop();
        tomcatAppServer.stop();
    }


    @Test
    public void testJokeRestService() throws IOException {
        TomcatMock tomcatMock = mock(TomcatMock.class);
        tomcatMockServer.setTomcatMock(tomcatMock);

        JokeResponse jokeResponse = new JokeResponse("success", 1, JOKE_TEXT, new ArrayList<String>());

        TomcatResponse icndbResponse = TomcatResponse.status(200).body(objectMapper.writeValueAsBytes(jokeResponse)).build();
        when(tomcatMock.method(eq(HttpMethod.GET), eq("/jokes/random/"), any(HttpServletRequest.class))).thenReturn(icndbResponse);

        HttpResponse response = HttpClientBuilder.create().build().execute(new HttpGet("http://localhost:4831/joke"));
        String jokeText = IOUtils.toString(response.getEntity().getContent());

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(JOKE_TEXT, jokeText);

        tomcatMockServer.stop();
        tomcatAppServer.stop();

    }
}
