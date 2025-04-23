package model;

import java.util.ArrayList;

public class Admin  extends Utente{
    //Un admin in quanto tale riceve un codice di accesso univoco
    String codAdmin;


    public Admin(String log, String ps, String code){
        super(log,ps, "admin");
        codAdmin = code;
    }

    //implementazione dei metodi specifici per l'utente Admin
    public void aggiungiVoli(Volo voloDaInserire){
        voliGestiti.add(voloDaInserire);
    }

    public void assegnaGate(Gate e, VoloPartenza v){
       v.setGate(e);
       e.assegnaVolo(v);
       System.out.println("il gate "+ e.codiceGate +" è stato assegnato al volo "+ v.codiceVolo);
    }

    public void aggiornaDestinazioneVolo(int codiceV, String destinazione){
       for(Volo v : voliGestiti){
           if(v.codiceVolo == codiceV){
            v.setAeroportoDestinazione(destinazione);
           }
       }
        System.out.println("Volo non trovato");
    }

    public void aggiornaOrarioArrivo(int codiceV,float nuovoOrario){
        for(Volo v : voliGestiti){
            if(v.codiceVolo == codiceV){
            v.setOrarioArrivo(nuovoOrario);
            }
        }
        System.out.println("Volo non trovato");
    }

    public void aggiornaStatoVolo(int codiceV, Volo.statoVolo nuovoStato) {
        for (Volo v : voliGestiti) {
            if (v.codiceVolo == codiceV) {
                v.setStatoVolo(nuovoStato);

            }
        }
        System.out.println("Volo non trovato");
    }

    public void aggiornaTuttoVolo(int codiceV, String nuovaDestinazione, float nuovoOrario, Volo.statoVolo nuovoStato){
        for(Volo v : voliGestiti){
            if(v.codiceVolo == codiceV){
                v.setAeroportoDestinazione(nuovaDestinazione);
                v.setOrarioArrivo(nuovoOrario);
                v.setStatoVolo(nuovoStato);
                return;
            }
        }
        System.out.println("Volo non trovato");
    }

}
