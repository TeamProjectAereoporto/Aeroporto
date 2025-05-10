import model.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //simulazione dati inseriti per login
        Utente utente = new Utente("Karol", "password", "Utente");
        Sistema sistema = new Sistema();
        //inserisce 3 utenti nel arraylist utenti in sistema
        for (int i = 0; i < 3; i++) {
            System.out.printf("%ninserisci il nome utente del %d utente:", i + 1);
            String nomeUtente = scanner.next();
            System.out.printf("%ninserisci la password del %d utente:", i + 1);
            String psw = scanner.next();
            System.out.printf("%ninserisci il ruolo del %d utente:", i + 1);
            String ruolo = scanner.next();
            Utente utenteprova = new Utente(nomeUtente, psw, ruolo);
            sistema.aggiungiUtente(utenteprova);
        }
        //stampa utenti
        for (Utente p : sistema.utenti) {
            System.out.printf("%nnome: %s, password: %s, ruolo:%s", p.getNomeUtente(), p.getPsw(), p.getRuolo());
        }
        //prova per controllare login
        boolean verifica = sistema.verificaUtente(utente.getNomeUtente(), utente.getPsw(), utente.getRuolo());
        if (verifica) {
            System.out.printf("Accesso effettuato correttamente come %s", utente.getRuolo());

        } else {
            System.out.printf("utente non trovato");
        }
        if (utente.getRuolo() == "Utente") {

        }

        Admin adminProva = new Admin("Admin", "Password", "AB1526");
        Gate g = new Gate("A1");
        VoloPartenza volo1 = new VoloPartenza(4679, "ItaAirways", 14, 0, Volo.statoVolo.INORARIO, "Malpensa");
        adminProva.aggiungiVoli(volo1);
        adminProva.visualizzaVoli();
        //adminProva.aggiornaTuttoVolo(4679, "Fiumicino", 15, Volo.statoVolo.INRITARDO);
        adminProva.visualizzaVoli();
    }
}
