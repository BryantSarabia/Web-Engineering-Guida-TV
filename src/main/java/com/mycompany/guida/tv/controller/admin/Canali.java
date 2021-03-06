package com.mycompany.guida.tv.controller.admin;


import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.impl.CanaleImpl;
import com.mycompany.guida.tv.data.model.Canale;
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
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


@MultipartConfig
public class Canali extends BaseController {

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
                }
                else if (request.getParameter("update") != null) {
                    action_update(request, response);
                }
                else if (request.getParameter("store") != null) {
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

        List <Canale> canali;
        if(request.getParameter("page") == null){
            canali = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getListaCanali(0, 10);
        }
        else {

            Integer numero = SecurityLayer.checkNumeric(request.getParameter("page"));
            int start=(numero-1)*10;
            int elements=10;
            canali = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getListaCanaliPaginated(start, elements);
        }
        int numero_pagine = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getNumeroCanali()/10;

        TemplateResult results = new TemplateResult(getServletContext());
        UtenteProxy me = (UtenteProxy) Methods.getMe(request);
        request.setAttribute("me", me);
        request.setAttribute("canali", canali);
        request.setAttribute("numero_pagine", numero_pagine);
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
        results.activate("/admin/canali/index.ftl.html", request, response);

    }

    private void action_loginredirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void action_create(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        TemplateResult results = new TemplateResult(getServletContext());
        UtenteProxy me = (UtenteProxy) Methods.getMe(request);
        request.setAttribute("me", me);
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
        results.activate("/admin/canali/new.ftl.html", request, response);
    }

    private void action_edit(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        int id = SecurityLayer.checkNumeric(request.getParameter("edit"));
        Canale canale = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getCanale(id);
        UtenteProxy me = (UtenteProxy) Methods.getMe(request);
        request.setAttribute("me", me);
        TemplateResult results = new TemplateResult(getServletContext());
        request.setAttribute("canale", canale);
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
        results.activate("/admin/canali/edit.ftl.html", request, response);
    }

    private void action_delete(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        try {
            Integer key = SecurityLayer.checkNumeric(request.getParameter("id"));
            if (key == null) {
                throw new DataException("Invalid Key");
            }
            Canale c = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getCanale(key);
            if (c == null) {
                throw new DataException("Invalid Key");
            }

            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().deleteCanale(key);
            request.setAttribute("success", "canale cancellato con successo!");
            action_default(request,response);
        } catch (DataException ex) {
            request.setAttribute("errors", ex.getMessage());
            action_default(request,response);
        }
    }

    private void action_store(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        try {
            String nome = request.getParameter("nome");
            Integer numero = (Integer) SecurityLayer.checkNumeric(request.getParameter("numero"));
            if (((String) nome).isBlank()) {
                throw new DataException("Invalid parameter: " + nome + " must be not empty");
            }
            Canale target = new CanaleImpl();
                target.setNome(nome);
                target.setNumero(numero);
                target.setLogo("null");
            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().storeCanale(target);

            Part image = request.getPart("image");
            if (image != null) {
                String name = target.getKey() + ".jpg";
                String path = getServletContext().getRealPath("img_tv/canali/small/") + File.separatorChar + name;
                String contentType = image.getContentType();
                long size = image.getSize();
                if (size > 0 && name != null && !name.isEmpty()) {
                    File new_file = new File(path);
                    Files.copy(image.getInputStream(), new_file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    target.setLogo("img_tv/canali/small/" + name);
                    ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().storeCanale(target);
                }
            }


            request.setAttribute("success", "canale creato");
            action_default(request,response);
        } catch (IOException | ServletException | DataException ex) {
            request.setAttribute("errors", ex.getMessage());
            action_create(request,response);

        }
    }
    private void action_update(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException  {
        Integer key = SecurityLayer.checkNumeric(request.getParameter("key"));

        try {
            String nome = request.getParameter("nome");
            Integer numero = (Integer) SecurityLayer.checkNumeric(request.getParameter("numero"));
            if (((String) nome).isBlank()) {
                throw new DataException("Invalid parameter: " + nome + " must be not empty");
            }  Canale target;
            target = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getCanale(key);
            target.setNome(nome);
            target.setNumero(numero);
            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().storeCanale(target);

            Part image = request.getPart("logo");
            if (image != null) {
                String name = target.getKey() + ".jpg";
                String path = getServletContext().getRealPath("img_tv/canali/small/") + File.separatorChar + name;
                long size = image.getSize();
                if (size > 0 && name != null && !name.isEmpty()) {
                    File new_file = new File(path);
                    Files.copy(image.getInputStream(), new_file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    target.setLogo("img_tv/canali/small/" + name);
                    ((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().storeCanale(target);
                }
            }

            request.setAttribute("success", "canale aggiornato");
            action_default(request,response);
        } catch (IOException | ServletException | DataException ex) {
            request.setAttribute("edit", key);
            request.setAttribute("errors", ex.getMessage());
            action_update(request,response);

        }
    }

}
