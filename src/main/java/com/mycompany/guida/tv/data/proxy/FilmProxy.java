package com.mycompany.guida.tv.data.proxy;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.dao.GenereDAO;
import com.mycompany.guida.tv.data.impl.FilmImpl;
import com.mycompany.guida.tv.data.model.Genere;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FilmProxy extends FilmImpl implements DataItemProxy {
    private boolean modified;
    protected final DataLayer dataLayer;

    public FilmProxy(DataLayer dataLayer) {
        super();
        this.modified = false;
        this.dataLayer = dataLayer;
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
