package model;

import java.util.ArrayList;

public class Utente {
    //attributi della superclasse Utente
    private String nomeUtente;
    private String psw;
    private String ruolo;
    protected static ArrayList<Volo> voliGestiti;

    //costruttore di Utente
    public Utente(String l, String p, String ruolo){
        nomeUtente = l;
        psw = p;
        this.ruolo=ruolo;
        voliGestiti = new ArrayList<>();
    }
    //metodo per visualizzare tutti i voli inseriti nel sistema che sarà comune a tutte le classi figlie di Utente

    public ArrayList<Volo> visualizzaVoli(){
        System.out.println("visualizza");
        return voliGestiti;
    }
    //getter
    public String getNomeUtente() {
        return nomeUtente;
    }

    public String getPsw() {
        return psw;
    }

    public String getRuolo() {
        return ruolo;
    }
    //setter

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public ArrayList getVoliGestiti(){
        return voliGestiti;
    }
}