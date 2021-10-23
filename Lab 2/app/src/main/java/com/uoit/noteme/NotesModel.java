package com.uoit.noteme;

public class NotesModel {
    private int id;
    private String title;
    private String subtitle;
    private String body;
    private String colour;

    public NotesModel(int id, String title, String subtitle, String body, String colour) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.body = body;
        this.colour = colour;
    }

    @Override
    public String toString() {
        return "NotesModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", body='" + body + '\'' +
                ", colour='" + colour + '\'' +
                '}';
    }

    public NotesModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
