package implementazionePostgres;

import connessioneDB.ConnessioneDB;
import dao.VoloDao;
import model.Volo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VoloDB implements VoloDao {

    private Connection connection;

    public VoloDB() {
        try {
            connection = ConnessioneDB.getInstance().connection;
        } catch (SQLException e) {
            System.err.println("Errore nel connessioneDB: "+ e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void aggiungiVoloDB(Volo volo) throws SQLException {
        String sql = "INSERT INTO volo (codice_volo, compagnia_aerea, aeroporto_origine, aeroporto_destinazione, orario_partenza_arrivo, ritardo, stato, id_gate) VALUES (?,?,?,?,?,?,?,?)";

        try(Connection connection = ConnessioneDB.getInstance().connection) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, volo.getCodiceVolo());
            preparedStatement.setString(2, volo.getCompagniaAerea());
            preparedStatement.setString(3, volo.getAeroportoOrigine());
            preparedStatement.setString(4, volo.getAeroportoDestinazione());
            preparedStatement.setString(5,volo.getOrarioArrivo());
            preparedStatement.setInt(6, volo.getRitardo());
            preparedStatement.setString(7,volo.getStato().toString());
            preparedStatement.setString(8,volo.getGate());

            int rwsAggiunte = preparedStatement.executeUpdate();
            System.out.println("Numero di righe aggiunte: "+ rwsAggiunte);
        } catch (SQLException e) {
            System.err.println("Errore nel connessioneDB durante l'aggiunta del volo: "+ e.getMessage());
        }
    }

    @Override
    public void modificaVoloDB(Volo volo) throws SQLException {
        String sql = "UPDATE volo SET compagnia_aerea = ?, aeroporto_origine = ?, aeroporto_destinazione = ?, orario_partenza_arrivo = ?, ritardo = ?, stato = ?, id_gate = ? WHERE codice_volo = ?";
        try(Connection connection = ConnessioneDB.getInstance().connection) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, volo.getCompagniaAerea());
            preparedStatement.setString(2, volo.getAeroportoOrigine());
            preparedStatement.setString(3, volo.getAeroportoDestinazione());
            preparedStatement.setString(4, volo.getOrarioArrivo());
            preparedStatement.setInt(5, volo.getRitardo());
            preparedStatement.setString(6, volo.getStato().toString());
            preparedStatement.setString(7, volo.getGate());
            preparedStatement.setInt(8, volo.getCodiceVolo());

            int rwsAggiunte = preparedStatement.executeUpdate();
            System.out.println("Numero di righe modificate: " + rwsAggiunte);
        } catch (SQLException e) {
            System.err.println("Errore durante la modifica: "+ e.getMessage());
        }
    }

    @Override
    public void eliminaVolo(int codiceVolo) throws SQLException {
        String sql = "DELETE FROM volo WHERE codice_volo = ?";

        try(Connection connection = ConnessioneDB.getInstance().connection) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, codiceVolo);

            int rwsAggiunte = preparedStatement.executeUpdate();
            System.out.println("Numero di righe eliminate: " + rwsAggiunte);
        } catch (SQLException e) {
            System.err.println("Errore durante l'eliminazione: "+ e.getMessage());
        }
    }

    @Override
    public Volo getVolo(int codiceVolo) throws SQLException {
        String sql = "SELECT * FROM volo WHERE codice_volo = ?";

        try(Connection connection = ConnessioneDB.getInstance().connection) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, codiceVolo);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                int codice = resultSet.getInt("codice_volo");
                String compagniaAerea = resultSet.getString("compagnia_aerea");
                String aeroportoOrigine= resultSet.getString("aeroporto_origine");
                String aeroportoDestinazione = resultSet.getString("aeroporto_destinazione");
                String  orarioArrivo = resultSet.getString("orario_arrivo");
                int ritardo = resultSet.getInt("ritardo");
                String statoString = resultSet.getString("stato");
                Volo.statoVolo stato = Volo.statoVolo.valueOf(statoString);
                String gate = resultSet.getString("id_idgate");

                return new Volo(codice,compagniaAerea,aeroportoOrigine,aeroportoDestinazione,orarioArrivo,ritardo,stato,gate);
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'eliminazione: "+ e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Volo> getTuttiVoli() throws SQLException {
        String sql = "SELECT * FROM volo";
        ArrayList<Volo> listaVoli = new ArrayList<>();

        try (Connection connection = ConnessioneDB.getInstance().connection;
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int codice = rs.getInt("codice_volo");
                String compagnia = rs.getString("compagnia_aerea");
                String origine = rs.getString("aeroporto_origine");
                String destinazione = rs.getString("aeroporto_destinazione");
                String orario = rs.getString("orario_partenza_arrivo");
                int ritardo = rs.getInt("ritardo");
                String statoStr = rs.getString("stato");
                Volo.statoVolo stato = Volo.statoVolo.valueOf(statoStr);
                String gate = rs.getString("id_gate");

                Volo volo = new Volo(codice, compagnia, origine, destinazione, orario, ritardo, stato, gate);
                listaVoli.add(volo);
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei voli: "+ e.getMessage());
        }
        return listaVoli;
    }

}