package com.smartherd.manga2;


public class Bookmark {
    private String mangaId;
    private String title;
    private String imageUrl;

    public Bookmark() {
        // Default constructor required for calls to DataSnapshot.getValue(Bookmark.class)
    }

    public Bookmark(String mangaId, String title, String imageUrl) {
        this.mangaId = mangaId;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getMangaId() {
        return mangaId;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
