package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.model.Serie;

public class SerieImpl extends ProgrammaImpl implements Serie {

    private int stagione;
    private int episodio;

    public SerieImpl() {
        super();
        this.stagione = 0;
        this.episodio = 0;
    }

    public int getStagione() {
        return stagione;
    }

    public void setStagione(int stagione) {
        this.stagione = stagione;
    }

    public int getEpisodio() {
        return episodio;
    }

    public void setEpisodio(int episodio) {
        this.episodio = episodio;
    }

}
