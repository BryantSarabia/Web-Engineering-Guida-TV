/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guida.tv.controller.auth;

import com.mycompany.guida.tv.controller.BaseController;
import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import com.mycompany.guida.tv.data.impl.UtenteImpl;
import com.mycompany.guida.tv.data.model.Utente;
import com.mycompany.guida.tv.result.FailureResult;
import com.mycompany.guida.tv.result.TemplateManagerException;
import com.mycompany.guida.tv.result.TemplateResult;
import com.mycompany.guida.tv.security.BCrypt;
import com.mycompany.guida.tv.security.SecurityLayer;
import com.mycompany.guida.tv.shared.Methods;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HP
 */
public class Register extends BaseController {

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
            throws ServletException{
        response.setContentType("text/html;charset=UTF-8");
        System.out.println( (GuidaTVDataLayer) request.getAttribute("datalayer"));
        
        try {
            if(request.getParameter("submit") != null){

                action_register(request, response);
                
            }
            else {
                 System.out.println( (GuidaTVDataLayer) request.getAttribute("datalayer") );
                action_default(request, response);
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
        if(s != null) {
            response.sendRedirect("profile");
        }
        else {
            TemplateResult results = new TemplateResult(getServletContext());
            results.activate("register.ftl.html", request, response);
        }
        
    }
       
       private void action_register(HttpServletRequest request, HttpServletResponse response) throws DataException, TemplateManagerException, IOException, Exception {
        
        String nome = ( request.getParameter("nome") != null) ? request.getParameter("nome") : "";
        String cognome = ( request.getParameter("cognome") != null) ? request.getParameter("cognome") : "";
        String email = ( request.getParameter("email") != null) ? request.getParameter("email") : "";
        String password = ( request.getParameter("password") != null) ? request.getParameter("password") : "";
        String confirm = ( request.getParameter("password_confirm") != null) ? request.getParameter("password_confirm") : "";
        boolean valid = true;
        String error_msg = "";
        
        if (!nome.isEmpty() && !cognome.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirm.isEmpty()) {
   
            // Sanitizzo tutti i campi e controllo se i campi sono validi
            if(!SecurityLayer.CheckEmail(email)) {
                error_msg += "Email non valida, \n";
                valid = false;
            }
            if(!password.equals(confirm)) {
                error_msg += "Le password non combaciano, \n";
                valid = false;
            }

            
            // Controllo se l'email e l'username sono presenti nel DB
            Utente exists_email = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente(email);
            if(exists_email != null ) {
                error_msg += "Email non disponibile, \n";
                valid = false;
            }
                     
            if(valid) {
               
                Utente newUser = new UtenteImpl();
                newUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
                newUser.setEmail(email);
                newUser.setNome(nome);
                newUser.setCognome(cognome);
                System.out.println(newUser.getPassword());
                
                newUser.setRuolo(((GuidaTVDataLayer) request.getAttribute("datalayer")).getRuoloDAO().getRuolo(1));
                newUser.setToken(Methods.generateNewToken(((GuidaTVDataLayer) request.getAttribute("datalayer"))));     
                newUser.setExpirationDate(LocalDate.now().plusDays(1));
                ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().storeUtente(newUser);
                
                if(newUser.getKey() == 0) {
                    error_msg += "Errore nell'inserimento dell'utente, \n";
                    valid = false;
                }
                else {
                    //Non invio mail ma stampo il link in un file .txt
                    SecurityLayer.generateVerificationLink(this.getServletContext().getInitParameter("files.directory") + "/VerificationLinks/" + newUser.getNome() + newUser.getCognome() + "Verify.txt", newUser);
                   
                    //Redirect
                    if (request.getParameter("referrer") != null) {
                        response.sendRedirect(request.getParameter("referrer"));
                    } else {
                        response.sendRedirect("login");
                    }
                }
                    response.sendRedirect("login");
            }
            
        }
        else {
            error_msg = "Compila correttamente tutti i campi.";
            valid = false;
        }
        
        if(!valid) {
            request.setAttribute("error", error_msg);
            action_default(request, response);                
        }
    }

}
