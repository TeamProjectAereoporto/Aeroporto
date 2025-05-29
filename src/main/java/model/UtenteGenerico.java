package model;

import java.util.ArrayList;
import java.util.logging.Logger;

//questa classe è un'estensione della superclasse Utente
public class UtenteGenerico extends Utente {
    public static ArrayList<Prenotazione> bigliettiAcquistati;
    private static final Logger logger = Logger.getLogger(UtenteGenerico.class.getName());
    //costruttore per l'utente generico.
    public UtenteGenerico(String l, String p){
        super(l,p);
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
            return bigliettiTrovati;
        }
        for (Prenotazione p : bigliettiAcquistati) {
            boolean nomeMatch = !nome.isEmpty() && p.getPasseggero().getNome().equalsIgnoreCase(nome.trim());
            boolean numeroMatch = codiceVolo != -1 && p.getVolo().getCodiceVolo() == codiceVolo;
            if ((nomeMatch && numeroMatch) || (nomeMatch && codiceVolo == -1) || (numeroMatch && nome.isEmpty())) {
                logger.info("ho trovato il biglietto\n");
                bigliettiTrovati.add(p);
            }
        }
        if (bigliettiTrovati.isEmpty()) {
            logger.info("Nessun biglietto trovato");
        }
        return bigliettiTrovati;
    }

}
