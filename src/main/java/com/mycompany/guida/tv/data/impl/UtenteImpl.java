package com.mycompany.guida.tv.data.impl;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.model.Ruolo;
import com.mycompany.guida.tv.data.model.Utente;
import com.mycompany.guida.tv.data.DataItemImpl;

import java.util.ArrayList;
import java.util.List;

public class UtenteImpl extends DataItemImpl<Integer> implements Utente {
    private int id=0;
    private String nome="";
    private String cognome="";
    private String email="";
    private String password="";
    private String token="";
    private String exp_date="";
    private List<Ruolo> ruoli = new ArrayList<Ruolo>();
    private List<Canale> interesa = new ArrayList<Canale>();
    private long version=0;

    public UtenteImpl(int id, String nome, String cognome, String email, String password, String token, String exp_date, List<Ruolo> ruoli, List<Canale> interesa, long version) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.token = token;
        this.exp_date = exp_date;
        this.ruoli = ruoli;
        this.interesa = interesa;
        this.version = version;
    }

    public UtenteImpl(String nome, String cognome, String email, String password, String token, String exp_date, List<Ruolo> ruoli, List<Canale> interesa, long version) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.token = token;
        this.exp_date = exp_date;
        this.ruoli = ruoli;
        this.interesa = interesa;
        this.version = version;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void setVersion(long version) {
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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Ruolo> getRuoli() {
        return ruoli;
    }

    public void setRuoli(List<Ruolo> ruoli) {
        this.ruoli = ruoli;
    }

    public List<Canale> getInteresa() {
        return interesa;
    }

    public void setInteresa(List<Canale> interesa) {
        this.interesa = interesa;
    }
}
