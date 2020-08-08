package com.mycompany.guida.tv.data.model;

import com.mycompany.guida.tv.data.DataItem;

public interface Programmazione extends DataItem<Integer> {

    public int getId();

    public void setId(int id);

    public Programma getProgramma();

    public void setProgramma(Programma programma);

    public Canale getCanale();

    public void setCanale(Canale canale);

    public String getStart_time();

    public void setStart_time(String start_time);

    public String getTime();

    public void setTime(String time);

    @Override
    public long getVersion();

    @Override
    public void setVersion(long version);
}
