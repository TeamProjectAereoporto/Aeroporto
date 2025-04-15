package model;

import java.util.ArrayList;

public class Admin  extends Utente {
    //Un admin in quanto tale riceve un codice di accesso univoco
    String codAdmin;


    public Admin(String log, String ps, String code) {
        super(log, ps);
        codAdmin = code;
    }

    //implementazione dei metodi specifici per l'utente Admin
    public void aggiungiVoli(Volo voloDaInserire) {
        voli.add(voloDaInserire);
    }

    public void leggiVoli() {
        for (Volo v : voli) {
            System.out.println(v);
        }
    }

        public void assegnaGate (Gate e, VoloPartenza v){
            v.setGate(e);
            e.assegnaVolo(v);
            System.out.println("il gate" + e.codiceGate + " è stato assegnato al volo " + v.codiceVolo);
        }
    }
