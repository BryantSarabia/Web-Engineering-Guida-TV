/*package com.mycompany.guida.tv.data.proxy;

import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.impl.SerieImpl;
import com.mycompany.guida.tv.data.model.Programma;


public class SerieProxy extends SerieImpl implements DataItemProxy {
    private boolean modified;
    protected final DataLayer dataLayer;

    public SerieProxy(DataLayer dataLayer) {
        super();
        this.modified = false;
        this.dataLayer = dataLayer;
    }

    @Override
    public void setProgramma(Programma programma) {
        this.modified = true;
        super.setProgramma(programma);
    }

    @Override
    public void setStagione(int stagione) {
        this.modified = true;
        super.setStagione(stagione);
    }

    @Override
    public void setEpisodio(int episodio) {
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
*/