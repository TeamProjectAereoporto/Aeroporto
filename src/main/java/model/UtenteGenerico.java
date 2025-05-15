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
    //ricerca per numero biglietto e nome
    public ArrayList<Prenotazione> cercaBiglietto(String nome, long numeroBiglietto, UtenteGenerico acquirente) {
        ArrayList<Prenotazione> bigliettiTrovati = new ArrayList<>();
        Prenotazione biglietto = new Prenotazione(21312324, "A5", Prenotazione.StatoPrenotazione.CONFERMATA, new Passeggero("nome", "cognome", "SDAAS2"));
        Prenotazione biglietto1 = new Prenotazione(33333333, "A5", Prenotazione.StatoPrenotazione.CONFERMATA, new Passeggero("nome", "cognome", "SDAAS2"));

        biglietti.add(biglietto);
        biglietti.add(biglietto1);
        // Verifica che stai cercando tra i tuoi stessi biglietti
        if (acquirente == this) {
            if (nome.isEmpty() && numeroBiglietto == -1) {
                System.out.println("è entrato nel primo if");
                return null;
            }

            for (Prenotazione p : biglietti) {
                boolean nomeMatch = !nome.isEmpty() && p.getPasseggero().getNome().equalsIgnoreCase(nome);
                boolean numeroMatch = numeroBiglietto != -1 && p.getNumeroBiglietto() == numeroBiglietto;

                if ((nomeMatch && numeroMatch) || (nomeMatch && numeroBiglietto == -1) || (numeroMatch && nome.isEmpty())) {
                    bigliettiTrovati.add(p);
                }
            }

            if (bigliettiTrovati.isEmpty()) {
                System.out.println("Nessun biglietto trovato");
            }
        }

        return bigliettiTrovati;
    }

}