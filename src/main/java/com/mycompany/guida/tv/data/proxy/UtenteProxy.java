package com.mycompany.guida.tv.data.proxy;
import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.impl.UtenteImpl;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.model.Ruolo;


import java.util.List;


public class UtenteProxy extends UtenteImpl implements DataItemProxy {

    private boolean modified;

    protected final DataLayer dataLayer;

    public UtenteProxy(String nome, String cognome, String email, String password, String token, String exp_date, List<Ruolo> ruoli, List<Canale> interesa, long version, DataLayer dataLayer) {
        super(nome, cognome, email, password, token, exp_date, ruoli, interesa, version);
        this.modified = false;
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
    public void setExp_date(String exp_date) {
        super.setExp_date(exp_date);
    }

    @Override
    public void setVersion(long version) {
        this.modified = true;
        super.setVersion(version);
    }

    @Override
    public int getId() {
        this.modified = true;
        return super.getId();
    }

    @Override
    public void setId(int id) {
        this.modified = true;
        super.setId(id);
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
    public void setRuoli(List<Ruolo> ruoli) {
        this.modified = true;
        super.setRuoli(ruoli);
    }

    @Override
    public void setInteresa(List<Canale> interesa) {
        this.modified = true;
        super.setInteresa(interesa);
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
