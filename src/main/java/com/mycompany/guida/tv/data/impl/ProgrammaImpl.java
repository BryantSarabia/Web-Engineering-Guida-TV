package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.model.Genere;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.DataItemImpl;

import java.util.ArrayList;
import java.util.List;

public class ProgrammaImpl extends DataItemImpl<Integer> implements Programma {
    private int id=0;
    private String titolo="";
    private String descrizione="";
    private String img="";
    private String link_ref="";
    private String durata="";
    private List<Genere> generi = new ArrayList<Genere>();
    private long version=0;

    public ProgrammaImpl(int id, String titolo, String descrizione, String img, String link_ref, String durata, List<Genere> generi, long version) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.img = img;
        this.link_ref = link_ref;
        this.durata = durata;
        this.generi = generi;
        this.version = version;
    }
    public ProgrammaImpl(String titolo, String descrizione, String img, String link_ref, String durata, List<Genere> generi, long version) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.img = img;
        this.link_ref = link_ref;
        this.durata = durata;
        this.generi = generi;
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void setVersion(long version) {
        this.version = version;
    }
}
