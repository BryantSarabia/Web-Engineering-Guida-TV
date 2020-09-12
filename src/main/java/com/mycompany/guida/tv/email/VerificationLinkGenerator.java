/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guida.tv.email;

import com.mycompany.guida.tv.data.model.Utente;
import com.mycompany.guida.tv.security.BCrypt;
import com.mycompany.guida.tv.security.SecurityLayer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xbit1
 */
public class VerificationLinkGenerator {

    public static void generateVerificationLink(String file_path, Utente utente) {
        try {
            File file = new File(file_path);
            if (file.createNewFile()) {
                System.out.println("Verification File created");
            } else {
                System.out.println("Failed creating new file or file already exists");
            }
            try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file_path))) {
                String link = "localhost:8080/guida-tv/verifyemail";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
                String trademark = "----------------------------------------------------------------------- GuidaTV -----------------------------------------------------------------------\n\n";
                String encrypted_token = URLEncoder.encode(BCrypt.hashpw(utente.getToken(), BCrypt.gensalt()), "UTF-8");
                String encrypted_email = URLEncoder.encode(SecurityLayer.encrypt(utente.getEmail(), SecurityLayer.getStaticKey()), "UTF-8");
                String text = "";
                link += "?token=" + encrypted_token + "&code=" + encrypted_email + "\n\n";
                System.out.println("Writing link");
                text += LocalDateTime.now().format(formatter) + "\n";
                text += "Email per " + utente.getNome() + " " + utente.getCognome() + " (" + utente.getEmail() + ") dallo staff di GuidaTV\n\n";
                text += "Ciao " + utente.getNome() + ",\n";
                text += "clicca il link qui sotto per confermare la tua email:\n";
                writer.write(trademark + text + link + trademark);
                System.out.println("Link successfully written");
            } catch (IOException ex) {
                Logger.getLogger(SecurityLayer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(SecurityLayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(SecurityLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
