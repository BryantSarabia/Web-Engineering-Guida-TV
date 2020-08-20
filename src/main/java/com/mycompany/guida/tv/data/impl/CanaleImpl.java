package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Programmazione;
import java.util.List;

public class CanaleImpl extends DataItemImpl<Integer> implements Canale {

    private String nome;
    private int numero;
    private String logo;
    private Programmazione programmazioneCorrente;
    private List<Programmazione> programmazioneGiornaliera;

    public CanaleImpl() {
        super();
        this.nome = "";
        this.numero = 0;
        this.logo = "";
        this.programmazioneCorrente = null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public void setProgrammazioneCorrente(Programmazione programmazioneCorrente) {
        this.programmazioneCorrente = programmazioneCorrente;
    }

    @Override
    public Programmazione getProgrammazioneCorrente() {
        return this.programmazioneCorrente;
    }
    
      /**
     * Ordino in base al canale
     * @param o
     */
    @Override
    public int compareTo(Object o) {
        if(o instanceof CanaleImpl) {
            if( this.getNumero() < ((CanaleImpl) o).getNumero() ) {
                return -1;
            } 
            else if( this.getNumero() > ((CanaleImpl) o).getNumero() ) {
                return 1;
            } 
            else return 0;
        }
        else return super.compareTo(o);
    }
    
    @Override
    public List<Programmazione> getProgrammazioneGiornaliera() {
        return programmazioneGiornaliera;
    }
    
    @Override
    public void setProgrammazioneGiornaliera(List<Programmazione> programmazioneGiornaliera) {
        this.programmazioneGiornaliera = programmazioneGiornaliera;
    }

}
