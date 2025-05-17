package model;

import java.util.Date;

public class VoloPartenza extends Volo{

    public VoloPartenza(int codiceVolo, String compagniaAerea, Date orarioArrivo, int ritardo, statoVolo stato, String aeroportoDestinazione) {
        super(codiceVolo, compagniaAerea, orarioArrivo, ritardo, stato, "Napoli", aeroportoDestinazione);
    }


    private Gate gateAssegnato;

    public void setGate(Gate g) {
        this.gateAssegnato = g;
    }

    public Gate getGate() {
        return gateAssegnato;
    }

}