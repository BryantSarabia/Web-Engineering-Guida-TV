package com.mycompany.guida.tv.controller.admin;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.impl.FilmImpl;
import com.mycompany.guida.tv.data.model.*;
import com.mycompany.guida.tv.data.proxy.UtenteProxy;
import com.mycompany.guida.tv.result.FailureResult;
import com.mycompany.guida.tv.result.TemplateManagerException;
import com.mycompany.guida.tv.result.TemplateResult;
import com.mycompany.guida.tv.security.SecurityLayer;
import com.mycompany.guida.tv.shared.Methods;
import com.mycompany.guida.tv.controller.BaseController;
import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


@MultipartConfig
public class Films extends BaseController {

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
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Programma.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            boolean is_admin = true; //SecurityLayer.checkAdminSession(request);

            if (is_admin) {
                if (request.getParameter("insert") != null) {
                    action_create(request, response);
                } else if (request.getParameter("edit") != null) {
                    action_edit(request, response);
                } else if (request.getParameter("store") != null) {
                    action_store(request, response);
                } else if (request.getParameter("update") != null) {
                    action_update(request, response);
                } else if (request.getParameter("delete") != null) {
                    action_delete(request, response);
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

        List<Film> film;
        if(request.getParameter("page") == null){
            film = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().getListaFilm(0, 10);
        }
        else {
            Integer numero = SecurityLayer.checkNumeric(request.getParameter("page"));
            int start=(numero-1)*10;
            int elements=10;
            film = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().getListaFilmPaginated(start, elements);
        }
        int numero_pagine = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().getNumeroFilm()/10;

        TemplateResult results = new TemplateResult(getServletContext());
        UtenteProxy me = (UtenteProxy) Methods.getMe(request);
        request.setAttribute("me", me);
        request.setAttribute("film",film);
        request.setAttribute("numero_pagine",numero_pagine);
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));

        results.activate("/admin/film/index.ftl.html", request, response);

    }

    private void action_edit(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {
        int id = SecurityLayer.checkNumeric(request.getParameter("edit"));
        Film film = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().getFilm(id);
        List<Genere> generi = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getGenereDAO().getGeneri();
        request.setAttribute("generi", generi);
        UtenteProxy me = (UtenteProxy) Methods.getMe(request);
        request.setAttribute("me", me);
        TemplateResult results = new TemplateResult(getServletContext());
        request.setAttribute("film", film);

        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
        results.activate("/admin/film/edit.ftl.html", request, response);
    }

    private void action_loginredirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/login");
    }
    private void action_update(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException, UnsupportedEncodingException {

        try {
            int id = SecurityLayer.checkNumeric(request.getParameter("id"));

            String titolo = request.getParameter("titolo");
            String descrizione = request.getParameter("descrizione");
            String link_ref = request.getParameter("link_ref");
            String durata = request.getParameter("durata");
            Film target = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().getFilm(id);

            ArrayList<Integer> generi = null;
            if (request.getParameterValues("genere") != null && !request.getParameter("genere").isEmpty()) {
                generi = new ArrayList<>();
                for (String g : request.getParameterValues("genere")) {
                    if (g != null) {
                        generi.add(SecurityLayer.checkNumeric(g));
                    }
                }
            }


            List<Genere> generi_list = new ArrayList<>();
            for(int i : generi){
                generi_list.add(((GuidaTVDataLayer) request.getAttribute("datalayer")).getGenereDAO().getGenere(i));
            }

            target.setGeneri(generi_list);
            
            if (((String) titolo).isBlank()) {
                throw new DataException("Invalid parameter: " + titolo + " must be not empty");
            }
            target.setTitolo(titolo);
            target.setDescrizione(descrizione);
            target.setDurata(durata);
            target.setLink_ref(link_ref);

            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().storeFilm(target);

            Part image = request.getPart("immagine");
            if (image != null) {
                String name = "prog_" + target.getKey() + ".jpg";
                String path = getServletContext().getRealPath("img_tv/progs/") + File.separatorChar + name;
                long size = image.getSize();
                if (size > 0 && name != null && !name.isEmpty()) {
                    File new_file = new File(path);
                    Files.copy(image.getInputStream(), new_file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    target.setImg("img_tv/progs/" + name);
                    ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().storeFilm(target);
                }
            }
            request.setAttribute("success", "Film aggiornato");
            action_default(request,response);
        } catch (IOException | ServletException | DataException ex) {
            int id = SecurityLayer.checkNumeric(request.getParameter("id"));

            request.setAttribute("errors", ex.getMessage());
            request.setAttribute("edit", id);
            action_edit(request,response);

        }
    }
    private void action_store(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, UnsupportedEncodingException,DataException {
        try {

            String titolo = request.getParameter("titolo");
            String descrizione = request.getParameter("descrizione");
            String link_ref = request.getParameter("link_ref");
            String durata = request.getParameter("durata");
            Film target = new FilmImpl();

            ArrayList<Integer> generi = null;
            if (request.getParameterValues("genere") != null && !request.getParameter("genere").isEmpty()) {
                generi = new ArrayList<>();
                for (String g : request.getParameterValues("genere")) {
                    if (g != null) {
                        generi.add(SecurityLayer.checkNumeric(g));
                    }
                }
            }

            if (((String) titolo).isBlank()) {
                throw new DataException("Invalid parameter: " + titolo + " must be not empty");
            }
            List<Genere> generi_list = new ArrayList<>();
            for(int i : generi){
                generi_list.add(((GuidaTVDataLayer) request.getAttribute("datalayer")).getGenereDAO().getGenere(i));
            }
            target.setGeneri(generi_list);
            target.setTitolo(titolo);
            if(descrizione != null) target.setDescrizione(descrizione);
            target.setDurata(durata);
            target.setLink_ref(link_ref);
            
            target.setImg("null");

            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().storeFilm(target);

            Part image = request.getPart("immagine");
            if (image != null) {
                String name = "prog_" + target.getKey() + ".jpg";
                String path = getServletContext().getRealPath("img_tv/progs/") + File.separatorChar + name;
                long size = image.getSize();
                if (size > 0 && name != null && !name.isEmpty()) {
                    File new_file = new File(path);
                    Files.copy(image.getInputStream(), new_file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    target.setImg("img_tv/progs/" + name);
                    ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().storeFilm(target);
                }
            }
            request.setAttribute("success", "Film creato");
            action_default(request,response);
        } catch (IOException | ServletException | DataException ex) {
            request.setAttribute("errors", ex.getMessage());
            action_create(request,response);

        }
    }

    private void action_create(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        TemplateResult results = new TemplateResult(getServletContext());
        List<Genere> generi = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getGenereDAO().getGeneri();

        UtenteProxy me = (UtenteProxy) Methods.getMe(request);
        request.setAttribute("me", me);
        request.setAttribute("generi", generi);
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
        results.activate("/admin/film/new.ftl.html", request, response);
    }

    private void action_delete(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {

        try {
            Integer key = SecurityLayer.checkNumeric(request.getParameter("id"));
            if (key == null) {
                throw new DataException("Invalid Key");
            }
            Programma p = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammaDAO().getProgramma(key);
            if (p == null) {
                throw new DataException("Invalid Key");
            }

            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammaDAO().deleteProgramma(key);

            request.setAttribute("success", "Film cancellato con successo!");
            action_default(request,response);
        } catch (DataException ex) {
            request.setAttribute("errors", ex.getMessage());
            action_default(request,response);


        }

    }

}
