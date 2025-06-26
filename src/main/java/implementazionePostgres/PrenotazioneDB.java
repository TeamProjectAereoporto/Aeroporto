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
    Connection connection = null;
    public PrenotazioneDB(){
        try{
            connection = ConnessioneDB.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addTicket(long numeroBiglietto, Passeggero passeggero, String postoAssegnato, Volo volo, String stato, Utente utente) {
        String sqlAddTicket = "INSERT INTO prenotazione (numero_biglietto, posto_assegnato,stato_prenotazione, numero_documento, id_utente, codice_volo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnessioneDB.getInstance().connection;
             PreparedStatement stmt = connection.prepareStatement(sqlAddTicket)){
            System.out.println("Connessione aperta? " + (connection != null && !connection.isClosed()));
            stmt.setLong(1, numeroBiglietto);
            stmt.setString(2, postoAssegnato);
            stmt.setString(3, stato.trim());
            stmt.setString(4, passeggero.getNumeroDocumento());
            stmt.setString(5, utente.getNomeUtente()  );
            stmt.setLong(6, volo.getCodiceVolo());
            System.out.println("DEBUG stato = [" + stato + "]");

            stmt.executeUpdate();
            System.out.println("Prenotazione inserita con successo.");

        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento della prenotazione: " + e.getMessage());
        }
    }

    public boolean deleteTicket(long numeroBiglietto) {
        String sqlDeleteTicket = "DELETE FROM prenotazione where numero_biglietto = ?";
        try(Connection connection = ConnessioneDB.getInstance().connection;
                PreparedStatement deleteTicket = connection.prepareStatement(sqlDeleteTicket)){
            deleteTicket.setLong(1,numeroBiglietto);
            int effectRows = deleteTicket.executeUpdate();
            if(effectRows>0){
                System.out.println("Prenotazione cancellata con successo");
                return true;
            }else{
                System.out.println("Si sono verificati dei problemi durante l'eliminazione");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    public void modifyTicket(String postoAssegnato, Passeggero passeggero) {

    }


    public void getTickets() {

    }
}

