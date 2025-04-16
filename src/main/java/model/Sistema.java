package model;
import java.util.ArrayList;

public class Sistema {
    public ArrayList<UtenteGenerico> utentiGenerici = new ArrayList<>();
    public ArrayList<Admin> tantiAdmin = new ArrayList<>();


    public Sistema(){}

    public void aggiungiUtenteGenerico(UtenteGenerico ug){
        utentiGenerici.add(ug);
    }
    public void aggiungiAdmin(Admin a){
        tantiAdmin.add(a);
    }

    public void aggiungiBiglietto(Prenotazione )

    public boolean verificaUtenteG(String login, String psw){
           for(int i = 0; i < utentiGenerici.size(); i++){
               if(login.equals(utentiGenerici.get(i).nomeUtente) && psw.equals(utentiGenerici.get(i).psw)){
                   System.out.println("Credenziali riconosciute, accesso consentito come utente generico");
                   return true;
               }
           }
        System.out.println("Accesso negato, nome utente o password errati");
        return false;
    }

    public boolean verificaAdmin(String login, String psw, String codAdmin){
        for(int i = 0; i < tantiAdmin.size(); i++){
            if(login.equals(tantiAdmin.get(i).nomeUtente) && psw.equals(tantiAdmin.get(i).psw) && codAdmin.equals(tantiAdmin.get(i).codAdmin)){
                System.out.println("Accesso consentito come admin");
                return true;
            }
        }
        System.out.println("Accesso negato, nome utente, password o codice admin errati");
        return false;
    }
}
