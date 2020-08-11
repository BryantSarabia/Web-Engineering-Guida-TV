package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Film;
import com.mycompany.guida.tv.data.model.Programma;
public class FilmImpl extends ProgrammaImpl implements Film {
    private Programma programma = null;


    public FilmImpl() {
        super();
        this.programma = null;

    }
}
