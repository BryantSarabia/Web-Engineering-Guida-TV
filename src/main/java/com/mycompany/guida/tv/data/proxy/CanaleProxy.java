package com.mycompany.guida.tv.data.proxy;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.dao.ProgrammazioneDAO;
import com.mycompany.guida.tv.data.impl.CanaleImpl;
import com.mycompany.guida.tv.data.model.Programmazione;
import java.util.logging.Level;
import java.util.logging.Logger;


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
    
       @Override
    public void setProgrammazioneCorrente(Programmazione corrente) {
        super.setProgrammazioneCorrente(corrente);
        this.modified = true;
    }
    
    @Override
    public Programmazione getProgrammazioneCorrente() {
        
        if (super.getProgrammazioneCorrente() == null) {
            try {
                super.setProgrammazioneCorrente(((ProgrammazioneDAO) dataLayer.getDAO(Programmazione.class)).currentProgram(getKey()));
            } catch (DataException ex) {
                Logger.getLogger(ProgrammazioneProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return super.getProgrammazioneCorrente();
    }
}
