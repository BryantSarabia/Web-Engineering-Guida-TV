package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Genere;
import com.mycompany.guida.tv.data.model.Programma;

import java.util.ArrayList;
import java.util.List;

public class GenereImpl extends DataItemImpl<Integer> implements Genere {
    private int id;
    private String nome;
    private List<Programma> programmi = new ArrayList<Programma>();
    private long version;

    public GenereImpl(int id, String nome, List<Programma> programmi, long version) {
        this.id = id;
        this.nome = nome;
        this.programmi = programmi;
        this.version = version;
    }

    public GenereImpl(String nome, List<Programma> programmi, long version) {
        this.nome = nome;
        this.programmi = programmi;
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void setVersion(long version) {
        this.version = version;
    }
}
