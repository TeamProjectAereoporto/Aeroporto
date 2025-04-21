package model;

import java.util.ArrayList;

public class Utente {
    //attributi della superclasse Utente
    public String nomeUtente;
    public String psw;
    public String ruolo;
    public ArrayList<Volo> voliGestiti = new ArrayList<>();

    //costruttore di Utente
    public Utente(String l, String p, String ruolo){
        nomeUtente = l;
        psw = p;
        this.ruolo=ruolo;
    }
    //metodo per visualizzare tutti i voli inseriti nel sistema che sarà comune a tutte le classi figlie di Utente
    public void visualizzaVoli(){
        for(Volo v : voliGestiti){
            System.out.println(v);
        }
    }
}