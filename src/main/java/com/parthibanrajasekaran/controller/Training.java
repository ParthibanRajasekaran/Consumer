package com.parthibanrajasekaran.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.stereotype.Component;

@Component
public class Training {


    OnboardingContent onboardingContent;
    @JsonInclude(Include.NON_NULL)
    String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public OnboardingContent getOnboardingContent() {
        return onboardingContent;
    }

    public void setOnboardingContent(OnboardingContent onboardingContent) {
        this.onboardingContent = onboardingContent;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @JsonInclude(Include.NON_DEFAULT)
    int price;
    @JsonInclude(Include.NON_NULL)
    String category;


}
