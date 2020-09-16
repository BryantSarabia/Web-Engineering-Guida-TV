package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.model.Programmazione;
import com.mycompany.guida.tv.data.model.Serie;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProgrammazioneImpl extends DataItemImpl<Integer> implements Programmazione, Comparable {

    private Canale canale;
    private Programma programma;
    private Serie episodio;
    private LocalDateTime startTime;
    private Integer durata;

    public ProgrammazioneImpl() {
        super();
        this.canale = null;
        this.programma = null;
        this.startTime = null;
        this.durata = 0;
    }

    @Override
    public Canale getCanale() {
        return canale;
    }

    @Override
    public void setCanale(Canale canale) {
        this.canale = canale;
    }
    
    @Override
    public Serie getEpisodio(){
        return this.episodio;
    }
    
    @Override
    public void setEpisodio(Serie episodio){
        this.episodio = episodio;
    }

    @Override
    public Programma getProgramma() {
        return programma;
    }

    @Override
    public void setProgramma(Programma programma) {
        this.programma = programma;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(LocalDateTime time) {
        this.startTime = time;
    }

    @Override
    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formatDateTime = startTime.format(formatter);
        String nowDate = LocalDateTime.now().format(formatter);
        //if(formatDateTime.equals(nowDate)) return "Oggi";
        return formatDateTime;
    }

    @Override
    public String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formatDateTime = startTime.format(formatter);
        return formatDateTime;
    }

    @Override
    public Integer getDurata() {
        return durata;
    }

    @Override
    public void setDurata(Integer durata) {
        this.durata = durata;
    }

    @Override
    public String getEndTime() {
        LocalDateTime endTime = startTime.plusMinutes(this.durata);
        String formatDateTime = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        return formatDateTime;
    }


    @Override
    public int compareTo(Object obj) {
        if(obj instanceof Programmazione) {
            Programmazione p = (Programmazione) obj;
            return (this.getStartTime().isBefore( p.getStartTime()) ? -1 : (this.getStartTime().equals(p.getStartTime()) ? 0 : 1 ) );
        }
        else return super.compareTo(obj);
    }

    @Override
    public String getStartTimeFormatted(String pattern) {
        return startTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    @Override
    public int getHour() {
        return startTime.toLocalTime().getHour();
    }

    @Override
    public boolean inOnda() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startTime) && now.isBefore(startTime.plusMinutes(durata));
    }
}
