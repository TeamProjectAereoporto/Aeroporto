package model;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Utente {
    //attributi della superclasse Utente
    private String nomeUtente;
    private String psw;
    protected static ArrayList<Volo> voliGestiti;
    private int ruolo;

    //costruttore di Utente
    public Utente(String l, String p, int ruolo){
        this.nomeUtente = l;
        this.psw = p;
        this.ruolo = ruolo;
        voliGestiti = new ArrayList<>();
    }

    public ArrayList<Volo> visualizzaVoli() {
        return voliGestiti;
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public String getPsw() {
        return psw;
    }

    public int getRuolo(){
        return ruolo;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }
}
