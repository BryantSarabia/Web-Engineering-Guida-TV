/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guida.tv.controller.auth;

import com.mycompany.guida.tv.controller.BaseController;
import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import com.mycompany.guida.tv.data.model.Utente;
import com.mycompany.guida.tv.result.FailureResult;
import com.mycompany.guida.tv.result.TemplateManagerException;
import com.mycompany.guida.tv.result.TemplateResult;
import com.mycompany.guida.tv.security.BCrypt;
import com.mycompany.guida.tv.security.SecurityLayer;
import java.io.IOException;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HP
 */
public class Login extends BaseController {

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

            if (request.getParameter("submit") != null) {
                action_login(request, response);
            } else {
                action_default(request, response);
            }
        } catch (DataException | IOException | TemplateManagerException ex) {
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
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException, IOException {

        HttpSession s = SecurityLayer.checkSession(request);
        if (s != null) {
            response.sendRedirect("profile");
        } else {
            if(request.getParameter("referrer") != null && !request.getParameter("referrer").isBlank()){
            String request_uri = URLDecoder.decode(request.getParameter("referrer"), "UTF-8");
            request.setAttribute("request_uri",request_uri);
            }
            TemplateResult results = new TemplateResult(getServletContext());
            results.activate("login.ftl.html", request, response);
        }

    }

    // Effettua il login e mi redireziona sul profilo
    private void action_login(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException, IOException {

        String email = (String) request.getParameter("email");

        if (!(SecurityLayer.CheckEmail((String) request.getParameter("email")))) {
            /* Email non valida */
            request.setAttribute("error", "Email non corrette.");
            action_default(request, response);
        }
        String password = (String) request.getParameter("password");

        if (request.getParameter("email") != null && request.getParameter("password") != null && !email.isEmpty() && !password.isEmpty()) {
            String utente_pass = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getPassword(email);
            if (utente_pass == null) {
                // Email non trovata
                request.setAttribute("error", "Credenziali non corrette.");
                action_default(request, response);
            }

            if (BCrypt.checkpw(password, utente_pass)) {
                // Credenziali corrette
                Utente me = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente(email);
                SecurityLayer.createSession(request, me.getEmail(), me.getKey());
                 if (me.getEmailVerifiedAt() == null) {
                    response.sendRedirect("verifyemail");
                }  else if (request.getParameter("referrer") != null) {
                    response.sendRedirect(URLDecoder.decode(request.getParameter("referrer")));

                } else {
                    response.sendRedirect("profile");
                }
            } else {
                request.setAttribute("error", "Credenziali non corrette.");
                action_default(request, response);
            }
        } else {
            request.setAttribute("error", "Compila tutti i campi");
            action_default(request, response);
        }

    }

}
