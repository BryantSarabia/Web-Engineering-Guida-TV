package com.mycompany.guida.tv.data.model;

import com.mycompany.guida.tv.data.DataItem;

import java.time.LocalDateTime;

public interface Programmazione extends DataItem<Integer> {

    public Programma getProgramma();

    public void setProgramma(Programma programma);

    public Canale getCanale();

    public void setCanale(Canale canale);

    public LocalDateTime getStartTime();

    public void setStartTime(LocalDateTime start_time);

    public String getTime();

    public int getHour();

    public String getEndTime();

    public Integer getDurata();

    public void setDurata(Integer durata);

    public String getDate();

    public String getStartTimeFormatted(String pattern);

    public boolean inOnda();

}
