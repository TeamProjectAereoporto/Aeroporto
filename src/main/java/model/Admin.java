package model;

import java.util.logging.Logger;

public class Admin  extends Utente{
    private String ruolo;
    private static final Logger logger = Logger.getLogger(Admin.class.getName());

    public Admin(String log, String ps, int ruolo){
        super(log,ps, 2);
    }

    //implementazione dei metodi specifici per l'utente Admin
    // Metodo per aggiungere un volo alla lista gestita dall'admin
    public void aggiungiVoli(Volo voloDaInserire){
        logger.info("volo aggiunto");
        voliGestiti.add(voloDaInserire);
    }
    // Metodo per assegnare un gate a un volo di partenza
    public void assegnaGate(Gate e, VoloPartenza v){
        v.setGate(e.getCodiceGate());
        e.assegnaVolo(v);
        logger.info("il gate "+ e.getCodiceGate() +" è stato assegnato al volo "+ v.getCodiceVolo());
    }
}
