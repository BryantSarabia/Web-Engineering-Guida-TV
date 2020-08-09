/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guida.tv.data.dao;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataLayer;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author HP
 */
public class GuidaTVDataLayer extends DataLayer{
    
        public GuidaTVDataLayer(DataSource datasource) throws SQLException {
        super(datasource);
    }
    
    @Override
    public void init() throws DataException {
        // QUI DECIDO DI UTILIZZARE L'IMPLEMENTAZIONE DI MYSQL
     
    }

    
}
