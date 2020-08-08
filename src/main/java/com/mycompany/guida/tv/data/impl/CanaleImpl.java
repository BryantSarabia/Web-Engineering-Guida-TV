package com.mycompany.guida.tv.data.impl;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.DataItemImpl;
public class CanaleImpl extends DataItemImpl<Integer> implements Canale {
    private int id;
    private String nome;
    private int numero;
    private String logo;
    private long version;

    public CanaleImpl(String nome, int numero, String logo, long version) {
        this.nome = nome;
        this.numero = numero;
        this.logo = logo;
        this.version = version;
    }

    public CanaleImpl(int id, String nome, int numero, String logo, long version) {
        this.id = id;
        this.nome = nome;
        this.numero = numero;
        this.logo = logo;
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void setVersion(long version) {
        this.version = version;
    }
}
