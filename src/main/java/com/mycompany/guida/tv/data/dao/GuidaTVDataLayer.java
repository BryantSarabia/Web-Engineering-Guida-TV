package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.model.Utente;
import java.sql.SQLException;
import javax.sql.DataSource;

public class GuidaTVDataLayer extends DataLayer{
    
        public GuidaTVDataLayer(DataSource datasource) throws SQLException {
        super(datasource);
    }
    
       @Override
    public void init() throws DataException {
        // QUI DECIDO DI UTILIZZARE L'IMPLEMENTAZIONE DI MYSQL
        registerDAO(Utente.class, new UtenteDAO_MySQL(this));
    }
    
    public UtenteDAO getUtenteDAO() {
        return (UtenteDAO) getDAO(Utente.class);
    }
    
    /*
    public RuoloDAO getRuoloDAO() {
        return (RuoloDAO) getDAO(Ruolo.class);
    }
    
    public CanaleDAO getCanaleDAO() {
        return (CanaleDAO) getDAO(Canale.class);
    }
    
    public ProgrammaDAO getProgrammaDAO() {
        return (ProgrammaDAO) getDAO(Programma.class);
    }
    
    public ProgrammazioneDAO getProgrammazioneDAO() {
        return (ProgrammazioneDAO) getDAO(Programmazione.class);
    }
    
    public GenereDAO getGenereDAO() {
        return (GenereDAO) getDAO(Genere.class);
    }
    
    public ClassificazioneDAO getClassificazioneDAO() {
        return (ClassificazioneDAO) getDAO(Classificazione.class);
    }
    
    public RicercaDAO getRicercaDAO() {
        return (RicercaDAO) getDAO(Ricerca.class);
    }
    
    public InteresseDAO getInteresseDAO() {
        return (InteresseDAO) getDAO(Interesse.class);
    } */
    
}
