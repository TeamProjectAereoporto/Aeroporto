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
    public Admin admin;
    public ArrayList<Volo> tuttiIVoli;
    public ArrayList<Prenotazione> tuttiIBiglietti;

    public Sistema(){
        utentiGenerici = new ArrayList<>();
        tantiAdmin = new ArrayList<>();
        utenti = new ArrayList<>();
        utente = new UtenteGenerico("karol", " leonardi");
        admin = new Admin("saso","saso", "231223");
        tuttiIVoli = new ArrayList<>();
        tuttiIBiglietti = new ArrayList<>();
        biglietto = new Prenotazione(312321123,"a3", Prenotazione.StatoPrenotazione.CONFERMATA, new Passeggero("", "", ""));
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
    public void aggiungiBiglietto(Prenotazione biglietto){
        tuttiIBiglietti.add(biglietto);
    }
    public boolean cancellaBiglietto(long numeroBiglietto){
       return biglietto.cancellaBiglietto(tuttiIBiglietti,numeroBiglietto);
    }
    public Long creaBiglietto(){
        return biglietto.creaNumeroBiglietto(tuttiIBiglietti);
    }
    public ArrayList getBiglietti(String nome, long numeroBiglietto, UtenteGenerico utente){
        return utente.cercaBiglietto(nome,numeroBiglietto, utente, tuttiIBiglietti);
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


}