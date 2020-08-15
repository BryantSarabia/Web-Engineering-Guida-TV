package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.model.Canale;
import java.util.List;

public interface CanaleDAO {
    
    Canale createCanale();
    
    Canale getCanale(int key) throws DataException;
    
    int getNumeroCanali() throws DataException;
    
    List<Canale> getListaCanali() throws DataException;
    
    List<Canale> getListaCanali(int page, int elements) throws DataException;
    
    List<Canale> getListaCanaliPaginated(int start_element, int elements) throws DataException;
    
    void storeCanale(Canale c) throws DataException;
    
    void deleteCanale(int key) throws DataException;
    
 
    
}
