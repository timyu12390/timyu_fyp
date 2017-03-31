package com.example.timyu.timyu_fyp.Class;

/**
 * Created by timyu on 3/3/2017.
 */

public class User {

    private long id;
    private String name;
    private String email;

    public User(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
