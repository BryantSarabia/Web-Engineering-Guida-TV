package com.mycompany.guida.tv.data.model;

import com.mycompany.guida.tv.data.DataItem;

public interface Film extends DataItem<Integer> {

    public int getId();

    public void setId(int id);

    public Programma getProgramma();

    public void setProgramma(Programma programma);

    @Override
    public long getVersion();

    @Override
    public void setVersion(long version);
}
