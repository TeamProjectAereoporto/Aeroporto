package model;

public class Gate {
    String codiceGate;
    int voloAssegnato;
    //il codice univoco del volo e l'attributo "volo assegnato" devono corrispondere. Dunque, quando verrà implementato il metodo per
    //assegnare un volo ad un gate sarà importante inizializzare questo attributo con il codice univoco del volo a cui viene assegnato.

    Gate(String coGate, int voloAss){
        codiceGate = coGate;
        voloAssegnato = voloAss;
    }
}
