package dao;

import model.Utente;

import java.sql.SQLException;

public interface UtenteDao {
    public void aggiungiUtenteDB(Utente utente) throws SQLException;
}
