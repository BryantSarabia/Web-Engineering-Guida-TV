package com.mycompany.guida.tv.admin;

import com.mycompany.guida.tv.controller.BaseController;
import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import com.mycompany.guida.tv.data.impl.SerieImpl;
import com.mycompany.guida.tv.data.model.Film;
import com.mycompany.guida.tv.data.model.Genere;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.model.Serie;
import com.mycompany.guida.tv.result.FailureResult;
import com.mycompany.guida.tv.result.TemplateManagerException;
import com.mycompany.guida.tv.result.TemplateResult;
import com.mycompany.guida.tv.security.SecurityLayer;
import com.mycompany.guida.tv.shared.Validator;

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
            boolean is_admin = true; //SecurityLayer.checkAdminSession(request);

            if (is_admin) {
                if (request.getParameter("insert") != null) {
                    action_create(request, response);
                } else if (request.getParameter("edit") != null) {
                    action_edit(request, response);
                } else if (request.getParameter("delete") != null) {
                    action_delete(request, response);
                } else if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
                    action_store(request, response);
                } else {
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
            Integer numero = (Integer) Validator.validate(request.getParameter("page"), new ArrayList<>(Arrays.asList(Validator.REQUIRED, Validator.INTEGER)), "numero");
            int start=(numero-1)*10;
            int elements=10;
            serie = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().getListaSeriePaginated(start, elements);
        }
        int numero_pagine = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().getNumeroFilm()/10;

        TemplateResult results = new TemplateResult(getServletContext());
     /*   UtenteProxy me = (UtenteProxy) Methods.getMe(request);
        request.setAttribute("me", me);*/
        request.setAttribute("numero_pagine", numero_pagine);
        request.setAttribute("serie", serie);
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
        results.activate("/admin/serie/index.ftl.html", request, response);

    }

    private void action_edit(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {
        int id_element = SecurityLayer.checkNumeric(request.getParameter("data_id"));
        Programma item = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammaDAO().getProgramma(id_element);
        List<Genere> generi = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getGenereDAO().getGeneri();
        request.setAttribute("generi", generi);

        TemplateResult results = new TemplateResult(getServletContext());
        request.setAttribute("item", item);
        request.setAttribute("outline_tpl", "");
        results.activate("/admin/serie/index.ftl.html", request, response);
    }

    private void action_loginredirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void action_store(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        try {

            String titolo = (String) Validator.validate(request.getParameter("titolo"), new ArrayList<>(Arrays.asList(Validator.REQUIRED, Validator.STRING_NOT_EMPTY, Validator.STRING_QUERY_PARAMETER)), "nome");
            String descrizione = (String) Validator.validate(request.getParameter("descrizione"), new ArrayList<>(Arrays.asList(Validator.STRING_QUERY_PARAMETER)), "descrizione");
            String linkRefDetails = (String) Validator.validate(request.getParameter("link_ref"), new ArrayList<>(Arrays.asList(Validator.REQUIRED, Validator.STRING_NOT_EMPTY)), "Link Details");
            String durata = (String) Validator.validate(request.getParameter("durata"), new ArrayList<>(Arrays.asList(Validator.REQUIRED, Validator.INTEGER)), "Durata");
            Integer stagione = (Integer) Validator.validate(request.getParameter("stagione"), new ArrayList<>(Arrays.asList(Validator.REQUIRED, Validator.INTEGER)), "Stagione");
            Integer episodio = (Integer) Validator.validate(request.getParameter("episodio"), new ArrayList<>(Arrays.asList(Validator.REQUIRED, Validator.INTEGER)), "Episodio");
            Integer id_genere = (Integer) Validator.validate(request.getParameter("genere"), new ArrayList<>(Arrays.asList(Validator.REQUIRED, Validator.INTEGER)), "Genere");
            Serie target = new SerieImpl();

            target.setTitolo(titolo);
            if(descrizione != null) {
                target.setDescrizione(descrizione);}
            target.setStagione(stagione);
            target.setEpisodio(episodio);
            target.setDurata(durata);
            target.setLink_ref(linkRefDetails);
           target.setGeneri((List<Genere>) ((GuidaTVDataLayer) request.getAttribute("datalayer")).getGenereDAO().getGenere(id_genere));
            target.setImg("null");
            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().storeSerie(target);

                Part image = request.getPart("immagine");
                if (image != null) {
                    String name = "prog_" + target.getKey() + ".jpg";
                    String path = getServletContext().getRealPath("img_tv/progs") + File.separatorChar + name;
                    String contentType = image.getContentType();
                    long size = image.getSize();
                    if (size > 0 && name != null && !name.isEmpty()) {
                        File new_file = new File(path);
                        Files.copy(image.getInputStream(), new_file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        target.setLink_ref("img_tv/progs" + name);
                        ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().storeSerie(target);
                    }
                }
            request.setAttribute("success", "Serie creata");
            TemplateResult results = new TemplateResult(getServletContext());
            List<Serie> serie = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().getListaSerie(0, 10);
            int numero_pagine = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().getNumeroFilm()/10;
            request.setAttribute("numero_pagine", numero_pagine);
            request.setAttribute("serie", serie);
            request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
            results.activate("/admin/serie/index.ftl.html", request, response);
        } catch (IOException | ServletException | DataException ex) {
            request.setAttribute("errors", ex.getMessage());
            TemplateResult results = new TemplateResult(getServletContext());
            List<Genere> generi = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getGenereDAO().getGeneri();
            request.setAttribute("generi", generi);
            request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
            results.activate("/admin/serie/new.ftl.html", request, response);

        }
    }

    private void action_create(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        TemplateResult results = new TemplateResult(getServletContext());
        List<Genere> generi = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getGenereDAO().getGeneri();

        request.setAttribute("generi", generi);
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
        results.activate("/admin/serie/new.ftl.html", request, response);
    }

    private void action_delete(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        try {
            Integer key = (Integer) Validator.validate(request.getParameter("data_id"), new ArrayList<>(Arrays.asList(Validator.REQUIRED, Validator.INTEGER)), "ID");
            if (key == null) {
                throw new DataException("Invalid Key");
            }
            Programma p = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammaDAO().getProgramma(key);
            if (p == null) {
                throw new DataException("Invalid Key");
            }

            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getProgrammaDAO().deleteProgramma(key);
            request.setAttribute("success", "true");
            List<Serie> serie = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().getListaSerie(0, 10);
            int numero_pagine = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().getNumeroFilm()/10;
            request.setAttribute("numero_pagine", numero_pagine);
            request.setAttribute("serie", serie);

        } catch (DataException ex) {
            request.setAttribute("errors", ex.getMessage());
            List<Serie> serie = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getSerieDAO().getListaSerie(0, 10);
            int numero_pagine = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getFilmDAO().getNumeroFilm()/10;
            request.setAttribute("numero_pagine", numero_pagine);
            request.setAttribute("serie", serie);

        }

    }

}
