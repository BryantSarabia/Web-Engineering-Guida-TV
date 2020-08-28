package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DAO;
import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.model.Utente;
import com.mycompany.guida.tv.data.proxy.InteressaProxy;
//import com.mycompany.guida.tv.utility.UtilityMethods;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mycompany.guida.tv.data.model.Interessa;

public class InteressaDAO_MySQL extends DAO implements InteressaDAO {
    PreparedStatement getInteresseByID, getInteressiUtente, insertInteresse, deleteInteresse;

    public InteressaDAO_MySQL(DataLayer dl) {
        super(dl);
    }
    
    /**
     * Inizializza i PreparedStatements
     * @throws DataException 
     */
    @Override
    public void init() throws DataException {
        super.init();
        
        try {
            
            // PREPARE STATEMENTS
            getInteresseByID = connection.prepareStatement("SELECT * FROM interessa WHERE id = ?");
            insertInteresse = connection.prepareStatement("INSERT INTO interessa(id_canale, id_utente, start_time, end_time) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            deleteInteresse = connection.prepareStatement("DELETE from interessa WHERE id = ?");
            getInteressiUtente = connection.prepareStatement("SELECT id FROM interessa WHERE id_utente = ?");
        } catch (SQLException ex) {
            Logger.getLogger("Errore nell'inizializzazione del DAO Programmazione");
        }
    }

    /**
     * Chiude tutti i PreparedStatements
     * @throws DataException 
     */
    public void destroy() throws DataException {
        try {
            /**
             * CLOSE ALL STATEMENTS
             */ 
            insertInteresse.close();
            getInteresseByID.close();
            deleteInteresse.close();
            getInteressiUtente.close();
        } catch (SQLException ex) {
            Logger.getLogger(RicercaDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public Interessa createInteresse(ResultSet rs) throws DataException {
        InteressaProxy interesse = createInteresse();
        try {
            interesse.setKey(rs.getInt("id"));
            interesse.setId_canale(rs.getInt("id_canale"));
            interesse.setId_utente(rs.getInt("id_utente"));
            interesse.setStartTime(rs.getObject("start_time", LocalTime.class));
            interesse.setEndTime(rs.getObject("end_time", LocalTime.class));
        } catch(SQLException ex) {
            throw new DataException("Unable to create ricerca object form ResultSet", ex);
        }
        return interesse;
    }
    
    @Override
    public InteressaProxy createInteresse() {
        return new InteressaProxy(getDataLayer());
    }

    @Override
    public List<Interessa> getInteressiUtente(Utente utente) throws DataException{
        List<Interessa> ints = null;
        
        try {
            getInteressiUtente.setInt(1, utente.getKey());
            try (ResultSet rs = getInteressiUtente.executeQuery()) {
                ints = new ArrayList<>();
                
                while (rs.next()) {
                    ints.add(getInteresse(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load ricerca by Utente", ex);
        }
        
        return ints;
    }

    @Override
    public Interessa getInteresse(int key) throws DataException {
        Interessa interesse = null;
        
        if (dataLayer.getCache().has(Interessa.class, key)) {
            interesse = dataLayer.getCache().get(Interessa.class, key);      
        } else  {
            try {
                getInteresseByID.setInt(1, key);
                try (ResultSet rs = getInteresseByID.executeQuery()) {
                    if (rs.next()) {
                        interesse = createInteresse(rs);
                        dataLayer.getCache().add(Interessa.class, interesse);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load interest by ID", ex);
            }
        }
        return interesse;
    }

    @Override
    public void storeInteresse(Interessa intrs) throws DataException {
        try {
            if (intrs.getKey() != null && intrs.getKey() > 0) { 
                // UPDATE NOT IMPLEMENTED - Non necessario
            } else { 
                // INSERT INTERESSE
               
                insertInteresse.setInt(1, intrs.getCanale().getKey());
                insertInteresse.setInt(2, intrs.getUtente().getKey());
                // SetObject con LocalTime da problemi! Quindi toString
                insertInteresse.setObject(3, intrs.getStartTime().toString());
                insertInteresse.setObject(4, intrs.getEndTime().toString());
                
                //UtilityMethods.debugConsole(this.getClass(), "update", insertInteresse.toString());
                
                if (insertInteresse.executeUpdate() == 1) {
                    //getGeneratedKeys per leggere chiave generata
                    try (ResultSet keys = insertInteresse.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            intrs.setKey(key);
                            dataLayer.getCache().add(Interessa.class, intrs);
                        }
                    }
                }
            }

            if (intrs instanceof DataItemProxy) {
                ((DataItemProxy) intrs).setModified(false);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to store interest", ex);
        }
    }

    @Override
    public boolean removeInteresse(int key) throws DataException {
        boolean deleted = false;
        Interessa target = getInteresse(key);
        if(target != null) {
            try {
                deleteInteresse.setInt(1, key);
                int rowCount = deleteInteresse.executeUpdate();
                if( dataLayer.getCache().has(Interessa.class, key) ) {
                    dataLayer.getCache().delete(Interessa.class, key);
                }
                if(rowCount > 0) deleted = true;
            } catch (SQLException ex) {
                throw new DataException("Unable to delete interest", ex);
            }
        }
        return deleted;
    }
    
}
