package com.mycompany.guida.tv.data.proxy;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.dao.GenereDAO;

import com.mycompany.guida.tv.data.impl.ProgrammaImpl;
import com.mycompany.guida.tv.data.model.Genere;
import com.mycompany.guida.tv.shared.Methods;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgrammaProxy extends ProgrammaImpl implements DataItemProxy {
    private boolean modified;
    protected final DataLayer dataLayer;

    public ProgrammaProxy( DataLayer dataLayer) {
        super();
        this.modified = false;
        this.dataLayer = dataLayer;
    }

    @Override
    public void setTitolo(String titolo) {
        this.modified = true;
        super.setTitolo(titolo);
    }

    @Override
    public void setDescrizione(String descrizione) {
        this.modified = true;
        super.setDescrizione(descrizione);
    }

    @Override
    public void setImg(String img) {
        this.modified = true;
        super.setImg(img);
    }

    @Override
    public void setLink_ref(String link_ref) {
        this.modified = true;
        super.setLink_ref(link_ref);
    }

    @Override
    public void setDurata(String durata) {
        this.modified = true;
        super.setDurata(durata);
    }

    @Override
    public void setGeneri(List<Genere> generi) {
        this.modified = true;
        super.setGeneri(generi);
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
    
       @Override
    public List<Genere> getGeneri() {
        if( super.getGeneri() == null ) {
            try {
                super.setGeneri(((GenereDAO) dataLayer.getDAO(Genere.class)).getGeneri(this.getKey()));
                
            } catch (DataException ex) {
                Logger.getLogger(ProgrammaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getGeneri();
    }
}