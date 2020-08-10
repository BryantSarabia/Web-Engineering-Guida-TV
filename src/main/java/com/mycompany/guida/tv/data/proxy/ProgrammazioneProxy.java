package com.mycompany.guida.tv.data.proxy;

import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.impl.ProgrammazioneImpl;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.model.Programma;

public class ProgrammazioneProxy extends ProgrammazioneImpl implements DataItemProxy {
    private boolean modified;
    protected final DataLayer dataLayer;

    public ProgrammazioneProxy(Programma programma, Canale canale, String start_time, String time, long version, DataLayer dataLayer) {
        super(programma, canale, start_time, time, version);
        this.modified = false;
        this.dataLayer = dataLayer;
    }

    @Override
    public void setId(int id) {
        this.modified = true;
        super.setId(id);
    }

    @Override
    public void setProgramma(Programma programma) {
        this.modified = true;
        super.setProgramma(programma);
    }

    @Override
    public void setCanale(Canale canale) {
        this.modified = true;
        super.setCanale(canale);
    }

    @Override
    public void setStart_time(String start_time) {
        this.modified = true;
        super.setStart_time(start_time);
    }

    @Override
    public void setTime(String time) {
        super.setTime(time);
    }

    @Override
    public void setVersion(long version) {
        this.modified = true;
        super.setVersion(version);
    }

    @Override
    public void setKey(Integer key) {
        this.modified = true;
        super.setKey(key);
    }

    @Override
    public boolean isModified() {
        return this.modified;
    }

    @Override
    public void setModified(boolean modified) {
        this.modified = modified;

    }
}
