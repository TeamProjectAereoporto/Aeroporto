package controller;
import model.*;
import java.util.ArrayList;


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
}