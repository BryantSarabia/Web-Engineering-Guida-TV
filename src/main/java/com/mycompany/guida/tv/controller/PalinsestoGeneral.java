/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guida.tv.controller;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.model.Programmazione;
import com.mycompany.guida.tv.result.FailureResult;
import com.mycompany.guida.tv.result.JSONResult;
import com.mycompany.guida.tv.result.TemplateManagerException;
import com.mycompany.guida.tv.result.TemplateResult;
import com.mycompany.guida.tv.security.SecurityLayer;
import com.mycompany.guida.tv.shared.Methods;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HP
 */
public class PalinsestoGeneral extends BaseController {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        response.setContentType("text/html;charset=UTF-8");
        int fascia = 1;
        System.out.println((GuidaTVDataLayer) request.getAttribute("datalayer"));
        try {
            if (request.getParameter("fascia") != null && request.getParameter("json") == null) {
                fascia = SecurityLayer.checkNumeric(request.getParameter("fascia"));
                if (fascia >= 1 && fascia <= 4) {
                    // Se la fascia non è valida la lascio a 1 (Mattina) altrimenti eseguo la action get_by_fascia
                    action_get_by_fascia(request, response, fascia);
                }
            } else if (request.getParameter("fascia") != null && request.getParameter("json") != null) {
                fascia = SecurityLayer.checkNumeric(request.getParameter("fascia"));
                System.out.println("JSON");
                if (fascia >= 1 && fascia <= 4) {
                    // Se la fascia non è valida la lascio a 1 (Mattina) altrimenti eseguo la action get_by_fascia
                    action_get_by_fascia_json(request, response, fascia);
                }
            } else if (request.getParameter("fascia") == null && request.getParameter("json") != null) {
                action_default_json(request, response);
            } else {
                action_default(request, response);
            }
        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

        } catch (DataException ex) {
            Logger.getLogger(PalinsestoGeneral.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
        return;
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {

        action_get_by_fascia(request, response, 1);
        System.out.println("action default");
    }

    private void action_default_json(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {

        action_get_by_fascia_json(request, response, 1);
        System.out.println("action json default");

    }

    private void action_get_by_fascia(HttpServletRequest request, HttpServletResponse response, int fascia) throws TemplateManagerException, DataException {
        /* Da fare */
        int elements = 5, page = 0;
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = SecurityLayer.checkNumeric(request.getParameter("page"));
        }

        try {
            TemplateResult results = new TemplateResult(getServletContext());

            LocalDate day = null;
            if (request.getParameter("day") != null) {
                day = (LocalDate) SecurityLayer.checkDate(request.getParameter("day"));
            }
            if (day == null) {
                day = LocalDate.now();
            }

            String day_target = day.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String start_str, end_str;

            switch (fascia) {
                case 1: {
                    start_str = day_target + " 06:00";
                    end_str = day_target + " 12:00";
                    break;
                }
                case 2: {
                    start_str = day_target + " 12:00";
                    end_str = day_target + " 18:00";
                    break;
                }
                case 3: {
                    start_str = day_target + " 18:00";
                    end_str = day_target + " 23:59";
                    break;
                }
                case 4: {
                    start_str = day_target + " 00:00";
                    end_str = day_target + " 06:00";
                    break;
                }
                default: {
                    // anche se non dovrebbe mai verificarsi lo metto per sicurezza. Setto mattina
                    start_str = day_target + " 06:00";
                    end_str = day_target + " 12:00";
                    break;
                }
            }

            // Converto le stinghe in LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime start = LocalDateTime.parse(start_str, formatter);
            LocalDateTime end = LocalDateTime.parse(end_str, formatter);;

            Map<Canale, List<Programmazione>> palinsesto = new TreeMap<>();

            for (Canale c : ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getListaCanali(page, elements)) {
                List<Programmazione> programmazione = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getProgrammazione(c.getKey(), start, end);
                palinsesto.put(c, programmazione);
            }

            // PAGINATION INFO
            request.setAttribute("numero_pagine", (int) (Math.ceil(((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getNumeroCanali() / elements)));
            request.setAttribute("pagina", page);

            // PALINSESTO INFO
            request.setAttribute("palinsesto", palinsesto);
            request.setAttribute("fascia", fascia);
            request.setAttribute("nome_fascia", Methods.getNomeFascia(fascia));
            request.setAttribute("day", day_target);
            request.setAttribute("start", start.format(DateTimeFormatter.ofPattern("HH:mm")));
            request.setAttribute("end", end.format(DateTimeFormatter.ofPattern("HH:mm")));
            results.activate("palinsesto.ftl.html", request, response);
        } catch (DataException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }

    private void action_get_by_fascia_json(HttpServletRequest request, HttpServletResponse response, int fascia) throws TemplateManagerException, DataException {
        /* Da fare */
        int elements = 5, page = 0;
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = SecurityLayer.checkNumeric(request.getParameter("page"));
        }

        try {
            JSONResult results = new JSONResult(getServletContext());

            LocalDate day = null;
            if (request.getParameter("day") != null) {
                day = (LocalDate) SecurityLayer.checkDate(request.getParameter("day"));
            }
            if (day == null) {
                day = LocalDate.now();
            }

            String day_target = day.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String start_str, end_str;

            switch (fascia) {
                case 1: {
                    start_str = day_target + " 06:00";
                    end_str = day_target + " 12:00";
                    break;
                }
                case 2: {
                    start_str = day_target + " 12:00";
                    end_str = day_target + " 18:00";
                    break;
                }
                case 3: {
                    start_str = day_target + " 18:00";
                    end_str = day_target + " 23:59";
                    break;
                }
                case 4: {
                    start_str = day_target + " 00:00";
                    end_str = day_target + " 06:00";
                    break;
                }
                default: {
                    // anche se non dovrebbe mai verificarsi lo metto per sicurezza. Setto mattina
                    start_str = day_target + " 06:00";
                    end_str = day_target + " 12:00";
                    break;
                }
            }

            // Converto le stinghe in LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime start = LocalDateTime.parse(start_str, formatter);
            LocalDateTime end = LocalDateTime.parse(end_str, formatter);;

            Map<Canale, List<Programmazione>> palinsesto = new TreeMap<>();

            for (Canale c : ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getListaCanali(page, elements)) {
                List<Programmazione> programmazione = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getProgrammazione(c.getKey(), start, end);
                System.out.println(programmazione);
                palinsesto.put(c, programmazione);
            }

            // PAGINATION INFO
            request.setAttribute("numero_pagine", (int) (Math.ceil(((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getNumeroCanali() / elements)));
            request.setAttribute("pagina", page);

            // PALINSESTO INFO
            request.setAttribute("palinsesto", palinsesto);
            request.setAttribute("fascia", fascia);
            request.setAttribute("nome_fascia", Methods.getNomeFascia(fascia));
            request.setAttribute("day", day_target);
            request.setAttribute("start", start.format(DateTimeFormatter.ofPattern("HH:mm")));
            request.setAttribute("end", end.format(DateTimeFormatter.ofPattern("HH:mm")));
            results.activate("/json/palinsesto_generale_json.ftl.json", request, response);
        } catch (DataException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }

}
