package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.model.Utente;
import java.time.LocalTime;
import com.mycompany.guida.tv.data.model.Interessa;

public class InteresseImpl extends DataItemImpl<Integer> implements Interessa {

    private Canale canale;
    private Utente utente;
    private LocalTime startTime;
    private LocalTime endTime;

    public InteresseImpl() {
        super();
        this.canale = null;
        this.utente = null;
        this.startTime = null;
        this.endTime = null;
    }

    @Override
    public Utente getUtente() {
        return utente;
    }

    @Override
    public void setUtente(Utente utente) {
        this.utente = utente;
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
    public LocalTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

}
