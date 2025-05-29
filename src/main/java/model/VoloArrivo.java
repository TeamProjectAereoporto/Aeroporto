package model;

public class VoloArrivo extends Volo {

    public VoloArrivo(int codiceVolo, String compagniaAerea, String aeroportoOrigine,
                      String orarioArrivo, int ritardo, statoVolo stato, String gate) {
        super(codiceVolo, compagniaAerea, aeroportoOrigine, "Napoli",
                orarioArrivo, ritardo, stato, gate);
    }

    public VoloArrivo(int codiceVolo, String compagniaAerea, String aeroportoOrigine,
                      String orarioArrivo, statoVolo stato, String gate) {
        this(codiceVolo, compagniaAerea, aeroportoOrigine, orarioArrivo, 0, stato, gate);
    }
}
