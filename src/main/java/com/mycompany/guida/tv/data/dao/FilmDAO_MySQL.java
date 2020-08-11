package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DAO;
import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.model.Film;
import com.mycompany.guida.tv.data.proxy.FilmProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilmDAO_MySQL extends DAO implements FilmDAO{
    
    private PreparedStatement getFilms, getFilmByID, getFilmsPaginate, getNumeroFilms, iFilm, uFilm, dFilm;
    
    public FilmDAO_MySQL(DataLayer dl) {
        super(dl);
    }
    
    @Override
    public void init() throws DataException {
        super.init();
        
        try {
            
            getFilms = connection.prepareStatement("SELECT * FROM programmi JOIN films ON programmi.id = films.id_programma ORDER BY titolo ASC");
            getFilmsPaginate = connection.prepareStatement("SELECT * FROM programmi JOIN films ON programmi.id = films.id_programma ORDER BY titolo ASC LIMIT ? OFFSET ?");
            getFilmByID = connection.prepareStatement("SELECT * FROM programmi JOIN films ON programmi.id = films.id_programma WHERE films.id=?");
            getNumeroFilms = connection.prepareStatement("SELECT COUNT(*) AS num FROM films");
            iFilm = connection.prepareStatement("INSERT INTO films(id_programma VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            uFilm = connection.prepareStatement("UPDATE films SET id_programma=?, version=? WHERE ID = ? AND Version = ?");
            dFilm = connection.prepareStatement("DELETE FROM films WHERE id = ?");
            
        } catch (SQLException ex) {
            Logger.getLogger("Errore nell'inizializzazione del DAO Film");
        }
    }
    
    public void destroy() {
        try {
            /**
             * CLOSE ALL STATEMENTS
             */ 
            getFilms.close();
            getFilmByID.close();
            getFilmsPaginate.close();
            getNumeroFilms.close();
            iFilm.close();
            uFilm.close();
            dFilm.close();
        } catch (SQLException ex) {
            Logger.getLogger(UtenteDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public FilmProxy createFilm() {
        return new FilmProxy(getDataLayer());
    }
    
    
    public Film createFilm(ResultSet rs) throws DataException{
        FilmProxy film = createFilm();
        
        try{
            film.setKey(rs.getInt("id"));
            film.setVersion(rs.getInt("version"));
            film.setTitolo(rs.getString("titolo"));
            
        } catch (SQLException ex){
            throw new DataException("Unable to create canale object form ResultSet", ex);
        }
    }
    
    @Override
    public Film getFilm(int arg0) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNumeroFilm() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Film> getListaFilm() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Film> getListaFilm(int arg0, int arg1) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Film> getListaFilmPaginated(int arg0, int arg1) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeFilm(Film arg0) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteFilm(Film arg0) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
