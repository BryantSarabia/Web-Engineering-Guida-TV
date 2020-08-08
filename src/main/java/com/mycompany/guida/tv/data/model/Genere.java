package com.mycompany.guida.tv.data.model;

import com.mycompany.guida.tv.data.DataItem;

import java.util.List;

public interface Genere extends DataItem<Integer> {

    public int getId();

    public void setId(int id);

    public String getNome();

    public void setNome(String nome);

    public List<Programma> getProgrammi();

    public void setProgrammi(List<Programma> programmi);

    @Override
    public long getVersion();

    @Override
    public void setVersion(long version);
}
