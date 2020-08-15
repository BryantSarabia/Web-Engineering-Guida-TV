package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DAO;
import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.model.Serie;
import com.mycompany.guida.tv.data.proxy.SerieProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerieDAO_MySQL extends DAO implements SerieDAO{
    private PreparedStatement getSeries, getSerieByID, getSeriesPaginate, getNumeroSerie, iProgramma, iSerie, uProgramma, uSerie, dProgramma, dSerie;
    
    public SerieDAO_MySQL(DataLayer dl) {
        super(dl);
    }
    
    @Override
    public void init() throws DataException {
        super.init();
        
        try {
            
            getSeries = connection.prepareStatement("SELECT * FROM programmi JOIN serie ON programmi.id = serie.id_programma ORDER BY titolo ASC");
            getSeriesPaginate = connection.prepareStatement("SELECT * FROM programmi JOIN serie ON programmi.id = serie.id_programma ORDER BY titolo ASC LIMIT ? OFFSET ?");
            getSerieByID = connection.prepareStatement("SELECT * FROM programmi JOIN serie ON programmi.id = serie.id_programma WHERE serie.id=?");
            getNumeroSerie = connection.prepareStatement("SELECT COUNT(*) AS num FROM serie");
            iProgramma = connection.prepareStatement("INSERT INTO programmi(titolo, descrizione, img, link_ref, durata) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            iSerie = connection.prepareStatement("INSERT INTO serie(id_programma,stagione,episodio) VALUES (?,?,?)");
            uProgramma = connection.prepareStatement("UPDATE programmi SET titolo=?, descrizione=?, img=?, link_ref=?, durata=?, version=? WHERE id = ? AND version = ?");
            uSerie = connection.prepareStatement("UPDATE serie SET id_programma=?, stagione=?, episodio=?, version=? WHERE id = ? AND version = ?");
            dProgramma = connection.prepareStatement("DELETE FROM programmi WHERE id = ?");
            dSerie = connection.prepareStatement("DELETE FROM serie WHERE id_programma = ?");
            
        } catch (SQLException ex) {
            Logger.getLogger("Errore nell'inizializzazione del DAO Film");
        }
    }
    
    public void destroy() {
        try {
            /**
             * CLOSE ALL STATEMENTS
             */ 
            getSeries.close();
            getSerieByID.close();
            getSeriesPaginate.close();
            getNumeroSerie.close();
            iProgramma.close();
            iSerie.close();
            uProgramma.close();
            uSerie.close();
            dProgramma.close();
            dSerie.close();
        } catch (SQLException ex) {
            Logger.getLogger(UtenteDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public SerieProxy createSerie(){
        return new SerieProxy(getDataLayer());
    }
    
    @Override
    public Serie createSerie(ResultSet rs) throws DataException{
        SerieProxy serie = createSerie();
        
        try{
            serie.setKey(rs.getInt("id"));
            serie.setVersion(rs.getInt("version"));
            serie.setTitolo(rs.getString("titolo"));
            serie.setDescrizione(rs.getString("descrizione"));
            serie.setImg(rs.getString("img"));
            serie.setLink_ref(rs.getString("link_ref"));
            serie.setDurata(rs.getString("durata"));
            serie.setStagione(rs.getInt("stagione"));
            serie.setEpisodio(rs.getInt("episodio"));
            
        } catch (SQLException ex){
            throw new DataException("Unable to create serie object form ResultSet", ex);
        }
        
        return serie;
    }
    
    @Override
    public Serie getSerie(int key) throws DataException{
        Serie serie = null;
        
        if (dataLayer.getCache().has(Serie.class, key)) {
            serie = dataLayer.getCache().get(Serie.class, key);
        } else {
            try {
                getSerieByID.setInt(1, key);
                try (ResultSet rs = getSerieByID.executeQuery()) {
                    if (rs.next()) {
                        serie = createSerie(rs);
                        dataLayer.getCache().add(Serie.class, serie);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load serie by ID", ex);
            }
        }
        return serie;
    }
    
    @Override
    public int getNumeroSerie() throws DataException{
        int result = 0;
        
        try {
            try (ResultSet rs = getNumeroSerie.executeQuery()) {
            
                while(rs.next()) {
                    result = rs.getInt("num");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to get numero serie", ex);
        }
        
        return result;
    }
    
    @Override
    public List<Serie> getListaSerie() throws DataException{
        List<Serie> returnList = new ArrayList<>();
        
        try {
            try (ResultSet rs = getSeries.executeQuery()) {
            
                while(rs.next()) {
                    returnList.add((Serie) getSerie(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable get elenco serie", ex);
        }
         
        
        return returnList;
    }
    
    @Override
    public List<Serie> getListaSerie(int page, int elements) throws DataException{
        List<Serie> returnList = new ArrayList<>();
        
        try {
            getSeriesPaginate.setInt(1, elements);
            getSeriesPaginate.setInt(2, page * elements);
            try (ResultSet rs = getSeriesPaginate.executeQuery()) {
            
                while(rs.next()) {
                    returnList.add((Serie) getSerie(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to get elenco serie paginato", ex);
        }
         
        
        return returnList;
    }
    
    @Override
    public List<Serie> getListaSeriePaginated(int start_element, int elements) throws DataException{
        return null;
    }
    
    @Override
    public void storeSerie(Serie serie) throws DataException{
        
    }
    
    @Override
    public void deleteSerie(int key) throws DataException{
        
    }
    
}
