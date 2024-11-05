package com.smartherd.manga2;

public class DataHolder   {

    private static DataHolder instance;
    private String mangaUrl;

    private DataHolder () {

    }

    public static synchronized DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public String getMangaUrl() {
        return mangaUrl;
    }

    public void setMangaUrl(String mangaUrl) {
        this.mangaUrl = mangaUrl;
    }
}
