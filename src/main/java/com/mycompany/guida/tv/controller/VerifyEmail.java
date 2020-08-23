/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guida.tv.controller;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import com.mycompany.guida.tv.data.impl.UtenteImpl;
import com.mycompany.guida.tv.data.model.Utente;
import com.mycompany.guida.tv.data.proxy.UtenteProxy;
import com.mycompany.guida.tv.result.FailureResult;
import com.mycompany.guida.tv.result.TemplateManagerException;
import com.mycompany.guida.tv.result.TemplateResult;
import com.mycompany.guida.tv.security.BCrypt;
import com.mycompany.guida.tv.security.SecurityLayer;
import com.mycompany.guida.tv.shared.Methods;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author xbit1
 */
public class VerifyEmail extends BaseController {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        response.setContentType("text/html;charset=UTF-8");
        System.out.println((GuidaTVDataLayer) request.getAttribute("datalayer"));
        try (PrintWriter out = response.getWriter()) {

            if (request.getParameter("token") != null && request.getParameter("code") != null) {
                action_verify(request, response, out);
            }  else if (request.getParameter("resend") != null) {
                action_resend(request, response);
            } else {
                action_default(request, response);
            }
        } catch (DataException |TemplateManagerException |IOException ex) {
             request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (Exception ex) {
            Logger.getLogger(VerifyEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void action_verify(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        try {
            Utente user = null;
            //Controllo che i parametri della request non siano vuoti
            if (request.getParameter("token") != null && request.getParameter("code") != null) {

                String decrypted_email = SecurityLayer.decrypt(request.getParameter("code"), SecurityLayer.getStaticKey());
                user = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtenteByEmail(decrypted_email);

                //Controllo che il codice (la mail criptata) corrisponda ad un utente presente nel database
                if (user == null) {
                    throw new Exception("Something went wrong");
                }

            } else {
                throw new Exception("Something went wrong");
            }
            //Controllo che il token dell'utente sia giusto
            if (BCrypt.checkpw(user.getToken(), request.getParameter("token"))) {

                //Controllo che il token dell'utente non sia scaduto
                if (LocalDate.now().isBefore(user.getExp_date())) {

                    //Se il token va bene memorizzo la data di conferma e faccio il redirect a /login
                    if (user.getEmailVerifiedAt() == null) {  //Se la mail è stata già verificata non faccio nulla

                        user.setEmailVerifiedAt(LocalDate.now());
                        ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().storeUtente(user);
                    }

                    response.sendRedirect("login");
                } else {
                    throw new Exception("Link expired");
                }
            } else {
                throw new Exception("Something went wrong");
            }
        } catch (Exception ex) {
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

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException, IOException {

        HttpSession s = SecurityLayer.checkSession(request);
        if (s != null) {
            // Se l'utente è loggato gli mostro la pagina di conferma
            TemplateResult results = new TemplateResult(getServletContext());
            results.activate("confirm_email.ftl.html", request, response);
        } else {
            // Altrimenti lo redireziono verso la pagina di login
            response.sendRedirect("login");
        }

    }

    private void action_resend(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("request received");
        HttpSession s = SecurityLayer.checkSession(request);
        if (Methods.getMe(request) != null) {
            UtenteProxy me = (UtenteProxy) Methods.getMe(request);
            me.setToken(Methods.generateNewToken(((GuidaTVDataLayer) request.getAttribute("datalayer"))));
            me.setExpirationDate(LocalDate.now().plusDays(1));
            ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().storeUtente(me);
            SecurityLayer.generateVerificationLink(this.getServletContext().getInitParameter("files.directory") + "/links.txt", me);
            response.sendRedirect("verifyemail");
        } else {
            action_default(request, response);
        }
    }
}
