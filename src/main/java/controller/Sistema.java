package controller;
import model.*;

import java.util.ArrayList;
import java.util.Random;

public class Sistema {
    public ArrayList<UtenteGenerico> utentiGenerici;
    public ArrayList<Admin> tantiAdmin;
    public ArrayList<Utente> utenti;
    public Prenotazione biglietto;
    public UtenteGenerico utente;
    public Utente utento;
    public Admin admin;

    public Sistema(){
        utentiGenerici = new ArrayList<>();
        tantiAdmin = new ArrayList<>();
        utenti = new ArrayList<>();
        utente = new UtenteGenerico("karol", " leonardi");
        admin = new Admin("saso","saso", "231223");
    }
    public void setAdmin(Admin admin){
        this.admin=admin;
    }
    public void aggiungiUtente(Utente ug){
        utenti.add(ug);
    }
    public void aggiungiUtenteGenerico(UtenteGenerico ug){
        utentiGenerici.add(ug);
    }
    public void aggiungiAdmin(Admin a){
        tantiAdmin.add(a);
    }
    public void aggiungiVolo(Volo v){
        admin.aggiungiVoli(v);
    }
    public ArrayList<Volo> visualizzaVoli(){
        return utente.visualizzaVoli();
    }
    public void aggiungiBiglietto(Prenotazione biglietto){

    }
    public ArrayList getBiglietti(String nome, long numeroBiglietto, UtenteGenerico utente){
        return utente.cercaBiglietto(nome,numeroBiglietto, utente);
    }
    public int verificaUtenteP(String username, String psw){
        for(Utente u : utenti){
            if(u.getNomeUtente().equals(username) && u.getPsw().equals(psw)){
                if (u.getRuolo().equals("utenteGenerico")){
                    return 1;
                }else{
                    return 2;
                }
            }
        }
        return 0;
    }
    //crea il numero biglietto
    public long creaNumeroBiglietto(ArrayList<Prenotazione> biglietti){
        Random casuale = new Random();
        boolean controllo = true;
        long min = 1000000000L;
        long max = 9999999999L;
        long numeroCasuale=0;
        while(controllo) {
            numeroCasuale = min + ((long) (casuale.nextDouble() * (max - min + 1)));
            if(controlloNumeroBigliettoEsistenti(biglietti,numeroCasuale)){
                controllo=false;
            }
        }
        return  numeroCasuale;
    }
    //controllo numeroBiglietti durante la creazione
    public boolean controlloNumeroBigliettoEsistenti(ArrayList<Prenotazione> biglietti, long numeroBiglietto){
        for (Prenotazione p : biglietti){
            if(p.getNumeroBiglietto()== numeroBiglietto){
                return false;
            }
        }
        return  true;
    }
}