package com.mycompany.guida.tv.controller.admin;

import com.mycompany.guida.tv.controller.BaseController;
import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import com.mycompany.guida.tv.data.impl.SerieImpl;
import com.mycompany.guida.tv.data.model.Genere;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.model.Serie;
import com.mycompany.guida.tv.data.proxy.UtenteProxy;
import com.mycompany.guida.tv.result.FailureResult;
import com.mycompany.guida.tv.result.TemplateManagerException;
import com.mycompany.guida.tv.result.TemplateResult;
import com.mycompany.guida.tv.security.SecurityLayer;
import com.mycompany.guida.tv.shared.Methods;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@MultipartConfig
public class Series extends BaseController {

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
            boolean is_admin = SecurityLayer.checkAdminSession(request);

            if (is_admin) {
                if (request.getParameter("insert") != null) {
                    action_create(request, response);
                } else if (request.getParameter("edit") != null) {
                    action_edit(request, response);
                } else if (request.getParameter("delete") != null) {
                    action_delete(request, response);
                } else if (request.getParameter("store") != null) {
                    action_store(request, response);
                } else if (request.getParameter("update") != null) {
                    action_update(request, response);
                } else if (request.getParameter("e") != null) {
                    action_e_default(request, response);
                }
                else if (request.getParameter("e_insert") != null) {
                    action_edit(request, response);
                }
                else if (request.getParameter("e_edit") != null) {
                    action_edit(request, response);
                }
                else if (request.getParameter("e_delete") != null) {
                    action_edit(request, response);
                }
                else if (request.getParameter("e_store") != null) {
                    action_edit(request, response);
                }
                else if (request.getParameter("e_update") != null) {
                    action_edit(request, response);
                }
                else {
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

        List<Serie> serie;
        if(request.getParameter("page") == null){
            serie = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().getListaSerie(0, 10);
        }
        else {
            Integer numero = SecurityLayer.checkNumeric(request.getParameter("page"));
            int start=(numero-1)*10;
            int elements=10;
            serie = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().getListaSeriePaginated(start, elements);
        }
        int numero_pagine = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().getNumeroFilm()/10;

        TemplateResult results = new TemplateResult(getServletContext());
        UtenteProxy me = (UtenteProxy) Methods.getMe(request);
        request.setAttribute("me", me);
        request.setAttribute("numero_pagine", numero_pagine);
        request.setAttribute("serie", serie);
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
        results.activate("/admin/serie/index.ftl.html", request, response);

    }
    private void action_e_default(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {
        List<Serie> serie;
        if(request.getParameter("page") == null){
            serie = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().getListaSerie(0, 10);
        }
        else {
            Integer numero = SecurityLayer.checkNumeric(request.getParameter("page"));
            int start=(numero-1)*10;
            int elements=10;
            serie = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().getListaSeriePaginated(start, elements);
        }
        int numero_pagine = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().getNumeroFilm()/10;

        TemplateResult results = new TemplateResult(getServletContext());
        UtenteProxy me = (UtenteProxy) Methods.getMe(request);
        request.setAttribute("me", me);
        request.setAttribute("numero_pagine", numero_pagine);
        request.setAttribute("serie", serie);
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
        results.activate("/admin/serie/index.ftl.html", request, response);
    }

        private void action_edit(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {
        int id = SecurityLayer.checkNumeric(request.getParameter("edit"));
        Serie serie = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().getSerie(id);
        List<Genere> generi = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getGenereDAO().getGeneri();
        request.setAttribute("generi", generi);
        UtenteProxy me = (UtenteProxy) Methods.getMe(request);
        request.setAttribute("me", me);
        TemplateResult results = new TemplateResult(getServletContext());
        request.setAttribute("serie", serie);
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
        results.activate("/admin/serie/edit.ftl.html", request, response);
    }

    private void action_loginredirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void action_store(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        try {
            String titolo = request.getParameter("titolo");
            String descrizione = request.getParameter("descrizione");
            String link_ref = request.getParameter("link_ref");
            String durata = request.getParameter("durata");
            String stg = request.getParameter("stagione");
            String epis = request.getParameter("episodio");
            /**
             * questa funzione serve per prendere generi
             */
            ArrayList<Integer> generi = null;
            if (request.getParameterValues("genere") != null && !request.getParameter("genere").isEmpty()) {
                generi = new ArrayList<>();
                for (String g : request.getParameterValues("genere")) {
                    if (g != null) {
                        generi.add(SecurityLayer.checkNumeric(g));
                    }
                }
            }
             
           Serie target = new SerieImpl();
            if (((String) titolo).isBlank()) {
                throw new DataException("Invalid parameter: " + titolo + " must be not empty");
            }
            Integer stagione=null;
            Integer episodio=null;
            if (((String) stg) != null) {
                    try{
                        stagione = SecurityLayer.checkNumeric(stg);
                    } catch (Exception ex) {
                        throw new DataException("Invalid parameter: " + stg + " must be a integer value");
                    }
                }
            if (((String) epis) != null) {
                try{
                    episodio = SecurityLayer.checkNumeric(stg);
                } catch (Exception ex) {
                    throw new DataException("Invalid parameter: " + epis + " must be a integer value");
                }
            }

            target.setTitolo(titolo);
            if(descrizione != null) {
                target.setDescrizione(descrizione);}
            target.setStagione(stagione);
            target.setEpisodio(episodio);
            target.setDurata(durata);
            target.setLink_ref(link_ref);
            List<Genere> generi_list = new ArrayList<>();
            for(int i : generi){
                generi_list.add(((GuidaTVDataLayer) request.getAttribute("datalayer")).getGenereDAO().getGenere(i));
            }
            target.setGeneri(generi_list);
            target.setImg("null");
            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().storeSerie(target);

                Part image = request.getPart("immagine");
                if (image != null) {
                    String name = "prog_" + target.getKey() + ".jpg";
                    String path = getServletContext().getRealPath("img_tv/progs/") + File.separatorChar + name;
                    long size = image.getSize();
                    if (size > 0 && name != null && !name.isEmpty()) {
                        File new_file = new File(path);
                        Files.copy(image.getInputStream(), new_file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        target.setImg("img_tv/progs/" + name);
                        ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().storeSerie(target);
                    }
                }
            request.setAttribute("success", request.getParameter("serie creata con successo"));
            action_default(request,response);
        } catch (IOException | ServletException | DataException ex) {
            request.setAttribute("errors", ex.getMessage());
            TemplateResult results = new TemplateResult(getServletContext());
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
        results.activate("/admin/serie/new.ftl.html", request, response);
    }
    private void action_update(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        int id = SecurityLayer.checkNumeric(request.getParameter("id"));

            try {
                String titolo = request.getParameter("titolo");
                String descrizione = request.getParameter("descrizione");
                String link_ref = request.getParameter("link_ref");
                String durata = request.getParameter("durata");
                String stg = request.getParameter("stagione");
                String epis = request.getParameter("episodio");

                ArrayList<Integer> generi = null;
                if (request.getParameterValues("genere") != null && !request.getParameter("genere").isEmpty()) {
                    generi = new ArrayList<>();
                    for (String g : request.getParameterValues("genere")) {
                        if (g != null) {
                            generi.add(SecurityLayer.checkNumeric(g));
                        }
                    }
                }
                Serie target = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().getSerie(id);
                if (((String) titolo).isBlank()) {
                    throw new DataException("Invalid parameter: " + titolo + " must be not empty");
                }
                Integer stagione=null;
                Integer episodio=null;
                if (((String) stg) != null) {
                    try{
                        stagione = SecurityLayer.checkNumeric(stg);
                    } catch (Exception ex) {
                        throw new DataException("Invalid parameter: " + stg + " must be a integer value");
                    }
                }
                if (((String) epis) != null) {
                    try{
                        episodio = SecurityLayer.checkNumeric(stg);
                    } catch (Exception ex) {
                        throw new DataException("Invalid parameter: " + epis + " must be a integer value");
                    }
                }
                target.setTitolo(titolo);
                    target.setDescrizione(descrizione);
                target.setStagione(stagione);
                target.setEpisodio(episodio);
                target.setDurata(durata);
                target.setLink_ref(link_ref);
                List<Genere> generi_list = new ArrayList<>();
                for(int i : generi){
                    generi_list.add(((GuidaTVDataLayer) request.getAttribute("datalayer")).getGenereDAO().getGenere(i));
                }
                target.setGeneri(generi_list);

                Part image = request.getPart("immagine");
                if (image != null) {
                    String name = "prog_" + target.getKey() + ".jpg";
                    String path = getServletContext().getRealPath("img_tv/progs/") + File.separatorChar + name;
                    long size = image.getSize();
                    if (size > 0 && name != null && !name.isEmpty()) {
                        File new_file = new File(path);
                        Files.copy(image.getInputStream(), new_file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        target.setImg("img_tv/progs/" + name);
                    }
                }
                ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().storeSerie(target);

                request.setAttribute("success", "serie aggiornata con successo!");
                action_default(request,response);
            } catch (IOException | ServletException | DataException ex) {
                request.setAttribute("errors", ex.getMessage());
                request.setAttribute("edit", id);
                action_edit(request,response);

            }
        }
    private void action_delete(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        Integer key = SecurityLayer.checkNumeric(request.getParameter("id"));

        try {
            if (key == null) {
                throw new DataException("Invalid Key");
            }
            Programma p = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammaDAO().getProgramma(key);
            if (p == null) {
                throw new DataException("Invalid Key");
            }

            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammaDAO().deleteProgramma(key);
            request.setAttribute("success", "Serie cancellata con successo!");
            action_default(request,response);
        } catch (DataException ex) {
            request.setAttribute("errors", ex.getMessage());
            action_default(request,response);
        }

    }

}
