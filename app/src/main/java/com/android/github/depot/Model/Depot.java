package com.android.github.depot.Model;

public class Depot {
    private String name,description,score,user,avatar;

    public Depot() {
    }

    public Depot(String name, String description, String score, String user, String avatar) {
        this.name = name;
        this.description = description;
        this.score = score;
        this.user = user;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
