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
import java.time.LocalTime;
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
public class PalinsestoCanale extends BaseController {

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
        try {
            if (request.getParameter("c_key") != null && request.getParameter("json") == null) {
                action_default(request, response);
            } else if (request.getParameter("c_key") != null && request.getParameter("json") != null) {
                action_json(request, response);
            } else {
                action_without_parameter(request, response);
            }
        } catch (TemplateManagerException | DataException | IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);

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

        Integer id_canale = SecurityLayer.checkNumeric(request.getParameter("c_key"));
        Integer fascia;
        if (request.getParameter("fascia") != null) {

            fascia = SecurityLayer.checkNumeric(request.getParameter("fascia"));
            if (fascia == null || fascia < 0 || fascia > 4) {
                fascia = 0;
            }

        } else {
            fascia = 0;
        }

        LocalTime start_time = Methods.getOrarioInizioFascia(fascia);
        LocalTime end_time = Methods.getOrarioFineFascia(fascia);
        //UtilityMethods.debugConsole(this.getClass(), "action_get_by_fascia", "Sono in palinsesto post fascia");

        try {
            TemplateResult results = new TemplateResult(getServletContext());

            LocalDateTime start = null;
            LocalDateTime end = null;

            LocalDate day = null;
            if (request.getParameter("day") != null) {
                day = (LocalDate) SecurityLayer.checkDate(request.getParameter("day"));
            }
            if (day == null) {
                day = LocalDate.now();
            }

            start = day.atTime(start_time);
            end = day.atTime(end_time);
            System.out.println(start);

            Canale c = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getCanale(id_canale);
            List<Programmazione> palinsesto = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getProgrammazione(id_canale, start, end);

            request.setAttribute("day", day);
            request.setAttribute("programmazioni", palinsesto);
            request.setAttribute("canale", c);
            request.setAttribute("fascia", fascia);
            request.setAttribute("start", start.format(DateTimeFormatter.ofPattern("HH:mm")));
            request.setAttribute("end", end.format(DateTimeFormatter.ofPattern("HH:mm")));
            results.activate("palinsesto_canale.ftl.html", request, response);
        } catch (DataException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void action_json(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {

        Integer id_canale = SecurityLayer.checkNumeric(request.getParameter("c_key"));
        Integer fascia;
        if (request.getParameter("fascia") != null) {

            fascia = SecurityLayer.checkNumeric(request.getParameter("fascia"));
            if (fascia == null || fascia < 0 || fascia > 4) {
                fascia = 0;
            }

        } else {
            fascia = 0;
        }

        LocalTime start_time = Methods.getOrarioInizioFascia(fascia);
        LocalTime end_time = Methods.getOrarioFineFascia(fascia);
        //UtilityMethods.debugConsole(this.getClass(), "action_get_by_fascia", "Sono in palinsesto post fascia");

        try {
            JSONResult results = new JSONResult(getServletContext());

            LocalDateTime start = null;
            LocalDateTime end = null;

            LocalDate day = null;
            if (request.getParameter("day") != null) {
                day = (LocalDate) SecurityLayer.checkDate(request.getParameter("day"));
            }
            if (day == null) {
                day = LocalDate.now();
            }

            start = day.atTime(start_time);
            end = day.atTime(end_time);

            Canale c = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getCanale(id_canale);
            List<Programmazione> palinsesto = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getProgrammazione(id_canale, start, end);

            request.setAttribute("day", day);
            request.setAttribute("programmazioni", palinsesto);
            request.setAttribute("canale", c);
            request.setAttribute("fascia", fascia);
            request.setAttribute("start", start.format(DateTimeFormatter.ofPattern("HH:mm")));
            request.setAttribute("end", end.format(DateTimeFormatter.ofPattern("HH:mm")));
            results.activate("/json/palinsesto_canale.ftl.json", request, response);
        } catch (DataException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    

    private void action_without_parameter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/palinsesto");
    }

}
