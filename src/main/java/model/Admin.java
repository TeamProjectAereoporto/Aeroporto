package model;

import java.util.ArrayList;

public class Admin  extends Utente{
    //Un admin in quanto tale riceve un codice di accesso univoco
    private String codAdmin;

    public Admin(String log, String ps, String code){
        super(log,ps, "admin");
        codAdmin = code;
    }

    //implementazione dei metodi specifici per l'utente Admin
    public void aggiungiVoli(Volo voloDaInserire){
        System.out.println("volo aggiunto");
        voliGestiti.add(voloDaInserire);
    }

    public void assegnaGate(Gate e, VoloPartenza v){
        v.setGate(e.getCodiceGate());
        e.assegnaVolo(v);
        System.out.println("il gate "+ e.getCodiceGate() +" è stato assegnato al volo "+ v.getCodiceVolo());
    }
    public String getCodAdmin(){
        return codAdmin;
    }
    public void setCodAdmin(String codAdmin){
        this.codAdmin=codAdmin;
    }
}