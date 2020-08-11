package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.model.Genere;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.DataItemImpl;

import java.util.ArrayList;
import java.util.List;

public class ProgrammaImpl extends DataItemImpl<Integer> implements Programma {

    private String titolo="";
    private String descrizione="";
    private String img="";
    private String link_ref="";
    private String durata="";
    private List<Genere> generi = new ArrayList<Genere>();


    public ProgrammaImpl() {
        super();
        this.titolo = "";
        this.descrizione = "";
        this.img = "";
        this.link_ref = "";
        this.durata = null;
        this.generi = null;

    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink_ref() {
        return link_ref;
    }

    public void setLink_ref(String link_ref) {
        this.link_ref = link_ref;
    }

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public List<Genere> getGeneri() {
        return generi;
    }

    public void setGeneri(List<Genere> generi) {
        this.generi = generi;
    }

}
