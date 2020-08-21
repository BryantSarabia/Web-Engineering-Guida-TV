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
import com.mycompany.guida.tv.data.model.Interessa;
import com.mycompany.guida.tv.data.model.Programma;
import com.mycompany.guida.tv.data.model.Programmazione;
import com.mycompany.guida.tv.security.SecurityLayer;
import com.mycompany.guida.tv.shared.Methods;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public void setIdRuolo(int id_ruolo) {
        this.id_ruolo = id_ruolo;
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
            String mail_text = "";
            
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
            
            //Stampo la programmazione di ogni canale
            for(Programmazione programmazione : prog){
                mail_text += programmazione.getCanale().getNome() + ": dalle " + programmazione.getStartTime() + " alle " + programmazione.getEndTime() + " - " + programmazione.getProgramma().getTitolo() + "\n";
            }
            
            //Prendo tutte le ricerche per cui l'utente vuole essere avvisato
            List<Ricerca> ricerche = this.getRicerche();             
            if(ricerche != null){
                mail_text += "\n Notifiche ricerche\n";
                if(ricerche.isEmpty()) mail_text = "Non hai salvato nessuna ricerca\n";
                for(Ricerca ricerca : ricerche){
                    Map<String, String> params = Methods.getQueryMap(ricerca.getQuery());
                    mail_text += "Ecco i parametri di ricerca che hai specificato:\n";
                    for(String param : params.keySet()){
                        mail_text += param + ": " + params.get(param) + "\n";
                    }
                    mail_text += "Clicca qui per vedere aggiornamenti sulla tua ricerca:\nlocalhost:8080/guida-tv/cerca?" + ricerca.getQuery();
                }
            }
            
            //1) Prendere tutti programmi del giorno per ogni canale negli interessi
            //2) Prendere tutti i programmi risultanti dalle ricerche
            //3) Unire le due liste e stampare tutto su file
            
        }
    }
}
