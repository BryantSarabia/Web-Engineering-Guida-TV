package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DAO;
import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.OptimisticLockException;
import com.mycompany.guida.tv.data.model.Utente;
import com.mycompany.guida.tv.data.proxy.UtenteProxy;
//import com.mycompany.guida.tv.utility.UtilityMethods;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtenteDAO_MySQL extends DAO implements UtenteDAO {

    private PreparedStatement countUsers, getUtenteByID, insertUtente, updateUtente, loginUtente, getUtenteByEmail, getUtenteByToken, getUtentiSendEmail, getUtentiPaginated, dUtente, checkToken;

    public UtenteDAO_MySQL(DataLayer dl) {
        super(dl);
    }

    @Override
    public void init() throws DataException {
        super.init();

        try {

            // PREPARE STATEMENTS
            countUsers = connection.prepareStatement("SELECT COUNT(*) AS NumeroUtenti FROM utenti");
            getUtenteByID = connection.prepareStatement("SELECT * FROM utenti WHERE id = ?");
            insertUtente = connection.prepareStatement("INSERT INTO utenti (nome, cognome, email, password, id_ruolo, token, exp_date) VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            updateUtente = connection.prepareStatement("UPDATE utenti SET nome=?, cognome=?, email=?,password=?, id_ruolo=?, send_email=?, email_verified_at=?, token=?, exp_date=?, version=? WHERE ID=? and version=?");
            loginUtente = connection.prepareStatement("SELECT id, password FROM utenti WHERE email = ?");
            getUtenteByEmail = connection.prepareStatement("SELECT id FROM utenti WHERE email = ?");
            getUtenteByToken = connection.prepareStatement("SELECT * FROM utenti WHERE token = ?");
            getUtentiSendEmail = connection.prepareStatement("SELECT id FROM utenti WHERE send_email = 1");
            getUtentiPaginated = connection.prepareStatement("SELECT * FROM utenti LIMIT ? OFFSET ?");
            dUtente = connection.prepareStatement("DELETE FROM utenti WHERE id = ?");
            checkToken = connection.prepareStatement("SELECT id AS Token FROM utenti where token = ?");

        } catch (SQLException ex) {
            Logger.getLogger("Errore nell'inizializzazione del DAO Utente");
        }
    }

    public void destroy() throws DataException {
        try {
            /**
             * CLOSE ALL STATEMENTS
             */
            countUsers.close();
            getUtenteByID.close();
            insertUtente.close();
            updateUtente.close();
            loginUtente.close();
            getUtenteByEmail.close();
            getUtenteByToken.close();
            getUtentiSendEmail.close();
            getUtentiPaginated.close();
            dUtente.close();
        } catch (SQLException ex) {
            Logger.getLogger(UtenteDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Integer getNumeroUtenti() throws DataException {
        Integer returnValue = -1;

        try {
            try (ResultSet rs = countUsers.executeQuery()) {

                if (rs.next()) {
                    returnValue = rs.getInt("NumeroUtenti");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable count users", ex);
        }

        return returnValue;
    }

    @Override
    public UtenteProxy createUtente() {
        return new UtenteProxy(getDataLayer());
    }

    public Utente createUtente(ResultSet rs) throws DataException {
        UtenteProxy u = createUtente();
        try {
            u.setKey(rs.getInt("id"));
            u.setVersion(rs.getLong("version"));
            u.setNome(rs.getString("nome"));
            u.setCognome(rs.getString("cognome"));
            u.setEmail(rs.getString("email"));
            u.setSendEmail(rs.getBoolean("send_email"));
            u.setPassword(rs.getString("password"));
            u.setIdRuolo(rs.getInt("id_ruolo"));
            u.setEmailVerifiedAt(rs.getObject("email_verified_at", LocalDate.class));
            u.setExpirationDate(rs.getObject("exp_date", LocalDate.class));
            u.setToken(rs.getString("token"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create utente object form ResultSet", ex);
        }
        return u;
    }

    public Utente getUtente(int utente_key) throws DataException {
        Utente utente = null;

        if (dataLayer.getCache().has(Utente.class, utente_key)) {
            utente = dataLayer.getCache().get(Utente.class, utente_key);
        } else {
            try {
                getUtenteByID.setInt(1, utente_key);
                try (ResultSet rs = getUtenteByID.executeQuery()) {
                    if (rs.next()) {
                        utente = createUtente(rs);
                        dataLayer.getCache().add(Utente.class, utente);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load utente by ID", ex);
            }
        }
        return utente;
    }

    public Utente getUtente(String email) throws DataException {
        Utente utente = null;

        try {
            loginUtente.setString(1, email);
            try (ResultSet rs = loginUtente.executeQuery()) {
                if (rs.next()) {
                    utente = getUtente(rs.getInt("id"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load utente by email", ex);
        }
        return utente;
    }

    public void storeUtente(Utente utente) throws DataException {

        try {
            if (utente.getKey() != null && utente.getKey() > 0) { //update
                // Se proxy non modificato non facciamo nulla
                if (utente instanceof DataItemProxy && !((DataItemProxy) utente).isModified()) {
                    return;
                }

                // Altrimenti
                updateUtente.setString(1, utente.getNome());
                updateUtente.setString(2, utente.getCognome());
                updateUtente.setString(3, utente.getEmail());
                updateUtente.setString(4, utente.getPassword());

                if (utente.getRuolo() != null) {
                    updateUtente.setInt(5, utente.getRuolo().getKey());
                } else {
                    updateUtente.setNull(5, java.sql.Types.INTEGER);
                }

                int send_email = utente.getSendEmail() ? 1 : 0;
                updateUtente.setBoolean(6, utente.getSendEmail());

                if (utente.getEmailVerifiedAt() != null) {
                    updateUtente.setDate(7, java.sql.Date.valueOf(utente.getEmailVerifiedAt()));
                } else {
                    updateUtente.setNull(7, java.sql.Types.DATE);
                }

                updateUtente.setString(8, utente.getToken());

                if (utente.getExp_date() != null) {
                    updateUtente.setDate(9, java.sql.Date.valueOf(utente.getExp_date()));
                } else {
                    updateUtente.setNull(9, java.sql.Types.DATE);
                }
                //UtilityMethods.debugConsole(this.getClass(), "store utente", updateUtente.toString());
                long current_version = utente.getVersion();
                long next_version = current_version + 1;

                updateUtente.setLong(10, next_version);
                updateUtente.setInt(11, utente.getKey());
                updateUtente.setLong(12, current_version);

                if (updateUtente.executeUpdate() == 0) {
                    throw new OptimisticLockException(utente);
                }
                utente.setVersion(next_version);
            } else { //insert
                System.out.println("inserendo utente");
                insertUtente.setString(1, utente.getNome());
                insertUtente.setString(2, utente.getCognome());
                insertUtente.setString(3, utente.getEmail());
                insertUtente.setString(4, utente.getPassword());

                if (utente.getRuolo() != null) {
                    insertUtente.setInt(5, utente.getRuolo().getKey());
                } else {
                    insertUtente.setNull(5, java.sql.Types.INTEGER); // Se l'utente non ha nessun ruolo, lo setto a 1 di default
                }

                insertUtente.setString(6, utente.getToken());

                if (utente.getExp_date() != null) {
                    insertUtente.setDate(7, java.sql.Date.valueOf(utente.getExp_date()));
                } else {
                    insertUtente.setNull(7, java.sql.Types.DATE);
                }

                if (insertUtente.executeUpdate() == 1) {
                    //getGeneratedKeys per leggere chiave generata
                    try (ResultSet keys = insertUtente.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            utente.setKey(key);
                            dataLayer.getCache().add(Utente.class, utente);
                        }
                    }
                }
            }

            //se abbiamo un proxy, resettiamo il suo attributo dirty
            if (utente instanceof DataItemProxy) {
                ((DataItemProxy) utente).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store utente", ex);
        }
    }

    public String getPassword(String email) throws DataException {
        String pass = null;

        try {
            loginUtente.setString(1, email);
            try (ResultSet rs = loginUtente.executeQuery()) {
                if (rs.next()) {
                    pass = rs.getString("password");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load password utente by email", ex);
        }
        return pass;
    }

    public Utente getUtenteByEmail(String email) throws DataException {
        Utente utente = null;

        try {
            getUtenteByEmail.setString(1, email);
            try (ResultSet rs = getUtenteByEmail.executeQuery()) {
                if (rs.next()) {
                    utente = getUtente(rs.getInt("id"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load utente by usernme", ex);
        }
        return utente;
    }

    public Utente getUtenteByToken(String token) throws DataException {
        Utente utente = null;

        try {
            getUtenteByToken.setString(1, token);
            try (ResultSet rs = getUtenteByToken.executeQuery()) {
                if (rs.next()) {
                    utente = getUtente(rs.getInt("id"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load utente by token", ex);
        }
        return utente;
    }

    public List<Utente> getUtentiSendEmail() throws DataException {
        List<Utente> users = null;

        try {
            try (ResultSet rs = getUtentiSendEmail.executeQuery()) {
                users = new ArrayList<>();
                while (rs.next()) {
                    users.add((Utente) getUtente(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load user for sending email", ex);
        }
        return users;
    }

    public List<Utente> getUtentiPaginated(int start_item, int elements) throws DataException {
        List<Utente> returnList = new ArrayList<>();

        try {
            getUtentiPaginated.setInt(1, elements);
            getUtentiPaginated.setInt(2, start_item);
            try (ResultSet rs = getUtentiPaginated.executeQuery()) {

                while (rs.next()) {
                    returnList.add((Utente) getUtente(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable get elenco utenti paginato", ex);
        }

        return returnList;
    }

    public void deleteUtente(int key) throws DataException {
        try {
            dUtente.setInt(1, key);
            int rows = dUtente.executeUpdate();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete utente by ID", ex);
        }
    }

    public boolean tokenExists(String token) throws DataException {

        try {
            checkToken.setString(1, token);
            try (ResultSet rs = checkToken.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to check token", ex);
        }
    }

}
