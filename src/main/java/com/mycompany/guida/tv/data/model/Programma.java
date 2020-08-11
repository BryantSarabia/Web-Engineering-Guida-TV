package com.mycompany.guida.tv.data.model;
import com.mycompany.guida.tv.data.DataItem;

import java.util.List;

public interface Programma extends DataItem<Integer> {

    public String getTitolo();

    public void setTitolo(String titolo);

    public String getDescrizione();

    public void setDescrizione(String descrizione);

    public String getImg();

    public void setImg(String img);

    public String getLink_ref();

    public void setLink_ref(String link_ref);

    public String getDurata();

    public void setDurata(String durata);

    public List<Genere> getGeneri();

    public void setGeneri(List<Genere> generi);


}
