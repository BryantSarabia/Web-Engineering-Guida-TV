package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.model.Film;
import com.mycompany.guida.tv.data.model.Genere;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.model.Programmazione;
import com.mycompany.guida.tv.data.model.Serie;
import com.mycompany.guida.tv.data.model.Utente;
import java.sql.SQLException;
import javax.sql.DataSource;

public class GuidaTVDataLayer extends DataLayer {

    public GuidaTVDataLayer(DataSource datasource) throws SQLException {
        super(datasource);
    }

    @Override
    public void init() throws DataException {
        // QUI DECIDO DI UTILIZZARE L'IMPLEMENTAZIONE DI MYSQL
        registerDAO(Utente.class, new UtenteDAO_MySQL(this));
        registerDAO(Canale.class, new CanaleDAO_MySQL(this));
        registerDAO(Programmazione.class, new ProgrammazioneDAO_MySQL(this));
        registerDAO(Programma.class, new ProgrammaDAO_MySQL(this));
        registerDAO(Film.class, new FilmDAO_MySQL(this));
        registerDAO(Genere.class, new GenereDAO_MySQL(this));
        registerDAO(Serie.class, new SerieDAO_MySQL(this));

    }

    public UtenteDAO getUtenteDAO() {
        return (UtenteDAO) getDAO(Utente.class);
    }

    /*
    public RuoloDAO getRuoloDAO() {
        return (RuoloDAO) getDAO(Ruolo.class);
    }
     */
    public CanaleDAO getCanaleDAO() {
        return (CanaleDAO) getDAO(Canale.class);
    }
    
    public SerieDAO getSerieDAO() {
        return (SerieDAO) getDAO(Serie.class);
    }

    public ProgrammaDAO getProgrammaDAO() {
        return (ProgrammaDAO) getDAO(Programma.class);
    }

    public FilmDAO getFilmDAO() {
        return (FilmDAO) getDAO(Film.class);
    }
    
    public ProgrammazioneDAO getProgrammazioneDAO() {
        return (ProgrammazioneDAO) getDAO(Programmazione.class);
    }
    /*
    public GenereDAO getGenereDAO() {
        return (GenereDAO) getDAO(Genere.class);
    }
    /*
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
