package model;

import java.util.ArrayList;

public class Utente {
    //attributi della superclasse Utente
    private String nomeUtente;
    private String psw;
    private String ruolo;
    public ArrayList<Volo> voliGestiti = new ArrayList<>();

    //costruttore di Utente
    public Utente(String l, String p, String ruolo){
        nomeUtente = l;
        psw = p;
        this.ruolo=ruolo;
    }
    //metodo per visualizzare tutti i voli inseriti nel sistema che sarà comune a tutte le classi figlie di Utente
    public ArrayList<Volo> visualizzaVoli(){
        Volo volo1 = new Volo(4679, "ItaAirways", 14, Volo.statoVolo.INORARIO,"napoli", "Malpensa" );
        Volo volo2 = new Volo(4679, "ItaAirways", 14, Volo.statoVolo.INORARIO,"napoli", "Malpensa" );
        Volo volo3 = new Volo(4679, "ItaAirways", 14, Volo.statoVolo.INORARIO,"napoli", "Malpensa" );
        voliGestiti.add(volo1);
        voliGestiti.add(volo2);
        voliGestiti.add(volo3);

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
}