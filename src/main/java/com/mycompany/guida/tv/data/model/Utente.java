package com.mycompany.guida.tv.data.model;

import com.mycompany.guida.tv.data.DataItem;
import com.mycompany.guida.tv.data.proxy.InteressaProxy;

import java.time.LocalDate;
import java.util.List;

public interface Utente extends DataItem<Integer> {

    public String getNome();

    public void setNome(String nome);

    public String getToken();

    public void setToken(String token);

    public String getCognome();

    public void setCognome(String cognome);

    public String getEmail();

    public void setEmail(String email);

    public String getPassword();

    public void setPassword(String password);

    public Ruolo getRuolo();

    public void setRuolo(Ruolo ruolo);

    public List<Ricerca> getRicerche();

    public void setRicerche(List<Ricerca> ricerche);

    public List<InteressaProxy> getInteressi();

    public void setInteressi(List<InteressaProxy> interessi);

    Boolean getSendEmail();

    void setSendEmail(Boolean send);

    public LocalDate getExp_date();

    void cleanInteressi();

    public void setExpirationDate(LocalDate ld);

    void setEmailVerifiedAt(LocalDate date);

    public LocalDate getEmailVerifiedAt();
}
