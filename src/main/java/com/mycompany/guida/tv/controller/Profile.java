/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guida.tv.controller;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import com.mycompany.guida.tv.data.model.Interessa;
import com.mycompany.guida.tv.data.proxy.InteressaProxy;
import com.mycompany.guida.tv.data.proxy.UtenteProxy;
import com.mycompany.guida.tv.result.FailureResult;
import com.mycompany.guida.tv.result.TemplateManagerException;
import com.mycompany.guida.tv.result.TemplateResult;
import com.mycompany.guida.tv.security.BCrypt;
import com.mycompany.guida.tv.security.SecurityLayer;
import com.mycompany.guida.tv.shared.Methods;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
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
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s != null) {
                if (Methods.getMe(request).getEmailVerifiedAt() == null) {
                     action_redirect(request, response);
                } else {
                    if (request.getParameter("update_password") != null) {
                        action_update_password(request, response);
                    } else if (request.getParameter("submit") != null) {
                        action_update(request, response);
                    } else {
                        action_default(request, response);
                    }
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
        try {
        } catch (Exception ex) {
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private void action_update(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {

        boolean valid = true;
        String error = "";

        if (!SecurityLayer.checkBoolean(request.getParameter("desidero"))) {
            error += "Invalid input";
            valid = false;
        } else {
            boolean send = (request.getParameter("desidero").equals("1"));
            List<Integer> id_canali = null;
            List<Integer> id_fasce = null;

            if (request.getParameterValues("canali") != null && !request.getParameter("canali").isEmpty()) {
                id_canali = new ArrayList<>();
                for (String c : request.getParameterValues("canali")) {
                    if (c != null) {
                        id_canali.add(SecurityLayer.checkNumeric(c));
                    }
                }
                if (id_canali.contains(0)) {
                    id_canali = null;
                }
            }

            if (request.getParameterValues("fasce") != null && !request.getParameter("fasce").isEmpty()) {
                id_fasce = new ArrayList<>();
                for (String f : request.getParameterValues("fasce")) {
                    if (f != null) {
                        id_fasce.add(SecurityLayer.checkNumeric(f));
                    }
                }
            }
            if (id_canali != null && id_fasce != null) {
                UtenteProxy me = (UtenteProxy) Methods.getMe(request);
                me.setSendEmail(send);
                me.cleanInteressi();
                List<Interessa> update_interessi = new ArrayList<>();
                for (Integer c_id : id_canali) {
                    if (id_fasce.contains(0)) {

                        Interessa to_add = new InteressaProxy((DataLayer) request.getAttribute("datalayer"));
                        to_add.setCanale(((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getCanale(c_id));
                        to_add.setUtente(me);
                        to_add.setStartTime(Methods.getOrarioInizioFascia(0));
                        to_add.setEndTime(Methods.getOrarioFineFascia(0));
                        update_interessi.add(to_add);
                        ((GuidaTVDataLayer) request.getAttribute("datalayer")).getInteressaDAO().storeInteresse(to_add);
                    } else {
                        for (Integer f_id : id_fasce) {
                            Interessa to_add = new InteressaProxy((DataLayer) request.getAttribute("datalayer"));
                            to_add.setCanale(((GuidaTVDataLayer) request.getAttribute("datalayer")).getCanaleDAO().getCanale(c_id));
                            to_add.setUtente(me);
                            to_add.setStartTime(Methods.getOrarioInizioFascia(f_id));
                            to_add.setEndTime(Methods.getOrarioFineFascia(f_id));
                            update_interessi.add(to_add);
                            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getInteressaDAO().storeInteresse(to_add);
                        }
                    }
                }
                me.setInteressi(update_interessi);
                ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().storeUtente(me);
            } else {
                if (send) {
                    // aggiorno email
                    UtenteProxy me = (UtenteProxy) Methods.getMe(request);
                    me.setSendEmail(send);
                    ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().storeUtente(me);
                } else {
                    UtenteProxy me = (UtenteProxy) Methods.getMe(request);
                    me.setSendEmail(send);
                    ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().storeUtente(me);
                }

            }

        }

        if (!valid) {
            request.setAttribute("error", error);
        } else {
            request.setAttribute("message", "Preferences updated successfully");
        }
        action_default(request, response);
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
    
        private void action_redirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("verifyemail");
    }

}
