package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.model.Serie;
import java.sql.ResultSet;
import java.util.List;

public interface SerieDAO {
     Serie createSerie();
    
    Serie createSerie(ResultSet rs) throws DataException;
    
    Serie getSerie(int key_programma) throws DataException;
    
    List<Serie> getEpisodi(int key_programma) throws DataException;
    
    Serie getEpisodio(int id_episodio) throws DataException;
    
     Serie getSerieByProgrammazione(int key_programma, int key_serie) throws DataException;
    
    int getNumeroSerie() throws DataException;
    
    List<Serie> getListaSerie() throws DataException;
    
    List<Serie> getListaSerie(int page, int elements) throws DataException;
    
    List<Serie> getListaSeriePaginated(int start_element, int elements) throws DataException;
    
    void storeSerie(Serie serie) throws DataException;
    
    void deleteSerie(int key) throws DataException;
}
