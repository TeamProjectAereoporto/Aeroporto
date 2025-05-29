package model;

import java.util.logging.Logger;

public class Admin  extends Utente{
    //Un admin in quanto tale riceve un codice di accesso univoco
    private String codAdmin;
    private static final Logger logger = Logger.getLogger(Admin.class.getName());

    public Admin(String log, String ps, String code){
        super(log,ps);
        codAdmin = code;
    }

    //implementazione dei metodi specifici per l'utente Admin
    public void aggiungiVoli(Volo voloDaInserire){
        logger.info("volo aggiunto");
        voliGestiti.add(voloDaInserire);
    }

    public void assegnaGate(Gate e, VoloPartenza v){
        v.setGate(e.getCodiceGate());
        e.assegnaVolo(v);
        logger.info("il gate "+ e.getCodiceGate() +" è stato assegnato al volo "+ v.getCodiceVolo());
    }
    public String getCodAdmin(){
        return codAdmin;
    }
    public void setCodAdmin(String codAdmin){
        this.codAdmin=codAdmin;
    }
}