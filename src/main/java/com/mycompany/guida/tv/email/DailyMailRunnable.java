/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guida.tv.email;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.dao.CanaleDAO;
import com.mycompany.guida.tv.data.dao.GenereDAO;
import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import com.mycompany.guida.tv.data.dao.ProgrammazioneDAO;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.model.Genere;
import com.mycompany.guida.tv.data.model.Interessa;
import com.mycompany.guida.tv.data.model.Programmazione;
import com.mycompany.guida.tv.data.model.Ricerca;
import com.mycompany.guida.tv.data.model.Utente;
import com.mycompany.guida.tv.data.proxy.UtenteProxy;
import com.mycompany.guida.tv.security.SecurityLayer;
import com.mycompany.guida.tv.shared.Methods;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author xbit1
 */
public class DailyMailRunnable implements Runnable{
    @Resource(name = "jdbc/guidatv")
    private DataSource ds;
    
    @Override
    public void run() {
            try {
                InitialContext ctx = new InitialContext();
                DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/guidatv");
                
                try (GuidaTVDataLayer datalayer = new GuidaTVDataLayer(ds)) {
                    datalayer.init();
                    
                    List<Utente> users = ((GuidaTVDataLayer) datalayer).getUtenteDAO().getUtentiSendEmail();
                    
                    for(Utente user : users){
                        sendDailyMail(user, datalayer);
                        System.out.println("\033[0;31m" + "Sending daily mail to: " + user.getNome());
                    }
                    
                } catch (SQLException ex) {
                    Logger.getLogger(DailyMailRunnable.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(DailyMailRunnable.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (NamingException ex) {
                Logger.getLogger(DailyMailRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void sendDailyMail(Utente user, GuidaTVDataLayer dataLayer) throws Exception{
        if(user.getSendEmail()){
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
            String trademark = "----------------------------------------------------------------------- GuidaTV -----------------------------------------------------------------------\n\n";
            String mail_text = trademark + LocalDateTime.now().format(formatter2) + "\n";
            mail_text += "Newsletter per " + user.getNome() + " " + user.getCognome() +" (" + user.getEmail() + ") dallo staff di GuidaTV\n\n";
            mail_text += "Ciao " + user.getNome() + ",\n";
            mail_text += "ecco la tua programmazione di oggi " + LocalDate.now().format(formatter1) + ":\n\n";
            
            List<Interessa> interessi = user.getInteressi();   //Prendo tutti i canali per cui l'utente ha espresso interesse
            
            List<Programmazione> prog = new ArrayList<Programmazione>();
            List<Canale> canali = new ArrayList<Canale>();
            
            Map<String, String> map = new HashMap<>();
            LocalDateTime inizio;
            LocalDateTime fine;
            
            
            //Per ogni interesse (o canale) prendo la sua programmazione del giorno nella fascia oraria specificata dall'utente
            for (Interessa interesse : interessi) {            
                try {
                     inizio = LocalDateTime.of(LocalDate.now(), interesse.getStartTime());
                     fine = LocalDateTime.of(LocalDate.now(), interesse.getEndTime());
                     prog.addAll(((ProgrammazioneDAO) dataLayer.getDAO(Programmazione.class)).getProgrammazione(interesse.getCanale().getKey(), inizio, fine));
                } catch (DataException ex) {
                    Logger.getLogger(UtenteProxy.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            //Stampo la programmazione di ogni canale in mail_text
            if(!prog.isEmpty()){
            for(Programmazione programmazione : prog){
                mail_text += programmazione.getCanale().getNome() + ": dalle " + programmazione.getTime() + " alle " + programmazione.getEndTime() + " - " + programmazione.getProgramma().getTitolo() + "\n";
            }
            } else mail_text += "Per oggi non c'è nulla in programma\n";
            //Prendo tutte le ricerche per cui l'utente vuole essere avvisato
            List<Ricerca> ricerche = user.getRicerche();             
            if(ricerche != null){
                mail_text += "\nAggiornamenti sulle tue ricerche:\n\n";
                if(ricerche.isEmpty()) mail_text += "Non hai salvato nessuna ricerca\n\n";
                for(Ricerca ricerca : ricerche){
                    Map<String, String> params = Methods.getQueryMap(ricerca.getQuery());
                    mail_text += "Ecco i parametri di ricerca che hai specificato:\n";
                    for(String param : params.keySet()){
                        
                        //Se il parametro sono gli id dei canali, prendo i canali e ne stampo i nomi
                        if(param.equals("Canali")){
                        String[] chiavi = params.get(param).split(",");
                        List<Canale> canaliToPrint = new ArrayList<Canale>();
                        mail_text += "Canali: ";
                            
                            for(String chiave : chiavi){
                                canaliToPrint.add(((CanaleDAO) dataLayer.getDAO(Canale.class)).getCanale(SecurityLayer.checkNumeric(chiave)));
                            }
                        
                            for(Canale canale : canaliToPrint){
                                mail_text += canale.getNome() + ", "; 
                            }
                            
                        //Rimuovo l'ultima virgola
                        mail_text = mail_text.substring(0, mail_text.length() - 2);
                        mail_text += "\n";
                        }
                        
                        //Se il parametro sono gli id dei generi, prendo i generi e ne stampo i nomi
                        if(param.equals("Generi")){
                        String[] chiavi = params.get(param).split(",");
                        List<Genere> generiToPrint = new ArrayList<Genere>();
                        mail_text += "Generi: ";
                        
                            for(String chiave : chiavi){
                                generiToPrint.add(((GenereDAO) dataLayer.getDAO(Genere.class)).getGenere(SecurityLayer.checkNumeric(chiave)));
                            }
                        
                            for(Genere genere : generiToPrint){
                                mail_text += genere.getNome() + ", "; 
                            }
                            
                        //Rimuovo l'ultima virgola
                        mail_text = mail_text.substring(0, mail_text.length() - 2);
                        mail_text += "\n";
                        }
                        
                        if(param.equals("min_ora")){
                            mail_text += "Orario minimo: " + params.get(param) + "\n"; 
                        }
                        
                        if(param.equals("max_ora")){
                            mail_text += "Orario massimo: " + params.get(param) + "\n"; 
                        }
                        
                        if(param.equals("date_min")){
                            mail_text += "Dal: " + params.get(param) + "\n"; 
                        }
                        
                        if(param.equals("date_max")){
                            mail_text += "Al: " + params.get(param) + "\n";
                        }
                        
                        if(param.equals("titolo")){
                            mail_text += "Titolo: \"" + params.get(param) + "\"\n";
                        }
                       
                    }
                    mail_text += "\nClicca qui per vedere aggiornamenti sulla tua ricerca:\nlocalhost:8080/guida-tv/cerca?" + ricerca.getQuery() + "\n\n";
                }
            }
            mail_text += trademark;
            String path = "E:/Desktop/Desktop/Progetti/Web-Engineering-Guida-TV/files/DailyMails/" + user.getNome()+user.getCognome()+".txt";
            File file = new File(path);
            if(file.createNewFile()) System.out.println("Daily mail created"); else System.out.println("Failed creating new file or file already exists");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
                writer.write(mail_text);
                System.out.println("File successfully written");
            }
            
        } else System.out.println("Send-mail set to false: " + user.getSendEmail());
    }
}