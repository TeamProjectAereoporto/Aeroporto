package model;

import java.util.ArrayList;

public class Checkin {

    public Passeggero passeggero;
    public Prenotazione biglietto;
    public Boolean controlloBiglietto(int numeroBiglietto, ArrayList<Prenotazione> biglietto){
        for(Prenotazione p: biglietto){
            if(p.getNumeroBiglietto() == numeroBiglietto && p.getStato() == Prenotazione.StatoPrenotazione.CONFERMATA){
                return true;
            }
        }
        return false;
    }
}