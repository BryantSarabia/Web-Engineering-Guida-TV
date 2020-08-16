package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.model.Genere;
import java.util.List;
public interface GenereDAO {
    
    Genere createGenere();
    
    //RETURN GENERE FROM KEY
    Genere getGenere(int key) throws DataException;
    
    int getNumeroGeneri() throws DataException;
    
    List<Genere> getGeneriPaginated(int start_item, int elements) throws DataException;
    
    List<Genere> getGeneri() throws DataException;
    
    List<Genere> getGeneri(int key) throws DataException;
    
    void storeGenere(Genere g) throws DataException;
    
    void deleteGenere(int key) throws DataException;
}
