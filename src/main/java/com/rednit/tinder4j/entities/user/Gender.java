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

    public static Gender fromId(int id) {
        switch (id) {
            case 0: return MALE;
            case 1: return FEMALE;
            default: return HIDDEN;
        }
    }
}
