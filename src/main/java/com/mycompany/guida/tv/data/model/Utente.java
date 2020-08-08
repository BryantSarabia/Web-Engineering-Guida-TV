package com.mycompany.guida.tv.data.model;
import com.mycompany.guida.tv.data.DataItem;

import java.util.List;

public interface Utente extends DataItem<Integer> {

    public int getId();

    public void setId(int id);

    public String getNome();

    public void setNome(String nome);

    public String getCognome();

    public void setCognome(String cognome);

    public String getEmail();

    public void setEmail(String email);

    public String getPassword();

    public void setPassword(String password);

    public List<Ruolo> getRuoli();

    public void setRuoli(List<Ruolo> ruoli);

    public List<Canale> getInteresa();

    public void setInteresa(List<Canale> interesa);

    @Override
    public void setVersion(long version);

    public long getVersion();

}
