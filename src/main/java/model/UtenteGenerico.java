package model;
import java.util.ArrayList;
//questa classe è un'estensione della superclasse Utente
public class UtenteGenerico extends Utente {
    public ArrayList<Prenotazione> biglietti;
    //costruttore per l'utente generico.
    public UtenteGenerico(String l, String p){
        super(l,p, "utenteGenerico");
        biglietti = new ArrayList<>();
    }

    //metodo per acquistare un biglietto
    public void prenotaVolo(long numeroBiglietto, String postoAssegnato){
        Prenotazione biglietto = new Prenotazione(numeroBiglietto, postoAssegnato, Prenotazione.StatoPrenotazione.CONFERMATA, new Passeggero("karol", "leonardi", "NKOPOP"));
        biglietti.add(biglietto);
    }
    public void modificaBiglietto(Prenotazione biglietto, String postoAssegnato, int stato){
        biglietto.setPostoAssegnato(postoAssegnato);
        if(stato==1){
            biglietto.setStato(Prenotazione.StatoPrenotazione.CONFERMATA);
        }else{
            biglietto.setStato(Prenotazione.StatoPrenotazione.IN_ATTESA);
        }
    }
    //ricerca per numero biglietto
    public Prenotazione cercaBiglietto(long numBiglietto){
        for(Prenotazione p: biglietti){
            if(p.getNumeroBiglietto()==numBiglietto){
                return p;
            }
        }
        return null;
    }
    //ricerca per nome
    public ArrayList<Prenotazione> cercaBiglietto(String nome){
        ArrayList<Prenotazione> bigliettiTrovati = new ArrayList<>();
        for(Prenotazione p : biglietti){
            if(p.getPasseggero().getNome()==nome){
                bigliettiTrovati.add(p);
            }
        }
        return bigliettiTrovati;
    }
}