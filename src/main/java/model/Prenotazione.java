package model;

import java.util.ArrayList;
import java.util.Random;

public class Prenotazione {
    private long numeroBiglietto;
    private String postoAssegnato;
    private StatoPrenotazione stato;
    private Passeggero passeggero;
    private Volo volo;
    public enum StatoPrenotazione {
        CONFERMATA(1),
        IN_ATTESA(2),
        CANCELLATA(3);

        private final int codice;

        StatoPrenotazione(int codice) {
            this.codice = codice;
        }

        public int getCodice() {
            return codice;
        }
    }
    //Costruttore
    public Prenotazione(){}
    public Prenotazione(long numeroBiglietto, String postoAssegnato, StatoPrenotazione stato, Passeggero passeggero, Volo volo) {
        this.numeroBiglietto = numeroBiglietto;
        this.postoAssegnato = postoAssegnato;
        this.stato = stato;
        this.passeggero=passeggero;
        this.volo=volo;
    }
    //get statoPrenotazione
    public StatoPrenotazione getStato() {
        return stato;
    }
    //set
    public void setStato(StatoPrenotazione stato) {
        this.stato = stato;
    }
    //get postoAssegnato
    public String getPostoAssegnato(){
        return postoAssegnato;
    }
    //set
    public void setPostoAssegnato(String postoAssegnato) {
        this.postoAssegnato = postoAssegnato;
    }
    //get numeroBiglietto
    public long getNumeroBiglietto(){
        return numeroBiglietto;
    }
    //set numeroBiglietto
    public void setNumeroBiglietto(long numeroBiglietto){
        this.numeroBiglietto=numeroBiglietto;
    }
    //set passeggero
    public void setPasseggero(Passeggero passeggero){
        this.passeggero=passeggero;
    }
    //return passeggero
    public Passeggero getPasseggero(){
        return passeggero;
    }
    //set passeggero
    public void setVolo(Volo volo) {this.volo = volo;}
    //return passeggero
    public Volo getVolo() {return volo;}
    //crea numeroBiglietto
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
    //controllo se il biglietto esistente è già creato
    public boolean controlloNumeroBigliettoEsistenti(ArrayList<Prenotazione> biglietti, long numeroBiglietto){
        for (Prenotazione p : biglietti){
            if(p.getNumeroBiglietto()== numeroBiglietto){
                return false;
            }
        }
        return  true;
    }
    public boolean cancellaBiglietto( long numeroBiglietto, ArrayList<Prenotazione> bigliettiAcquistati){
        for(Prenotazione p : bigliettiAcquistati){
            if(p.getNumeroBiglietto()==numeroBiglietto){
                bigliettiAcquistati.remove(p);
                return true;
            }
        }
            return false;
    }
    public void modificaBiglietto(Prenotazione bigliettoModificato, ArrayList<Prenotazione> biglietti){
        for (Prenotazione p: biglietti){
            if(p.getNumeroBiglietto()==bigliettoModificato.getNumeroBiglietto()){
                p.setNumeroBiglietto(bigliettoModificato.getNumeroBiglietto());
                p.setPostoAssegnato(bigliettoModificato.getPostoAssegnato());
                p.setStato(bigliettoModificato.getStato());
            }
        }
    }
    public String toString(){
        return "Biglietto: "+numeroBiglietto+"\nPosto: "+postoAssegnato+ "\nStato: "+stato+"\nnome "+ passeggero.getNome()+"\ncognome "+passeggero.getCognome()+"\nci "+ passeggero.getNumeroDocumento();
    }

}
