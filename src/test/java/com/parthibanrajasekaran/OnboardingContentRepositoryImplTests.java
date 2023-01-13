package com.parthibanrajasekaran;

import com.parthibanrajasekaran.controller.OnboardingContent;
import com.parthibanrajasekaran.repository.OnboardingContentRepository;
import com.parthibanrajasekaran.repository.OnboardingContentRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OnboardingContentRepositoryImplTests {
    @Mock
    private OnboardingContentRepository repository;
    @InjectMocks
    private OnboardingContentRepositoryImpl repositoryImpl;

    @Test
    public void testFindAllByAuthor_validAuthor() {
        String authorName = "Max Smith";
        OnboardingContent onboardingContent1 = new OnboardingContent("book1", authorName, "book11", "isbn1");
        OnboardingContent onboardingContent2 = new OnboardingContent("book2", authorName, "book22", "isbn2");
        OnboardingContent onboardingContent3 = new OnboardingContent("book3", "Jane Doe", "book33", "isbn3");
        List<OnboardingContent> books = Arrays.asList(onboardingContent1, onboardingContent2, onboardingContent3);
        when(repository.findAll()).thenReturn(books);
        List<OnboardingContent> result = repositoryImpl.findAllByAuthor(authorName);
        assertEquals(2, result.size());
        assertTrue(result.contains(onboardingContent1));
        assertTrue(result.contains(onboardingContent2));
    }

    @Test
    public void testFindAllByAuthor_invalidAuthor() {
        String authorName = "Max Smith";
        OnboardingContent onboardingContent1 = new OnboardingContent("book1", "Jane Doe", "book11", "isbn1");
        OnboardingContent onboardingContent2 = new OnboardingContent("book2", "Jane Doe", "book22", "isbn2");
        OnboardingContent onboardingContent3 = new OnboardingContent("book3", "Jane Doe", "book33", "isbn3");
        List<OnboardingContent> books = Arrays.asList(onboardingContent1, onboardingContent2, onboardingContent3);
        when(repository.findAll()).thenReturn(books);
        List<OnboardingContent> result = repositoryImpl.findAllByAuthor(authorName);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByName_validBook() {
        String bookName = "book1";
        OnboardingContent onboardingContent1 = new OnboardingContent(bookName, "John Smith", "book11", "isbn1");
        OnboardingContent onboardingContent2 = new OnboardingContent("book2", "Jane Doe", "book22", "isbn2");
        OnboardingContent onboardingContent3 = new OnboardingContent("book3", "John Smith", "book33", "isbn3");
        List<OnboardingContent> books = Arrays.asList(onboardingContent1, onboardingContent2, onboardingContent3);
        when(repository.findAll()).thenReturn(books);
        OnboardingContent result = repositoryImpl.findByName(bookName);
        assertEquals(onboardingContent1, result);
    }


    @Test
    public void testFindByName_invalidBook() {
        String bookName = "book1";
        OnboardingContent onboardingContent1 = new OnboardingContent("book2", "John Smith", "book11", "isbn1");
        OnboardingContent onboardingContent2 = new OnboardingContent("book3", "Jane Doe", "book22", "isbn2");
        OnboardingContent onboardingContent3 = new OnboardingContent("book4", "John Smith", "book33", "isbn3");
        List<OnboardingContent> books = Arrays.asList(onboardingContent1, onboardingContent2, onboardingContent3);
        when(repository.findAll()).thenReturn(books);
        OnboardingContent result = repositoryImpl.findByName(bookName);
        assertNull(result);
    }
}
