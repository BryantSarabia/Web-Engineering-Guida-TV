package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.model.Serie;

public class SerieImpl extends ProgrammaImpl implements Serie {

    private int stagione;
    private int episodio;

    public SerieImpl() {
        super();
        this.stagione = 0;
        this.episodio = 0;
    }

    @Override
    public int getStagione() {
        return stagione;
    }

    @Override
    public void setStagione(int stagione) {
        this.stagione = stagione;
    }

    @Override
    public int getEpisodio() {
        return episodio;
    }

    @Override
    public void setEpisodio(int episodio) {
        this.episodio = episodio;
    }

}
