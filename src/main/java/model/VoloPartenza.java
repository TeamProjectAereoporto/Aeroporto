package model;

import java.time.LocalDate;

public class VoloPartenza extends Volo {

    public VoloPartenza(int codiceVolo, String compagniaAerea, String aeroportoDestinazione, String orarioArrivo,
                        int ritardo, statoVolo stato, String gate, LocalDate dataVolo) {
        super(codiceVolo, compagniaAerea, "Napoli", aeroportoDestinazione, orarioArrivo, ritardo, stato, gate, dataVolo);
    }

}

