/*package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DAO;
import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.OptimisticLockException;
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
            uSerie = connection.prepareStatement("UPDATE serie SET id_programma=?, stagione=?, episodio=?, version=? WHERE id_programma = ? AND version = ?");
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
 /*           getSeries.close();
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
        List<Serie> returnList = new ArrayList<>();
        
        try {
            getSeriesPaginate.setInt(1, elements);
            getSeriesPaginate.setInt(2, start_element);
            try (ResultSet rs = getSeriesPaginate.executeQuery()) {
            
                while(rs.next()) {
                    returnList.add((Serie) getSerie(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable get elenco serie paginato", ex);
        }
         
        
        return returnList;
    }
    
    @Override
    public void storeSerie(Serie serie) throws DataException{
        try {
            if (serie.getKey() != null && serie.getKey() > 0) { //update
                // Se proxy non modificato non facciamo nulla
                if (serie instanceof DataItemProxy && !((DataItemProxy) serie).isModified()) {
                    return;
                }
                
                // Altrimenti
                uProgramma.setString(1, serie.getTitolo());
                uProgramma.setString(5, serie.getDurata());
                
                if (serie.getDescrizione() != null && !serie.getDescrizione().isBlank()) {
                    uProgramma.setString(2, serie.getDescrizione());
                } else {
                    uProgramma.setNull(2, java.sql.Types.VARCHAR);
                }
                
                if (serie.getImg() != null && !serie.getImg().isBlank()) {
                    uProgramma.setString(3, serie.getImg());
                } else {
                    uProgramma.setNull(3, java.sql.Types.VARCHAR);
                }
                
                if (serie.getLink_ref() != null && !serie.getLink_ref().isBlank()) {
                    uProgramma.setString(4, serie.getLink_ref());
                } else {
                    uProgramma.setNull(4, java.sql.Types.VARCHAR);
                }
                
                long current_version = serie.getVersion();
                long next_version = current_version + 1;
                

                uProgramma.setLong(6, next_version);
                uProgramma.setInt(7, serie.getKey());
                uProgramma.setLong(8, current_version);

                if (uProgramma.executeUpdate() == 0) {
                    throw new OptimisticLockException(serie);
                }
                serie.setVersion(next_version);
                
                //update tabella serie (!!!Dubbi su key e version!!!)
                uSerie.setInt(1, serie.getKey());
                uSerie.setInt(2, serie.getStagione());
                uSerie.setInt(3, serie.getEpisodio());
                uSerie.setLong(4, next_version);
                uSerie.setInt(5, serie.getKey());
                uSerie.setLong(6, current_version);
            } else { //insert
                if(/*getProgramma(key).isNotEmpty()*/){         //se il programma esiste già e voglio solo aggiungere un episodio
                    iSerie.setInt(1, key);
                    iSerie.setInt(2, serie.getStagione());
                    iSerie.setInt(3, serie.getEpisodio());
                } else {
                    
                iProgramma.setString(1, serie.getTitolo());
                iProgramma.setString(5, serie.getDurata());
                
                if (serie.getDescrizione() != null && !serie.getDescrizione().isBlank()) {
                    iProgramma.setString(2, serie.getDescrizione());
                } else {
                    iProgramma.setNull(2, java.sql.Types.VARCHAR);
                }
                
                if (serie.getImg() != null && !serie.getImg().isBlank()) {
                    iProgramma.setString(3, serie.getImg());
                } else {
                    iProgramma.setNull(3, java.sql.Types.VARCHAR);
                }
                
                if (serie.getLink_ref() != null && !serie.getLink_ref().isBlank()) {
                    iProgramma.setString(4, serie.getLink_ref());
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
                            serie.setKey(key);
                            dataLayer.getCache().add(Serie.class, serie);
                        }
                    }
                }
                
                //Aggiungo anche la foreign key nella tabella films
                    iSerie.setInt(1, id_programma);
                    iSerie.setInt(2, serie.getStagione());
                    iSerie.setInt(3, serie.getEpisodio());
                    iSerie.executeUpdate();
                }

            //se abbiamo un proxy, resettiamo il suo attributo dirty
            if (serie instanceof DataItemProxy) {
                ((DataItemProxy) serie).setModified(false);
            }
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store serie", ex);
        }
    }
    
    @Override
    public void deleteSerie(int key) throws DataException{
        try {
            dProgramma.setInt(1, key);
            if(dProgramma.executeUpdate() == 1){
                dSerie.setInt(1, key);
                if(dSerie.executeUpdate() != 1){
                    throw new SQLException("Unable to delete child row in serie table");
                }
            } else throw new SQLException();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete serie by ID", ex);
        }
    }
    
}
*/