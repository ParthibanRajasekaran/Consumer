package com.parthibanrajasekaran.repository;

import com.parthibanrajasekaran.controller.OnboardingContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnboardingContentRepository extends JpaRepository<OnboardingContent, String>, OnboardingContentRepositoryCustom {

}
