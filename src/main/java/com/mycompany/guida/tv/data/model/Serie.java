package com.mycompany.guida.tv.data.model;

public interface Serie extends Programma {
    
    public int getKeyEpisodio();
    
    public void setKeyEpisodio(int key_episodio);
    
    public int getStagione();

    public void setStagione(int stagione);

    public int getEpisodio();

    public void setEpisodio(int episodio);

}
