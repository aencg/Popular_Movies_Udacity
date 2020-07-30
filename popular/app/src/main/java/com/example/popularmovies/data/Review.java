package com.example.popularmovies.data;

import java.io.Serializable;

import androidx.room.*;

public class Review implements Serializable {
    public Review() {
    }

    public Review( String idMovie, String content, String author) {
        this.idMovie = idMovie;
        this.content = content;
        this.author = author;
    }

    private String idMovie;
    private String content;
    private String author;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Review{" +
                "idMovie='" + idMovie + '\'' +
                "content='" + content + '\'' +
                ", author='" + author + '\'' +
                '}';
    }


    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }
}
