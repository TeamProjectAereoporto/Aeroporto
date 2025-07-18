package implementazione_postgres;

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
import java.util.List;
import java.util.logging.Logger;

public class PrenotazioneDB implements PrenotazioneDAO {
    // Connessione al database
    Connection connection = null;
    VoloDB voloDB;
    PasseggeroDB passeggeroDB;
    UtenteDB utenteDB;
    Logger logger = Logger.getLogger(getClass().getName());
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
        try (Connection connAddTicket = ConnessioneDB.getInstance().connection;
             PreparedStatement stmt = connAddTicket.prepareStatement(sqlAddTicket)){

            // Impostazione parametri della query con i dati della prenotazione
            stmt.setLong(1, numeroBiglietto);
            stmt.setString(2, postoAssegnato);
            stmt.setString(3, stato.trim());
            stmt.setInt(4, id);
            stmt.setString(5, utente.getNomeUtente());
            stmt.setInt(6, volo.getCodiceVolo());

            // Debug per mostrare lo stato della prenotazione
            logger.info("DEBUG stato = [" + stato + "]");

            // Esecuzione dell'inserimento nel database
            stmt.executeUpdate();
            logger.info("Prenotazione inserita con successo.");

        } catch (SQLException e) {
            // Stampa eventuali errori durante l'inserimento
            logger.info("Errore durante l'inserimento della prenotazione: " + e.getMessage());
        }
    }

    // Metodo per cancellare una prenotazione tramite numero biglietto
    public boolean deleteTicket(long numeroBiglietto) {
        // Query SQL per cancellare la prenotazione dalla tabella
        String sqlDeleteTicket = "DELETE FROM prenotazione where numero_biglietto = ?";

        // Gestione sicura della connessione e statement
        try(Connection connDeleteTicket = ConnessioneDB.getInstance().connection;
            PreparedStatement deleteTicket = connDeleteTicket.prepareStatement(sqlDeleteTicket)){

            // Impostazione parametro per la query
            deleteTicket.setLong(1,numeroBiglietto);

            // Esecuzione della cancellazione
            int effectRows = deleteTicket.executeUpdate();

            // Controllo se la cancellazione ha avuto successo
            if(effectRows>0){
                logger.info("Prenotazione cancellata con successo");
                return true;
            }else{
                logger.info("Si sono verificati dei problemi durante l'eliminazione");
            }
        }catch (SQLException e){
            // Stampa eventuali errori SQL
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per modificare una prenotazione (non ancora implementato)
    public boolean modifyTicket(long numeroBiglietto, String nuovoPosto,String nome,String cognome, String nuovoNumeroDocumento) {
        String updatePostoSql = """
        UPDATE prenotazione
        SET posto_assegnato = ?
        WHERE numero_biglietto = ?
        """;

        String updatePasseggeroSql = """
        UPDATE passeggero
        SET numero_documento = ?,
            nome = ?,
            cognome = ?
        WHERE id_passeggero = (
            SELECT id_passeggero
            FROM prenotazione
            WHERE numero_biglietto = ?
        )
        """;

        try (Connection conn = ConnessioneDB.getInstance().connection) {
            conn.setAutoCommit(false); // Start transaction

            // 1. Aggiorna numero_documento del passeggero associato
            try (PreparedStatement stmt2 = conn.prepareStatement(updatePasseggeroSql)) {
                stmt2.setString(1, nuovoNumeroDocumento);
                stmt2.setString(2,nome);
                stmt2.setString(3,cognome);
                stmt2.setLong(4, numeroBiglietto);
                stmt2.executeUpdate();
            }
            // 2. Aggiorna posto_assegnato
            try (PreparedStatement stmt1 = conn.prepareStatement(updatePostoSql)) {
                stmt1.setString(1, nuovoPosto);
                stmt1.setLong(2, numeroBiglietto);
                stmt1.executeUpdate();
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                ConnessioneDB.getInstance().connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }


    // Metodo per recuperare tutte le prenotazioni
    @Override
    public ArrayList<Prenotazione> getTickets(String username, String nome, int codiceVolo) throws  SQLException {
        ArrayList<Prenotazione> biglietti = new ArrayList<>();
        String sqlGetTickets;
        if (codiceVolo == -1 && !nome.isEmpty()) {
            sqlGetTickets = "SELECT p.*\n" +
                    "FROM prenotazione p\n" +
                    "JOIN passeggero pa ON p.id_passeggero = pa.id_passeggero\n" +
                    "WHERE p.id_utente = ? AND pa.nome = ?;\n";

            try (Connection connGetTickets = ConnessioneDB.getInstance().connection;
                 PreparedStatement stmt = connGetTickets.prepareStatement(sqlGetTickets)) {
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
        } else if(codiceVolo != -1 && nome.isEmpty()){
            sqlGetTickets = "SELECT p.*\n" +
                    "FROM prenotazione p\n" +
                    "JOIN volo vo ON p.codice_volo = vo.codice_volo\n" +
                    "WHERE p.id_utente = ? AND vo.codice_volo = ?;\n";
            try (Connection connGetTickets = ConnessioneDB.getInstance().connection;
                 PreparedStatement stmt = connGetTickets.prepareStatement(sqlGetTickets)) {
                stmt.setString(1, username);
                stmt.setInt(2, codiceVolo);
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
            sqlGetTickets = "SELECT p.*\n" +
                    "FROM prenotazione p\n" +
                    "JOIN passeggero pa ON p.id_passeggero = pa.id_passeggero\n" +
                    "JOIN volo vo ON p.codice_volo = vo.codice_volo\n" +
                    "WHERE p.id_utente = ? AND vo.codice_volo = ? AND pa.nome = ?;";

            try (Connection connGetTickets = ConnessioneDB.getInstance().connection;
                 PreparedStatement stmt = connGetTickets.prepareStatement(sqlGetTickets)) {
                stmt.setString(1, username);
                stmt.setInt(2, codiceVolo);
                stmt.setString(3, nome);
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
        }
        return biglietti;
    }

    public ArrayList<Prenotazione> getTicketsByCodiceVolo(String username, String nome, int codiceVolo) throws  SQLException {
        ArrayList<Prenotazione> biglietti = new ArrayList<>();
        String sqlGetTickets;
        if (codiceVolo == -1 && !nome.isEmpty()) {
            sqlGetTickets = "SELECT p.*\n" +
                    "FROM prenotazione p\n" +
                    "JOIN passeggero pa ON p.id_passeggero = pa.id_passeggero\n" +
                    "WHERE p.id_utente = ? AND pa.nome = ?;\n";

            try (Connection connGetTickets = ConnessioneDB.getInstance().connection;
                 PreparedStatement stmt = connGetTickets.prepareStatement(sqlGetTickets)) {
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
        } else if(codiceVolo != -1 && nome.isEmpty()){
            sqlGetTickets = "SELECT p.*\n" +
                    "FROM prenotazione p\n" +
                    "JOIN volo vo ON p.codice_volo = vo.codice_volo\n" +
                    "WHERE p.id_utente = ? AND vo.codice_volo = ?;\n";
            try (Connection connGetTickets = ConnessioneDB.getInstance().connection;
                 PreparedStatement stmt = connGetTickets.prepareStatement(sqlGetTickets)) {
                stmt.setString(1, username);
                stmt.setInt(2, codiceVolo);
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
            sqlGetTickets = "SELECT p.*\n" +
                    "FROM prenotazione p\n" +
                    "JOIN passeggero pa ON p.id_passeggero = pa.id_passeggero\n" +
                    "JOIN volo vo ON p.codice_volo = vo.codice_volo\n" +
                    "WHERE p.id_utente = ? AND vo.codice_volo = ? AND pa.nome = ?;";

            try (Connection connGetTickets = ConnessioneDB.getInstance().connection;
                 PreparedStatement stmt = connGetTickets.prepareStatement(sqlGetTickets)) {
                stmt.setString(1, username);
                stmt.setInt(2, codiceVolo);
                stmt.setString(3, nome);
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
        }
        return biglietti;
    }
    public List<String> getPostiOccupatiPerVolo(int codiceVolo) throws SQLException {
        List<String> postiOccupati = new ArrayList<>();
        String sql = "SELECT posto_assegnato FROM prenotazione WHERE codice_volo = ?";

        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codiceVolo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                postiOccupati.add(rs.getString("posto_assegnato"));
            }
        }

        return postiOccupati;
    };

}