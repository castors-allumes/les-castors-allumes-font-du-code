package org.castors.dto;

/**
 * Created by ballandd on 29/04/2016.
 */
public class Vetement {

    public static final String NAME = "nom";

    public static final String KEY_WORD_1 = "keyWord1";

    public static final String KEY_WORD_2 = "keyWord2";

    public static final String KEY_WORD_3 = "keyWord3";

    //image
    private String nom;

    //enum ?
    private String type;

    //
    private String keyWord1;

    private String keyWord2;

    private String keyWord3;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyWord1() {
        return keyWord1;
    }

    public void setKeyWord1(String keyWord1) {
        this.keyWord1 = keyWord1;
    }

    public String getKeyWord2() {
        return keyWord2;
    }

    public void setKeyWord2(String keyWord2) {
        this.keyWord2 = keyWord2;
    }

    public String getKeyWord3() {
        return keyWord3;
    }

    public void setKeyWord3(String keyWord3) {
        this.keyWord3 = keyWord3;
    }
}
