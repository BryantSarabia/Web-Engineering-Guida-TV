package com.mycompany.guida.tv.data.proxy;

import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.impl.CanaleImpl;


public class CanaleProxy extends CanaleImpl implements DataItemProxy {
    private boolean modified;
    protected final DataLayer dataLayer;

    public CanaleProxy( DataLayer dataLayer) {
        super();
        this.modified = false;
        this.dataLayer = dataLayer;
    }

    @Override
    public void setNome(String nome) {
        this.modified = true;
        super.setNome(nome);
    }

    @Override
    public void setNumero(int numero) {
        this.modified = true;
        super.setNumero(numero);
    }

    @Override
    public void setLogo(String logo) {
        this.modified = true;
        super.setLogo(logo);
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
