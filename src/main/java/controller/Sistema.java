package controller;
import implementazione_postgres.*;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class Sistema {
    private static List<Utente> utenti;
    private Prenotazione biglietto;
    private UtenteGenerico utente;
    private Admin admin;
    private List<Prenotazione> tuttiIBiglietti;
    private final Random casuale= new Random();
    private VoloDB voloDB;
    private UtenteDB utenteDB;
    private PrenotazioneDB prenotazioneDB;
    private PasseggeroDB passeggeroDB;
    Logger logger = Logger.getLogger(getClass().getName());

    public Sistema(){
        utenti = new ArrayList<>();
        tuttiIBiglietti = new ArrayList<>();
        biglietto = new Prenotazione();
        voloDB = new VoloDB();
        utenteDB = new UtenteDB();
        prenotazioneDB = new PrenotazioneDB();
        passeggeroDB =  new PasseggeroDB();
        admin = creaAdmin();
    }

    public Admin getAdmin(){
        return admin;
    }

    public Utente getUtente(){
        return utente;
    }

    public Admin creaAdmin(){
        if(!utenteDB.esisteAlmenoUnAdmin()){
            Admin primoAdminCreatoPerIlSistema = new Admin("admin", "admin123", 2);
            try {
                utenteDB.aggiungiUtenteDB(primoAdminCreatoPerIlSistema);
                logger.info("admin creato e salvato nel DB: \nUsername: " + primoAdminCreatoPerIlSistema.getNomeUtente() + "\nPassword: "+ primoAdminCreatoPerIlSistema.getPsw());
            } catch (SQLException e) {
                logger.severe("Errore salvando admin nel DB: " + e.getMessage());
                return null;
            }
            return primoAdminCreatoPerIlSistema;
        }
        return null;
    }

    public void aggiungiUtente(Utente ug){
        utenti.add(ug);
        try {
            utenteDB.aggiungiUtenteDB(ug);
        } catch (SQLException e) {
            logger.info("Errore aggiungendo utente al DB: " + e.getMessage());
        }
    }

    public void aggiungiVolo(Volo v){
        try {
            voloDB.aggiungiVoloDB(v);
            admin.aggiungiVoli(v);
        } catch (SQLException e) {
            logger.info("Errore aggiungendo volo: " + e.getMessage());
        }
    }

    public ArrayList<Volo> visualizzaVoli(String data){
        try {
            return voloDB.getTuttiVoli(data);
        } catch (SQLException e) {
            logger.info("Errore nel recupero voli: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void eliminaVolo(int codiceVolo) {
        try {
            voloDB.eliminaVolo(codiceVolo);
        } catch (SQLException e) {
            logger.info("Errore eliminando volo: " + e.getMessage());
        }
    }

    public void modificaVolo(Volo v) {
        try {
            voloDB.modificaVoloDB(v);
        } catch (SQLException e) {
            logger.info("Errore modificando volo: " + e.getMessage());
        }
    }

    public void aggiungiBiglietto(Prenotazione biglietto){
        if (utente != null) {
            prenotazioneDB.addTicket(biglietto.getNumeroBiglietto(),
                    biglietto.getPasseggero().getId_passeggero(),
                    biglietto.getPostoAssegnato(),
                    biglietto.getVolo(),
                    biglietto.getStato().toString(),
                    biglietto.getAcquirente());
            utente.prenotaVolo(biglietto);
            tuttiIBiglietti.add(biglietto);
        } else {
            logger.info("Errore: nessun utente generico loggato.");
        }
    }

    public Long creaNumBiglietto(){
        return biglietto.creaNumeroBiglietto((ArrayList<Prenotazione>) tuttiIBiglietti);
    }

    public List<Prenotazione> getBiglietti(String username, String nome, int codiceVolo) {
        try {
            return prenotazioneDB.getTickets(username, nome, codiceVolo);
        } catch (SQLException _) {
            return new ArrayList<>();
        }
    }

    public int verificaUtenteP(String username, String psw) {
        try {
            Utente u = utenteDB.verificaCredenziali(username, psw);
            if (u != null) {
                if (u instanceof UtenteGenerico) {
                    return 1;
                } else if (u instanceof Admin) {
                    return 2;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int aggiungiPasseggero(Passeggero passeggero) throws SQLException {
        return passeggeroDB.aggiungiPasseggero(passeggero);
    }

    public boolean cancellaBiglietto(long numeroBiglietto){
        return prenotazioneDB.deleteTicket(numeroBiglietto);
    }

    public boolean controlloPasseggeroInVolo(String cdf,int codiceVolo) throws SQLException {
        return passeggeroDB.passeggeroGiaPrenotato(cdf,codiceVolo);
    }

    public boolean verificaUtenteUnivoco(String username){
        for(Utente u : utenti){
            if(u.getNomeUtente().equals(username)){
                return false;
            }
        }
        return true;
    }

    public void setUtenteLoggato(Utente u) {
        if (u instanceof UtenteGenerico ug) {
            this.utente = ug;
        } else if (u instanceof Admin a) {
            this.admin = a;
        }
    }

    public boolean modificaBiglietto(Prenotazione b, String nome,String cognome,String cdf){
        biglietto.modificaBiglietto(b, UtenteGenerico.bigliettiAcquistati);
        return prenotazioneDB.modifyTicket(b.getNumeroBiglietto(),b.getPostoAssegnato(),nome,cognome,cdf);
    }

    public boolean login(String username, String psw) throws SQLException {
        Utente u = utenteDB.verificaCredenziali(username, psw);
        if (u != null) {
            setUtenteLoggato(u);
            return true;
        }
        return false;
    }

    public void logout(){
        this.utente = null;
    }

    public int getLastId() throws SQLException{
        return passeggeroDB.getLastId();
    }

    public Volo getVolo(int codice){
        try {
            return voloDB.getVolo(codice);
        } catch (SQLException e) {
            logger.info("Non sono riuscito a ottenere il volo dal DB tramite codice: "+ e.getMessage());
        }
        return null;
    }

    public ArrayList<Volo> visualizzaVoliInPartenza(String data) {
        try {
            return voloDB.getVoliInPartenza(data);
        } catch (SQLException e) {
            logger.info("Errore nel recupero voli in partenza: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public ArrayList<Volo> visualizzaVoliInArrivo(String data) {
        try {
            return voloDB.getVoliInArrivo( data);
        } catch (SQLException e) {
            logger.info("Errore nel recupero voli in arrivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}