package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.model.Genere;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.DataItemImpl;

import java.util.ArrayList;
import java.util.List;

public class ProgrammaImpl extends DataItemImpl<Integer> implements Programma {

    private String titolo;
    private String descrizione;
    private String img;
    private String link_ref;
    private String durata;
    private List<Genere> generi;


    public ProgrammaImpl() {
        super();
        this.titolo = "";
        this.descrizione = "";
        this.img = "";
        this.link_ref = "";
        this.durata = null;
        this.generi = null;

    }

    @Override
    public String getTitolo() {
        return titolo;
    }

    @Override
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String getImg() {
        return img;
    }

    @Override
    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String getLink_ref() {
        return link_ref;
    }

    @Override
    public void setLink_ref(String link_ref) {
        this.link_ref = link_ref;
    }

    @Override
    public String getDurata() {
        return durata;
    }

    @Override
    public void setDurata(String durata) {
        this.durata = durata;
    }

    @Override
    public List<Genere> getGeneri() {
        return generi;
    }

    @Override
    public void setGeneri(List<Genere> generi) {
        this.generi = generi;
    }

          /**
     * Ordino in base al titolo
     * @param o
     */

    @Override
     public int compareTo(Object o) {
        if(o instanceof ProgrammaImpl) {
            if( this.getTitolo().length() < ((ProgrammaImpl) o).getTitolo().length() ) {
                return -1;
            } 
            else if( this.getTitolo().length() > ((ProgrammaImpl) o).getTitolo().length() ) {
                return 1;
            } 
            else return 0;
        }
        else return super.compareTo(o);
    }

}
