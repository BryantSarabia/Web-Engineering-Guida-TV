package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DAO;
import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.OptimisticLockException;
import com.mycompany.guida.tv.data.model.Film;
import com.mycompany.guida.tv.data.proxy.FilmProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilmDAO_MySQL extends DAO implements FilmDAO{
    
    private PreparedStatement getFilms, getFilmByID, getFilmsPaginate, getNumeroFilms, iProgramma, iFilm, uProgramma, uFilm, dProgramma, dFilm;
    
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
            iProgramma = connection.prepareStatement("INSERT INTO programmi(titolo, descrizione, img, link_ref, durata) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            iFilm = connection.prepareStatement("INSERT INTO films(id_programma) VALUES (?)");
            uProgramma = connection.prepareStatement("UPDATE programmi SET titolo=?, descrizione=?, img=?, link_ref=?, durata=?, version=? WHERE id = ? AND version = ?");
            uFilm = connection.prepareStatement("UPDATE films SET id_programma=?, version=? WHERE id = ? AND version = ?");
            dProgramma = connection.prepareStatement("DELETE FROM programmi WHERE id = ?");
            dFilm = connection.prepareStatement("DELETE FROM films WHERE id_programma = ?");
            
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
    
    @Override
    public Film createFilm(ResultSet rs) throws DataException{
        FilmProxy film = createFilm();
        
        try{
            film.setKey(rs.getInt("id"));
            film.setVersion(rs.getInt("version"));
            film.setTitolo(rs.getString("titolo"));
            film.setDescrizione(rs.getString("descrizione"));
            film.setImg(rs.getString("img"));
            film.setLink_ref(rs.getString("link_ref"));
            film.setDurata(rs.getString("durata"));
            
        } catch (SQLException ex){
            throw new DataException("Unable to create canale object form ResultSet", ex);
        }
        
        return film;
    }
    
    @Override
    public Film getFilm(int key) throws DataException {
        Film film = null;
        
        if (dataLayer.getCache().has(Film.class, key)) {
            film = dataLayer.getCache().get(Film.class, key);
        } else {
            try {
                getFilmByID.setInt(1, key);
                try (ResultSet rs = getFilmByID.executeQuery()) {
                    if (rs.next()) {
                        film = createFilm(rs);
                        dataLayer.getCache().add(Film.class, film);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load film by ID", ex);
            }
        }
        return film;
    }

    @Override
    public int getNumeroFilm() throws DataException {
        int result = 0;
        
        try {
            try (ResultSet rs = getNumeroFilms.executeQuery()) {
            
                while(rs.next()) {
                    result = rs.getInt("num");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to get numero film", ex);
        }
        
        return result;
    }

    @Override
    public List<Film> getListaFilm() throws DataException {
        List<Film> returnList = new ArrayList<>();
        
        try {
            try (ResultSet rs = getFilms.executeQuery()) {
            
                while(rs.next()) {
                    returnList.add((Film) getFilm(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable get elenco canali", ex);
        }
         
        
        return returnList;
    }

    @Override
    public List<Film> getListaFilm(int page, int elements) throws DataException {
        List<Film> returnList = new ArrayList<>();
        
        try {
            getFilmsPaginate.setInt(1, elements);
            getFilmsPaginate.setInt(2, page * elements);
            try (ResultSet rs = getFilmsPaginate.executeQuery()) {
            
                while(rs.next()) {
                    returnList.add((Film) getFilm(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to get elenco film paginato", ex);
        }
         
        
        return returnList;
    }

    @Override
    public List<Film> getListaFilmPaginated(int first_index, int elements) throws DataException {
        List<Film> returnList = new ArrayList<>();
        
        try {
            getFilmsPaginate.setInt(1, elements);
            getFilmsPaginate.setInt(2, first_index);
            try (ResultSet rs = getFilmsPaginate.executeQuery()) {
            
                while(rs.next()) {
                    returnList.add((Film) getFilm(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable get elenco film paginato", ex);
        }
         
        
        return returnList;
    }

    @Override
    public void storeFilm(Film film) throws DataException {
        try {
            if (film.getKey() != null && film.getKey() > 0) { //update
                // Se proxy non modificato non facciamo nulla
                if (film instanceof DataItemProxy && !((DataItemProxy) film).isModified()) {
                    return;
                }
                
                // Altrimenti
                uProgramma.setString(1, film.getTitolo());
                uProgramma.setString(5, film.getDurata());
                
                if (film.getDescrizione() != null && !film.getDescrizione().isBlank()) {
                    uProgramma.setString(2, film.getDescrizione());
                } else {
                    uProgramma.setNull(2, java.sql.Types.VARCHAR);
                }
                
                if (film.getImg() != null && !film.getImg().isBlank()) {
                    uProgramma.setString(3, film.getImg());
                } else {
                    uProgramma.setNull(3, java.sql.Types.VARCHAR);
                }
                
                if (film.getLink_ref() != null && !film.getLink_ref().isBlank()) {
                    uProgramma.setString(4, film.getLink_ref());
                } else {
                    uProgramma.setNull(4, java.sql.Types.VARCHAR);
                }
                
                long current_version = film.getVersion();
                long next_version = current_version + 1;
                

                uProgramma.setLong(6, next_version);
                uProgramma.setInt(7, film.getKey());
                uProgramma.setLong(8, current_version);

                if (uProgramma.executeUpdate() == 0) {
                    throw new OptimisticLockException(film);
                }
                film.setVersion(next_version);
            } else { //insert
                iProgramma.setString(1, film.getTitolo());
                iProgramma.setString(5, film.getDurata());
                
                if (film.getDescrizione() != null && !film.getDescrizione().isBlank()) {
                    iProgramma.setString(2, film.getDescrizione());
                } else {
                    iProgramma.setNull(2, java.sql.Types.VARCHAR);
                }
                
                if (film.getImg() != null && !film.getImg().isBlank()) {
                    iProgramma.setString(3, film.getImg());
                } else {
                    iProgramma.setNull(3, java.sql.Types.VARCHAR);
                }
                
                if (film.getLink_ref() != null && !film.getLink_ref().isBlank()) {
                    iProgramma.setString(4, film.getLink_ref());
                } else {
                    iProgramma.setNull(4, java.sql.Types.VARCHAR);
                }
                
                int id_programma = 0;
                
                if (iProgramma.executeUpdate() == 1) {
                    //getGeneratedKeys per leggere chiave generata
                    try (ResultSet keys = iProgramma.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            id_programma = key;
                            film.setKey(key);
                            dataLayer.getCache().add(Film.class, film);
                        }
                    }
                }
                
                //Aggiungo anche la foreign key nella tabella films
                iFilm.setInt(1, id_programma);
                iFilm.executeUpdate();
            }

            //se abbiamo un proxy, resettiamo il suo attributo dirty
            if (film instanceof DataItemProxy) {
                ((DataItemProxy) film).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store film", ex);
        }
    }

    @Override
    public void deleteFilm(int key) throws DataException {
        try {
            dProgramma.setInt(1, key);
            if(dProgramma.executeUpdate() == 1){
                dFilm.setInt(1, key);
                if(dFilm.executeUpdate() != 1){
                    throw new SQLException("Unable to delete child row in films table");
                }
            } else throw new SQLException();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete canale by ID", ex);
        }
    }
}
