package model;

public class Utente {
    //attributi della superclasse Utente
    String nomeUtente;
    String psw;

    //costruttore di Utente
    Utente(String l, String p){
        nomeUtente = l;
        psw = p;
    }

    //metodo per visualizzare tutti i voli inseriti nel sistema che sarà comune a tutte le classi figlie di Utente
    public void visualizzaVoli(){
        //ancora da implementare
    }
}
