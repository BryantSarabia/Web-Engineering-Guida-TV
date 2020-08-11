package com.mycompany.guida.tv.data.model;

import com.mycompany.guida.tv.data.DataItem;

public interface Ricerca extends DataItem<Integer> {

    String getQuery();

    void setQuery(String query);

}
