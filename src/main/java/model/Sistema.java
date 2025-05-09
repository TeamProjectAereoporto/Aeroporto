package model;
import java.util.ArrayList;
import java.util.Random;

public class Sistema {
    public ArrayList<UtenteGenerico> utentiGenerici;
    public ArrayList<Admin> tantiAdmin;
    public ArrayList<Utente> utenti;


    public Sistema(){
        utentiGenerici = new ArrayList<>();
        tantiAdmin = new ArrayList<>();
        utenti = new ArrayList<>();
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
    public boolean verificaUtente(String login, String psw, String ruolo) {
        for(Utente p : utenti){
            if(p.getNomeUtente().equals(login) && p.getPsw().equals(psw) && p.getRuolo().equals(ruolo)){
                return true;
            }
        }
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