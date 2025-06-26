package model;

import java.util.ArrayList;
import java.util.logging.Logger;

// Questa classe estende la superclasse Utente per rappresentare un utente generico
public class UtenteGenerico extends Utente {
    // Lista statica dei biglietti acquistati dall'utente generico
    public static ArrayList<Prenotazione> bigliettiAcquistati;

    // Logger per il tracciamento delle operazioni
    private static final Logger logger = Logger.getLogger(UtenteGenerico.class.getName());

    // Costruttore che inizializza l'utente generico con login, password e ruolo fisso a 1
    public UtenteGenerico(String l, String p, int ruolo){
        super(l,p, 1);
        bigliettiAcquistati = new ArrayList<>();
    }

    // Metodo per aggiungere una prenotazione alla lista dei biglietti acquistati
    public void prenotaVolo(Prenotazione biglietto){
        bigliettiAcquistati.add(biglietto);
    }

    // Metodo per modificare un biglietto esistente aggiornando il posto e lo stato
    public void modificaBiglietto(Prenotazione biglietto, String postoAssegnato, int stato){
        biglietto.setPostoAssegnato(postoAssegnato);
        if(stato==1){
            biglietto.setStato(Prenotazione.StatoPrenotazione.CONFERMATA);
        }else{
            biglietto.setStato(Prenotazione.StatoPrenotazione.IN_ATTESA);
        }
    }

    // Metodo per cercare biglietti acquistati filtrando per nome del passeggero e/o codice del volo
    public ArrayList<Prenotazione> cercaBiglietto(String nome, int codiceVolo) {
        // Lista per memorizzare i biglietti trovati
        ArrayList<Prenotazione> bigliettiTrovati = new ArrayList<>();

        // Se i parametri di ricerca sono vuoti o non validi, restituisce lista vuota
        if (nome.isEmpty() && codiceVolo == -1) {
            return bigliettiTrovati;
        }

        // Cicla tutti i biglietti acquistati per verificare corrispondenza con criteri di ricerca
        for (Prenotazione p : bigliettiAcquistati) {
            boolean nomeMatch = !nome.isEmpty() && p.getPasseggero().getNome().equalsIgnoreCase(nome.trim());
            boolean numeroMatch = codiceVolo != -1 && p.getVolo().getCodiceVolo() == codiceVolo;

            // Aggiunge il biglietto se uno o entrambi i criteri corrispondono
            if ((nomeMatch && numeroMatch) || (nomeMatch && codiceVolo == -1) || (numeroMatch && nome.isEmpty())) {
                logger.info("ho trovato il biglietto\n");
                bigliettiTrovati.add(p);
            }
        }

        // Log se non è stato trovato alcun biglietto corrispondente
        if (bigliettiTrovati.isEmpty()) {
            logger.info("Nessun biglietto trovato");
        }

        return bigliettiTrovati;
    }

}
