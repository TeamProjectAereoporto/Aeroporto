package implementazione_postgres;

import connessioneDB.ConnessioneDB;
import dao.VoloDao;
import model.Volo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Logger;

public class VoloDB implements VoloDao {

    Logger logger = Logger.getLogger(getClass().getName());
    // Connessione condivisa
    private Connection connection;


    public VoloDB() {
        try {
            connection = ConnessioneDB.getInstance().connection;
        } catch (SQLException e) {
            logger.info("Errore nella connessione al DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Metodo per ottenere voli in partenza (Capodichino = origine)
    public ArrayList<Volo> getVoliInPartenza() throws SQLException {
        String sql = "SELECT * FROM volo WHERE aeroporto_origine = 'Capodichino'";
        return getVoliByQuery(sql);
    }

    // Metodo per ottenere voli in arrivo (Capodichino = destinazione)
    public ArrayList<Volo> getVoliInArrivo() throws SQLException {
        String sql = "SELECT * FROM volo WHERE aeroporto_destinazione = 'Capodichino'";
        return getVoliByQuery(sql);
    }

    // Metodo helper per eseguire query e costruire lista voli
    private ArrayList<Volo> getVoliByQuery(String sql) throws SQLException {
        ArrayList<Volo> listaVoli = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Volo volo = new Volo(
                        rs.getInt("codice_volo"),
                        rs.getString("compagnia_aerea"),
                        rs.getString("aeroporto_origine"),
                        rs.getString("aeroporto_destinazione"),
                        rs.getString("orario_partenza_arrivo"),
                        rs.getInt("ritardo"),
                        Volo.statoVolo.valueOf(rs.getString("stato")),
                        rs.getString("id_gate"),
                        rs.getDate("data_volo") != null ? rs.getDate("data_volo").toLocalDate() : null
                );
                listaVoli.add(volo);
            }
        } catch (SQLException e) {
            logger.info("Errore durante il recupero dei voli: " + e.getMessage());
        }
        return listaVoli;
    }
    // Inserisce un volo nel DB
    @Override
    public void aggiungiVoloDB(Volo volo) throws SQLException {
        String sql = "INSERT INTO volo (codice_volo, compagnia_aerea, aeroporto_origine, aeroporto_destinazione, orario_partenza_arrivo, ritardo, stato, id_gate, data_volo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, volo.getCodiceVolo());
            ps.setString(2, volo.getCompagniaAerea());
            ps.setString(3, volo.getAeroportoOrigine());
            ps.setString(4, volo.getAeroportoDestinazione());
            ps.setString(5, volo.getOrarioArrivo());
            ps.setInt(6, volo.getRitardo());
            ps.setString(7, volo.getStato().toString());
            ps.setString(8, volo.getGate());
            ps.setDate(9, java.sql.Date.valueOf(volo.getDataVolo()));

            int righeInserite = ps.executeUpdate();
            logger.info("Numero di righe aggiunte: " + righeInserite);

        } catch (SQLException e) {
            logger.info("Errore durante l'aggiunta del volo: " + e.getMessage());}
    }

    // Modifica un volo esistente nel DB
    @Override
    public void modificaVoloDB(Volo volo) throws SQLException {
        String sql = "UPDATE volo SET compagnia_aerea = ?, aeroporto_origine = ?, aeroporto_destinazione = ?, orario_partenza_arrivo = ?, ritardo = ?, stato = ?, id_gate = ?, data_volo = ? WHERE codice_volo = ?";

        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, volo.getCompagniaAerea());
            ps.setString(2, volo.getAeroportoOrigine());
            ps.setString(3, volo.getAeroportoDestinazione());
            ps.setString(4, volo.getOrarioArrivo());
            ps.setInt(5, volo.getRitardo());
            ps.setString(6, volo.getStato().toString());
            ps.setString(7, volo.getGate());
            ps.setDate(8, java.sql.Date.valueOf(volo.getDataVolo()));
            ps.setInt(9, volo.getCodiceVolo());

            int righeModificate = ps.executeUpdate();
            logger.info("Numero di righe modificate: " + righeModificate);

        } catch (SQLException e) {
            logger.info("Errore durante la modifica del volo: " + e.getMessage());
        }
    }

    // Elimina un volo dal DB
    @Override
    public void eliminaVolo(int codiceVolo) throws SQLException {
        String sql = "DELETE FROM volo WHERE codice_volo = ?";

        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codiceVolo);

            int righeEliminate = ps.executeUpdate();
            logger.info("Numero di righe eliminate: " + righeEliminate);

        } catch (SQLException e) {
            logger.info("Errore durante l'eliminazione del volo: " + e.getMessage());
        }
    }

    // Recupera un singolo volo dal DB
    @Override
    public Volo getVolo(int codiceVolo) throws SQLException {
        String sql = "SELECT * "+
                "FROM volo WHERE codice_volo = ?";

        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codiceVolo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Volo(
                            rs.getInt("codice_volo"),
                            rs.getString("compagnia_aerea"),
                            rs.getString("aeroporto_origine"),
                            rs.getString("aeroporto_destinazione"),
                            rs.getString("orario_partenza_arrivo"),
                            rs.getInt("ritardo"),
                            Volo.statoVolo.valueOf(rs.getString("stato")),
                            rs.getString("id_gate"),
                            rs.getDate("data_volo") != null ? rs.getDate("data_volo").toLocalDate() : null
                    );
                }
            }
        } catch (SQLException e) {
            logger.info("Errore durante il recupero del volo: " + e.getMessage());

        }
        return null;  // Se non trovato
    }

    // Recupera tutti i voli dal DB
    @Override
    public ArrayList<Volo> getTuttiVoli() throws SQLException {
        String sql = "SELECT *" +
                "FROM volo";
        ArrayList<Volo> listaVoli = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Volo volo = new Volo(
                        rs.getInt("codice_volo"),
                        rs.getString("compagnia_aerea"),
                        rs.getString("aeroporto_origine"),
                        rs.getString("aeroporto_destinazione"),
                        rs.getString("orario_partenza_arrivo"),
                        rs.getInt("ritardo"),
                        Volo.statoVolo.valueOf(rs.getString("stato")),
                        rs.getString("id_gate"),
                        rs.getDate("data_volo") != null ? rs.getDate("data_volo").toLocalDate() : null
                );
                listaVoli.add(volo);
            }

        } catch (SQLException e) {
            logger.info("Errore durante il recupero dei voli: " + e.getMessage());
        }
        return listaVoli;
    }
}