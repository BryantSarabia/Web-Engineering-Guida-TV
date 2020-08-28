package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Ruolo;

public class RuoloImpl  extends DataItemImpl<Integer> implements Ruolo {
    private String nome;
    private String descrizione;

    public RuoloImpl() {
        super();
        this.nome = "";
        this.descrizione = "";

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
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }


}
