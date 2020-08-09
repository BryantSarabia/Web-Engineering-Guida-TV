/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guida.tv.controller;

import com.mycompany.guida.tv.result.TemplateManagerException;
import com.mycompany.guida.tv.result.TemplateResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HP
 */
public class Home extends BaseController {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws com.mycompany.guida.tv.result.TemplateManagerException
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException{
        try {
            response.setContentType("text/html;charset=UTF-8");
            action_default(request, response);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException{
        TemplateResult results = new TemplateResult(getServletContext());
         results.activate("home.ftl.html", request, response);
    }

}
