package com.parthibanrajasekaran.controller;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Onboarding_Inventory")
public class OnboardingContent {
    @Column(name = "book_name")
    private String book_name;
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "isbn")
    private String isbn;
    @Column(name = "aisle")
    private int aisle;
    @Column(name = "author")
    private String author;


    public OnboardingContent() {

    }

    public OnboardingContent(String bookName, String authorName, String id, String isbn) {
        this.book_name = bookName;
        this.author = authorName;
        this.isbn = isbn;
        this.id = id;
    }

    public OnboardingContent(String bookName, String authorName, String id, String isbn, int aisle) {
        this.book_name = bookName;
        this.author = authorName;
        this.isbn = isbn;
        this.id = id;
        this.aisle = aisle;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAisle() {
        return aisle;
    }

    public void setAisle(int aisle) {
        this.aisle = aisle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
