package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.model.Serie;

public class SerieImpl extends DataItemImpl<Integer> implements Serie {
    private int id=0;
    private Programma programma = null;
    private String stagione="";
    private String episodio="";
    private long version=0;

    public SerieImpl(int id, Programma programma, String stagione, String episodio, long version) {
        this.id = id;
        this.programma = programma;
        this.stagione = stagione;
        this.episodio = episodio;
        this.version = version;
    }

    public SerieImpl(Programma programma, String stagione, String episodio, long version) {
        this.programma = programma;
        this.stagione = stagione;
        this.episodio = episodio;
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Programma getProgramma() {
        return programma;
    }

    public void setProgramma(Programma programma) {
        this.programma = programma;
    }

    public String getStagione() {
        return stagione;
    }

    public void setStagione(String stagione) {
        this.stagione = stagione;
    }

    public String getEpisodio() {
        return episodio;
    }

    public void setEpisodio(String episodio) {
        this.episodio = episodio;
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
