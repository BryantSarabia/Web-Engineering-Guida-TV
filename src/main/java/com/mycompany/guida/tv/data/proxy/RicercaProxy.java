package com.mycompany.guida.tv.data.proxy;

import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.impl.RicercaImpl;

public class RicercaProxy extends RicercaImpl implements DataItemProxy {

    private boolean modified;

    private final DataLayer dataLayer;

    public RicercaProxy(DataLayer dl) {
        super();

        this.modified = false;
        this.dataLayer = dl;
    }

    @Override
    public void setQuery(String query) {
        this.modified = true;
        super.setQuery(query);
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
