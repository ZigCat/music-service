package com.github.zigcat.ormlite.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tag_album")
public class TagAlbum {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Tag tag;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Album album;

    public TagAlbum() {
    }

    public TagAlbum(int id, Tag tag, Album album) {
        this.id = id;
        this.tag = tag;
        this.album = album;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    @Override
    public String toString() {
        return "TagAlbum{" +
                "id=" + id +
                ", tag=" + tag +
                ", album=" + album +
                '}';
    }
}
