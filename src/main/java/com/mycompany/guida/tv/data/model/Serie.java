package com.mycompany.guida.tv.data.model;

public interface Serie extends Programma {
    
    public int getKey_episodio();
    
    public void setKeyEpisodio(int key_episodio);
    
    public int getStagione();

    public void setStagione(int stagione);

    public int getEpisodio();

    public void setEpisodio(int episodio);
    
    //public int getDurataEpisodio();
    
    //public void setDurataEpisodio(int durata_episodio);

}
