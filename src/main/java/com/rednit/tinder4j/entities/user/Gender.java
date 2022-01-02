package com.rednit.tinder4j.entities.user;

/**
 * All available Genders.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public enum Gender {

    /**
     * Gender is not displayed.
     */
    HIDDEN(-1),

    /**
     * Male. id 0.
     */
    MALE(0),

    /**
     * Female. id 1.
     */
    FEMALE(1);

    private final int id;

    Gender(int id) {
        this.id = id;
    }

    /**
     * Gets the Gender id.
     *
     * @return the gender id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets a {@link Gender} from id
     *
     * @param id the id to get the {@link Gender} from
     * @return a {@link Gender}
     */
    public static Gender fromId(int id) {
        switch (id) {
            case 0:
                return MALE;
            case 1:
                return FEMALE;
            default:
                return HIDDEN;
        }
    }
}
