package org.castors.enumeration;

/**
 * Created by ballandd on 30/04/2016.
 */
public enum LookIndex {

    THEME("theme"),
    VETEMENT("vetement");

    private String name;

    LookIndex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
