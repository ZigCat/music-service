package com.github.zigcat.ormlite.models;

import com.github.zigcat.ormlite.controllers.GroupController;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.time.LocalDate;

@DatabaseTable(tableName = "group")
public class Group {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String creationDate;

    @DatabaseField
    private String dateOfDestroy;

    public boolean checkGroup(Group otherGroup){
        if(otherGroup.getId() == id && otherGroup.getName().equals(name)
                && otherGroup.getCreationDate().equals(creationDate)){
            return true;
        }
        return false;
    }

    public static Group getById(int id) throws SQLException {
        for(Group g: GroupController.groupDao.queryForAll()){
            if(g.getId() == id){
                return g;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", dateOfDestroy='" + dateOfDestroy + '\'' +
                '}';
    }

    public Group(int id, String name, LocalDate creation, LocalDate destroy) {
        this.id = id;
        this.name = name;
        this.creationDate = creation.format(User.dateTimeFormatter);
        this.dateOfDestroy = destroy.format(User.dateTimeFormatter);
    }

    public Group(int id, String name, LocalDate creation) {
        this.id = id;
        this.name = name;
        this.creationDate = creation.format(User.dateTimeFormatter);
    }

    public Group() {
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDateOfDestroy() {
        return dateOfDestroy;
    }

    public void setDateOfDestroy(String dateOfDestroy) {
        this.dateOfDestroy = dateOfDestroy;
    }
}
