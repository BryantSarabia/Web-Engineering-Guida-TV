package com.mycompany.guida.tv.data.model;

import com.mycompany.guida.tv.data.DataItem;

public interface Serie extends DataItem<Integer> {

    public int getId();

    public void setId(int id);

    public Programma getProgramma();

    public void setProgramma(Programma programma);

    public String getStagione();

    public void setStagione(String stagione);

    public String getEpisodio();

    public void setEpisodio(String episodio);

    @Override
    public long getVersion();

    @Override
    public void setVersion(long version);
}
