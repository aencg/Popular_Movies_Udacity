package com.example.popularmovies.data;

public class Trailer {

    private String idMovie;


    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    public Trailer(  String idMovie, String content) {
        this.idMovie = idMovie;
        this.content = content;
    }

    public Trailer( ) {
    }
}
