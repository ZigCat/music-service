package com.github.zigcat.ormlite.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "author_group")
public class AuthorGroup {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Author author;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Group group;

    public AuthorGroup() {
    }

    public AuthorGroup(int id, Author author, Group group) {
        this.id = id;
        this.author = author;
        this.group = group;
    }

    @Override
    public String toString() {
        return "AuthorGroup{" +
                "id=" + id +
                ", author=" + author +
                ", group=" + group +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
