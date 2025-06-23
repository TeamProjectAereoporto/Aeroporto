package dao;

import model.Volo;

import java.sql.SQLException;
import java.util.ArrayList;

public interface VoloDao {
    public void aggiungiVoloDB(Volo volo) throws SQLException;
    public void modificaVoloDB(Volo volo) throws SQLException;
    public void eliminaVolo(int codiceVolo) throws SQLException;
    public void getVolo(int codiceVolo) throws SQLException;
    public void getTuttiVoli(ArrayList<Volo> volo) throws SQLException;
}
