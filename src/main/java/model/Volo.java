package model;


public class Volo {
    private int codiceVolo;
    private String compagniaAerea;
    private String aeroportoOrigine;
    private String aeroportoDestinazione;
    private String orarioArrivo;
    private int ritardo; //se lo stato del volo è indicato come in ritardo, allora qui verrà salvato il tempo di ritardo in minuti/ore.
    private statoVolo stato;
    private String gate;

    public enum statoVolo{
        DECOLLATO(1),
        PROGRAMMATO(2),
        INRITARDO(3),
        INORARIO(4),
        ATTERRATO( 5),
        CANCELLATO(6);

        private final int codice;
        statoVolo(int codice){
            this.codice = codice;
        }
        public int getStato(){
            return codice;
        }
    }
    //getter


    public int getCodiceVolo() {
        return codiceVolo;
    }

    public String getOrarioArrivo() {
        return orarioArrivo;
    }

    public int getRitardo() {
        return ritardo;
    }

    public statoVolo getStato() {
        return stato;
    }

    public String getAeroportoDestinazione() {
        return aeroportoDestinazione;
    }

    public String getAeroportoOrigine() {
        return aeroportoOrigine;
    }

    public String getCompagniaAerea() {
        return compagniaAerea;
    }
    public String getGate(){
        return gate;
    }
    //setter
    public void setAeroportoDestinazione(String aeroportoDestinazione) {
        this.aeroportoDestinazione = aeroportoDestinazione;
    }

    public void setAeroportoOrigine(String aeroportoOrigine) {
        this.aeroportoOrigine = aeroportoOrigine;
    }

    public void setCodiceVolo(int codiceVolo) {
        this.codiceVolo = codiceVolo;
    }

    public void setCompagniaAerea(String compagniaAerea) {
        this.compagniaAerea = compagniaAerea;
    }

    public void setOrarioArrivo(String orarioArrivo) {
        this.orarioArrivo = orarioArrivo;
    }

    public void setRitardo(int ritardo) {
        this.ritardo = ritardo;
    }

    public void setStato(statoVolo stato) {
        this.stato = stato;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public Volo(int codiceVolo, String compagniaAerea, String aeroportoOrigine, String aeroportoDestinazione,
                String orarioArrivo, int ritardo, statoVolo stato, String gate) {
        this.codiceVolo = codiceVolo;
        this.compagniaAerea = compagniaAerea;
        this.aeroportoOrigine = aeroportoOrigine;
        this.aeroportoDestinazione = aeroportoDestinazione;
        this.orarioArrivo = orarioArrivo;
        this.ritardo = ritardo;
        this.stato = stato;
        this.gate = gate;
    }

    public Volo(int codiceVolo, String compagniaAerea, String aeroportoOrigine, String aeroportoDestinazione,
                String orarioArrivo, statoVolo stato, String gate) {
        this(codiceVolo, compagniaAerea, aeroportoOrigine, aeroportoDestinazione,
                orarioArrivo, 0, stato, gate);
    }

    @Override
    public String toString() {
        return "Volo " + codiceVolo + " da " + aeroportoOrigine + " a " + aeroportoDestinazione +
                " con arrivo previsto alle " + orarioArrivo + " (" + stato + ")" + "al gate " + gate;
    }
}