package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Genere;
import com.mycompany.guida.tv.data.model.Programma;

import java.util.ArrayList;
import java.util.List;

public class GenereImpl extends DataItemImpl<Integer> implements Genere {
    private String nome;
    private List<Programma> programmi = new ArrayList<Programma>();


    public GenereImpl() {
        super();
        this.nome = "";
        this.programmi = null;

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Programma> getProgrammi() {
        return programmi;
    }

    public void setProgrammi(List<Programma> programmi) {
        this.programmi = programmi;
    }

}
