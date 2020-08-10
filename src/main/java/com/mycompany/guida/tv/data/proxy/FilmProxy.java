package com.mycompany.guida.tv.data.proxy;

import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.impl.FilmImpl;
import com.mycompany.guida.tv.data.model.Programma;


public class FilmProxy extends FilmImpl implements DataItemProxy {
    private boolean modified;
    protected final DataLayer dataLayer;

    public FilmProxy(Programma programma, long version, DataLayer dataLayer) {
        super(programma, version);
        this.modified = false;
        this.dataLayer = dataLayer;
    }

    @Override
    public void setId(int id) {
        this.modified = true;
        super.setId(id);
    }

    @Override
    public void setProgramma(Programma programma) {
        this.modified = true;
        super.setProgramma(programma);
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
