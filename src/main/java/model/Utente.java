package model;

import java.util.ArrayList;

public class Utente {
    //attributi della superclasse Utente
    String nomeUtente;
    String psw;
    public ArrayList<Volo> voliGestiti = new ArrayList<>();

    //costruttore di Utente
    Utente(String l, String p){
        nomeUtente = l;
        psw = p;
    }
    //metodo per visualizzare tutti i voli inseriti nel sistema che sarà comune a tutte le classi figlie di Utente
    public void visualizzaVoli(){
        for(Volo v : voliGestiti){
            System.out.println(v);
        }
    }
}
