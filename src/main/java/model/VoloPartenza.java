package model;

public class VoloPartenza extends Volo{

    public VoloPartenza(int codiceVolo, String compagniaAerea, float orarioArrivo, int ritardo, statoVolo stato, String aeroportoDestinazione) {
        super(codiceVolo, compagniaAerea, orarioArrivo, ritardo, stato, "Napoli", aeroportoDestinazione);
    }


    public Gate gateAssegnato;

    public void setGate(Gate g) {
        this.gateAssegnato = g;
    }

    public Gate getGate() {
        return gateAssegnato;
    }

}
