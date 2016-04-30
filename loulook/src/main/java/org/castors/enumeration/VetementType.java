package org.castors.enumeration;


/**
 * Created by ballandd on 30/04/2016.
 */
public enum VetementType {
    ACCESSOIRE("accessoire"),
    BAS("bas"),
    HAUT("haut"),
    MANTEAU("manteau"),
    CHAUSSURES("chaussures");

    VetementType(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
