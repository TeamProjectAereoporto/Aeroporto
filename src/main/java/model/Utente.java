package model;

import java.util.ArrayList;
import java.util.Date;

public class Utente {
    //attributi della superclasse Utente
    private String nomeUtente;
    private String psw;
    private String ruolo;
    protected ArrayList<Volo> voliGestiti;

    //costruttore di Utente
    public Utente(String l, String p, String ruolo){
        nomeUtente = l;
        psw = p;
        this.ruolo=ruolo;
        voliGestiti = new ArrayList<>();
    }
    //metodo per visualizzare tutti i voli inseriti nel sistema che sarà comune a tutte le classi figlie di Utente
    public ArrayList<Volo> visualizzaVoli(){
        Date orarioArrivo = new Date(); // ora attuale, oppure puoi usare un altro costruttore per una data specifica
        Volo volo1 = new Volo(
                12345,                   // codiceVolo
                "Alitalia",                        // compagniaAerea
                orarioArrivo,                      // orarioArrivo
                15,                                // ritardo in minuti
                Volo.statoVolo.PROGRAMMATO,         // stato del volo (enum)
                "Fiumicino",                       // aeroporto di origine
                "Linate"                           // aeroporto di destinazione
        );
        voliGestiti.add(volo1);
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