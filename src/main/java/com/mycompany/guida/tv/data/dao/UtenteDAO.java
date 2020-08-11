package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.model.Utente;
import java.util.List;

public interface UtenteDAO {

    Integer getNumeroUtenti() throws DataException;

    Utente createUtente() throws DataException;

    Utente getUtente(int utente_key) throws DataException;

    Utente getUtente(String email) throws DataException;

    Utente getUtenteByUsername(String username) throws DataException;

    Utente getUtenteByToken(String token) throws DataException;

    void storeUtente(Utente utente) throws DataException;

    String getPassword(String email) throws DataException;

    List<Utente> getUtentiSendEmail() throws DataException;

    List<Utente> getUtentiPaginated(int start_item, int elements) throws DataException;

    void deleteUtente(int key) throws DataException;

    boolean tokenExists(String token) throws DataException;

}
