package com.mycompany.guida.tv.data.proxy;

import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.impl.SerieImpl;
import com.mycompany.guida.tv.data.model.Programma;


public class SerieProxy extends SerieImpl implements DataItemProxy {
    private boolean modified;
    protected final DataLayer dataLayer;

    public SerieProxy(Programma programma, String stagione, String episodio, long version,  DataLayer dataLayer) {
        super(programma, stagione, episodio, version);
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
    public void setStagione(String stagione) {
        this.modified = true;
        super.setStagione(stagione);
    }

    @Override
    public void setEpisodio(String episodio) {
        this.modified = true;
        super.setEpisodio(episodio);
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
        this.modified = modified;

    }
}
