# Tomcat Mock Server
This project aims to help you write integration tests for a web application that run as a part of your build. It includes code to help start an embedded tomcat instance with the application under test deployed and also an additional embedded tomcat that allows you to mock out any http dependencies.

## Example:
You have a rest application that deploys to tomcat as a war. This rest application has a service "/joke" which uses the
internet chuck norris database (icndb) to retrieve a joke. You can integration test this service using a regular unit test (see junit-example for more complete picture):

```java

// service code:
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



// Test code:
    private static final String JOKE_TEXT = "4 out of 5 doctors fail to recommend Chuck Norris as a solution to most problems. Also, 80% of doctors die unexplained, needlessly brutal deaths.";

    @Test
    public void testJokeRestService() throws IOException {
        TomcatAppServer tomcatAppServer;
        tomcatAppServer = new TomcatAppServer();
        tomcatAppServer.setPort(4831);
        tomcatAppServer.setServletInitializerClass(SpringServletContainerInitializer.class);
        tomcatAppServer.start();

        TomcatMockServer tomcatMockServer;
        tomcatMockServer = new TomcatMockServer();
        tomcatMockServer.setPort(5831);
        tomcatMockServer.start();

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
```

1. First you start up an embedded tomcat. This one uses SpringServletContainerInitializer which will pick up any WebApplicationInitializer classes on the classpath.
2. Next you can start up an embedded tomcat to act as the downstream service (TomcatMockServer), 
3. You can put a mock of TomcatMock into the mock server and use a mocking library like mockito to mock interctions.
4. This mock returns a TomcatResponse instance with a status of 200 and a json body with a joke inside. 
5. Be sure to stop your servers when finished.

*It may also be optimal to start these servers at the beginning of a suite
and stop them at the end, just make sure you don't dirty any context between tests.*