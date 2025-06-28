package implementazionePostgres;

import connessioneDB.ConnessioneDB;
import dao.PrenotazioneDAO;
import model.Prenotazione;
import model.Utente;
import model.Volo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrenotazioneDB implements PrenotazioneDAO {
    // Connessione al database
    Connection connection = null;
    VoloDB voloDB;
    PasseggeroDB passeggeroDB;
    UtenteDB utenteDB;

    // Costruttore: ottiene l'istanza della connessione al database
    public PrenotazioneDB(){
        try{
            connection = ConnessioneDB.getInstance().connection;
            voloDB = new VoloDB();
            passeggeroDB = new PasseggeroDB();
            utenteDB = new UtenteDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodo per aggiungere una prenotazione (biglietto) nel database
    public void addTicket(long numeroBiglietto, int id, String postoAssegnato, Volo volo, String stato, Utente utente) {
        // Query SQL per inserire una nuova prenotazione nella tabella "prenotazione"
        String sqlAddTicket = "INSERT INTO prenotazione (numero_biglietto, posto_assegnato,stato_prenotazione, id_passeggero, id_utente, codice_volo) VALUES (?, ?, ?, ?, ?, ?)";

        // Uso di try-with-resources per la gestione sicura di connessione e statement
        try (Connection connection = ConnessioneDB.getInstance().connection;
             PreparedStatement stmt = connection.prepareStatement(sqlAddTicket)){

            // Debug per verificare se la connessione è aperta
            System.out.println("Connessione aperta? " + (connection != null && !connection.isClosed()));

            // Impostazione parametri della query con i dati della prenotazione
            stmt.setLong(1, numeroBiglietto);
            stmt.setString(2, postoAssegnato);
            stmt.setString(3, stato.trim());
            stmt.setInt(4, id);
            stmt.setString(5, utente.getNomeUtente());
            stmt.setInt(6, volo.getCodiceVolo());

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
    public boolean modifyTicket(long numeroBiglietto, String postoAssegnato, String cdf) {
        String sqlModifyTickt = "UPDATE prenotazione\n" +
                "SET posto_assegnato = ?,\n"+
                "numero_documento = ?\n" +
                "WHERE numero_biglietto = ?";
        try(Connection connection = ConnessioneDB.getInstance().connection;
            PreparedStatement deleteTicket = connection.prepareStatement(sqlModifyTickt)){
            deleteTicket.setString(1,postoAssegnato);
            deleteTicket.setString(2, cdf);
            deleteTicket.setLong(3,numeroBiglietto);
            int i = deleteTicket.executeUpdate();
            if(i>0){
                System.out.println("Righe modificate: "+i);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return false;
    }

    // Metodo per recuperare tutte le prenotazioni
    public ArrayList<Prenotazione> getTickets(String username, String nome, int codiceVolo) throws  SQLException{
        ArrayList<Prenotazione> biglietti = new ArrayList<>();
        if(codiceVolo == 0 || !nome.isEmpty()) {
            String sqlGetTickets = "SELECT p.*\n" +
                    "FROM prenotazione p\n" +
                    "JOIN passeggero pa ON p.id_passeggero = pa.id_passeggero\n" +
                    "WHERE p.id_utente = ? AND pa.nome = ?;\n";

            try (Connection connection = ConnessioneDB.getInstance().connection;
                 PreparedStatement stmt = connection.prepareStatement(sqlGetTickets)) {
                stmt.setString(1, username);
                stmt.setString(2, nome);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Prenotazione p = new Prenotazione();
                    p.setNumeroBiglietto(rs.getLong("numero_biglietto"));
                    p.setStato(Prenotazione.StatoPrenotazione.valueOf(rs.getString("stato_prenotazione")));
                    p.setPostoAssegnato(rs.getString("posto_assegnato"));
                    p.setVolo(voloDB.getVolo(rs.getInt("codice_volo")));
                    p.setPasseggero(passeggeroDB.getPasseggero(rs.getInt("id_passeggero")));
                    p.setAcquirente(utenteDB.getUtenteByUsername(rs.getString("id_utente")));
                    biglietti.add(p);
                }
            }
        }else{

        }
        return biglietti;
    }
}
