package model;
import java.util.ArrayList;
//questa classe è un'estensione della superclasse Utente
public class UtenteGenerico extends Utente {

    //costruttore per l'utente generico.
    public UtenteGenerico(String l, String p){
        super(l,p, "utenteGenerico");
    }
    //metodo per acquistare un biglietto
    public void prenotaVolo(ArrayList<Prenotazione> biglietti, int numeroBiglietto, String postoAssegnato){
        Prenotazione biglietto = new Prenotazione(numeroBiglietto, postoAssegnato, Prenotazione.StatoPrenotazione.CONFERMATA);
        biglietti.add(biglietto);
    }
    public void modificaBiglietto(Prenotazione biglietto, String postoAssegnato, int stato){
        biglietto.postoAssegnato=postoAssegnato;
        if(stato==1){
            biglietto.stato=Prenotazione.StatoPrenotazione.CONFERMATA;
        }else{
            biglietto.stato=Prenotazione.StatoPrenotazione.IN_ATTESA;
        }
    }
    public Prenotazione cercaBiglietto(ArrayList<Prenotazione> biglietti, int numBiglietto, String numDocumento){
        for(Prenotazione p: biglietti){
            if(p.numeroBiglietto==numBiglietto){
                return p;
            }
        }
        return null;
    }
}
