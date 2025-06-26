package model;

import java.util.ArrayList;

public class Gate {

    // Codice identificativo univoco del gate
    private final String codiceGate;

    // Volo di partenza attualmente assegnato a questo gate
    private VoloPartenza voloAssegnato;

    // Costruttore: inizializza il gate con il codice e senza alcun volo assegnato
    public Gate(String coGate){
        codiceGate = coGate;
        voloAssegnato = null;
    }

    // Assegna un volo di partenza a questo gate
    public void assegnaVolo(VoloPartenza v){
        voloAssegnato = v;
    }

    // Restituisce il volo di partenza attualmente assegnato al gate (null se nessuno)
    public VoloPartenza getVolo(){
        return voloAssegnato;
    }

    // Restituisce il codice identificativo del gate
    public String getCodiceGate(){
        return codiceGate;
    }
}
