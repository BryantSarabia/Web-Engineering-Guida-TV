package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.model.Programmazione;
import com.mycompany.guida.tv.data.proxy.ProgrammazioneProxy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface ProgrammazioneDAO {
    
    Programmazione createProgrammazione();
    
    Programmazione getProgrammazione(int key) throws DataException;
    
    int getNumeroProgrammazioni() throws DataException;
    
    int getNumeroProgrammazioni(LocalDate start, LocalDate end) throws DataException;

    List<Programmazione> getProgrammazione(LocalDateTime start, LocalDateTime end) throws DataException;
    
    List<ProgrammazioneProxy> getProgrammazione(int canale_key, LocalDateTime start, LocalDateTime end) throws DataException;
    
    List<Programmazione> getProgrammazioneSpecifica(int programma_key, LocalDate start, LocalDate end, LocalTime start_min, LocalTime start_max) throws DataException;
    
    List<Programmazione> getProgrammazioneSerie(int id_serie) throws DataException;
    
    List<Programmazione> getProgrammazioneCorrente() throws DataException;
    
    List<Programmazione> getProgrammazioniPaginated(int start_item, int elements) throws DataException;
    
    List<Programmazione> getProgrammazioniPaginated(LocalDate start, LocalDate end, int start_item, int elements) throws DataException;
    
    List<Programmazione> getLatest(int limit) throws DataException;
    
    Programmazione currentProgram(int canale_key) throws DataException;
    
    void storeProgrammazione(Programmazione prog) throws DataException;
    
    void deleteProgrammazione(int key) throws DataException;
    
}
