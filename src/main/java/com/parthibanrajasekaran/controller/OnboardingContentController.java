package com.parthibanrajasekaran.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parthibanrajasekaran.repository.OnboardingContentRepository;
import com.parthibanrajasekaran.service.OnboardingContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
public class OnboardingContentController {

    @Autowired
    OnboardingContentRepository repository;

    @Autowired
    TrainingCost productPrices;

    @Autowired
    OnboardingContentService onboardingContentService;

    String baseUrl = "http://localhost:8181";

    private static final Logger logger = LoggerFactory.getLogger(OnboardingContentController.class);

    @PostMapping("/addBook")
    public ResponseEntity addBook(@RequestBody OnboardingContent onboardingContent) {
        String id = onboardingContentService.buildId(onboardingContent.getIsbn(), onboardingContent.getAisle());
        AddResponse ad = new AddResponse();

        if (!onboardingContentService.checkBookAlreadyExist(id))//mock
        {
            logger.info("Book do not exist so creating one");
            onboardingContent.setId(id);
            repository.save(onboardingContent);
            HttpHeaders headers = new HttpHeaders();
            headers.add("unique", id);
            ad.setMsg("Success Book is Added");
            ad.setId(id);

            return new ResponseEntity<>(ad, headers, HttpStatus.CREATED);
        } else {
            logger.info("Book  exist so skipping creation");
            ad.setMsg("Book already exist");
            ad.setId(id);

            return new ResponseEntity<>(ad, HttpStatus.ACCEPTED);
        }
    }

    @CrossOrigin
    @RequestMapping("/getBooks/{id}")
    public OnboardingContent getBookById(@PathVariable(value = "id") String id) {
        try {
            return repository.findById(id).get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @GetMapping("getBooks/author")
    public List<OnboardingContent> getBookByAuthorName(@RequestParam(value = "authorname") String authorname) {
        return repository.findAllByAuthor(authorname);
    }

    @PutMapping("/updateBook/{id}")
    public ResponseEntity<OnboardingContent> updateBook(@PathVariable(value = "id") String id, @RequestBody OnboardingContent onboardingContent) {
        OnboardingContent existingBook = onboardingContentService.getBookById(id);
        existingBook.setAisle(onboardingContent.getAisle());
        existingBook.setAuthor(onboardingContent.getAuthor());
        existingBook.setBook_name(onboardingContent.getBook_name());
        repository.save(existingBook);
        return new ResponseEntity<>(existingBook, HttpStatus.OK);
    }

    @DeleteMapping("/deleteBook")
    public ResponseEntity<String> deleteBookById(@RequestBody OnboardingContent onboardingContent) {
        OnboardingContent bookToBeDeleted = onboardingContentService.getBookById(onboardingContent.getId());
        repository.delete(bookToBeDeleted);
        logger.info("Book  is deleted ");
        return new ResponseEntity<>("Book is deleted", HttpStatus.CREATED);

    }

    @GetMapping("/getBooks")
    public List<OnboardingContent> getBooks() {
        return repository.findAll();
    }


    @GetMapping("/getTrainingDetails/{name}")
    public Training getTrainingDetails(@PathVariable(value = "name") String name) throws JsonProcessingException {
        Training training = new Training();

        TestRestTemplate restTemplate = new TestRestTemplate();
        OnboardingContent lib = repository.findByName(name);
        training.setOnboardingContent(lib);
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/getCourseByName/" + name, String.class);
        if (response.getStatusCode().is4xxClientError()) {
            training.setMsg(name + " Category and price details are not available at this time");
        } else {
            ObjectMapper mapper = new ObjectMapper();

            AllCourseDetails allCourseDetails = mapper.readValue(response.getBody(), AllCourseDetails.class);

            training.setCategory(allCourseDetails.getCategory());
            training.setPrice(allCourseDetails.getPrice());
        }
        return training;
    }


    @CrossOrigin
    @GetMapping("/getProductPrices")
    public TrainingCost getProductPrices() throws JsonProcessingException {
        productPrices.setBooksPrice(250);

        long sum = 0;
        for (int i = 0; i < getAllCoursesDetails().length; i++) {
            sum = sum + getAllCoursesDetails()[i].getPrice();
        }

        productPrices.setCoursesPrice(sum);

        return productPrices;
    }

    public void setBaseUrl(String url) {
        baseUrl = url;
    }

    public AllCourseDetails[] getAllCoursesDetails() throws JsonProcessingException {

        TestRestTemplate restTemplate = new TestRestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/allCourseDetails", String.class);
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(response.getBody(), AllCourseDetails[].class);
    }

}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

