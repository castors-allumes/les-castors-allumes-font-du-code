package org.castors.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ballandd on 30/04/2016.
 */
public class Theme {

    public static final String ID_NAME = "id";

    public static final String LABEL = "Nom";

    //
    private int id;

    //nom theme
    @JsonProperty("Nom")
    private String nom;

    //1-3 mot cle
    private String keyWord1;

    private String keyWord2;

    private String keyWord3;

    //nom vetement / optionel
    private String masterPiece;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public String getMasterPiece() {
        return masterPiece;
    }

    public void setMasterPiece(String masterPiece) {
        this.masterPiece = masterPiece;

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
