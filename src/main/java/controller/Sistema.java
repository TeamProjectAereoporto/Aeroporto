package controller;
import implementazionePostgres.*;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

//la classe controller si occupa della gestione e della logica delle operazioni svolte dalle classi del model.
/*normalmente ci sarebbe stata la necessità di creare e lavorare con più controller ognuno specializzato nella gestione di
una singola operazione o in un campo specifo. Per attenerci alla traccia si è preferito crearne uno solo.
 */
public class Sistema {

    public static ArrayList<Utente> utenti;
    public Prenotazione biglietto;
    public UtenteGenerico utente;
    public Admin admin;
    public ArrayList<Prenotazione> tuttiIBiglietti;
    private final Random casuale= new Random();
    private VoloDB voloDB;
    private UtenteDB utenteDB;
    private PrenotazioneDB prenotazioneDB;
    private PasseggeroDB passeggeroDB;
    public Sistema(){
        utenti = new ArrayList<>();
        tuttiIBiglietti = new ArrayList<>();
        biglietto = new Prenotazione();
        voloDB = new VoloDB();
        utenteDB = new UtenteDB();
        prenotazioneDB = new PrenotazioneDB();
        passeggeroDB =  new PasseggeroDB();
    }

    //il sistema aggiunge un utente.
    public void aggiungiUtente(Utente ug){
            utenti.add(ug);
        try {
            utenteDB.aggiungiUtenteDB(ug); // aggiungi anche nel DB
        } catch (SQLException e) {
            System.err.println("Errore aggiungendo utente al DB: " + e.getMessage());
        }
    }

    //in quanto admin si può aggiungere un volo alla tabella
    public void aggiungiVolo(Volo v){
        try {
            voloDB.aggiungiVoloDB(v);
            admin.aggiungiVoli(v);
        } catch (SQLException e) {
            System.err.println("Errore aggiungendo volo: " + e.getMessage());
        }
    }

    //tutti gli utenti possono visualizzare tutti i voli tramite questo metodo
    public ArrayList<Volo> visualizzaVoli(){
        try {
            return voloDB.getTuttiVoli();
        } catch (SQLException e) {
            System.err.println("Errore nel recupero voli: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    //da admin si ha la possibilità di eliminare un volo dalla tabella
    public void eliminaVolo(int codiceVolo) {
        try {
            voloDB.eliminaVolo(codiceVolo);
        } catch (SQLException e) {
            System.err.println("Errore eliminando volo: " + e.getMessage());
        }
    }

    //da admin si può modificare un volo all'interno della tabella
    public void modificaVolo(Volo v) {
        try {
            voloDB.modificaVoloDB(v);
        } catch (SQLException e) {
            System.err.println("Errore modificando volo: " + e.getMessage());
        }
    }
    //aggiungi il biglietto tra i biglietti acquistati dell'utente e tutti i biglietti
    public void aggiungiBiglietto(Prenotazione biglietto){
        if (utente != null) {
            prenotazioneDB.addTicket(biglietto.getNumeroBiglietto(),biglietto.getPasseggero(),biglietto.getPostoAssegnato(),biglietto.getVolo(),biglietto.getStato().toString(),biglietto.getAcquirente());
            utente.prenotaVolo(biglietto);
            tuttiIBiglietti.add(biglietto);
        } else {
            System.err.println("Errore: nessun utente generico loggato.");
        }
    }

    //metodo per eliminare un biglietto disponibile per l'utente generico che prenota per sé o per un eventuale passeggero
    public boolean cancellaBiglietto(long numeroBiglietto){
        return biglietto.cancellaBiglietto(numeroBiglietto, UtenteGenerico.bigliettiAcquistati)
                && prenotazioneDB.deleteTicket(numeroBiglietto);
    }
    //metodo finalizzato alla creazione di un numero biglietto univoco e random
    public Long creaNumBiglietto(){
        return biglietto.creaNumeroBiglietto(tuttiIBiglietti);
    }
    //metodo necessario alla tabella dell'area personale dell'utente generico che vuole visualizzare tutti i suoi voli prenotati
    public ArrayList getBiglietti(String nome, int codiceVolo){
        return utente.cercaBiglietto(nome,codiceVolo);
    }

    //Il metodo serve a verificare se l'utente è un admin o un utente generico
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
        return 0; // login fallito
    }

    //aggiunge un passeggero al DB
    public void aggiungiPasseggero(Passeggero passeggero) throws SQLException {
        passeggeroDB.aggiungiPasseggero(passeggero);
    }

    //si assicura che l'username dell'utente sia univoco e non duplicato
    public boolean verificaUtenteUnivoco(String username){
        for(Utente u : utenti){
            if(u.getNomeUtente().equals(username) ){
               return false;
            }
        }
        return true;
    }

    public void setUtenteLoggato(Utente u){
        if (u instanceof UtenteGenerico) {
            this.utente = (UtenteGenerico) u;
        } else if (u instanceof Admin) {
            this.admin = (Admin) u;
        }
    }

    public void modificaBiglietto(Prenotazione b){
        biglietto.modificaBiglietto(b, UtenteGenerico.bigliettiAcquistati);
    }
    public boolean login(String username, String psw) throws SQLException {
        Utente u = utenteDB.verificaCredenziali(username, psw);
        if (u != null) {
            setUtenteLoggato(u); // Imposta correttamente l'utente loggato (utente o admin)
            return true;
        }
        return false; // login fallito
    }

    //effettua il logout dal sistema.
    public void logout(Utente utente){
        this.utente = null;
    }

    //questo metodo è stato utilizzato una volta sola volta per riempire il db di voli automaticamente e poi cancellato
    //al fine di simulare il progetto "a regime".
    public void generaContenutiCasuali(){

        String[] nomiCompagnie = {"Aircampnia","RaynAir","AliItalia","AirRoma","AliGermany",
                "AirRomania","FlyNaples","FlyRomenia","FlyHighIT","FranceFly","SpainFly","AirTool",
                "AmericaFly","NYflyHigh","NigeriaFly","JapanFly","TokyoFly"};
        String[] Aeroporti= {"Capodichino", "Roma", "Latina","Heathrow Airport","John F. Kennedy International Airport",
        "Charles de Gaulle Airport","Frankfurt Airport ","Tokyo Haneda Airport","Los Angeles International Airport ",
                "Dubai International Airport","Singapore Changi Airport","Incheon International Airport","Beijing Capital International Airport"};
        String[] orari = {
                "06:15", "07:30", "08:45", "09:00", "10:20",
                "11:55", "12:10", "13:25", "14:40", "15:50",
                "16:05", "17:30", "18:15", "19:45", "20:10",
                "21:00", "22:25", "23:50", "00:30", "01:45"
        };
        String[] gate = {"A1","B1","C1","D1","E1","F1","G1","H1","I1","J1","K1","L1","M1","N1","O1","P1","Q1","R1","S1","T1","U1","V1","W1","X1","Y1","Z1"};
        String[] stati = {
                "INRITARDO",
                "INORARIO",
                "DECOLLATO",
                "PROGRAMMATO",
                "ATTERRATO",
                "CANCELLATO"};

        int codiceVolo;
        int ritardo;
        String statoVolo = "";
        String compagnia;
        String aeroportoOrigine;
        String aeroportoDestinazione;
        String orarioArrivo;
        String gate1;
        int possibilita;
        for(int i=0;i<40;i++){
             ritardo =0;
             codiceVolo = casuale.nextInt(9000) + 1000;
             possibilita = casuale.nextInt(6);
             System.out.println(possibilita);
             if(possibilita == 0) {
                     ritardo =  casuale.nextInt(181)+1;
                     statoVolo = stati[0];
             }else{
                 statoVolo = stati[casuale.nextInt(4) + 2];
             }
             compagnia = nomiCompagnie[casuale.nextInt(17)];
             aeroportoOrigine = Aeroporti[casuale.nextInt(13)];
             aeroportoDestinazione = Aeroporti[casuale.nextInt(13)];
             orarioArrivo = orari[casuale.nextInt(20)];
             gate1 = gate[casuale.nextInt(26)];
            Volo v=new Volo(codiceVolo,compagnia,aeroportoOrigine,aeroportoDestinazione,orarioArrivo,ritardo,Volo.statoVolo.valueOf(statoVolo), gate1);
            admin.aggiungiVoli(v);
            try{
                voloDB.aggiungiVoloDB(v);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

}