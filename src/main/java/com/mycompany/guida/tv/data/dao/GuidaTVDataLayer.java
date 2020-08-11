package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataLayer;
import java.sql.SQLException;
import javax.sql.DataSource;

public class GuidaTVDataLayer extends DataLayer{
    
        public GuidaTVDataLayer(DataSource datasource) throws SQLException {
        super(datasource);
    }
    
    @Override
    public void init() throws DataException {
        // QUI DECIDO DI UTILIZZARE L'IMPLEMENTAZIONE DI MYSQL
     
    }

    
}
