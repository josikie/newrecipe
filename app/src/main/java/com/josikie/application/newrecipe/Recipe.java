package com.josikie.application.newrecipe;

public class Recipe {
    private String title;
    private String date;
    private String author;
    private String sectionsName;
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSectionsName() {
        return sectionsName;
    }

    public void setSectionsName(String sectionsName) {
        this.sectionsName = sectionsName;
    }


    public Recipe(String title, String date, String author, String sectionsName, String link) {
        this.title = title;
        this.date = date;
        this.author = author;
        this.sectionsName = sectionsName;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
