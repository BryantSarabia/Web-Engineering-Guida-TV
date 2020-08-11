package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.model.Ruolo;
import java.util.List;

public interface RuoloDAO {
    
    Ruolo createRuolo();
    
    Ruolo getRuoloUtente(int UtenteKey) throws DataException;
    
    Ruolo getRuolo(int key) throws DataException;
    
    List<Ruolo> getRuoli() throws DataException;
    
}
