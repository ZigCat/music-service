package com.github.zigcat.ormlite.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "category_genre")
public class CategoryGenre {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Genre genre;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Category category;

    public CategoryGenre() {
    }

    public CategoryGenre(int id, Genre genre, Category category) {
        this.id = id;
        this.genre = genre;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "CategoryGenre{" +
                "id=" + id +
                ", genre=" + genre.getId() +
                ", category=" + category.getId() +
                '}';
    }
}
