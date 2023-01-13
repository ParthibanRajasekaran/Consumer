package com.parthibanrajasekaran;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.parthibanrajasekaran.controller.AddResponse;
import com.parthibanrajasekaran.controller.OnboardingContent;
import com.parthibanrajasekaran.controller.OnboardingContentController;
import com.parthibanrajasekaran.repository.OnboardingContentRepository;
import com.parthibanrajasekaran.service.OnboardingContentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class OnboardingContentTests {

    @Autowired
    OnboardingContentController controller;
    @MockBean
    OnboardingContentRepository repository;
    @MockBean
    OnboardingContentService onboardingContentService;
    @Autowired
    private MockMvc mockMvc;


    @Test
    public void checkBuildIDLogic() {
        OnboardingContentService lib = new OnboardingContentService();
        String id = lib.buildId("ZMAN", 24);
        assertEquals(id, "OLDZMAN24");
        String id1 = lib.buildId("MAN", 24);
        assertEquals(id1, "MAN24");
    }

    @Test
    public void addBookTest() {
        //mock

        OnboardingContent lib = buildLibrary();
        when(onboardingContentService.buildId(lib.getIsbn(), lib.getAisle())).thenReturn(lib.getId());
        when(onboardingContentService.checkBookAlreadyExist(lib.getId())).thenReturn(false);
        when(repository.save(any())).thenReturn(lib);
        ResponseEntity response = controller.addBook(buildLibrary());//step
        System.out.println(response.getStatusCode());
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        AddResponse ad = (AddResponse) response.getBody();
        assert ad != null;
        assertEquals(lib.getId(), ad.getId());
        assertEquals("Success Book is Added", ad.getMsg());

        //call Mock service from code

    }

    @Test
    public void testAddBook_existingBook() {
        OnboardingContent onboardingContent = new OnboardingContent("book1", "John Smith", "publisher1", "isbn1", 1);
        String id = "isbn1" + 1;
        when(onboardingContentService.buildId(onboardingContent.getIsbn(), onboardingContent.getAisle())).thenReturn(id);
        when(onboardingContentService.checkBookAlreadyExist(id)).thenReturn(true);
        AddResponse addResponse = (AddResponse) controller.addBook(onboardingContent).getBody();
        assert addResponse != null;
        assertEquals("Book already exist", addResponse.getMsg());
        assertEquals(id, addResponse.getId());
        assertEquals(HttpStatus.ACCEPTED, controller.addBook(onboardingContent).getStatusCode());
    }

    @Test
    public void testGetBookById_validId() {
        OnboardingContent onboardingContent = new OnboardingContent("book1", "John Smith", "publisher1", "isbn1", 1);
        String id = "isbn1" + 1;
        when(onboardingContentService.getBookById(id)).thenReturn(onboardingContent);
        when(repository.findById(id)).thenReturn(Optional.of(onboardingContent));
        assertEquals(onboardingContent, controller.getBookById(id));
    }

    @Test
    public void addBookControllerTest() throws Exception {
        OnboardingContent lib = buildLibrary();
        ObjectMapper map = new ObjectMapper();
        String jsonString = map.writeValueAsString(lib);


        when(onboardingContentService.buildId(lib.getIsbn(), lib.getAisle())).thenReturn(lib.getId());
        when(onboardingContentService.checkBookAlreadyExist(lib.getId())).thenReturn(false);
        when(repository.save(any())).thenReturn(lib);

        this.mockMvc.perform(post("/addBook").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)).andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(lib.getId()));

    }

    @Test
    public void getBookByAuthorTest() throws Exception {
        List<OnboardingContent> li = new ArrayList<OnboardingContent>();
        li.add(buildLibrary());
        li.add(buildLibrary());
        when(repository.findAllByAuthor(any())).thenReturn(li);
        this.mockMvc.perform(get("/getBooks/author").param("authorname", "Red Devil"))
                .andDo(print()).andExpect(status().isOk()).
                andExpect(jsonPath("$.length()", is(2))).
                andExpect(jsonPath("$.[0].id").value("qwerty"));

    }

    @Test
    public void updateBookTest() throws Exception {
        OnboardingContent lib = buildLibrary();
        ObjectMapper map = new ObjectMapper();
        String jsonString = map.writeValueAsString(UpdateLibrary());
        when(onboardingContentService.getBookById(any())).thenReturn(buildLibrary());
        this.mockMvc.perform(put("/updateBook/" + lib.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json("{\"book_name\":\"RestAssured\",\"id\":\"qwerty\",\"isbn\":\"rainyday\",\"aisle\":322,\"author\":\"United\"}"));

    }

    @Test
    public void deleteBookControllerTest() throws Exception {
        when(onboardingContentService.getBookById(any())).thenReturn(buildLibrary());
        doNothing().when(repository).delete(buildLibrary());
        this.mockMvc.perform(delete("/deleteBook").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\" : \"qwerty322\"}")).andDo(print())
                .andExpect(status().isCreated()).andExpect(content().string("Book is deleted"));


    }

    @Test
    public void testGetBookById_invalidId() {
        String id = "isbn1and1";
        when(onboardingContentService.getBookById(id)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> controller.getBookById(id));
    }


    public OnboardingContent buildLibrary() {
        OnboardingContent lib = new OnboardingContent();
        lib.setAisle(322);
        lib.setBook_name("RestSharp");
        lib.setIsbn("rainyday");
        lib.setAuthor("Red Devil");
        lib.setId("qwerty");
        return lib;
    }

    public OnboardingContent UpdateLibrary() {
        OnboardingContent lib = new OnboardingContent();
        lib.setAisle(322);
        lib.setBook_name("RestAssured");
        lib.setIsbn("rainyday");
        lib.setAuthor("United");
        lib.setId("qwerty");
        return lib;

    }


}
