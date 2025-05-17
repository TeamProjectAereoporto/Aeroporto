package model;

import java.util.Date;

public class VoloArrivo extends Volo{

    public VoloArrivo(int codiceVolo, String compagniaAerea, Date orarioArrivo, int ritardo, statoVolo stato, String aeroportoOrigine, String gate){
        super(codiceVolo,compagniaAerea,orarioArrivo,ritardo,stato,aeroportoOrigine, "Napoli", gate);
    }
}