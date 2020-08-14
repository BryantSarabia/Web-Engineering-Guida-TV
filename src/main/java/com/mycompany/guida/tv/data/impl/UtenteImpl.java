package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.model.*;
import com.mycompany.guida.tv.data.DataItemImpl;

import java.time.LocalDate;
import java.util.List;

public class UtenteImpl extends DataItemImpl<Integer> implements Utente {

    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String token;
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

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public LocalDate getExp_date() {
        return exp_date;
    }

    @Override
    public LocalDate getEmailVerifiedAt() {
        return emailVerifiedAt;
    }
    
    @Override
    public void setEmailVerifiedAt(LocalDate emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

   

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getCognome() {
        return cognome;
    }

    @Override
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Ruolo getRuolo() {
        return this.ruolo;
    }

    @Override
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    @Override
    public List<Ricerca> getRicerche() {
        return ricerche;
    }

    @Override
    public void setRicerche(List<Ricerca> ricerche) {
        this.ricerche = ricerche;
    }

    @Override
    public List<Interesse> getInteressi() {
        return interessi;
    }

    @Override
    public void setInteressi(List<Interesse> interessi) {
        this.interessi = interessi;
    }

    @Override
    public void setExpirationDate(LocalDate ld) {
        this.exp_date = ld;
    }
}
