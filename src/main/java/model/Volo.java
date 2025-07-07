package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Classe che rappresenta un volo con i suoi attributi principali
public class Volo {
    // Codice identificativo univoco del volo
    private int codiceVolo;
    // Nome della compagnia aerea che gestisce il volo
    private String compagniaAerea;
    // Aeroporto di partenza del volo
    private String aeroportoOrigine;
    // Aeroporto di destinazione del volo
    private String aeroportoDestinazione;
    // Orario previsto di arrivo del volo
    private String orarioArrivo;
    // Ritardo accumulato dal volo, espresso in minuti o ore
    private int ritardo;
    // Stato attuale del volo (es. decollato, programmato, in ritardo, ecc.)
    private statoVolo stato;
    // Gate di imbarco associato al volo
    private String gate;
    //data di arrivo o partenza del volo
    private LocalDate dataVolo;

    // Enumerazione per rappresentare i possibili stati di un volo
    public enum statoVolo{
        DECOLLATO(1),     // Volo decollato
        PROGRAMMATO(2),   // Volo programmato ma non ancora partito
        INRITARDO(3),     // Volo in ritardo
        INORARIO(4),      // Volo in orario
        ATTERRATO(5),     // Volo atterrato
        CANCELLATO(6);    // Volo cancellato

        // Codice associato allo stato per eventuali utilizzi numerici
        private final int codice;

        // Costruttore dell'enumerazione
        statoVolo(int codice){
            this.codice = codice;
        }

        // Metodo getter per ottenere il codice dello stato
        public int getStato(){
            return codice;
        }
    }

    // Metodi getter per accedere ai vari attributi privati

    public int getCodiceVolo() {
        return codiceVolo;
    }

    public String getOrarioArrivo() {
        return orarioArrivo;
    }

    public int getRitardo() {
        return ritardo;
    }

    public statoVolo getStato() {
        return stato;
    }

    public String getAeroportoDestinazione() {
        return aeroportoDestinazione;
    }

    public String getAeroportoOrigine() {
        return aeroportoOrigine;
    }

    public String getCompagniaAerea() {
        return compagniaAerea;
    }

    public String getGate(){
        return gate;
    }
    public LocalDate getDataVolo() {
        return dataVolo;
    }

    // Metodi setter per modificare i valori degli attributi privati

    public void setAeroportoDestinazione(String aeroportoDestinazione) {
        this.aeroportoDestinazione = aeroportoDestinazione;
    }

    public void setAeroportoOrigine(String aeroportoOrigine) {
        this.aeroportoOrigine = aeroportoOrigine;
    }

    public void setCodiceVolo(int codiceVolo) {
        this.codiceVolo = codiceVolo;
    }

    public void setCompagniaAerea(String compagniaAerea) {
        this.compagniaAerea = compagniaAerea;
    }

    public void setOrarioArrivo(String orarioArrivo) {
        this.orarioArrivo = orarioArrivo;
    }

    public void setRitardo(int ritardo) {
        this.ritardo = ritardo;
    }

    public void setStato(statoVolo stato) {
        this.stato = stato;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public void setDataVolo(LocalDate dataVolo){
        this.dataVolo = dataVolo;
    }

    // Costruttore completo con tutti i parametri inclusi
    public Volo(int codiceVolo, String compagniaAerea, String aeroportoOrigine, String aeroportoDestinazione,
                String orarioArrivo, int ritardo, statoVolo stato, String gate,  LocalDate dataVolo) {
        this.codiceVolo = codiceVolo;
        this.compagniaAerea = compagniaAerea;
        this.aeroportoOrigine = aeroportoOrigine;
        this.aeroportoDestinazione = aeroportoDestinazione;
        this.orarioArrivo = orarioArrivo;
        this.ritardo = ritardo;
        this.stato = stato;
        this.gate = gate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.dataVolo = LocalDate.parse(dataVolo.format(formatter), formatter);
    }

    // Costruttore senza ritardo, imposta ritardo a zero di default
    public Volo(int codiceVolo, String compagniaAerea, String aeroportoOrigine, String aeroportoDestinazione,
                String orarioArrivo, statoVolo stato, String gate, LocalDate dataVolo) {
        this(codiceVolo, compagniaAerea, aeroportoOrigine, aeroportoDestinazione,
                orarioArrivo, 0, stato, gate, dataVolo);
    }

    // Metodo per rappresentare l'oggetto come stringa in modo leggibile
    @Override
    public String toString() {
        return "Volo " + codiceVolo + " da " + aeroportoOrigine + " a " + aeroportoDestinazione +
                " con arrivo previsto alle " + orarioArrivo + " (" + stato + ")" + " al gate " + gate;
    }
}
