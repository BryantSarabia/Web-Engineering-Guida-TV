package com.mycompany.guida.tv.data.model;

import com.mycompany.guida.tv.data.DataItem;

public interface Serie extends Programma {

    public int getStagione();

    public void setStagione(int stagione);

    public int getEpisodio();

    public void setEpisodio(int episodio);

}
