package model;

import java.util.ArrayList;

public class Checkin {

    private Passeggero passeggero;
    public ArrayList<Prenotazione> biglietti;

    public Checkin(Passeggero passeggero) {
        this.passeggero = passeggero;
        this.biglietti = new ArrayList<>();
    }

    public void aggiungiBiglietto(Prenotazione biglietto) {
        biglietti.add(biglietto);
    }

    public boolean controlloBiglietto(int numeroBiglietto) {
        for (Prenotazione p : biglietti) {
            if (p.getNumeroBiglietto() == numeroBiglietto &&
                    p.getStato() == Prenotazione.StatoPrenotazione.CONFERMATA) {
                return true;
            }
        }
        return false;
    }

    // Getter se serve accedere ai biglietti
    public ArrayList<Prenotazione> getBiglietti() {
        return new ArrayList<>(biglietti);
    }

    public Passeggero getPasseggero() {
        return passeggero;
    }
}
