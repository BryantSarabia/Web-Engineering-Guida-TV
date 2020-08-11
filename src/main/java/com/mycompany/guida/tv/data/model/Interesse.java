package com.mycompany.guida.tv.data.model;

import com.mycompany.guida.tv.data.DataItem;
import java.time.LocalTime;

public interface Interesse extends DataItem<Integer> {

    Utente getUtente();

    void setUtente(Utente utente);

    Canale getCanale();

    void setCanale(Canale canale);

    LocalTime getStartTime();

    void setStartTime(LocalTime startTime);

    LocalTime getEndTime();

    void setEndTime(LocalTime endTime);

}
