package model;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Utente {
    //attributi della superclasse Utente
    private String nomeUtente;
    private String psw;
    protected static ArrayList<Volo> voliGestiti;

    //costruttore di Utente
    public Utente(String l, String p){
        nomeUtente = l;
        psw = p;
        voliGestiti = new ArrayList<>();
    }
    //metodo per visualizzare tutti i voli inseriti nel sistema che sarà comune a tutte le classi figlie di Utente

    public ArrayList<Volo> visualizzaVoli() {
        return voliGestiti;
    }

    //getter
    public String getNomeUtente() {
        return nomeUtente;
    }

    public String getPsw() {
        return psw;
    }

    //setter

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }
}