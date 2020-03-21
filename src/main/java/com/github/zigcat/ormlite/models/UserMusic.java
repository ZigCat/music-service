package com.github.zigcat.ormlite.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user_music")
public class UserMusic {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true)
    private Music music;

    @DatabaseField(foreign = true)
    private User user;

    public UserMusic(int id, Music music, User user) {
        this.id = id;
        this.music = music;
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserMusic{" +
                "id=" + id +
                ", music=" + music.getId() +
                ", user=" + user.getId() +
                '}';
    }

    public UserMusic() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
