package dao;

import model.Passeggero;

import java.sql.SQLException;

public interface PasseggeroDao {
    public void aggiungiPasseggero(Passeggero passeggero) throws SQLException;
    public Passeggero getPasseggero(String Nome, String Cognome) throws SQLException;
}
