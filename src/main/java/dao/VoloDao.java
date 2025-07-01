package dao;

import model.Volo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface VoloDao {
    public void aggiungiVoloDB(Volo volo) throws SQLException;
    public void modificaVoloDB(Volo volo) throws SQLException;
    public void eliminaVolo(int codiceVolo) throws SQLException;
    public Volo getVolo(int codiceVolo) throws SQLException;
    public List<Volo> getTuttiVoli() throws SQLException;
    public ArrayList<Volo> getVoliByQuery(String sql) throws SQLException;
    public ArrayList<Volo> getVoliInArrivo() throws SQLException;
    public ArrayList<Volo> getVoliInPartenza() throws SQLException;
}
