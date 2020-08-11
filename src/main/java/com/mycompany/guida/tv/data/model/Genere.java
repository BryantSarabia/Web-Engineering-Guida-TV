package com.mycompany.guida.tv.data.model;

import com.mycompany.guida.tv.data.DataItem;

import java.util.List;

public interface Genere extends DataItem<Integer> {

    public String getNome();

    public void setNome(String nome);

    public List<Programma> getProgrammi();

    public void setProgrammi(List<Programma> programmi);

}
