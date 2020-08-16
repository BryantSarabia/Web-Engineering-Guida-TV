package com.mycompany.guida.tv.data.proxy;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.dao.CanaleDAO;
import com.mycompany.guida.tv.data.dao.FilmDAO;
import com.mycompany.guida.tv.data.dao.ProgrammaDAO;
import com.mycompany.guida.tv.data.dao.ProgrammazioneDAO;
import com.mycompany.guida.tv.data.dao.SerieDAO;
import com.mycompany.guida.tv.data.impl.ProgrammazioneImpl;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.model.Film;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.model.Programmazione;
import com.mycompany.guida.tv.data.model.Serie;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgrammazioneProxy extends ProgrammazioneImpl implements DataItemProxy {

    private boolean modified;
    private int canale_key, programma_key;

    private final DataLayer dataLayer;

    public ProgrammazioneProxy(DataLayer dataLayer) {
        super();

        this.modified = false;
        this.canale_key = 0;
        this.programma_key = 0;

        this.dataLayer = dataLayer;
    }

    public int getCanale_key() {
        return canale_key;
    }

    public void setCanale_key(int canale_key) {
        this.canale_key = canale_key;
    }

    public int getProgramma_key() {
        return programma_key;
    }

    public void setProgramma_key(int programma_key) {
        this.programma_key = programma_key;
    }

    @Override
    public boolean isModified() {
        return this.modified;
    }

    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    @Override
    public Canale getCanale() {

        if (super.getCanale() == null && canale_key > 0) {
            try {
                super.setCanale(((CanaleDAO) dataLayer.getDAO(Canale.class)).getCanale(canale_key));
            } catch (DataException ex) {
                Logger.getLogger(ProgrammazioneProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return super.getCanale();
    }

    @Override
    public void setCanale(Canale canale) {
        super.setCanale(canale);
        this.modified = true;
    }

    @Override
    public Programma getProgramma() {
        
        if (super.getProgramma() == null && programma_key > 0) {
            //UtilityMethods.debugConsole(this.getClass(), "getProgramma()", "Getting Program " + programma_key + " datalayer: " + dataLayer + " obj: " + dataLayer.getDAO(Programma.class));
            try {
                
                Programma programma = ((FilmDAO) dataLayer.getDAO(Film.class)).getFilm(programma_key);
                if(programma == null) {
                    programma = ((SerieDAO) dataLayer.getDAO(Serie.class)).getSerie(programma_key);
                }
                
                if(programma == null) {
                    programma = ((ProgrammaDAO) dataLayer.getDAO(Programma.class)).getProgramma(programma_key);
                }
                
                if(programma == null) {
                    throw new DataException("The program doesn't exist");
                } else {
                    //super.setProgramma(programma);
                }
                
                super.setProgramma(programma);
                
                //super.setProgramma(((FilmDAO) dataLayer.getDAO(Film.class)).getFilm(programma_key));
            } catch (DataException ex) {
                Logger.getLogger(ProgrammazioneProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return super.getProgramma();
         
    }

    @Override
    public void setProgramma(Programma programma) {
        super.setProgramma(programma);
        this.modified = true;
    }

    @Override
    public void setStartTime(LocalDateTime time) {
        super.setStartTime(time);
        this.modified = true;
    }

    @Override
    public void setDurata(Integer durata) {
        super.setDurata(durata);
        this.modified = true;
    }

}
