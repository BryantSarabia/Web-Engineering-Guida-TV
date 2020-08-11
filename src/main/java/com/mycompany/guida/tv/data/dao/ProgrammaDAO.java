package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.model.Programma;
import java.util.List;

public interface ProgrammaDAO {

    Programma createProgramma();

    Integer getNumeroProgrammi() throws DataException;

    List<Programma> getProgrammi() throws DataException;

    List<Programma> getProgrammiDistinctSerie() throws DataException;

    List<Programma> getIdSerie() throws DataException;

    List<Programma> getProgrammiPaginated(int start_item, int elements) throws DataException;

    Programma getProgramma(int key) throws DataException;

    List<Programma> cercaProgrammi(String nome, int genere_key) throws DataException;
    
    List<Programma> getRelatedPrograms(Programma programma) throws DataException;

    void storeProgramma(Programma p) throws DataException;

    void deleteProgramma(int key) throws DataException;
}
