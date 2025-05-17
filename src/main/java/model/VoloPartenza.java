package model;

import java.util.Date;

public class VoloPartenza extends Volo{

    public VoloPartenza(int codiceVolo, String compagniaAerea, Date orarioArrivo, int ritardo, statoVolo stato, String aeroportoDestinazione, String gate) {
        super(codiceVolo, compagniaAerea, orarioArrivo, ritardo, stato, "Napoli", aeroportoDestinazione, gate);
    }

}