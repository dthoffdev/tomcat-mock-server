package com.dthoffman.tomcatmock.junit.spring;

import java.util.List;

/**
 * Created by dhoffman on 8/9/16.
 */
public class JokeResponse {
    private String type;
    private JokeBody value;

    public JokeResponse() {

    }

    public JokeResponse(String type, int id, String joke, List<String> categories) {
        this.type = type;
        this.value = new JokeBody();
        this.value.id = id;
        this.value.joke = joke;
        this.value.categories = categories;
    }

    public String getType() {
        return type;
    }

    public JokeBody getValue() {
        return value;
    }

    public static class JokeBody {
        private int id;
        private String joke;
        private List<String> categories;

        public int getId() {
            return id;
        }

        public String getJoke() {
            return joke;
        }

        public List<String> getCategories() {
            return categories;
        }
    }

}
