/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guida.tv.controller;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import com.mycompany.guida.tv.data.model.Utente;
import com.mycompany.guida.tv.data.proxy.UtenteProxy;
import com.mycompany.guida.tv.result.FailureResult;
import com.mycompany.guida.tv.result.TemplateManagerException;
import com.mycompany.guida.tv.result.TemplateResult;
import com.mycompany.guida.tv.security.BCrypt;
import com.mycompany.guida.tv.security.SecurityLayer;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author HP
 */
public class Profile extends BaseController {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws DataException
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        response.setContentType("text/html;charset=UTF-8");
        int fascia = 1;
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s != null) {
                if (request.getParameter("update_password") != null) {
                    action_update_password(request, response);
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

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {

        TemplateResult results = new TemplateResult(getServletContext());
        UtenteProxy me = (UtenteProxy) ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente((int) request.getSession().getAttribute("userid"));
        request.setAttribute("me", me);
        results.activate("profile.ftl.html", request, response);

    }

    private void action_update_password(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {
        String password = (request.getParameter("password") != null) ? request.getParameter("password") : "";
        String confirm = (request.getParameter("password_confirm") != null) ? request.getParameter("password_confirm") : "";
        boolean valid = true;
        String error_msg = "";

        if (!password.isEmpty() && !confirm.isEmpty()) {

            if (!password.equals(confirm)) {
                error_msg += "Le password non combaciano, \n";
                valid = false;
            }

            if (valid) {

                UtenteProxy me = (UtenteProxy) ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente((int) request.getSession().getAttribute("userid"));
                me.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
                ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().storeUtente(me);

            }
            if (!valid) {
                request.setAttribute("error", error_msg);
            } else {
                request.setAttribute("message", "Info updated successfully");
            }
            action_default(request, response);

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
    
     private void action_loginredirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("login");
    }

}
