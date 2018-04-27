package com.example.anar.gridlayout;

public class Movie {

    public String title;
    public int year;
    public String director;
    public String thumbnail;
    public String description;

    public Movie() {

    }

    public Movie(String title, int year, String director, String thumbnail, String description) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    // getters
    public String getTitle() { return title; }

    public int getYear() {
        return year;
    }

    public String getDirector() { return director; }

    public String getThumbnail() { return thumbnail;}

    public String getDescription() { return description;}

    // setters
    public void setTitle(String title) { this.title = title; }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public void setDescription(String description) { this.thumbnail = description; }

}
