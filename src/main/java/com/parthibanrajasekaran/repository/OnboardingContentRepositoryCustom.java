package com.parthibanrajasekaran.repository;

import com.parthibanrajasekaran.controller.OnboardingContent;

import java.util.List;

public interface OnboardingContentRepositoryCustom {

    List<OnboardingContent> findAllByAuthor(String authorName);

    OnboardingContent findByName(String bookName);

}
