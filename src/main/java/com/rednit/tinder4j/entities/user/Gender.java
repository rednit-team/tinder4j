package com.rednit.tinder4j.entities.user;

public enum Gender {

    HIDDEN(-1),
    MALE(0),
    FEMALE(1);

    private final int id;

    Gender(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
