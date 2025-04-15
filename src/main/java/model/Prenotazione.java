package model;
import java.util.ArrayList;
public class Prenotazione {
    public int numeroBiglietto;
    public String postoAssegnato;
    public StatoPrenotazione stato;
    public ArrayList<Prenotazione> numeroBiglietti = new ArrayList<>();


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
    public Prenotazione(int numeroBiglietto, String postoAssegnato, StatoPrenotazione stato) {
        this.numeroBiglietto = numeroBiglietto;
        this.postoAssegnato = postoAssegnato;
        this.stato = stato;
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
    public int getNumeroBiglietto(){
        return numeroBiglietto;
    }
    //set numeroBiglietto
    public void setNumeroBiglietto(int numeroBiglietto){
        this.numeroBiglietto=numeroBiglietto;
    }
}