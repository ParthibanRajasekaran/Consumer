package com.parthibanrajasekaran;


import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parthibanrajasekaran.controller.OnboardingContentController;
import com.parthibanrajasekaran.controller.Training;
import com.parthibanrajasekaran.controller.TrainingCost;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "CoursesCatalogue")
public class PactOnboardingContentTests {
    @Autowired
    private OnboardingContentController onboardingContentController;

    @Pact(consumer = "OnboardingContent")
    public RequestResponsePact pactAllCourseDetails(PactDslWithProvider builder) {
        return builder.given("courses exist")
                .uponReceiving("getting all courses")
                .path("/allCourseDetails")
                .willRespondWith()
                .status(200)
                .body(Objects.requireNonNull(PactDslJsonArray.arrayMinLike(2)
                        .stringType("course_name")
                        .stringType("id")
                        .integerType("price", 25)
                        .stringType("category").closeObject())).toPact();
    }


    @Pact(consumer = "OnboardingContent")
    public RequestResponsePact getCourseByName(PactDslWithProvider builder) {
        return builder.given("course appium exists")
                .uponReceiving("get course details of appium")
                .path("/getCourseByName/Appium")
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .integerType("price", 55)
                        .stringType("category", "mobile test automation")).toPact();
    }

    @Pact(consumer = "OnboardingContent")
    public RequestResponsePact getUnavailableCourseByName(PactDslWithProvider builder) {
        return builder.given("course spring boot does not exists")
                .uponReceiving("get course details of Spring boot")
                .path("/getCourseByName/Springboot")
                .willRespondWith()
                .status(404)
                .toPact();
    }


    @Test
    @PactTestFor(pactMethod = "pactAllCourseDetails", port = "9999")
    public void testAllProductsSum(MockServer mockServer) throws JsonProcessingException {

        String expectedResponseBody = "{\"booksPrice\":250,\"coursesPrice\":50}";

        onboardingContentController.setBaseUrl(mockServer.getUrl());
        TrainingCost trainingCost = onboardingContentController.getProductPrices();
        ObjectMapper objectMapper = new ObjectMapper();
        String actualResponseBody = objectMapper.writeValueAsString(trainingCost);

        Assertions.assertEquals(expectedResponseBody, actualResponseBody);
    }


    @Test
    @PactTestFor(pactMethod = "getCourseByName", port = "9999")
    public void testGetProductFullDetails(MockServer mockServer) throws JsonProcessingException {
        String expectedResponseBody = "{\"onboardingContent\":{\"book_name\":\"Appium\",\"id\":\"zxcv\",\"isbn\":\"zxcvb\",\"aisle\":12,\"author\":\"Wazza\"},\"price\":55,\"category\":\"mobile test automation\"}";

        onboardingContentController.setBaseUrl(mockServer.getUrl());
        Training training = onboardingContentController.getTrainingDetails("Appium");

        ObjectMapper objectMapper = new ObjectMapper();
        String actualResponseBody = objectMapper.writeValueAsString(training);

        Assertions.assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Test
    @PactTestFor(pactMethod = "getUnavailableCourseByName", port = "9999")
    public void testGetUnavailableProductDetails(MockServer mockServer) throws JsonProcessingException {
        String expectedResponseBody = "{\"onboardingContent\":{\"book_name\":\"Springboot\",\"id\":\"spring\",\"isbn\":\"sprung\",\"aisle\":777,\"author\":\"Wazza\"},\"msg\":\"Springboot Category and price details are not available at this time\"}";

        onboardingContentController.setBaseUrl(mockServer.getUrl());
        Training training = onboardingContentController.getTrainingDetails("Springboot");

        ObjectMapper objectMapper = new ObjectMapper();
        String actualResponseBody = objectMapper.writeValueAsString(training);

        Assertions.assertEquals(expectedResponseBody, actualResponseBody);
    }


}
