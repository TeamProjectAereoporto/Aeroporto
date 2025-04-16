package model;

public class Volo {
    int codiceVolo;
    String compagniaAerea;
    String aeroportoOrigine;
    String aeroportoDestinazione;
    float orarioArrivo;
    int ritardo; //se lo stato del volo è indicato come in ritardo, allora qui verrà salvato il tempo di ritardo in minuti/ore.
    public statoVolo stato;

    public enum statoVolo{
        DECOLLATO(1),
        PROGRAMMATO(2),
        INRITARDO(3),
        INORARIO(4),
        ATTERRATO(5),
        CANCELLATO(6);

        private final int codice;
        private statoVolo(int codice){
            this.codice = codice;
        }
        public int getStato(){
            return codice;
        }
    }

   public Volo(int codiceVolo, String compagniaAerea,float orarioArrivo, int ritardo,statoVolo stato, String aeroportoOrigine, String aeroportoDestinazione){
     this.codiceVolo = codiceVolo;
     this.compagniaAerea = compagniaAerea;
     this.orarioArrivo = orarioArrivo;
     this.ritardo = ritardo;
     this.stato = stato;
     this.aeroportoOrigine = aeroportoOrigine;
     this.aeroportoDestinazione = aeroportoDestinazione;
    }

    public Volo(int codiceVolo, String compagniaAerea,float orarioArrivo,statoVolo stato, String aeroportoOrigine, String aeroportoDestinazione){
        this.codiceVolo = codiceVolo;
        this.compagniaAerea = compagniaAerea;
        this.orarioArrivo = orarioArrivo;
        this.stato = stato;
        this.aeroportoOrigine = aeroportoOrigine;
        this.aeroportoDestinazione = aeroportoDestinazione;
    }

    @Override
    public String toString() {
        return "Volo " + codiceVolo + " da " + aeroportoOrigine + " a " + aeroportoDestinazione +
                " con arrivo previsto alle " + orarioArrivo + " (" + stato + ")";
    }



}
