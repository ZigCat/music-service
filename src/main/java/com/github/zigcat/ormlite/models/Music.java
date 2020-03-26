package com.github.zigcat.ormlite.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;

@DatabaseTable(tableName = "music")
public class Music {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Genre genre;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Author author;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Group group;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Album album;

    @DatabaseField
    private String creationDate;

    @DatabaseField
    private String content;

    public Music(int id, String name, Genre genre, Author author, Album album, Group group, LocalDate creation, String content) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.author = author;
        this.album = album;
        this.group = group;
        this.creationDate = creation.format(User.dateTimeFormatter);
        this.content = content;
    }

    public Music() {
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationDate='" + creationDate + '\'' +
                '}';
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
