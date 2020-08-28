/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guida.tv.shared;

import com.mycompany.guida.tv.data.DataException;
import com.mycompany.guida.tv.data.dao.GuidaTVDataLayer;
import com.mycompany.guida.tv.data.model.Utente;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;

public class Methods {

    /**
     * Funzione customizzata di debugging utile a ricordare dove vengono
     * inserite le diverse print di debug.
     *
     * @param c
     * @param method
     * @param message
     */
    public static void debugConsole(Class c, String method, String message) {
        System.out.println(ConsoleColors.RED + "[Debugging] " + c.getName() + "\t\t->" + method + ConsoleColors.RESET + "\t" + message);
    }

    /**
     * Restituisce l'utente loggato in sessione. Controllare che la sessione
     * esista prima della chiamata.
     *
     * @param request
     * @return
     * @throws DataException
     */
    public static Utente getMe(HttpServletRequest request) throws DataException {
        return ((GuidaTVDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente((int) request.getSession().getAttribute("userid"));
    }

    /**
     * Restituisce una stringa alfanumerica della dimensione desiderata
     *
     * @param size
     * @return
     */
    public static String getRandomString(int size) {
        String base = "";
        base += "abcdefghijklmnopqrstuvwxyz";
        base += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        base += "0123456789";
        Random random = new Random();
        String to_return = "";
        for (int i = 0; i < size; i++) {
            to_return += base.charAt(random.nextInt(base.length()));
        }
        return to_return;
    }

    public static String generateNewToken(GuidaTVDataLayer dl) throws DataException {
        String token;
        do {
            token = getRandomString(10);
        } while (dl.getUtenteDAO().tokenExists(token));
        return token;
    }

    public static Map<String, String> getQueryMap(String query) throws UnsupportedEncodingException {
        String[] params = query.split("&");
        String canali_list = "";
        String generi_list = "";
        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            String[] p = param.split("=");
            String name = p[0];
            if (p.length > 1) {
                String value = p[1];
                map.put(name, URLDecoder.decode(value, "UTF-8"));
                if (name.equals("canale")) {
                    canali_list += URLDecoder.decode(value, "UTF-8") + ",";
                }
                if (name.equals("generi")) {
                    generi_list += URLDecoder.decode(value, "UTF-8") + ",";
                }
            }
            if (!canali_list.isBlank()) {
                map.put("Canali", canali_list);
            }
            if (!generi_list.isBlank()) {
                map.put("Generi", generi_list);
            }
        }
        return map;
    }

    public static String getQueryList(String name, List<Integer> values) {
        String ret = "";
        if (values != null) {
            for (int i : values) {
                ret += name + "=" + i + "&";
            }

            if (ret.charAt(ret.length() - 1) == '&') {
                ret = ret.substring(0, ret.length() - 1);
            }
        }
        return ret;
    }

    /*
     * Metodi per gestione Fasce Orarie
     * 1 : Mattina
     * 2 : Pomeriggio
     * 3 : Sera
     * 4 : Notte
     */
    public static String getNomeFascia(int fascia_id) {
        switch (fascia_id) {
            case 0:
                return "Tutte le fasce";
            case 1:
                return "Mattina";
            case 2:
                return "Pomeriggio";
            case 3:
                return "Sera";
            case 4:
                return "Notte";
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Restituisce l'orario di inizio della fascia indicata
     *
     * @param fascia_id
     * @return
     */
    public static LocalTime getOrarioInizioFascia(int fascia_id) {
        switch (fascia_id) {
            case 0:
                return LocalTime.parse("00:00:00");
            case 1:
                return LocalTime.parse("06:00:00");
            case 2:
                return LocalTime.parse("12:00:00");
            case 3:
                return LocalTime.parse("18:00:00");
            case 4:
                return LocalTime.parse("00:00:00");
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Restituisce l'rario di fine della fascia indicata
     *
     * @param fascia_id
     * @return
     */
    public static LocalTime getOrarioFineFascia(int fascia_id) {
        switch (fascia_id) {
            case 0:
                return LocalTime.parse("23:59:00");
            case 1:
                return LocalTime.parse("11:59:00");
            case 2:
                return LocalTime.parse("17:59:00");
            case 3:
                return LocalTime.parse("23:59:00");
            case 4:
                return LocalTime.parse("05:59:00");
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void clearRequestAttributes(HttpServletRequest request) {
        while (request.getAttributeNames().hasMoreElements()) {
            request.removeAttribute(request.getAttributeNames().nextElement());
        }
    }

}
