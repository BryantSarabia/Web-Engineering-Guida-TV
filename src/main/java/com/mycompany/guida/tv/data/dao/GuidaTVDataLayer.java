package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.model.*;

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
        registerDAO(Ricerca.class, new RicercaDAO_MySQL(this));
        registerDAO(Interessa.class, new InteressaDAO_MySQL(this));
        registerDAO(Ruolo.class, new RuoloDAO_MySQL(this));
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

    public GenereDAO getGenereDAO() {
        return (GenereDAO) getDAO(Genere.class);
    }

    public RicercaDAO getRicercaDAO() {
        return (RicercaDAO) getDAO(Ricerca.class);
    }

    public InteressaDAO getInteressaDAO() {
        return (InteressaDAO) getDAO(Interessa.class);
    }

    public RuoloDAO getRuoloDAO() {
        return (RuoloDAO) getDAO(Ruolo.class);
    }

}
