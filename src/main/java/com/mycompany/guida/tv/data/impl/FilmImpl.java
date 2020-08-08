package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Film;
import com.mycompany.guida.tv.data.model.Programma;
public class FilmImpl extends DataItemImpl<Integer> implements Film {
    private int id=0;
    private Programma programma = null;
    private long version=0;

    public FilmImpl(int id, Programma programma, long version) {
        this.id = id;
        this.programma = programma;
        this.version = version;
    }

    public FilmImpl(Programma programma, long version) {
        this.programma = programma;
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

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void setVersion(long version) {
        this.version = version;
    }
}
