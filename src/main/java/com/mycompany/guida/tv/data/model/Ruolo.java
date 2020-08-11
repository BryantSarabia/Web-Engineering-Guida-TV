package com.mycompany.guida.tv.data.model;

import com.mycompany.guida.tv.data.DataItem;

public interface Ruolo extends DataItem<Integer> {

    public String getNome();

    public void setNome(String nome);

    public String getDescrizione();

    public void setDescrizione(String descrizione);

}
