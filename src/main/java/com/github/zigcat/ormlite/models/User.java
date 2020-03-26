package com.github.zigcat.ormlite.models;

import com.github.zigcat.ormlite.controllers.UserController;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.services.Security;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@DatabaseTable(tableName = "user")
public class User {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String surname;

    @DatabaseField
    private String email;

    @DatabaseField
    private String password;

    @DatabaseField
    private String regDate;

    @DatabaseField(dataType = DataType.ENUM_STRING)
    private Role role;

    private LocalDate registrationDate;
    private static Logger l = LoggerFactory.getLogger(User.class);
    public static DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy MM dd");

    public User(int id, String name, String surname, String email, String password, Role role){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.regDate = LocalDate.now().format(dateTimeFormatter);
        this.role = role;
    }

    public User(int id, String name, String surname, String email, String password, LocalDate regDate, Role role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.registrationDate = regDate;
        this.role = role;
    }

    public User(){

    }

    public boolean checkUser(User otherUser) throws NullPointerException{
        return otherUser.getId() == id && otherUser.getName().equals(name) && otherUser.getSurname().equals(surname);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}
