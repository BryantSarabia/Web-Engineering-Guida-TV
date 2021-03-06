package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DAO;
import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.OptimisticLockException;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.model.Programmazione;
import com.mycompany.guida.tv.data.proxy.ProgrammaProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgrammaDAO_MySQL extends DAO implements ProgrammaDAO {

    private PreparedStatement countPrograms;
    private PreparedStatement getProgrammi, getProgrammiPaginated;
    private PreparedStatement getProgrammaByID;
    private PreparedStatement getProgrammiByTitolo, getProgrammiByGenere, getProgrammiByTitoloAndGenere;
    private PreparedStatement iProgramma, uProgramma, dProgramma;

    public ProgrammaDAO_MySQL(DataLayer dl) {
        super(dl);
    }

    @Override
    public void init() throws DataException {
        super.init();

        try {

            // PREPARE STATEMENTS
            countPrograms = connection.prepareStatement("SELECT COUNT(*) AS NumeroProgrammi FROM programmi");
            getProgrammi = connection.prepareStatement("SELECT * FROM programmi");
            getProgrammaByID = connection.prepareStatement("SELECT * FROM programmi WHERE id = ?");
            getProgrammiPaginated = connection.prepareStatement("SELECT * FROM programmi LIMIT ? OFFSET ?");
            getProgrammiByTitolo = connection.prepareStatement("SELECT * FROM programmi WHERE titolo LIKE ?");
            getProgrammiByGenere = connection.prepareStatement("SELECT * FROM programmi JOIN programma_ha_generi ON programmi.id=programma_ha_generi.id_programma WHERE programma_ha_generi.id_genere=?");
            getProgrammiByTitoloAndGenere = connection.prepareStatement("SELECT * FROM programmi JOIN programma_ha_generi ON programmi.id=programma_ha_generi.id_programma WHERE programma_ha_generi.id_genere= ? AND programmi.titolo LIKE ?");
            iProgramma = connection.prepareStatement("INSERT INTO programmi(titolo, descrizione, img, link_ref, durata) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uProgramma = connection.prepareStatement("UPDATE programmi SET titolo=?, descrizione=?, img=?, link_ref=?, durata=?, version=? WHERE id=? AND version=?");
            dProgramma = connection.prepareStatement("DELETE FROM programmi WHERE id = ?");

        } catch (SQLException ex) {
            Logger.getLogger("Errore nell'inizializzazione del DAO Programma");
        }
    }

    public void destroy() throws DataException {
        try {
            /**
             * CLOSE ALL STATEMENTS
             */
            countPrograms.close();
            getProgrammi.close();
            getProgrammaByID.close();
            getProgrammiPaginated.close();
            iProgramma.close();
            uProgramma.close();
            dProgramma.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProgrammazioneDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Integer getNumeroProgrammi() throws DataException {

        Integer returnValue = -1;

        try {
            try (ResultSet rs = countPrograms.executeQuery()) {

                if (rs.next()) {
                    returnValue = rs.getInt("NumeroProgrammi");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable count programs", ex);
        }

        return returnValue;
    }

    @Override
    public List<Programma> getProgrammi() throws DataException {

        List<Programma> returnList = new ArrayList<>();

        try {
            try (ResultSet rs = getProgrammi.executeQuery()) {

                while (rs.next()) {
                    returnList.add((Programma) getProgramma(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable return program list", ex);
        }

        return returnList;

    }

    public Programma createProgramma(ResultSet rs) throws DataException {
        ProgrammaProxy p = createProgramma();

        try {

            p.setKey(rs.getInt("id"));
            p.setTitolo(rs.getString("titolo"));
            p.setDescrizione(rs.getString("descrizione"));
            p.setImg(rs.getString("img"));
            p.setLink_ref(rs.getString("link_ref"));
            p.setDurata(rs.getString("durata"));
            p.setVersion(rs.getInt("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create programma object form ResultSet", ex);
        }

        return p;
    }

    @Override
    public ProgrammaProxy createProgramma() {
        return new ProgrammaProxy(getDataLayer());
    }

    @Override
    public Programma getProgramma(int key) throws DataException {
        Programma prog = null;
        //prima vediamo se l'oggetto è già stato caricato
        if (dataLayer.getCache().has(Programma.class, key)) {
            prog = dataLayer.getCache().get(Programma.class, key);
        } else {
            //altrimenti lo carichiamo dal database
            try {
                getProgrammaByID.setInt(1, key);
                try (ResultSet rs = getProgrammaByID.executeQuery()) {
                    if (rs.next()) {
                        prog = createProgramma(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Programma.class, prog);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load programma by ID", ex);
            }
        }
        return prog;
    }

    /**
     * Effettua la ricerca dei programmi
     *
     * @param nome
     * @param genere_key
     * @return
     * @throws DataException
     */
    @Override
    public List<Programma> cercaProgrammi(String nome, int genere_key) throws DataException {
        List<Programma> returnList = new ArrayList<>();

        if (nome == null && genere_key > 0) {

            try {
                getProgrammiByGenere.setInt(1, genere_key);
                try (ResultSet rs = getProgrammiByGenere.executeQuery()) {

                    while (rs.next()) {
                        returnList.add((Programma) getProgramma(rs.getInt("id")));
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable return program list", ex);
            }

        } else if (nome != null && genere_key == 0) {

            try {
                getProgrammiByTitolo.setString(1, "%" + nome + "%");

                try (ResultSet rs = getProgrammiByTitolo.executeQuery()) {

                    while (rs.next()) {
                        returnList.add((Programma) getProgramma(rs.getInt("id")));
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable return program list", ex);
            }

        } else if (nome != null && genere_key > 0) {

            try {
                getProgrammiByTitoloAndGenere.setString(1, "%" + nome + "%");
                getProgrammiByTitoloAndGenere.setInt(2, genere_key);
                try (ResultSet rs = getProgrammiByTitoloAndGenere.executeQuery()) {
                    while (rs.next()) {
                        returnList.add((Programma) getProgramma(rs.getInt("id")));
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable return program list", ex);
            }

        } else {
            return getProgrammi();
        }

        return returnList;
    }

    @Override
    public List<Programma> getProgrammiPaginated(int start_item, int elements) throws DataException {
        List<Programma> returnList = new ArrayList<>();

        try {
            getProgrammiPaginated.setInt(1, elements);
            getProgrammiPaginated.setInt(2, start_item);
            try (ResultSet rs = getProgrammiPaginated.executeQuery()) {

                while (rs.next()) {
                    returnList.add((Programma) getProgramma(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable get elenco programmi paginato", ex);
        }

        return returnList;
    }

    @Override
    public void storeProgramma(Programma programma) throws DataException {
        try {
            if (programma.getKey() != null && programma.getKey() > 0) { //update
                // Se proxy non modificato non facciamo nulla
                if (programma instanceof DataItemProxy && !((DataItemProxy) programma).isModified()) {
                    return;
                }

                // Altrimenti
                uProgramma.setString(1, programma.getTitolo());
                uProgramma.setString(2, programma.getDescrizione());
                if (programma.getImg() != null && !programma.getImg().isBlank()) {
                    uProgramma.setString(3, programma.getImg());
                } else {
                    uProgramma.setNull(3, java.sql.Types.INTEGER);
                }
                uProgramma.setString(4, programma.getLink_ref());

                uProgramma.setString(10, programma.getDurata());

                long current_version = programma.getVersion();
                long next_version = current_version + 1;

                uProgramma.setLong(11, next_version);
                uProgramma.setInt(12, programma.getKey());
                uProgramma.setLong(13, current_version);

                if (uProgramma.executeUpdate() == 0) {
                    throw new OptimisticLockException(programma);
                }
                programma.setVersion(next_version);
            } else { //insert
                iProgramma.setString(1, programma.getTitolo());
                iProgramma.setString(2, programma.getDescrizione());
                if (programma.getImg() != null && !programma.getImg().isBlank()) {
                    iProgramma.setString(3, programma.getImg());
                } else {
                    iProgramma.setNull(3, java.sql.Types.INTEGER);
                }
                iProgramma.setString(4, programma.getLink_ref());

                iProgramma.setString(10, programma.getDurata());

                if (iProgramma.executeUpdate() == 1) {
                    //getGeneratedKeys per leggere chiave generata
                    try (ResultSet keys = iProgramma.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            programma.setKey(key);
                            dataLayer.getCache().add(Programma.class, programma);
                        }
                    }
                }
            }

            //se abbiamo un proxy, resettiamo il suo attributo dirty
            if (programma instanceof DataItemProxy) {
                ((DataItemProxy) programma).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store programma", ex);
        }
    }

    @Override
    public void deleteProgramma(int key) throws DataException {
        try {
            dProgramma.setInt(1, key);
            int rows = dProgramma.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete programma by ID", ex);
        }
    }

}
