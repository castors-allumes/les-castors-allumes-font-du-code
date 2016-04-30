package org.castors.enumeration;

/**
 * Created by ballandd on 30/04/2016.
 */
public enum LookType {

    ACCESSOIRE("accessoire"),
    VESTE("veste"),
    CHEMISE("chemise"),
    PANTALON("pantalon"),
    CHAUSSURE("chaussure");

    LookType(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
