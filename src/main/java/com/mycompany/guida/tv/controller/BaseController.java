/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guida.tv.controller;

import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import com.mycompany.guida.tv.security.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
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
public abstract class BaseController extends HttpServlet {

    // INITIALIZE THE DATASOURCE WITH CONNECTION POOLING
    @Resource(name = "jdbc/guidatv")
    private DataSource ds;

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException;

    private void processBaseRequest(HttpServletRequest request, HttpServletResponse response) {
        try (GuidaTVDataLayer datalayer = new GuidaTVDataLayer(ds)) {
            datalayer.init();
            request.setAttribute("datalayer", datalayer);

            System.out.println("Inizializzo DataLayer: " + datalayer);

            /**
             * Navbar Management
             */
            boolean logged = (SecurityLayer.checkSession(request) != null && request.isRequestedSessionIdValid() && !request.getSession(false).isNew());
            request.setAttribute("logged", logged);
            request.setAttribute("is_admin", SecurityLayer.checkAdminSession(request));

            /* Referrer link quando si fa login */
            String request_uri;

            if (request.getQueryString() == null) {
                request_uri = URLEncoder.encode(request.getRequestURI(), "UTF-8");

            } else {
                request_uri = URLEncoder.encode(request.getRequestURI() + "?" + request.getQueryString(), "UTF-8");
            }

            request.setAttribute("request_uri", request_uri);

            /* Ricerca */
            /**
             * Parametri per la ricerca
             */
            request.setAttribute("canali", datalayer.getCanaleDAO().getListaCanali());
            request.setAttribute("generi", datalayer.getGenereDAO().getGeneri());
            request.setAttribute("min_date", LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            request.setAttribute("max_date", LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            // GIORNI DELLA SETTIMANA
            List<LocalDate> settimana = new ArrayList();
            for (int i = 0; i <= 7; i++) {
                settimana.add(LocalDate.now().plusDays(i));
            }
            request.setAttribute("settimana", settimana);

            // Last programmazioni per la bottom bar
            request.setAttribute("bottom_bar", datalayer.getProgrammazioneDAO().getLatest(3));

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
