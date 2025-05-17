package model;
import java.util.ArrayList;
import java.util.Random;

public class Prenotazione {
    private long numeroBiglietto;
    private String postoAssegnato;
    private StatoPrenotazione stato;
    private Passeggero passeggero;
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
    public Prenotazione(long numeroBiglietto, String postoAssegnato, StatoPrenotazione stato, Passeggero passeggero) {
        this.numeroBiglietto = numeroBiglietto;
        this.postoAssegnato = postoAssegnato;
        this.stato = stato;
        this.passeggero=passeggero;
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
    public Passeggero getPasseggero(){
        return passeggero;
    }

}