package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.model.Programma;
import java.util.List;

public interface ProgrammaDAO {


    //Helper per creare un oggetto programma
    Programma createProgramma();
    
    //Metodo che restituisce il numero di programmi presenti sul db
    Integer getNumeroProgrammi() throws DataException;
    
    //Metodo che restituisce tutti i programmi presenti sul db
    List<Programma> getProgrammi() throws DataException;
    
    //Metodo per la paginazione dei programmi
    List<Programma> getProgrammiPaginated(int start_item, int elements) throws DataException;
    
    //Metodo che restituisce un programma in base all'id specificato
    Programma getProgramma(int key) throws DataException;
    
    //Metodo per effettuare la ricerca di un programma in base a nome e genere
    List<Programma> cercaProgrammi(String nome, int genere_key) throws DataException;
    
    //Metodo per inserire o aggiornare un programma
    void storeProgramma(Programma p) throws DataException;
    
    //Metodo per eliminare un programma
    void deleteProgramma(int key) throws DataException;
}
