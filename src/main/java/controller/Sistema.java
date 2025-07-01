package controller;
import implementazione_postgres.*;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

//la classe controller si occupa della gestione e della logica delle operazioni svolte dalle classi del model.
/*normalmente ci sarebbe stata la necessità di creare e lavorare con più controller ognuno specializzato nella gestione di
una singola operazione o in un campo specifo. Per attenerci alla traccia si è preferito crearne uno solo.
 */
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
        creaAdmin();
    }

    //metodo per ricevere admin
    public Admin getAdmin(){
        return admin;
    }
    //metodo per ricevere utente
    public Utente getUtente(){
        return utente;
    }

    //se non esiste crea un admin
    public void creaAdmin(){
        if(!utenteDB.esisteAlmenoUnAdmin()){
            Admin primoAdminCreatoPerIlSistema = new Admin("admin", "admin123", 2);
            logger.info("admin creato: \nUsername: " + admin.getNomeUtente() + "\nPassword: "+ admin.getPsw());
        }
    }

    //il sistema aggiunge un utente.
    public void aggiungiUtente(Utente ug){
        utenti.add(ug);
        try {
            utenteDB.aggiungiUtenteDB(ug); // aggiungi anche nel DB
        } catch (SQLException e) {
            logger.info("Errore aggiungendo utente al DB: " + e.getMessage());
        }
    }

    //in quanto admin si può aggiungere un volo alla tabella
    public void aggiungiVolo(Volo v){
        try {
            voloDB.aggiungiVoloDB(v);
            admin.aggiungiVoli(v);
        } catch (SQLException e) {
            logger.info("Errore aggiungendo volo: " + e.getMessage());
        }
    }

    //tutti gli utenti possono visualizzare tutti i voli tramite questo metodo
    public ArrayList<Volo> visualizzaVoli(){
        try {
            return voloDB.getTuttiVoli();
        } catch (SQLException e) {
            logger.info("Errore nel recupero voli: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    //da admin si ha la possibilità di eliminare un volo dalla tabella
    public void eliminaVolo(int codiceVolo) {
        try {
            voloDB.eliminaVolo(codiceVolo);
        } catch (SQLException e) {
            logger.info("Errore eliminando volo: " + e.getMessage());
        }
    }

    //da admin si può modificare un volo all'interno della tabella
    public void modificaVolo(Volo v) {
        try {
            voloDB.modificaVoloDB(v);
        } catch (SQLException e) {
            logger.info("Errore modificando volo: " + e.getMessage());
        }
    }
    //aggiungi il biglietto tra i biglietti acquistati dell'utente e tutti i biglietti
    public void aggiungiBiglietto(Prenotazione biglietto){
        if (utente != null) {
            // 1 = variabile id_passeggero da correggere
            prenotazioneDB.addTicket(biglietto.getNumeroBiglietto(),biglietto.getPasseggero().getId_passeggero(),biglietto.getPostoAssegnato(),biglietto.getVolo(),biglietto.getStato().toString(),biglietto.getAcquirente());
            utente.prenotaVolo(biglietto);
            tuttiIBiglietti.add(biglietto);
        } else {
            logger.info("Errore: nessun utente generico loggato.");
        }
    }

    //metodo finalizzato alla creazione di un numero biglietto univoco e random
    public Long creaNumBiglietto(){
        return biglietto.creaNumeroBiglietto((ArrayList<Prenotazione>) tuttiIBiglietti);
    }
    //metodo necessario alla tabella dell'area personale dell'utente generico che vuole visualizzare tutti i suoi voli prenotati
    public List<Prenotazione> getBiglietti(String username, String nome, int codiceVolo) {
        try {
            return prenotazioneDB.getTickets(username, nome, codiceVolo);
        } catch (SQLException _) {
            return new ArrayList<>();
        }
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
    public int aggiungiPasseggero(Passeggero passeggero) throws SQLException {
        return passeggeroDB.aggiungiPasseggero(passeggero);
    }
    //metodo per eliminare un biglietto disponibile per l'utente generico che prenota per sé o per un eventuale passeggero
    public boolean cancellaBiglietto(long numeroBiglietto){
        return prenotazioneDB.deleteTicket(numeroBiglietto);
    }
    public boolean controlloPasseggeroInVolo(String cdf,int codiceVolo) throws SQLException {
        return passeggeroDB.passeggeroGiaPrenotato(cdf,codiceVolo);
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

    public void setUtenteLoggato(Utente u) {
        if (u instanceof UtenteGenerico ug) {
            this.utente = ug;
        } else if (u instanceof Admin a) {
            this.admin = a;
        }
    }


    public boolean modificaBiglietto(Prenotazione b, String nome,String cognome,String cdf){
        biglietto.modificaBiglietto(b, UtenteGenerico.bigliettiAcquistati);
        return  prenotazioneDB.modifyTicket(b.getNumeroBiglietto(),b.getPostoAssegnato(),nome,cognome,cdf);
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
    public void logout(){
        this.utente = null;
    }

    //questo metodo è stato utilizzato una volta sola volta per riempire il db di voli automaticamente e poi cancellato
    //al fine di simulare il progetto "a regime".
    public int getLastId() throws SQLException{
        return passeggeroDB.getLastId();
    }
    public void generaContenutiCasuali() {
        int codiceVolo;
        int ritardo;
        String statoVolo = "";
        String compagnia;
        String aeroportoOrigine;
        String aeroportoDestinazione;
        String orarioArrivo;
        String gate1;
        int possibilita;
        final String Capodichino = "Capodichino";
        String[] nomiCompagnie = {"Aircampnia","RaynAir","AliItalia","AirRoma","AliGermany",
                "AirRomania","FlyNaples","FlyRomenia","FlyHighIT","FranceFly","SpainFly","AirTool",
                "AmericaFly","NYflyHigh","NigeriaFly","JapanFly","TokyoFly"};
        String[] aeroporti = {Capodichino, "Roma", "Latina","Heathrow Airport","John F. Kennedy International Airport",
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

        for(int i=0;i<40;i++){
            ritardo =0;
            codiceVolo = casuale.nextInt(9000) + 1000;
            possibilita = casuale.nextInt(6);
            logger.info("possibilità ritardo: "+possibilita);

            // Modifica per garantire che almeno uno dei due aeroporti sia "Capodichino"
            if(casuale.nextBoolean()) {
                aeroportoOrigine = Capodichino;
                aeroportoDestinazione = aeroporti[casuale.nextInt(aeroporti.length)];
            } else {
                aeroportoOrigine = aeroporti[casuale.nextInt(aeroporti.length)];
                aeroportoDestinazione = Capodichino;
            }

            // Gestione ritardo e stato
            if(possibilita == 0) {
                ritardo = casuale.nextInt(181)+1;
                statoVolo = stati[0]; // INRITARDO
            } else {
                statoVolo = stati[casuale.nextInt(4) + 2];
            }

            // Gestione gate per voli in arrivo a Capodichino
            if(aeroportoDestinazione.equals(Capodichino)) {
                gate1 = ""; // Voli in arrivo a Capodichino non hanno gate
            } else {
                gate1 = gate[casuale.nextInt(gate.length)];
                // Solo voli con gate possono essere cancellati
                if(statoVolo.equals("CANCELLATO") && gate1.isEmpty()) {
                    statoVolo = stati[casuale.nextInt(4) + 2]; // Assegna un altro stato
                }
            }

            // Verifica finale che voli con ritardo abbiano stato INRITARDO
            if(ritardo > 0) {
                statoVolo = stati[0]; // INRITARDO
            }

            compagnia = nomiCompagnie[casuale.nextInt(nomiCompagnie.length)];
            orarioArrivo = orari[casuale.nextInt(orari.length)];

            Volo v = new Volo(codiceVolo, compagnia, aeroportoOrigine, aeroportoDestinazione, orarioArrivo, ritardo, Volo.statoVolo.valueOf(statoVolo), gate1);
            admin.aggiungiVoli(v);
            try {
                voloDB.aggiungiVoloDB(v);
            } catch (SQLException e) {
                logger.info("Errore nella generazione di contenuti casuali nel DB"+ e.getMessage());
            }
        }
    }

    public Volo getVolo(int codice){
        try {
            return voloDB.getVolo(codice);
        } catch (SQLException e) {
            logger.info("Non sono riuscito a ottenere il volo dal DB tramite codice: "+ e.getMessage());
        }
        return null;
    }
    // Metodo per visualizzare solo voli in partenza da Capodichino
    public ArrayList<Volo> visualizzaVoliInPartenza() {
        try {
            return voloDB.getVoliInPartenza();
        } catch (SQLException e) {
            logger.info("Errore nel recupero voli in partenza: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Metodo per visualizzare solo voli in arrivo a Capodichino
    public ArrayList<Volo> visualizzaVoliInArrivo() {
        try {
            return voloDB.getVoliInArrivo();
        } catch (SQLException e) {
            logger.info("Errore nel recupero voli in arrivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}