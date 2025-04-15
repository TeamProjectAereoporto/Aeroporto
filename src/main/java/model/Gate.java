package model;

import java.util.ArrayList;

public class Gate {
    String codiceGate;
    VoloPartenza voloAssegnato;

    Gate(String coGate){
        codiceGate = coGate;
        voloAssegnato = null;
    }

    public void assegnaVolo(VoloPartenza v){
        voloAssegnato = v;
    }

    public VoloPartenza getVolo(){
        return voloAssegnato;
    }

    public String getCodiceGate( ){
        return codiceGate;
    }
}
