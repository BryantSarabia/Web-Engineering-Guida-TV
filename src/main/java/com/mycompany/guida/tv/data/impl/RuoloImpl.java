package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Ruolo;

public class RuoloImpl  extends DataItemImpl<Integer> implements Ruolo {
    private String nome="";
    private String descrizione="";

    public RuoloImpl() {
        super();
        this.nome = "";
        this.descrizione = "";

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


}
