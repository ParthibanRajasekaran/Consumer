package com.parthibanrajasekaran;

import com.parthibanrajasekaran.controller.OnboardingContent;
import com.parthibanrajasekaran.repository.OnboardingContentRepository;
import com.parthibanrajasekaran.service.OnboardingContentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OnboardingContentServiceTests {
    @Mock
    private OnboardingContentRepository repository;
    @InjectMocks
    private OnboardingContentService service;

    @Test
    public void testBuildId_validIsbn() {
        String isbn = "1234567890";
        int aisle = 1;
        String expectedId = isbn + aisle;
        String resultId = service.buildId(isbn, aisle);
        assertEquals(expectedId, resultId);
    }

    @Test
    public void testBuildId_invalidIsbn() {
        String isbn = "Z1234567890";
        int aisle = 1;
        String expectedId = "OLD" + isbn + aisle;
        String resultId = service.buildId(isbn, aisle);
        assertEquals(expectedId, resultId);
    }

    @Test
    public void testCheckBookAlreadyExist_bookExists() {
        String id = "1234567890";
        OnboardingContent onboardingContent = new OnboardingContent();
        when(repository.findById(id)).thenReturn(Optional.of(onboardingContent));
        assertTrue(service.checkBookAlreadyExist(id));
    }

    @Test
    public void testCheckBookAlreadyExist_bookDoesNotExist() {
        String id = "1234567890";
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertFalse(service.checkBookAlreadyExist(id));
    }

    @Test
    public void testGetBookById_bookExists() {
        String id = "1234567890";
        OnboardingContent onboardingContent = new OnboardingContent();
        when(repository.findById(id)).thenReturn(Optional.of(onboardingContent));
        assertEquals(onboardingContent, service.getBookById(id));
    }

    @Test
    public void testGetBookById_bookDoesNotExist() {
        String id = "1234567890";
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(AssertionError.class, () -> service.getBookById(id));
    }
}

