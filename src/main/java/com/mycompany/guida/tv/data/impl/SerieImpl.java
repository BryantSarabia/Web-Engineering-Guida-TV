package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.model.Serie;

public class SerieImpl extends ProgrammaImpl implements Serie {

    private int stagione;
    private int episodio;
    private int key_episodio;
    private int durata_episodio;

    public SerieImpl() {
        super();
        this.stagione = 0;
        this.episodio = 0;
        this.key_episodio = 0;
        //this.durata_episodio = 0;
        
    }
    
    @Override 
    public int getKey_episodio() {
        return key_episodio;
    }
    
    @Override
    public void setKeyEpisodio(int key_episodio){
        this.key_episodio = key_episodio;
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
    
//    @Override
//    public int getDurataEpisodio(){
//        return durata_episodio;
//    }
    
//    public void setDurataEpisodio(int durata_episodio){
//        this.durata_episodio = durata_episodio;
//    }

}
