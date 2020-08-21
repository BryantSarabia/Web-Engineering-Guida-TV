package com.mycompany.guida.tv.data.proxy;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.DataItemProxy;
import com.mycompany.guida.tv.data.DataLayer;
import com.mycompany.guida.tv.data.dao.*;
import com.mycompany.guida.tv.data.impl.UtenteImpl;
import com.mycompany.guida.tv.data.model.Canale;
import com.mycompany.guida.tv.data.model.Ricerca;
import com.mycompany.guida.tv.data.model.Ruolo;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mycompany.guida.tv.data.dao.InteressaDAO;
import com.mycompany.guida.tv.data.dao.ProgrammaDAO;
import com.mycompany.guida.tv.data.dao.ProgrammazioneDAO;
import com.mycompany.guida.tv.data.model.Genere;
import com.mycompany.guida.tv.data.model.Interessa;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.model.Programmazione;
import com.mycompany.guida.tv.security.SecurityLayer;
import com.mycompany.guida.tv.shared.Methods;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UtenteProxy extends UtenteImpl implements DataItemProxy {

    private boolean modified;
    private int id_ruolo;
    protected final DataLayer dataLayer;

    public UtenteProxy(DataLayer dataLayer) {
        super();
        this.modified = false;
        this.dataLayer = dataLayer;
    }

    @Override
    public void setKey(Integer key) {
        this.modified = true;
        super.setKey(key);
    }

    @Override
    public void setToken(String token) {
        this.modified = true;
        super.setToken(token);
    }

    @Override
    public void setVersion(long version) {
        this.modified = true;
        super.setVersion(version);
    }

    @Override
    public void setNome(String nome) {
        this.modified = true;
        super.setNome(nome);
    }

    @Override
    public void setCognome(String cognome) {
        this.modified = true;
        super.setCognome(cognome);
    }

    @Override
    public void setEmail(String email) {
        this.modified = true;
        super.setEmail(email);
    }

    @Override
    public void setSendEmail(Boolean sendemail) {
        this.modified = true;
        super.setSendEmail(sendemail);
    }

    @Override
    public void setPassword(String password) {
        this.modified = true;
        super.setPassword(password);
    }

    @Override
    public void setRuolo(Ruolo ruolo) {
        this.modified = true;
        super.setRuolo(ruolo);
    }

    @Override
    public void setRicerche(List<Ricerca> ricerche) {
        this.modified = true;
        super.setRicerche(ricerche);
    }

    @Override
    public void setInteressi(List<Interessa> interessi) {
        this.modified = true;
        super.setInteressi(interessi);
    }

    @Override
    public void setEmailVerifiedAt(LocalDate emailVerifiedAt) {
        super.setEmailVerifiedAt(emailVerifiedAt);
    }

    @Override
    public void setExpirationDate(LocalDate exp_date) {
        super.setExpirationDate(exp_date);
    }

    @Override
    public boolean isModified() {
        return this.modified;
    }

    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
    }
    
    public void setIdRuolo(int key){
        this.id_ruolo = key;
    }
    
    @Override
    public List<Ricerca> getRicerche() {
        if (super.getRicerche() == null) {
            try {
                super.setRicerche(((RicercaDAO) dataLayer.getDAO(Ricerca.class)).getRicercheUtente(this));
            } catch (DataException ex) {
                Logger.getLogger(UtenteProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getRicerche();
    }

    @Override
    public List<Interessa> getInteressi() {
        if (super.getInteressi() == null) {
            try {
                super.setInteressi(((InteressaDAO) dataLayer.getDAO(Interessa.class)).getInteressiUtente(this));
            } catch (DataException ex) {
                Logger.getLogger(UtenteProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getInteressi();
    }

    @Override
    public void cleanInteressi() {
        this.modified = true;
        if (getInteressi() != null) {
            for (Interessa i : getInteressi()) {
                try {
                    ((InteressaDAO) dataLayer.getDAO(Interessa.class)).removeInteresse(i.getKey());
                } catch (DataException ex) {
                    Logger.getLogger(UtenteProxy.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        super.cleanInteressi();
    }
    public int getIdRuolo() {
        return id_ruolo;
    }

    @Override
    public Ruolo getRuolo() {
        if (super.getRuolo() == null && id_ruolo > 0) {
            try {
                super.setRuolo(((RuoloDAO) dataLayer.getDAO(Ruolo.class)).getRuolo(id_ruolo));
            } catch (DataException ex) {
                Logger.getLogger(ProgrammazioneProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return super.getRuolo();
    }
    
    public void sendDailyMail() throws Exception{
        if(this.getSendEmail()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String mail_text = "Ciao " + this.getNome() + ",\n";
            mail_text += "ecco la tua programmazione di oggi " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n\n";
            
            List<Interessa> interessi = this.getInteressi();   //Prendo tutti i canali per cui l'utente ha espresso interesse
            
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
            for(Programmazione programmazione : prog){
                mail_text += programmazione.getCanale().getNome() + ": dalle " + programmazione.getTime() + " alle " + programmazione.getEndTime() + " - " + programmazione.getProgramma().getTitolo() + "\n";
            }
            
            //Prendo tutte le ricerche per cui l'utente vuole essere avvisato
            List<Ricerca> ricerche = this.getRicerche();             
            if(ricerche != null){
                mail_text += "\nAggiornamenti sulle tue ricerche\n\n";
                if(ricerche.isEmpty()) mail_text = "Non hai salvato nessuna ricerca\n";
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
            
            String path = "E:/Desktop/Desktop/Progetti/Web-Engineering-Guida-TV/files/" + this.getNome()+this.getCognome()+".txt";
            File file = new File(path);
            if(file.createNewFile()) System.out.println("Daily mail created"); else System.out.println("Failed creating new file or file already exists");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
                writer.write(mail_text);
                System.out.println("File successfully written");
            }
            
        } else System.out.println("Send-mail set to false: " + this.getSendEmail());
    }
    }
