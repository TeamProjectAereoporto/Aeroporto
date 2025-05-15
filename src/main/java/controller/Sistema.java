package controller;
import model.*;

import java.util.ArrayList;
import java.util.Random;

public class Sistema {
    public ArrayList<UtenteGenerico> utentiGenerici;
    public ArrayList<Admin> tantiAdmin;
    public ArrayList<Utente> utenti;
    public ArrayList<Volo> voli;
    public Prenotazione biglietto;
    public UtenteGenerico utente;
    public UtenteGenerico utento;

    public Sistema(){
        utentiGenerici = new ArrayList<>();
        tantiAdmin = new ArrayList<>();
        utenti = new ArrayList<>();
        voli = new ArrayList<>();
        utente = new UtenteGenerico("karol", " leonardi");
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
    public ArrayList<Volo> visualizzaVoli(){
        return utente.visualizzaVoli();
    }
    public ArrayList getBiglietti(String nome, long numeroBiglietto){
        return utente.cercaBiglietto(nome,numeroBiglietto, utente);
    }
    public int verificaUtenteP(String username, String psw){
        Utente ne = new Utente("karol", "karol", "utenteGenerico");
        Utente ne1 = new Utente("saso", "saso", "admin");
        utenti.add(ne);
        utenti.add(ne1);
        for(Utente u : utenti){
            if(u.getNomeUtente().equals(username) && u.getPsw().equals(psw)){
                if (u.getRuolo().equals("utenteGenerico")){
                    System.out.println("utente");
                    return 1;
                }else{
                    System.out.println("utenteAD");
                    return 2;
                }
            }
        }
        System.out.println("utentNOe");
        return 0;
    }
    public boolean verificaAdmin(String login, String psw, String codAdmin){
        for(int i = 0; i < tantiAdmin.size(); i++){
            if(login.equals(tantiAdmin.get(i).getNomeUtente()) && psw.equals(tantiAdmin.get(i).getPsw()) && codAdmin.equals(tantiAdmin.get(i).getCodAdmin())){
                System.out.println("Accesso consentito come admin");
                return true;
            }
        }
        System.out.println("Accesso negato, nome utente, password o codice admin errati");
        return false;
    }
    public boolean verificaUtente(String login, String psw) {
        for(Utente p : utenti){
            if(p.getNomeUtente().equals(login) && p.getPsw().equals(psw)){
                return true;
            }
        }
        return false;
    }
    public boolean verificaUtenteG(String login, String psw){
        for(int i = 0; i < utentiGenerici.size(); i++){
            if(login.equals(utentiGenerici.get(i).getNomeUtente()) && psw.equals(utentiGenerici.get(i).getPsw())){
                System.out.println("Credenziali riconosciute, accesso consentito come utente generico");
                return true;
            }
        }
        System.out.println("Accesso negato, nome utente o password errati");
        return false;
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