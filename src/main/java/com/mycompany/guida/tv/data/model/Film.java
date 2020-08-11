package com.mycompany.guida.tv.data.model;

import com.mycompany.guida.tv.data.DataItem;

public interface Film extends DataItem<Integer> {

    public Programma getProgramma();

    public void setProgramma(Programma programma);

}
