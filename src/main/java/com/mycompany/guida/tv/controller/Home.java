/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guida.tv.controller;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.model.Film;
import com.mycompany.guida.tv.data.model.Genere;
import com.mycompany.guida.tv.data.model.Programmazione;
import com.mycompany.guida.tv.data.model.Serie;
import com.mycompany.guida.tv.result.FailureResult;
import com.mycompany.guida.tv.result.JSONResult;
import com.mycompany.guida.tv.result.TemplateManagerException;
import com.mycompany.guida.tv.result.TemplateResult;
import com.mycompany.guida.tv.security.SecurityLayer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Home extends BaseController {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws com.mycompany.guida.tv.result.TemplateManagerException
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            if (request.getParameter("page") != null && !request.getParameter("page").isEmpty() && request.getParameter("json") == null) {
                int page = SecurityLayer.checkNumeric(request.getParameter("page"));
                action_paginated(request, response, page);
            } else if (request.getParameter("page") != null && !request.getParameter("page").isEmpty() && request.getParameter("json") != null) {
                int page = SecurityLayer.checkNumeric(request.getParameter("page"));
                action_paginated_json(request, response, page);
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

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {
        action_paginated(request, response, 0);
    }

    private void action_paginated(HttpServletRequest request, HttpServletResponse response, int page) throws DataException, TemplateManagerException {

        int numero_canali = 0;
        int canali_per_pagina = 6;

        try {
            List<Serie> series = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().getListaSerie();
            for(Serie serie : series){
                System.out.println("titolo: " + serie.getTitolo());
                System.out.println("stagione: " + serie.getStagione());
                System.out.println("episodio: " + serie.getEpisodio());
            }
            
            TemplateResult results = new TemplateResult(getServletContext());
            List<Canale> canali = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getListaCanali(page, canali_per_pagina);
            numero_canali = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getNumeroCanali();
            Map<Canale, Programmazione> current = new TreeMap();

            for (Canale c : canali) {
                Programmazione programmazione = c.getProgrammazioneCorrente();
                if (programmazione != null) {
                    current.put(c, programmazione);
                } else {
                    current.put(c, null);
                }
            }

            request.setAttribute("numero_pagine", (int) (Math.ceil(numero_canali / canali_per_pagina)));
            request.setAttribute("pagina", page);
            request.setAttribute("current_prog", current);
            results.activate("home.ftl.html", request, response);
        } catch (DataException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }

    }

    private void action_paginated_json(HttpServletRequest request, HttpServletResponse response, int page) throws DataException, TemplateManagerException {

        int numero_canali = 0;
        int canali_per_pagina = 6;

        try {
            JSONResult results = new JSONResult(getServletContext());
            List<Canale> canali = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getListaCanali(page, canali_per_pagina);
            numero_canali = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getNumeroCanali();
            Map<Canale, Programmazione> current = new TreeMap();

            for (Canale c : canali) {
                Programmazione programmazione = c.getProgrammazioneCorrente();
                if (programmazione != null) {
                    current.put(c, programmazione);
                } else {
                    current.put(c, null);
                }
            }

            request.setAttribute("numero_pagine", (int) (Math.ceil(numero_canali / canali_per_pagina)));
            request.setAttribute("pagina", page);
            request.setAttribute("current_prog", current);
            results.activate("/json/home_paginated.ftl.json", request, response);
        } catch (DataException ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
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

}
