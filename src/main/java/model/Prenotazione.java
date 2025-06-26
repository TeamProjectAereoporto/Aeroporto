package model;

import java.util.ArrayList;
import java.util.Random;

public class Prenotazione {
    // Numero univoco del biglietto assegnato alla prenotazione
    private long numeroBiglietto;

    // Posto assegnato al passeggero per questo volo
    private String postoAssegnato;

    // Stato attuale della prenotazione (es. confermata, in attesa, cancellata)
    private StatoPrenotazione stato;

    // Passeggero associato a questa prenotazione
    private Passeggero passeggero;

    // Volo a cui la prenotazione è riferita
    private Volo volo;

    // Utente che ha effettuato l'acquisto della prenotazione
    private Utente acquirente;

    // Enum interno che rappresenta i possibili stati di una prenotazione
    public enum StatoPrenotazione {
        CONFERMATA(1),
        IN_ATTESA(2),
        CANCELLATA(3);

        private final int codice;

        StatoPrenotazione(int codice) {
            this.codice = codice;
        }

        // Ritorna il codice numerico associato allo stato della prenotazione
        public int getCodice() {
            return codice;
        }
    }

    // Costruttore vuoto (di default)
    public Prenotazione(){}

    // Costruttore completo per inizializzare tutti i campi della prenotazione
    public Prenotazione(long numeroBiglietto, String postoAssegnato, StatoPrenotazione stato, Passeggero passeggero, Volo volo, Utente acquirente) {
        this.numeroBiglietto = numeroBiglietto;
        this.postoAssegnato = postoAssegnato;
        this.stato = stato;
        this.passeggero = passeggero;
        this.volo = volo;
        this.acquirente = acquirente;
    }

    // Getter per lo stato della prenotazione
    public StatoPrenotazione getStato() {
        return stato;
    }

    // Setter per lo stato della prenotazione
    public void setStato(StatoPrenotazione stato) {
        this.stato = stato;
    }

    // Getter per il posto assegnato
    public String getPostoAssegnato(){
        return postoAssegnato;
    }

    // Setter per il posto assegnato
    public void setPostoAssegnato(String postoAssegnato) {
        this.postoAssegnato = postoAssegnato;
    }

    // Getter per il numero del biglietto
    public long getNumeroBiglietto(){
        return numeroBiglietto;
    }

    // Setter per il numero del biglietto
    public void setNumeroBiglietto(long numeroBiglietto){
        this.numeroBiglietto = numeroBiglietto;
    }

    // Setter per il passeggero
    public void setPasseggero(Passeggero passeggero){
        this.passeggero = passeggero;
    }

    // Getter per il passeggero
    public Passeggero getPasseggero(){
        return passeggero;
    }

    // Setter per il volo associato
    public void setVolo(Volo volo) {
        this.volo = volo;
    }

    // Getter per il volo associato
    public Volo getVolo() {
        return volo;
    }

    // Setter per l'acquirente (utente che ha effettuato l'acquisto)
    public void setAcquirente(Utente u){
        acquirente = u;
    }

    // Getter per l'acquirente
    public Utente getAcquirente(){
        return acquirente;
    }

    /*
     * Genera un numero biglietto casuale univoco che non è già presente nella lista biglietti.
     */
    public long creaNumeroBiglietto(ArrayList<Prenotazione> biglietti){
        Random casuale = new Random();
        boolean controllo = true;
        long min = 1000000000L;  // minimo numero biglietto a 10 cifre
        long max = 9999999999L;  // massimo numero biglietto a 10 cifre
        long numeroCasuale = 0;

        while(controllo) {
            // genera numero casuale nell'intervallo definito
            numeroCasuale = min + ((long) (casuale.nextDouble() * (max - min + 1)));
            // controlla che il numero non esista già nella lista biglietti
            if(controlloNumeroBigliettoEsistenti(biglietti, numeroCasuale)){
                controllo = false;
            }
        }
        return numeroCasuale;
    }

    /*
     * Controlla se un numero biglietto è già presente nella lista biglietti.
     */
    public boolean controlloNumeroBigliettoEsistenti(ArrayList<Prenotazione> biglietti, long numeroBiglietto){
        for (Prenotazione p : biglietti){
            if(p.getNumeroBiglietto() == numeroBiglietto){
                return false; // numero già esistente
            }
        }
        return true; // numero non trovato, quindi valido
    }

    /*
     * Cancella un biglietto dalla lista delle prenotazioni acquistate tramite il numero biglietto.
     */
    public boolean cancellaBiglietto(long numeroBiglietto, ArrayList<Prenotazione> bigliettiAcquistati){
        for(Prenotazione p : bigliettiAcquistati){
            if(p.getNumeroBiglietto() == numeroBiglietto){
                bigliettiAcquistati.remove(p);
                return true;
            }
        }
        return false;
    }

    /*
     * Modifica una prenotazione esistente nella lista in base al numero biglietto.
     */
    public void modificaBiglietto(Prenotazione bigliettoModificato, ArrayList<Prenotazione> biglietti){
        for (Prenotazione p : biglietti){
            if(p.getNumeroBiglietto() == bigliettoModificato.getNumeroBiglietto()){
                p.setNumeroBiglietto(bigliettoModificato.getNumeroBiglietto());
                p.setPostoAssegnato(bigliettoModificato.getPostoAssegnato());
                p.setStato(bigliettoModificato.getStato());
            }
        }
    }

    // Override del metodo toString per la rappresentazione testuale dell'oggetto Prenotazione
    public String toString(){
        return "Biglietto: " + numeroBiglietto +
                "\nPosto: " + postoAssegnato +
                "\nStato: " + stato +
                "\nnome " + passeggero.getNome() +
                "\ncognome " + passeggero.getCognome() +
                "\nci " + passeggero.getNumeroDocumento();
    }

}
