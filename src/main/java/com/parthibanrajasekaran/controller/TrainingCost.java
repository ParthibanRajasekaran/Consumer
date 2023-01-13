package com.parthibanrajasekaran.controller;

import org.springframework.stereotype.Component;

@Component
public class TrainingCost {


    public long getBooksPrice() {
        return booksPrice;
    }

    public void setBooksPrice(long booksPrice) {
        this.booksPrice = booksPrice;
    }

    public long getCoursesPrice() {
        return coursesPrice;
    }

    public void setCoursesPrice(long coursesPrice) {
        this.coursesPrice = coursesPrice;
    }

    long booksPrice;
    long coursesPrice;

}
