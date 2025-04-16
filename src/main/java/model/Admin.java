package model;

import java.util.ArrayList;

public class Admin  extends Utente{
    //Un admin in quanto tale riceve un codice di accesso univoco
    String codAdmin;

    Admin(String log, String ps, String code){
        super(log,ps);
        codAdmin = code;
    }

    //implementazione dei metodi specifici per l'utente Admin
    public void aggiungiVoli(Volo voloDaInserire){
        voliGestiti.add(voloDaInserire);
    }
    public void assegnaGate(Gate e, VoloPartenza v){
       v.setGate(e);
       e.assegnaVolo(v);
       System.out.println("il gate"+ e.codiceGate +" è stato assegnato al volo "+ v.codiceVolo);
    }
}
