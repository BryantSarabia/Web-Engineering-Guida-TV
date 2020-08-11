package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.model.Ricerca;
import com.mycompany.guida.tv.data.model.Utente;
import java.util.List;

public interface RicercaDAO {
    
    Ricerca createRicerca();
    
    Ricerca getRicerca(int id_ricerca) throws DataException;
    
    List<Ricerca> getRicercheUtente(Utente utente) throws DataException;
    
    void storeRicerca(Ricerca ricerca, int id_utente) throws DataException;
    
    boolean removeRicerca(int id_ricerca) throws DataException;
    
}
