package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Genere;
import com.mycompany.guida.tv.data.model.Programma;

import java.util.ArrayList;
import java.util.List;

public class GenereImpl extends DataItemImpl<Integer> implements Genere {

    private String nome;
    private List<Programma> programmi = new ArrayList<>();

    public GenereImpl() {
        super();
        this.nome = "";
        this.programmi = null;

    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public List<Programma> getProgrammi() {
        return programmi;
    }

    @Override
    public void setProgrammi(List<Programma> programmi) {
        this.programmi = programmi;
    }
    
      /**
     * Ordino in base al genere
     * @param o
     */

    @Override
     public int compareTo(Object o) {
        if(o instanceof GenereImpl) {
            if( this.getNome().length() < ((GenereImpl) o).getNome().length() ) {
                return -1;
            } 
            else if( this.getNome().length() > ((GenereImpl) o).getNome().length() ) {
                return 1;
            } 
            else return 0;
        }
        else return super.compareTo(o);
    }

}
