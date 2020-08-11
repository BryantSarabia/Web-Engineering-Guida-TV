package com.mycompany.guida.tv.data.impl;
import com.mycompany.guida.tv.data.model.*;
import com.mycompany.guida.tv.data.DataItemImpl;

import java.time.LocalDate;
import java.util.List;

public class UtenteImpl extends DataItemImpl<Integer> implements Utente {
    private String nome="";
    private String cognome="";
    private String email="";
    private String password="";
    private String token="";
    private LocalDate emailVerifiedAt;
    private LocalDate exp_date;
    private Ruolo ruolo;
    private List<Ricerca> ricerche;
    private List<Interesse> interessi;

    public UtenteImpl() {
        super();
        this.nome = "";
        this.cognome = "";
        this.email = "";
        this.password = "";
        this.token = "";
        this.emailVerifiedAt = null;
        this.exp_date = null;
        this.ruolo = null;
        this.ricerche = null;
        this.interessi = null;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDate getExp_date() {
        return exp_date;
    }

    public LocalDate getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(LocalDate emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public void setExp_date(LocalDate exp_date) {
        this.exp_date = exp_date;
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

    public Ruolo getRuolo() {
        return this.ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    public List<Ricerca> getRicerche() {
        return ricerche;
    }

    public void setRicerche(List<Ricerca> ricerche) {
        this.ricerche = ricerche;
    }

    public List<Interesse> getInteressi() {
        return interessi;
    }

    public void setInteressi(List<Interesse> interessi) {
        this.interessi = interessi;
    }
}
