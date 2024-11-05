package com.smartherd.manga2;

public class Manga {
    private String id;
    private String title;
    private String imageUrl;

    public Manga(String id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
}
