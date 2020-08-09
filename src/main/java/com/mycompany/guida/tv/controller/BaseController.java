/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guida.tv.controller;

import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author HP
 */
public abstract class  BaseController extends HttpServlet {

        // INITIALIZE THE DATASOURCE WITH CONNECTION POOLING
    @Resource(name = "jdbc/guidatv")
    private DataSource ds;
    
    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException;

 private void processBaseRequest(HttpServletRequest request, HttpServletResponse response) {
        try (GuidaTVDataLayer datalayer = new GuidaTVDataLayer(ds)) {
            datalayer.init();
            request.setAttribute("datalayer", datalayer);
            
            //System.out.println("Inizializzo DataLayer: " + datalayer);
            processRequest(request, response);
        } catch (Exception ex) {
            ex.printStackTrace(); //for debugging only
        }
    }

 @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

}
