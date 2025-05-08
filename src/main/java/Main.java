import model.*;

import java.awt.desktop.PrintFilesEvent;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        //simulazione dati inseriti per login
        Utente utente = new Utente("Karol","password","Utente");
        Sistema sistema = new Sistema();
        //inserisce 3 utenti nel arraylist utenti in sistema
        for(int i=0; i<3; i++){
            System.out.printf("%ninserisci il nome utente del %d utente:",i+1);
            String nomeUtente = scanner.next();
            System.out.printf("%ninserisci la password del %d utente:",i+1);
            String psw = scanner.next();
            System.out.printf("%ninserisci il ruolo del %d utente:",i+1);
            String ruolo = scanner.next();
            Utente utenteprova= new Utente(nomeUtente,psw,ruolo);
            sistema.aggiungiUtente(utenteprova);
        }

        //prova per controllare login
        boolean verifica = sistema.verificaUtente(utente.getNomeUtente(), utente.getPsw(), utente.getRuolo());
        if(verifica){
            System.out.printf("Accesso effettuato correttamente come %s",utente.getRuolo());

        }else{
            System.out.printf("utente non trovato");
        }
        if(utente.getRuolo().equals("Utente")){
            UtenteGenerico user = new UtenteGenerico(utente.getNomeUtente(), utente.getPsw());
            ArrayList<Prenotazione> biglietti = new ArrayList<>();
            System.out.printf("%nBentornato nell'Aereoporto digitale di napoli %s",utente.getNomeUtente());

                long numeroBiglietto= sistema.creaNumeroBiglietto(biglietti);
                user.prenotaVolo(biglietti,numeroBiglietto,"A2");
                numeroBiglietto= sistema.creaNumeroBiglietto(biglietti);
                user.prenotaVolo(biglietti,numeroBiglietto,"A3");
                numeroBiglietto= sistema.creaNumeroBiglietto(biglietti);
                user.prenotaVolo(biglietti,numeroBiglietto,"A3");
            for(Prenotazione v : biglietti){
                System.out.printf("Numero biglietto: %s %n Posto assegnato: %s %n Stato Prenotazione: %s %n",v.getNumeroBiglietto(), v.getPostoAssegnato(),v.getStato());
            }
                Prenotazione biglietto = user.cercaBiglietto(biglietti, numeroBiglietto);
                System.out.printf("biglietto: %n Numero biglietto: %s %n Posto assegnato %s %n Stato Prenotazione %s %n", biglietto.getNumeroBiglietto(), biglietto.getPostoAssegnato(), biglietto.getStato());
            System.out.printf("biglietto prima di essere modificato: %n Numero biglietto: %s %n Posto assegnato %s %n Stato Prenotazione %s %n", biglietti.get(1).getNumeroBiglietto(), biglietti.get(1).getPostoAssegnato(), biglietti.get(1).getStato());
            user.modificaBiglietto(biglietti.get(1), "A4", 2 );
                System.out.printf("biglietto modificato: %n Numero biglietto: %s %n Posto assegnato %s %n Stato Prenotazione %s %n", biglietti.get(1).getNumeroBiglietto(), biglietti.get(1).getPostoAssegnato(), biglietti.get(1).getStato());


        }
    }
}