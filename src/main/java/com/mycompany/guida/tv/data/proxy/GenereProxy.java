package com.mycompany.guida.tv.data.proxy;

import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;

import com.mycompany.guida.tv.data.impl.GenereImpl;
import com.mycompany.guida.tv.data.model.Programma;

import java.util.List;

public class GenereProxy extends GenereImpl implements DataItemProxy {
    private boolean modified;
    protected final DataLayer dataLayer;

    public GenereProxy(String nome, List<Programma> programmi, long version, DataLayer dataLayer) {
        super(nome, programmi, version);
        this.modified = false;
        this.dataLayer = dataLayer;
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
    public void setProgrammi(List<Programma> programmi) {
        this.modified = true;
        super.setProgrammi(programmi);
    }

    @Override
    public void setVersion(long version) {
        this.modified = true;
        super.setVersion(version);
    }

    @Override
    public void setKey(Integer key) {
        this.modified = true;
        super.setKey(key);
    }

    @Override
    public boolean isModified() {
        return this.modified;
    }

    @Override
    public void setModified(boolean modified) {
        this.modified=modified;
    }
}