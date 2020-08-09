
package com.mycompany.guida.tv.data.model;

import com.mycompany.guida.tv.data.DataItem;

public interface Canale extends DataItem<Integer> {

    public int getId();

    public void setId(int id);

    public String getNome();

    public void setNome(String nome);

    public int getNumero();

    public void setNumero(int numero);

    public String getLogo();

    public void setLogo(String logo);

    @Override
    public long getVersion();

    @Override
    public void setVersion(long version);
}
