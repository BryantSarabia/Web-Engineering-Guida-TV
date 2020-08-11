package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Film;
import com.mycompany.guida.tv.data.model.Programma;
public class FilmImpl extends DataItemImpl<Integer> implements Film {
    private Programma programma = null;


    public FilmImpl() {
        super();
        this.programma = null;

    }

    public Programma getProgramma() {
        return programma;
    }

    public void setProgramma(Programma programma) {
        this.programma = programma;
    }

}
