package com.github.zigcat.ormlite.models;

import com.github.zigcat.ormlite.controllers.AuthorController;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;

@DatabaseTable(tableName = "author")
public class Author {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String birthday;

    @DatabaseField
    private String dateOfDeath;

    @DatabaseField
    private String description;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Group group;


    private static Logger l = LoggerFactory.getLogger(Author.class);

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'';
    }

    public boolean checkAuthor(Author otherAuthor){
        return otherAuthor.getId() == id && otherAuthor.getName().equals(name)
                && otherAuthor.getBirthday().equals(birthday);
    }

    public Author(int id, String name, Group group, String description, LocalDate birth, LocalDate death) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.birthday = birth.format(User.dateTimeFormatter);
        this.dateOfDeath = death.format(User.dateTimeFormatter);
        this.description = description;
    }

    public Author(int id, String name, Group group, LocalDate birthday, String description) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.birthday = birthday.format(User.dateTimeFormatter);
        this.description = description;
    }

    public Author() {
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(String dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
