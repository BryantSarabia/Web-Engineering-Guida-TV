package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Ruolo;

public class RuoloImpl  extends DataItemImpl<Integer> implements Ruolo {
    private int id=0;
    private String nome="";
    private String descrizione="";
    private long version=0;

    public RuoloImpl(int id, String nome, String descrizione, long version) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.version = version;
    }

    public RuoloImpl(String nome, String descrizione, long version) {
        this.nome = nome;
        this.descrizione = descrizione;
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
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
