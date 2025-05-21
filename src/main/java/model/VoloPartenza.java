package model;

import java.util.Date;

public class VoloPartenza extends Volo{

    public VoloPartenza(int codiceVolo, String compagniaAerea, String orarioArrivo, int ritardo, statoVolo stato, String aeroportoDestinazione, String gate) {
        super(codiceVolo, compagniaAerea, orarioArrivo, ritardo, stato, "Napoli", aeroportoDestinazione, gate);
    }

}