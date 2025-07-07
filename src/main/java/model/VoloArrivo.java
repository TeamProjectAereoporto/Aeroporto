package model;

import java.time.LocalDate;

public class VoloArrivo extends Volo {

    public VoloArrivo(int codiceVolo, String compagniaAerea, String aeroportoOrigine,
                      String orarioArrivo, int ritardo, statoVolo stato, String gate, LocalDate dataVolo) {
        super(codiceVolo, compagniaAerea, aeroportoOrigine, "Napoli",
                orarioArrivo, ritardo, stato, gate, dataVolo);
    }

    public VoloArrivo(int codiceVolo, String compagniaAerea, String aeroportoOrigine,
                      String orarioArrivo, statoVolo stato, String gate, LocalDate dataVolo) {
        this(codiceVolo, compagniaAerea, aeroportoOrigine, orarioArrivo, 0, stato, gate, dataVolo);
    }
}
