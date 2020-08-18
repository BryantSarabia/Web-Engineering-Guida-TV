package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Ricerca;

public class RicercaImpl extends DataItemImpl<Integer> implements Ricerca {

    private String query;

    public RicercaImpl() {
        super();
        this.query = "";
    }

    @Override
    public String getQuery() {
        return this.query;
    }

    @Override
    public void setQuery(String query) {
        this.query = query;
    }

}
