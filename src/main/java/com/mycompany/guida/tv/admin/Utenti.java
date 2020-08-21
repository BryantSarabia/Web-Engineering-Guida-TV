package com.mycompany.guida.tv.admin;


import com.mycompany.guida.tv.controller.BaseController;
import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import com.mycompany.guida.tv.data.impl.UtenteImpl;
import com.mycompany.guida.tv.data.model.Ruolo;
import com.mycompany.guida.tv.data.model.Utente;
import com.mycompany.guida.tv.data.proxy.UtenteProxy;
import com.mycompany.guida.tv.result.FailureResult;
import com.mycompany.guida.tv.result.TemplateManagerException;
import com.mycompany.guida.tv.result.TemplateResult;
import com.mycompany.guida.tv.security.BCrypt;
import com.mycompany.guida.tv.security.SecurityLayer;
import com.mycompany.guida.tv.shared.Methods;
import com.mycompany.guida.tv.shared.Validator;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Utenti extends BaseController {

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
            boolean is_admin = true;//SecurityLayer.checkAdminSession(request);
            if (is_admin) {
                if (request.getParameter("draw") != null) {
                    action_paginate_results(request, response);
                } else if (request.getParameter("insert") != null) {
                    action_create(request, response);
                } else if (request.getParameter("edit") != null) {
                    action_edit(request, response);
                } else if (request.getParameter("delete") != null) {
                    action_delete(request, response);
                } else if (request.getParameter("store") != null) {
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

    private void action_paginate_results(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {
        // Sanitizzazione parametri vs XSS
        int draw = SecurityLayer.checkNumeric(request.getParameter("draw"));
        int start = SecurityLayer.checkNumeric(request.getParameter("start"));
        int length = SecurityLayer.checkNumeric(request.getParameter("length"));
        int total = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getNumeroUtenti();

        List<Utente> utenti = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtentiPaginated(start, length);

        request.setAttribute("draw", draw);
        request.setAttribute("total", String.valueOf(total));
        request.setAttribute("utenti", utenti);

    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException {

        TemplateResult results = new TemplateResult(getServletContext());

        List<Utente> utenti = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtentiPaginated(0, 15);
      /*  UtenteProxy me = (UtenteProxy) Methods.getMe(request);
        request.setAttribute("me", me);*/
        request.setAttribute("utenti", utenti);
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
        results.activate("/admin/users/index.ftl.html", request, response);

    }

    private void action_loginredirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void action_create(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        List<Ruolo> ruoli = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getRuoloDAO().getRuoli();

        TemplateResult results = new TemplateResult(getServletContext());
        request.setAttribute("outline_tpl", request.getServletContext().getInitParameter("view.outline_admin"));
       request.setAttribute("ruoli", ruoli);
        results.activate("/admin/users/new.ftl.html", request, response);
    }

    private void action_edit(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, DataException {
        int id_element = SecurityLayer.checkNumeric(request.getParameter("data_id"));
        Utente item = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente(id_element);
        List<Ruolo> ruoli = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getRuoloDAO().getRuoli();


        TemplateResult results = new TemplateResult(getServletContext());
        request.setAttribute("item", item);
        request.setAttribute("ruoli", ruoli);
        request.setAttribute("outline_tpl", "");
        results.activate("/admin/partials/utente_form.ftl.html", request, response);
    }

    private void action_delete(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        // Controllo se l'id è reale
        try {
            Integer key = (Integer) Validator.validate(request.getParameter("data_id"), new ArrayList<>(Arrays.asList(Validator.REQUIRED, Validator.INTEGER)), "ID");
            if (key == null) {
                throw new DataException("Invalid Key");
            }
            Utente u = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente(key);
            if (u == null) {
                throw new DataException("Invalid Key");
            }

            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().deleteUtente(key);
            request.setAttribute("errors", "");
            request.setAttribute("success", "true");
        } catch (DataException ex) {
            // GESTISCO IN MODO DIVERSO L'ECCEZIONE
            request.setAttribute("errors", ex.getMessage());
            request.setAttribute("success", "false");
        }
    }

    private void action_store(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        try {
            Integer key = (Integer) Validator.validate(request.getParameter("key"), new ArrayList<>(Arrays.asList(Validator.INTEGER)), "ID");
            String nome = (String) Validator.validate(request.getParameter("nome"), new ArrayList<>(Arrays.asList(Validator.REQUIRED, Validator.STRING_NOT_EMPTY, Validator.STRING_WITHOUT_SPECIAL)), "nome");
            String email = (String) Validator.validate(request.getParameter("email"), new ArrayList<>(Arrays.asList(Validator.STRING_NOT_EMPTY, Validator.STRING_EMAIL)), "email");
            String cognome = (String) Validator.validate(request.getParameter("cognome"), new ArrayList<>(Arrays.asList(Validator.REQUIRED, Validator.STRING_NOT_EMPTY, Validator.STRING_WITHOUT_SPECIAL)) , "cognome");
            Boolean sendEmail = (Boolean) Validator.validate(request.getParameter("sendEmail"), new ArrayList<>(Arrays.asList(Validator.REQUIRED, Validator.BOOLEAN)), "send email" );
            String password = (String) Validator.validate(request.getParameter("password"), new ArrayList<>(Arrays.asList(Validator.PASSWORD)), "password" );
            LocalDate emailVerifiedAt = (LocalDate) Validator.validate(request.getParameter("verified"), new ArrayList<>(Arrays.asList(Validator.DATE)) , "data di nascita");
            Integer id_ruolo = (Integer) Validator.validate(request.getParameter("ruolo"), new ArrayList<>(Arrays.asList(Validator.REQUIRED, Validator.INTEGER)), "Ruolo");
            Utente target;

            if (key == null) {
                target = new UtenteImpl();
                if(password == null) throw new DataException("Password required on insert");
                if(email == null) throw new DataException("Email required on insert");
                target.setEmail(email);
                target.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            } else {
                target = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente(key);
                if(target == null) throw new DataException("INVALID ID");
            }

            Ruolo user_role = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getRuoloDAO().getRuolo(id_ruolo);
            if(user_role == null) throw new DataException("Invalid role");

            target.setNome(nome);
            target.setCognome(cognome);
            target.setEmailVerifiedAt(emailVerifiedAt);
            target.setSendEmail(sendEmail);
            target.setRuolo(user_role);
            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().storeUtente(target);


            request.setAttribute("errors", "");
            request.setAttribute("success", "true");

        } catch (DataException ex) {
            // GESTISCO IN MODO DIVERSO L'ECCEZIONE
            request.setAttribute("errors", ex.getMessage());
            request.setAttribute("success", "false");

        }
    }

}
