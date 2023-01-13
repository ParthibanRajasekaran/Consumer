package com.parthibanrajasekaran;

import com.parthibanrajasekaran.controller.OnboardingContent;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;
import java.util.Random;

@SpringBootTest
public class OnboardingContentTestsIT {

    int min = 1;
    int max = 10000;
    Random r = new Random();
    int randomNumber = r.nextInt((max - min) + 1) + min;

    //mvn test
    //TestRestTemplate Rest Assured
    @Test
    public void getAuthorNameBooksTest() throws JSONException {
        String expected = "[\r\n" +
                "    {\r\n" +
                "        \"book_name\": \"Microservices\",\r\n" +
                "        \"id\": \"asdf\",\r\n" +
                "        \"isbn\": \"asdfg\",\r\n" +
                "        \"aisle\": 11,\r\n" +
                "        \"author\": \"Rooney\"\r\n" +
                "    },\r\n" +
                "    {\r\n" +
                "        \"book_name\": \"Selenium\",\r\n" +
                "        \"id\": \"qwer\",\r\n" +
                "        \"isbn\": \"qwert\",\r\n" +
                "        \"aisle\": 22,\r\n" +
                "        \"author\": \"Rooney\"\r\n" +
                "    }\r\n" +
                "]";

        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/getBooks/author?authorname=Rooney", String.class);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        JSONAssert.assertEquals(expected, response.getBody(), false);


    }

    @Test
    public void addBookIntegrationTest() {

        TestRestTemplate restTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OnboardingContent> request = new HttpEntity<OnboardingContent>(buildLibrary(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/addBook", request, String.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(buildLibrary().getId(), Objects.requireNonNull(response.getHeaders().get("unique")).get(0));
    }

    public OnboardingContent buildLibrary() {
        OnboardingContent lib = new OnboardingContent();
        lib.setAisle(randomNumber);
        lib.setBook_name("RestSharp");
        lib.setIsbn("qwerty");
        lib.setAuthor("Red Devil");
        lib.setId("qwerty" + randomNumber);
        return lib;
    }


}
