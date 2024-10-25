package com;

import java.io.Serializable;

public class CD implements Serializable{
    private float id;
    private String nom;
    private float duree;
    private boolean isEmprunt;
    public boolean isEmprunt() {
        return isEmprunt;
    }
    public void setEmprunt(boolean isEmprunt) {
        this.isEmprunt = isEmprunt;
    }

    
    public CD() {
    }
    
    public CD(float id,String nom, float duree, boolean isEmprunt) {
        this.id=id;
        this.nom = nom;
        this.duree = duree;
        this.isEmprunt = isEmprunt;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public float getDuree() {
        return duree;
    }
    public void setDuree(float duree) {
        this.duree = duree;
    }
    public float getId() {
        return id;
    }
    public void setId(float id) {
        this.id = id;
    }
    
}
