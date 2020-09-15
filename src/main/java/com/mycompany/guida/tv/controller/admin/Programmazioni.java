package com.mycompany.guida.tv.controller.admin;


import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.impl.ProgrammazioneImpl;
import com.mycompany.guida.tv.data.model.*;

import com.mycompany.guida.tv.data.proxy.UtenteProxy;
import com.mycompany.guida.tv.result.FailureResult;
import com.mycompany.guida.tv.result.TemplateManagerException;
import com.mycompany.guida.tv.result.TemplateResult;
import com.mycompany.guida.tv.security.SecurityLayer;
import com.mycompany.guida.tv.shared.Methods;
import com.mycompany.guida.tv.controller.BaseController;
import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Programmazioni extends BaseController {

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
            boolean is_admin =  SecurityLayer.checkAdminSession(request);

            if (is_admin) {
                if (request.getParameter("insert") != null) {
                    action_create(request, response);
                }
                else if (request.getParameter("insert_s") != null) {
                    action_s_create(request, response);
                }else if (request.getParameter("edit") != null) {
                    action_edit(request, response);
                } else if (request.getParameter("delete") != null) {
                    action_delete(request, response);
                } else if (request.getParameter("store") != null) {
                    action_store(request, response);
                }  else if (request.getParameter("store_s") != null) {
                    action_s_store(request, response);
                } else if (request.getParameter("update") != null) {
                    action_update(request, response);
                }else {
                    action_default(request, response);
                }

            } else {
                action_loginredirect(request, response);
            }
        } catch (IOException | DataException | TemplateManagerException ex) {
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

        List<Programmazione> programmazioni;
        if(request.getParameter("page") == null){
            programmazioni = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getProgrammazioniPaginated(0, 10);
        }
        else {
            Integer numero = SecurityLayer.checkNumeric(request.getParameter("page"));
            int start=(numero-1)*10;
            int elements=10;
            programmazioni = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getProgrammazioniPaginated(start, elements);
        }
        int numero_pagine = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getNumeroProgrammazioni()/10;

        TemplateResult results = new TemplateResult(getServletContext());
        UtenteProxy me = (UtenteProxy) Methods.getMe(request);
        request.setAttribute("me", me);
        request.setAttribute("programmazioni", programmazioni);
        request.setAttribute("numero_pagine", numero_pagine);
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));

        results.activate("/admin/programmazioni/index.ftl.html", request, response);

    }

    private void action_loginredirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void action_create(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        TemplateResult results = new TemplateResult(getServletContext());
        List<Film> programmi = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().getListaFilm();
        List<Canale> canali = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getListaCanali();
        UtenteProxy me = (UtenteProxy) Methods.getMe(request);
        request.setAttribute("me", me);
        request.setAttribute("programmi", programmi);
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));

        results.activate("/admin/programmazioni/new.ftl.html", request, response);
    }
    private void action_s_create(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        TemplateResult results = new TemplateResult(getServletContext());
        List<Serie> programmi = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().getListaSerie();
        List<Canale> canali = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getListaCanali();
        UtenteProxy me = (UtenteProxy) Methods.getMe(request);
        request.setAttribute("me", me);
        request.setAttribute("programmi", programmi);
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));

        results.activate("/admin/programmazioni/new_s.ftl.html", request, response);
    }

    private void action_edit(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {
        Integer key = SecurityLayer.checkNumeric(request.getParameter("edit"));
        if (key == null) {
            throw new DataException("Invalid Key");
        }
        Programmazione programmazione = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getProgrammazione(key);
        UtenteProxy me = (UtenteProxy) Methods.getMe(request);
        request.setAttribute("me", me);

        String start_time= programmazione.getDate()+" "+programmazione.getTime()+":00";
        LocalDateTime start = LocalDateTime.parse(start_time, DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss"));
        String d=String.format("%d",start.getDayOfMonth());
        String m=String.format("%d",start.getMonthValue());
        String y=String.format("%d",start.getYear());
        if(m.length()==1){ m='0'+m;}
        String date = y + "/"+ m+'/'+d;
        TemplateResult results = new TemplateResult(getServletContext());
        request.setAttribute("programmazione", programmazione);
        request.setAttribute("date", date);
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
        if(programmazione.getEpisodio()==null){
            List<Film> programmi = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().getListaFilm();
            request.setAttribute("programmi", programmi);
            results.activate("/admin/programmazioni/edit.ftl.html", request, response);}
        else{
            List<Serie> programmi = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().getListaSerie();
            request.setAttribute("programmi", programmi);
            results.activate("/admin/programmazioni/edit_s.ftl.html", request, response);}
    }
    private void action_s_store(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        try {

            Integer id_canale = SecurityLayer.checkNumeric(request.getParameter("canale"));
            Integer id_programma = SecurityLayer.checkNumeric(request.getParameter("programma"));
            Integer durata = SecurityLayer.checkNumeric(request.getParameter("durata"));
            String data =request.getParameter("date");
            String time =request.getParameter("time");
            String start_time= data+" "+time+":00";
            LocalDateTime start = LocalDateTime.parse(start_time, DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));

            Canale c = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getCanale(id_canale);
            Serie s = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().getEpisodio(id_programma);
            Programma p = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammaDAO().getProgramma(s.getKey());
            Programmazione target = new ProgrammazioneImpl();
            target.setProgramma(p);
            target.setEpisodio(s);
            target.setCanale(c);

            target.setStartTime(start);
            target.setDurata(durata);
            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().storeProgrammazione(target);
            List<Programmazione> programmazioni = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getProgrammazioniPaginated(0, 10);
            int numero_pagine = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getNumeroProgrammazioni()/10;
            request.setAttribute("programmazioni", programmazioni);
            request.setAttribute("numero_pagine", numero_pagine);
            request.setAttribute("success", start_time);
            TemplateResult results = new TemplateResult(getServletContext());
            request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
            results.activate("/admin/programmazioni/index.ftl.html", request, response);
        } catch (DataException ex) {
            request.setAttribute("errors", ex.getMessage());
            TemplateResult results = new TemplateResult(getServletContext());
            request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
            results.activate("/admin/programmazioni/new.ftl.html", request, response);

        }
    }
    private void action_store(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        try {

            Integer id_canale = SecurityLayer.checkNumeric(request.getParameter("canale"));
            Integer id_programma = SecurityLayer.checkNumeric(request.getParameter("programma"));
            Integer durata = SecurityLayer.checkNumeric(request.getParameter("durata"));
            String data =request.getParameter("date");
            String time =request.getParameter("time");
            String start_time= data+" "+time+":00";
          LocalDateTime start = LocalDateTime.parse(start_time, DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));

            Canale c = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getCanale(id_canale);
            Film p = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().getFilm(id_programma);
            
            //Per differenziare fra film e serie setto a null il campo id_serie con setKeyEpisodio a 0
            Serie s = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().createSerie();
            s.setKeyEpisodio(0);

            Programmazione target = new ProgrammazioneImpl();
            target.setProgramma(p);
            target.setCanale(c);
            target.setEpisodio(s);

            target.setStartTime(start);
            target.setDurata(durata);
            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().storeProgrammazione(target);
            List<Programmazione> programmazioni = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getProgrammazioniPaginated(0, 10);
            int numero_pagine = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getNumeroProgrammazioni()/10;
            request.setAttribute("programmazioni", programmazioni);
            request.setAttribute("numero_pagine", numero_pagine);
            request.setAttribute("success", start_time);
            TemplateResult results = new TemplateResult(getServletContext());
            request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
            results.activate("/admin/programmazioni/index.ftl.html", request, response);
        } catch (DataException ex) {
            request.setAttribute("errors", ex.getMessage());
            TemplateResult results = new TemplateResult(getServletContext());
            request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
            results.activate("/admin/programmazioni/new.ftl.html", request, response);

        }
    }
    private void action_update(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, UnsupportedEncodingException, DataException {
        try {
            Integer key = SecurityLayer.checkNumeric(request.getParameter("id"));

            Integer id_canale = SecurityLayer.checkNumeric(request.getParameter("canale"));
            Integer id_programma = SecurityLayer.checkNumeric(request.getParameter("programma"));
            Integer durata = SecurityLayer.checkNumeric(request.getParameter("durata"));

            Canale c = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getCanale(id_canale);
            Programma p = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammaDAO().getProgramma(id_programma);
            Programmazione target = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getProgrammazione(key);

            String data =request.getParameter("date_time");
            String time =request.getParameter("time");
            String start_time= data+" "+time+":00";
            //   LocalDateTime start = LocalDateTime.parse(start_time, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

               LocalDateTime start = LocalDateTime.parse(start_time, DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss"));

            target.setProgramma(p);
            target.setCanale(c);
            target.setStartTime(start);
            target.setDurata(durata);
            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().storeProgrammazione(target);

            request.setAttribute("success", start_time);
            action_default(request,response);
        } catch (DataException ex) {
            Integer key = SecurityLayer.checkNumeric(request.getParameter("id"));

            request.setAttribute("errors", ex.getMessage());
            request.setAttribute("edit", key);
            action_edit(request,response);

        }
    }
    private void action_delete(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        Integer key = SecurityLayer.checkNumeric(request.getParameter("id"));
        try {
            if (key == null) {
                throw new DataException("Invalid Key");
            }
            Programmazione p = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().getProgrammazione(key);
            if (p == null) {
                throw new DataException("Invalid Key");
            }
            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammazioneDAO().deleteProgrammazione(key);

            request.setAttribute("success", "programmazione cancellata con successo!");
            action_default(request,response);

        } catch (DataException ex) {
            request.setAttribute("errors", ex.getMessage());
            action_default(request,response);

        }

    }


}
