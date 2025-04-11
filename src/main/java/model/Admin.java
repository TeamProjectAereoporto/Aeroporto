package model;

public class Admin  extends Utente{
    //Un admin in quanto tale riceve un codice di accesso univoco
    String codAdmin;

    Admin(String log, String ps, String code){
        super(log,ps);
        codAdmin = code;
    }

    //implementazione dei metodi specifici per l'utente
    public static void aggiungiVoli(){
        //la definizione del metodo sarà fatta quando saranno implementate tutte le classi
    }
    public static void aggiornaVoli(){

    }

    public static void assegnaGate(){

    }
}
