package model;

public class VoloPartenza extends Volo {

    public VoloPartenza(int codiceVolo, String compagniaAerea, String aeroportoDestinazione, String orarioArrivo,
                        int ritardo, statoVolo stato, String gate) {
        super(codiceVolo, compagniaAerea, "Napoli", aeroportoDestinazione, orarioArrivo, ritardo, stato, gate);
    }

}

