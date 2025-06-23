package controller;
import model.*;
import java.util.ArrayList;
import java.util.Random;


public class Sistema {
    public static ArrayList<Utente> utenti;
    public Prenotazione biglietto;
    public UtenteGenerico utente;
    public Admin admin;
    public ArrayList<Prenotazione> tuttiIBiglietti;
    private final Random casuale= new Random();
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

    public boolean cancellaBiglietto(long numeroBiglietto){
        return biglietto.cancellaBiglietto(numeroBiglietto, UtenteGenerico.bigliettiAcquistati);
    }


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

                    return 1;
                }else if(u instanceof Admin){
                    return 2;
                }
            }
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
        biglietto.modificaBiglietto(b, UtenteGenerico.bigliettiAcquistati);
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
    /*public void generaContenutiCasuali(){

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
        }

    }*/

}