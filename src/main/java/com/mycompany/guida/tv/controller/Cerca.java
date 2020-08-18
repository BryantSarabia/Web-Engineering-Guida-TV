/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guida.tv.controller;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import com.mycompany.guida.tv.data.impl.RicercaImpl;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.model.Programmazione;
import com.mycompany.guida.tv.data.model.Ricerca;
import com.mycompany.guida.tv.data.proxy.UtenteProxy;
import com.mycompany.guida.tv.result.FailureResult;
import com.mycompany.guida.tv.result.StreamResult;
import com.mycompany.guida.tv.result.TemplateManagerException;
import com.mycompany.guida.tv.result.TemplateResult;
import com.mycompany.guida.tv.security.SecurityLayer;
import com.mycompany.guida.tv.shared.Methods;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Federico Di Menna
 */
public class Cerca extends BaseController {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        response.setContentType("text/html;charset=UTF-8");

        try {
            //if(request.getParameter("resize") != null) action_resize(request, response);
            //else 
            action_default(request, response);

        } catch (DataException | TemplateManagerException | IOException ex) {
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

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException, IOException {

        String titolo = "";
        List<Integer> canali = null, generi = null;
        LocalDate date_min = null, date_max = null;
        LocalTime min_ora = null, max_ora = null;

        // VALIDAZIONE CAMPI
        titolo = (String) SecurityLayer.removeSpecialCharsQuery(request.getParameter("titolo"));
        //UtilityMethods.debugConsole(this.getClass(), "action_default", "titolo: " + titolo);
        if (request.getParameterValues("generi") != null && !request.getParameter("generi").isEmpty()) {
            generi = new ArrayList<>();
            for (String g : request.getParameterValues("generi")) {
                if (g != null) {
                    generi.add(SecurityLayer.checkNumeric(g));
                }
            }
        }
        if (request.getParameterValues("canali") != null && !request.getParameter("canali").isEmpty()) {
            canali = new ArrayList<>();
            for (String c : request.getParameterValues("canali")) {
                if (c != null) {
                    canali.add(SecurityLayer.checkNumeric(c));
                }
            }
        }
        if (request.getParameter("min_ora") != null && !request.getParameter("min_ora").isEmpty()) {
            min_ora = (LocalTime) SecurityLayer.checkTime(request.getParameter("min_ora"));
        }
        if (request.getParameter("max_ora") != null && !request.getParameter("max_ora").isEmpty()) {
            max_ora = (LocalTime) SecurityLayer.checkTime(request.getParameter("max_ora"));
        }
        if (request.getParameter("date_min") != null && !request.getParameter("date_min").isEmpty()) {
            date_min = (LocalDate) SecurityLayer.checkDate(request.getParameter("date_min"));
        }
        if (request.getParameter("date_max") != null && !request.getParameter("date_max").isEmpty()) {
            date_max = (LocalDate) SecurityLayer.checkDate(request.getParameter("date_max"));
        }

        if (request.getParameter("send_email") != null && SecurityLayer.checkSession(request) != null) {
            // DEVO SALVARE LA RICERCA
            UtenteProxy me = (UtenteProxy) Methods.getMe(request);
            boolean exists = false;

            // CREO UNA QUERY STRING DAI PARAMETRI ANALIZZATI
            // Per motivi di sicurezza non salvo direttamente l'input dell'utente (querystring) nel DB
            String titolo_query = "titolo=" + URLEncoder.encode(titolo, "UTF-8");
            String genere_query = Methods.getQueryList("generi", generi);

            String canale_query = Methods.getQueryList("canale", canali);
            String start_min_query = (min_ora == null) ? "min_ora=" : "min_ora=" + URLEncoder.encode(min_ora.toString(), "UTF-8");
            String start_max_query = (max_ora == null) ? "max_ora=" : "max_ora=" + URLEncoder.encode(max_ora.toString(), "UTF-8");
            String date_min_query = (date_min == null) ? "date_min=" : "date_min=" + URLEncoder.encode(date_min.toString(), "UTF-8");
            String date_max_query = (date_max == null) ? "date_max=" : "date_max=" + URLEncoder.encode(date_max.toString(), "UTF-8");
            String safe_queryString = titolo_query + "&" + genere_query + "&" + canale_query + "&" + start_min_query + "&" + start_max_query + "&" + date_min_query + "&" + date_max_query;

            // Controllo se la ricerca è già presente
            for (Ricerca prec : ((GuidaTVDataLayer) request.getAttribute("datalayer")).getRicercaDAO().getRicercheUtente(me)) {
                if (prec.getQuery().equals(safe_queryString)) {
                    // La ricerca è presente
                    exists = true;
                }
            }

            if (!exists) {
                // La ricerca è nuova e la salvo
                Ricerca da_salvare = new RicercaImpl();
                da_salvare.setQuery(safe_queryString);
                ((GuidaTVDataLayer) request.getAttribute("datalayer")).getRicercaDAO().storeRicerca(da_salvare, me.getKey());
            }
        }

        try {
            TemplateResult results = new TemplateResult(getServletContext());
            if ((titolo.length() > 0) && generi == null && canali == null) {
                response.sendRedirect(request.getContextPath() + "/palinsesto");
            }

            List<Programma> interessati = new ArrayList<>();

            if (titolo != null && generi.contains(0)) {
                // Se ho solo il titolo cerco solo per titolo
                interessati.addAll(((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammaDAO().cercaProgrammi(titolo, 0));
            } else if ((titolo.length() > 0) && !generi.contains(0)) {
                // Se ho anche il genere cerco per titolo e per genere stando
                // attento a non inserire duplicati
                for (Integer id_genere : generi) {
                    for (Programma p : ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammaDAO().cercaProgrammi(titolo, id_genere)) {
                        if (!interessati.contains(p)) {
                            interessati.add(p);
                        }
                    }
                }
            } else if ((titolo.length() == 0) && !generi.contains(0)) {
                // Significa che ho solo il genere
                for (Integer id_genere : generi) {

                    for (Programma p : ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammaDAO().cercaProgrammi(null, id_genere)) {
                        if (!interessati.contains(p)) {
                            interessati.add(p);
                        }
                    }
                }
            }

            // a questo punto ho i programmi che mi interessano e devo filtrare le programmazioni
            LocalDate start, end;

            if (date_min == null) {
                start = LocalDate.now().minusMonths(1);
            } else {
                start = date_min;
            }

            if (date_max == null) {
                end = LocalDate.now().plusMonths(1);
            } else {
                end = date_max;
            }

            Map<Canale, List<Programmazione>> programmazioni_per_canale = new TreeMap<>();

            if (canali == null || canali.contains(0)) {
                // se non sto filtrando per canale
                for (Programma p : interessati) {
                    for (Programmazione prog : ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getProgrammazioneSpecifica(p.getKey(), start, end, min_ora, max_ora)) {

                        if (programmazioni_per_canale.containsKey(prog.getCanale())) {
                            if (!programmazioni_per_canale.get(prog.getCanale()).contains(prog)) {
                                programmazioni_per_canale.get(prog.getCanale()).add(prog);
                            }
                        } else {
                            List<Programmazione> toInsert = new ArrayList<>();
                            toInsert.add(prog);
                            programmazioni_per_canale.put(prog.getCanale(), toInsert);
                        }
                    }
                }
            } else {
                // Se sto filtrando per canale
                for (Programma p : interessati) {
                    for (Programmazione prog : ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getProgrammazioneSpecifica(p.getKey(), start, end, min_ora, max_ora)) {
                        if ((boolean) request.getAttribute("logged")) {

                            if (canali.contains(prog.getCanale().getKey())) {
                                if (programmazioni_per_canale.containsKey(prog.getCanale())) {
                                    if (!programmazioni_per_canale.get(prog.getCanale()).contains(prog)) {
                                        programmazioni_per_canale.get(prog.getCanale()).add(prog);
                                    }
                                } else {
                                    List<Programmazione> toInsert = new ArrayList<>();
                                    toInsert.add(prog);
                                    programmazioni_per_canale.put(prog.getCanale(), toInsert);
                                }
                            }

                        }
                    }
                }
            }

            // Ordino le programmazioni per start_time
            for (Map.Entry<Canale, List<Programmazione>> entry : programmazioni_per_canale.entrySet()) {
                Collections.sort(entry.getValue(), Collections.reverseOrder());
            }

            request.setAttribute("risultati", programmazioni_per_canale);

            request.setAttribute("titoloSearchValue", titolo);
            request.setAttribute("generiSearchValue", generi);
            request.setAttribute("canaliSearchValue", canali);
            request.setAttribute("startMinSearchValue", min_ora);
            request.setAttribute("startMaxSearchValue", max_ora);
            request.setAttribute("dateMinSearchValue", date_min);
            request.setAttribute("dateMaxSearchValue", date_max);

            results.activate("risultati.ftl.html", request, response);
        } catch (DataException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }

}
