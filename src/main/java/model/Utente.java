package model;

import java.util.ArrayList;

public class Utente {
    //attributi della superclasse Utente
    String nomeUtente;
    String psw;
    public ArrayList<Volo> voli = new ArrayList<>();

    //costruttore di Utente
    public Utente(String l, String p) {
        nomeUtente = l;
        psw = p;
    }

    public void leggiVoli() {
        for (Volo v : voli) {
            System.out.println(v);
        }
    }
}
