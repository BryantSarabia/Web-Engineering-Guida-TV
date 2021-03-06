package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.model.Film;
import java.sql.ResultSet;
import java.util.List;

public interface FilmDAO{
    
    Film createFilm();
    
    Film createFilm(ResultSet rs) throws DataException;
    
    Film getFilm(int key) throws DataException;
    
    int getNumeroFilm() throws DataException;
    
    List<Film> getListaFilm() throws DataException;
    
    List<Film> getListaFilm(int page, int elements) throws DataException;
    
    List<Film> getListaFilmPaginated(int start_element, int elements) throws DataException;
    
    void storeFilm(Film film) throws DataException;
    
    void deleteFilm(int key) throws DataException;
            
}
