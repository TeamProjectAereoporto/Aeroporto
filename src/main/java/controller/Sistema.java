package controller;
import model.*;
import java.util.ArrayList;
import java.util.Random;


public class Sistema {
    public ArrayList<Utente> utenti;
    public Prenotazione biglietto;
    public UtenteGenerico utente;
    public Admin admin;
    public ArrayList<Prenotazione> tuttiIBiglietti;

    public Sistema(){
        utenti = new ArrayList<>();
        utente = new UtenteGenerico("karol", " leonardi");
        admin = new Admin("saso","saso", "231223");
        tuttiIBiglietti = new ArrayList<>();
        biglietto = new Prenotazione();
    }
    public void aggiungiUtente(Utente ug){
        utenti.add(ug);
    }
    public void aggiungiVolo(Volo v){
        admin.aggiungiVoli(v);
    }
    public ArrayList<Volo> visualizzaVoli(){
        return utente.visualizzaVoli();
    }
    //aggiungi il biglietto tra i biglietti acquistati dell'utente e tutti i biglietti
    public void aggiungiBiglietto(Prenotazione biglietto){
        utente.prenotaVolo(biglietto);
        tuttiIBiglietti.add(biglietto);
    }
    //cancella un biglietto
    public boolean cancellaBiglietto(long numeroBiglietto){
        return biglietto.cancellaBiglietto(tuttiIBiglietti,numeroBiglietto, utente.bigliettiAcquistati);
    }
    //crea il numero di un biglietto unico
    public Long creaNumBiglietto(){
        return biglietto.creaNumeroBiglietto(tuttiIBiglietti);
    }
    public ArrayList getBiglietti(String nome, int codiceVolo){
        return utente.cercaBiglietto(nome,codiceVolo);
    }
    public int verificaUtenteP(String username, String psw){
        for(Utente u : utenti){
            if(u.getNomeUtente().equals(username) && u.getPsw().equals(psw)){
                if(u instanceof UtenteGenerico){
                    //setUtenteLoggato(new UtenteGenerico(username,psw));
                    return 1;
                }else if(u instanceof Admin){
                    //setUtenteLoggato(new Admin(username,psw, "prova"));
                    return 2;
                }
            }
            //System.out.println(u.getNomeUtente()+" "+u.getPsw()); debbubing
        }

        return 0;
    }
    public boolean verificaUtenteUnivoco(String username){
        for(Utente u : utenti){
            if(u.getNomeUtente().equals(username) ){
               return false;
            }
        }
        return true;
    }
    public void setUtenteLoggato(Utente u){
        if(u instanceof UtenteGenerico){
            this.utente = (UtenteGenerico) u;
        }else{
            this.admin = (Admin) u;
        }
    }
    public void modificaBiglietto(Prenotazione b){
        biglietto.modificaBiglietto(b,utente.bigliettiAcquistati);
    }
    public void login(String username, String psw){
        if(verificaUtenteP(username,psw)==1){
            setUtenteLoggato(utente);
        }else if(verificaUtenteP(username,psw)==2){
            setUtenteLoggato(admin);
        }
    }
    public void logout(Utente utente){
        utente =null;
    }
    public void generaContenutiCasuali(){
        Random casuale= new Random();
        /*Random ritardo = new Random();
        Random statoVolo = new Random();
        Random compagnia = new Random();
        Random aeroportoOrigine = new Random();
        Random aeroportoDestinazione = new Random();
        Random orarioPartenza = new Random();
        Random orarioArrivo = new Random();
        Random gate = new Random();*/
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
        String[] gate = {"1A","1B","1C","1D","1E","1F","1G","1H","1I","1J","1K","1L","1M","1N","1O","1P","1Q","1R","1S","1T","1U","1V","1W","1X","1Y","1Z"};
        String[] stati = {"DECOLLATO",
                "PROGRAMMATO",
                "INRITARDO",
                "INORARIO",
                "ATTERRATO",
                "CANCELLATO"};
        for(int i=0;i<40;i++){
            int codiceVolo = casuale.nextInt(9000) + 1000;
        int ritardo = casuale.nextInt((180 - 1 + 1) + 1);
        String statoVolo = stati[casuale.nextInt((5 - 1 + 1) + 1)];
        String compagnia = nomiCompagnie[casuale.nextInt(17)];
        String aeroportoOrigine = Aeroporti[casuale.nextInt(13)];
        String aeroportoDestinazione = Aeroporti[casuale.nextInt(13)];
        String orarioArrivo = orari[casuale.nextInt(20)];
        String gate1 = gate[casuale.nextInt(26)];
        Volo v=new Volo(codiceVolo,compagnia,aeroportoOrigine,aeroportoDestinazione,orarioArrivo,ritardo,Volo.statoVolo.valueOf(statoVolo), gate1);
        admin.aggiungiVoli(v);
        }

    }
}