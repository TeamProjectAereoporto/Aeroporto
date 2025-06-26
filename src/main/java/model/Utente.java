package model;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Utente {
    // Attributo che memorizza il nome utente
    private String nomeUtente;

    // Attributo che memorizza la password dell'utente
    private String psw;

    // Lista statica dei voli gestiti da tutti gli utenti di questa classe
    protected static ArrayList<Volo> voliGestiti;

    // Ruolo dell'utente (es. amministratore, utente normale, ecc.)
    private int ruolo;

    // Costruttore della classe Utente che inizializza nome utente, password e ruolo
    public Utente(String l, String p, int ruolo){
        this.nomeUtente = l;
        this.psw = p;
        this.ruolo = ruolo;
        // Inizializza la lista dei voli gestiti (statica condivisa)
        voliGestiti = new ArrayList<>();
    }

    // Metodo per visualizzare la lista dei voli gestiti
    public ArrayList<Volo> visualizzaVoli() {
        return voliGestiti;
    }

    // Getter per il nome utente
    public String getNomeUtente() {
        return nomeUtente;
    }

    // Getter per la password
    public String getPsw() {
        return psw;
    }

    // Getter per il ruolo dell'utente
    public int getRuolo(){
        return ruolo;
    }

    // Setter per il nome utente
    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    // Setter per la password
    public void setPsw(String psw) {
        this.psw = psw;
    }
}
