package model;

public class VoloArrivo extends Volo{

    public VoloArrivo(int codiceVolo, String compagniaAerea,float orarioArrivo, int ritardo,statoVolo stato, String aeroportoOrigine){
        super(codiceVolo,compagniaAerea,orarioArrivo,ritardo,stato,aeroportoOrigine, "Napoli");
    }
}