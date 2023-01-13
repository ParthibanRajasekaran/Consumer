package com.parthibanrajasekaran.service;

import com.parthibanrajasekaran.controller.OnboardingContent;
import com.parthibanrajasekaran.repository.OnboardingContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OnboardingContentService {

    @Autowired
    OnboardingContentRepository repository;

    public String buildId(String isbn, int aisle) {
        if (!isbn.startsWith("Z")) {
            return isbn + aisle;
        }
        return "OLD" + isbn + aisle;
    }

    public boolean checkBookAlreadyExist(String id) {
        Optional<OnboardingContent> lib = repository.findById(id);
        return lib.isPresent();
    }

    public OnboardingContent getBookById(String id) {
        assert repository.findById(id).isPresent();
        return repository.findById(id).get();
    }
}
