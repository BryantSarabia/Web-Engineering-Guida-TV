package com.mycompany.guida.tv.data.impl;

import com.mycompany.guida.tv.data.DataItemImpl;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.model.Programmazione;

public class ProgrammazioneImpl extends DataItemImpl<Integer> implements Programmazione {
    private int id;
    private Programma programma;
    private Canale canale;
    private String start_time;
    private String time;
    private long version;

    public ProgrammazioneImpl(int id, Programma programma, Canale canale, String start_time, String time, long version) {
        this.id = id;
        this.programma = programma;
        this.canale = canale;
        this.start_time = start_time;
        this.time = time;
        this.version = version;
    }

    public ProgrammazioneImpl(Programma programma, Canale canale, String start_time, String time, long version) {
        this.programma = programma;
        this.canale = canale;
        this.start_time = start_time;
        this.time = time;
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Programma getProgramma() {
        return programma;
    }

    public void setProgramma(Programma programma) {
        this.programma = programma;
    }

    public Canale getCanale() {
        return canale;
    }

    public void setCanale(Canale canale) {
        this.canale = canale;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void setVersion(long version) {
        this.version = version;
    }
}
