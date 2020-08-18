package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.model.Utente;
import java.util.List;
import com.mycompany.guida.tv.data.model.Interessa;
public interface InteressaDAO {
    
    Interessa createInteresse();
    
    Interessa getInteresse(int id_interesse) throws DataException;
    
    List<Interessa> getInteressiUtente(Utente utente) throws DataException;
    
    void storeInteresse(Interessa interesse) throws DataException;
    
    boolean removeInteresse(int id_interesse) throws DataException;
    
}
