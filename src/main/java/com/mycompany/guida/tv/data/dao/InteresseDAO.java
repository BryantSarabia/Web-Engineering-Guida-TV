package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.model.Interesse;
import com.mycompany.guida.tv.data.model.Utente;
import java.util.List;
public interface InteresseDAO {
    
    Interesse createInteresse();
    
    Interesse getInteresse(int id_interesse) throws DataException;
    
    List<Interesse> getInteressiUtente(Utente utente) throws DataException;
    
    void storeInteresse(Interesse interesse) throws DataException;
    
    boolean removeInteresse(int id_interesse) throws DataException;
    
}
