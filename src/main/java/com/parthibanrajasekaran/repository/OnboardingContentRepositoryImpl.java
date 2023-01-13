package com.parthibanrajasekaran.repository;

import com.parthibanrajasekaran.controller.OnboardingContent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class OnboardingContentRepositoryImpl implements OnboardingContentRepositoryCustom {

    @Autowired
    OnboardingContentRepository repository;

    @Override
    public List<OnboardingContent> findAllByAuthor(String authorName) {
        List<OnboardingContent> booksWithAuthor = new ArrayList<>();

        List<OnboardingContent> books = repository.findAll();
        for (OnboardingContent item : books)
            if (item.getAuthor().equalsIgnoreCase(authorName)) {
                booksWithAuthor.add(item);
            }
        return booksWithAuthor;
    }

    @Override
    public OnboardingContent findByName(String bookName) {
        List<OnboardingContent> books = repository.findAll();
        for (OnboardingContent item : books)
            if (item.getBook_name().equalsIgnoreCase(bookName)) {
                return item;
            }
        return null;


    }


}
