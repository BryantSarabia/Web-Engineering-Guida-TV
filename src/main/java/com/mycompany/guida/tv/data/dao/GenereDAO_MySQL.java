package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DAO;
import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.OptimisticLockException;
import com.mycompany.guida.tv.data.impl.GenereImpl;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.model.Genere;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.proxy.GenereProxy;
import com.mycompany.guida.tv.shared.Methods;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenereDAO_MySQL extends DAO implements GenereDAO {

    private PreparedStatement getGenereByID, getGeneri, getGeneriByProg;
    private PreparedStatement getNumeroGeneri, getGeneriPaginated;
    private PreparedStatement iGenere, uGenere, dGenere;

    public GenereDAO_MySQL(DataLayer dl) {
        super(dl);
    }

    @Override
    public void init() throws DataException {
        super.init();

        try {

            getGenereByID = connection.prepareStatement("SELECT * FROM generi WHERE id = ?");
            getGeneriByProg = connection.prepareStatement("SELECT generi.id, generi.nome FROM generi JOIN programma_ha_generi ON generi.id = programma_ha_generi.id_genere JOIN programmi ON programmi.id = programma_ha_generi.id_programma WHERE programmi.id=? ");
            getGeneri = connection.prepareStatement("SELECT * FROM generi ORDER BY nome");
            getNumeroGeneri = connection.prepareStatement("SELECT COUNT(*) AS num FROM generi");
            getGeneriPaginated = connection.prepareStatement("SELECT * FROM generi LIMIT ? OFFSET ?");
            iGenere = connection.prepareStatement("INSERT INTO generi(nome) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            uGenere = connection.prepareStatement("UPDATE generi SET nome=?, version=? WHERE id = ? AND version = ?");
            dGenere = connection.prepareStatement("DELETE FROM generi WHERE id = ?");

        } catch (SQLException ex) {
            Logger.getLogger("Errore nell'inizializzazione del DAO Genere");
        }
    }

    public void destroy() {

        try {
            /**
             * CLOSE ALL STATEMENTS
             */
            getGenereByID.close();
            getGeneri.close();
            getNumeroGeneri.close();
            getGeneriPaginated.close();
            iGenere.close();
            uGenere.close();
            dGenere.close();
        } catch (SQLException ex) {
            Logger.getLogger(UtenteDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Genere getGenere(int key) throws DataException {

        Genere genere = null;

        if (dataLayer.getCache().has(Genere.class, key)) {
            // Se l'oggett è in cache lo restituisco
            genere = dataLayer.getCache().get(Genere.class, key);
        } else {
            //altrimenti lo carichiamo dal database
            try {
                getGenereByID.setInt(1, key);
                try (ResultSet rs = getGenereByID.executeQuery()) {
                    if (rs.next()) {
                        genere = createGenere(rs);
                        //e lo mettiamo anche nella cache
                        dataLayer.getCache().add(Genere.class, genere);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load genere by ID", ex);
            }
        }
        return genere;
    }

    public Genere createGenere(ResultSet rs) throws DataException {
        GenereProxy g = createGenere();
        try {
            g.setKey(rs.getInt("id"));
            g.setNome(rs.getString("nome"));
            g.setVersion(rs.getInt("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create genere object form ResultSet", ex);
        }
        return g;
    }

    @Override
    public GenereProxy createGenere() {
        return new GenereProxy(getDataLayer());
    }

    @Override
    public List<Genere> getGeneri() throws DataException {
        List<Genere> returnList = new ArrayList<>();

        try {
            try (ResultSet rs = getGeneri.executeQuery()) {

                while (rs.next()) {
                    returnList.add((Genere) getGenere(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable return genre list", ex);
        }

        return returnList;
    }

    @Override
    public int getNumeroGeneri() throws DataException {
        int result = 0;

        try {
            try (ResultSet rs = getNumeroGeneri.executeQuery()) {

                while (rs.next()) {
                    result = rs.getInt("num");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable get numero generi", ex);
        }

        return result;
    }

    @Override
    public List<Genere> getGeneriPaginated(int start_item, int elements) throws DataException {
        List<Genere> returnList = new ArrayList<>();

        try {
            getGeneriPaginated.setInt(1, elements);
            getGeneriPaginated.setInt(2, start_item);
            try (ResultSet rs = getGeneriPaginated.executeQuery()) {

                while (rs.next()) {
                    returnList.add((Genere) getGenere(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable get elenco generi paginato", ex);
        }

        return returnList;
    }

    @Override
    public void storeGenere(Genere genere) throws DataException {
        try {
            if (genere.getKey() != null && genere.getKey() > 0) { //update
                // Se proxy non modificato non facciamo nulla
                if (genere instanceof DataItemProxy && !((DataItemProxy) genere).isModified()) {
                    return;
                }

                // Altrimenti
                uGenere.setString(1, genere.getNome());

                long current_version = genere.getVersion();
                long next_version = current_version + 1;

                uGenere.setLong(2, next_version);
                uGenere.setInt(3, genere.getKey());
                uGenere.setLong(4, current_version);

                if (uGenere.executeUpdate() == 0) {
                    throw new OptimisticLockException(genere);
                }
                genere.setVersion(next_version);
            } else { //insert
                iGenere.setString(1, genere.getNome());

                if (iGenere.executeUpdate() == 1) {
                    //getGeneratedKeys per leggere chiave generata
                    try (ResultSet keys = iGenere.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            genere.setKey(key);
                            dataLayer.getCache().add(Genere.class, genere);
                        }
                    }
                }
            }

            //se abbiamo un proxy, resettiamo il suo attributo dirty
            if (genere instanceof DataItemProxy) {
                ((DataItemProxy) genere).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store genere", ex);
        }
    }

    @Override
    public void deleteGenere(int key) throws DataException {
        try {
            dGenere.setInt(1, key);
            int rows = dGenere.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete genere by ID", ex);
        }
    }

    @Override
    public List<Genere> getGeneri(int key) throws DataException {
        List<Genere> returnList = new ArrayList<>();

        try {

            getGeneriByProg.setInt(1, key);
            try (ResultSet rs = getGeneriByProg.executeQuery()) {

                while (rs.next()) {
                    returnList.add((Genere) getGenere(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable return genre list", ex);
        }

        return returnList;
    }

}
