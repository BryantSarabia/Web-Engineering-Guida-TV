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
import com.mycompany.guida.tv.security.BCrypt;
import com.mycompany.guida.tv.security.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            throws ServletException{
        response.setContentType("text/html;charset=UTF-8");
        System.out.println( (GuidaTVDataLayer) request.getAttribute("datalayer"));
        try ( PrintWriter out = response.getWriter()) {
            action_verify(request, response, out);
        } catch (IOException ex){
            Logger.getLogger(VerifyEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void action_verify(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        try{
            Utente user = null;
            //Controllo che i parametri della request non siano vuoti
            if(request.getParameter("token") != null && request.getParameter("code") != null){
                
                String decrypted_email = SecurityLayer.decrypt(request.getParameter("code"), SecurityLayer.getStaticKey());
                user = ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtenteByEmail(decrypted_email);
                
                //Controllo che il codice (la mail criptata) corrisponda ad un utente presente nel database
                if(user == null){
                   throw new Exception("Something went wrong");
               }
               
            } else { 
                throw new Exception("Something went wrong");
            }
            //Controllo che il token dell'utente sia giusto
            if(BCrypt.checkpw(user.getToken(), request.getParameter("token"))){
                
                //Controllo che il token dell'utente non sia scaduto
                if(LocalDate.now().isBefore(user.getExp_date())){
                    
                    //Se il token va bene memorizzo la data di conferma e faccio il redirect a /login
                    if(user.getEmailVerifiedAt() == null){  //Se la mail è stata già verificata non faccio nulla
                    
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
        } catch (Exception ex){
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

}
