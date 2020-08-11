package com.mycompany.guida.tv.data.impl;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.DataItemImpl;
public class CanaleImpl extends DataItemImpl<Integer> implements Canale {
    private String nome;
    private int numero;
    private String logo;


    public CanaleImpl() {
        super();
        this.nome = "";
        this.numero = 0;
        this.logo = "";

    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}
