package model;

public class Volo {
    int codiceVolo;
    String compagniaAerea;
    String aeroportoOrigine;
    String aeroportoDestinazione;
    int orarioArrivo;
    int ritardo; //non so se cancellarlo in quanto è presente anche come enum ed è considerato nello stato del volo.

    public enum statoVolo{
        DECOLLATO(1),
        PROGRAMMATO(2),
        INRITARDO(3),
        ATTERRATO(4),
        CANCELLATO(5)

        private final int codice;
        private statoVolo(int codice){
            this.codice = codice;
        }
        public int getStato(){
            return codice;
        }
    }

    Volo()
}
