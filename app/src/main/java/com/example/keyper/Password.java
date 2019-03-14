package com.example.keyper;

public class Password {

    private String title;
    private String content;
    private int id;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public Password (String title, String content, int id) {
        this.title = title;
        this.content = content;
        this.id = id;
    }
}
