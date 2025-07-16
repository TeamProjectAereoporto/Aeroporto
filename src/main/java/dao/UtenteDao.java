package dao;

import model.Utente;

import java.sql.SQLException;

public interface UtenteDao {
    public void aggiungiUtenteDB(Utente utente) throws SQLException;
    public Utente verificaCredenziali(String username, String password) throws SQLException;
    public Utente getUtenteByUsername(String username) throws SQLException;
    public Boolean verificaUtenteUnivoco(String username) throws SQLException;
}
