package implementazionePostgres;

import connessioneDB.ConnessioneDB;
import dao.PrenotazioneDAO;
import model.Passeggero;
import model.Utente;
import model.Volo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrenotazioneDB implements PrenotazioneDAO {
    // Connessione al database
    Connection connection = null;

    // Costruttore: ottiene l'istanza della connessione al database
    public PrenotazioneDB(){
        try{
            connection = ConnessioneDB.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodo per aggiungere una prenotazione (biglietto) nel database
    public void addTicket(long numeroBiglietto, Passeggero passeggero, String postoAssegnato, Volo volo, String stato, Utente utente) {
        // Query SQL per inserire una nuova prenotazione nella tabella "prenotazione"
        String sqlAddTicket = "INSERT INTO prenotazione (numero_biglietto, posto_assegnato,stato_prenotazione, numero_documento, id_utente, codice_volo) VALUES (?, ?, ?, ?, ?, ?)";

        // Uso di try-with-resources per la gestione sicura di connessione e statement
        try (Connection connection = ConnessioneDB.getInstance().connection;
             PreparedStatement stmt = connection.prepareStatement(sqlAddTicket)){

            // Debug per verificare se la connessione è aperta
            System.out.println("Connessione aperta? " + (connection != null && !connection.isClosed()));

            // Impostazione parametri della query con i dati della prenotazione
            stmt.setLong(1, numeroBiglietto);
            stmt.setString(2, postoAssegnato);
            stmt.setString(3, stato.trim());
            stmt.setString(4, passeggero.getNumeroDocumento());
            stmt.setString(5, utente.getNomeUtente());
            stmt.setLong(6, volo.getCodiceVolo());

            // Debug per mostrare lo stato della prenotazione
            System.out.println("DEBUG stato = [" + stato + "]");

            // Esecuzione dell'inserimento nel database
            stmt.executeUpdate();
            System.out.println("Prenotazione inserita con successo.");

        } catch (SQLException e) {
            // Stampa eventuali errori durante l'inserimento
            System.err.println("Errore durante l'inserimento della prenotazione: " + e.getMessage());
        }
    }

    // Metodo per cancellare una prenotazione tramite numero biglietto
    public boolean deleteTicket(long numeroBiglietto) {
        // Query SQL per cancellare la prenotazione dalla tabella
        String sqlDeleteTicket = "DELETE FROM prenotazione where numero_biglietto = ?";

        // Gestione sicura della connessione e statement
        try(Connection connection = ConnessioneDB.getInstance().connection;
            PreparedStatement deleteTicket = connection.prepareStatement(sqlDeleteTicket)){

            // Impostazione parametro per la query
            deleteTicket.setLong(1,numeroBiglietto);

            // Esecuzione della cancellazione
            int effectRows = deleteTicket.executeUpdate();

            // Controllo se la cancellazione ha avuto successo
            if(effectRows>0){
                System.out.println("Prenotazione cancellata con successo");
                return true;
            }else{
                System.out.println("Si sono verificati dei problemi durante l'eliminazione");
            }
        }catch (SQLException e){
            // Stampa eventuali errori SQL
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per modificare una prenotazione (non ancora implementato)
    public void modifyTicket(String postoAssegnato, Passeggero passeggero) {
        //da fare
    }

    // Metodo per recuperare tutte le prenotazioni (non ancora implementato)
    public void getTickets() {
        //da fare
    }
}
