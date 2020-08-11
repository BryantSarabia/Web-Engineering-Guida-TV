package com.mycompany.guida.tv.data.proxy;
import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.impl.UtenteImpl;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.model.Interesse;
import com.mycompany.guida.tv.data.model.Ricerca;
import com.mycompany.guida.tv.data.model.Ruolo;


import java.time.LocalDate;
import java.util.List;


public class UtenteProxy extends UtenteImpl implements DataItemProxy {

    private boolean modified;

    protected final DataLayer dataLayer;

    public UtenteProxy(DataLayer dataLayer) {
        super();
        this.modified=false;
        this.dataLayer = dataLayer;
    }

    @Override
    public void setKey(Integer key) {
        this.modified = true;
        super.setKey(key);
    }
    @Override
    public void setToken(String token) {
        this.modified = true;
        super.setToken(token);
    }

    @Override
    public void setVersion(long version) {
        this.modified = true;
        super.setVersion(version);
    }

    @Override
    public void setNome(String nome) {
        this.modified = true;
        super.setNome(nome);
    }

    @Override
    public void setCognome(String cognome) {
        this.modified = true;
        super.setCognome(cognome);
    }

    @Override
    public void setEmail(String email) {
        this.modified = true;
        super.setEmail(email);
    }

    @Override
    public void setPassword(String password) {
        this.modified = true;
        super.setPassword(password);
    }

    @Override
    public void setRuolo(Ruolo ruolo) {
        this.modified = true;
        super.setRuolo(ruolo);
    }

    @Override
    public void setRicerche(List<Ricerca> ricerche) {
        this.modified = true;
        super.setRicerche(ricerche);
    }

    @Override
    public void setInteressi(List<Interesse> interessi) {
        this.modified = true;
        super.setInteressi(interessi);
    }

    @Override
    public void setEmailVerifiedAt(LocalDate emailVerifiedAt) {
        super.setEmailVerifiedAt(emailVerifiedAt);
    }

    @Override
    public void setExp_date(LocalDate exp_date) {
        super.setExp_date(exp_date);
    }

    @Override
    public boolean isModified() {
        return this.modified;
    }

    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
    }
}
