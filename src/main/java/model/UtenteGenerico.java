package model;
import gui.Prenota;

import java.util.ArrayList;
//questa classe è un'estensione della superclasse Utente
public class UtenteGenerico extends Utente {
    public ArrayList<Prenotazione> bigliettiAcquistati;
    //costruttore per l'utente generico.
    public UtenteGenerico(String l, String p){
        super(l,p, "utenteGenerico");
        bigliettiAcquistati = new ArrayList<>();
    }
    //metodo per acquistare un biglietto
    public void prenotaVolo(Prenotazione biglietto){
        bigliettiAcquistati.add(biglietto);
    }
    public void modificaBiglietto(Prenotazione biglietto, String postoAssegnato, int stato){
        biglietto.setPostoAssegnato(postoAssegnato);
        if(stato==1){
            biglietto.setStato(Prenotazione.StatoPrenotazione.CONFERMATA);
        }else{
            biglietto.setStato(Prenotazione.StatoPrenotazione.IN_ATTESA);
        }
    }

    //ricerca per numero biglietto e nome
    public ArrayList<Prenotazione> cercaBiglietto(String nome, int codiceVolo) {
        ArrayList<Prenotazione> bigliettiTrovati = new ArrayList<>();
            if (nome.isEmpty() && codiceVolo == -1) {
                return null;
            }
            for (Prenotazione p : bigliettiAcquistati) {
                boolean nomeMatch = !nome.isEmpty() && p.getPasseggero().getNome().equalsIgnoreCase(nome);
                boolean numeroMatch = codiceVolo != -1 && p.getVolo().getCodiceVolo() == codiceVolo;
                System.out.println(p);
                if ((nomeMatch && numeroMatch) || (nomeMatch && codiceVolo == -1) || (numeroMatch && nome.isEmpty())) {
                    System.out.println("sono dentro\n");
                    bigliettiTrovati.add(p);
                }
            }
            if (bigliettiTrovati.isEmpty()) {
                System.out.println("Nessun biglietto trovato");
            }
        return bigliettiTrovati;
    }

}