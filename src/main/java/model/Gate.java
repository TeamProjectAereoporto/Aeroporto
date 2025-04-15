package model;

import java.util.ArrayList;

public class Gate {
    String codiceGate;
    VoloPartenza voloAssegnato;
    //il codice univoco del volo e l'attributo "volo assegnato" devono corrispondere. Dunque, quando verrà implementato il metodo per
    //assegnare un volo ad un gate sarà importante inizializzare questo attributo con il codice univoco del volo a cui viene assegnato.

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

    public String getCodiceGate(){
        return codiceGate;
    }



}
