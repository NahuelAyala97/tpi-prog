//package dao;
//
//import entities.GrupoSanguineo;
//import entities.HistoriaClinica;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class HistoriaClinicaDAO implements GenericDAO<HistoriaClinica> {
//
//
//    private static final String SQL_INSERT = "INSERT INTO historiaclinica (id, nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones) VALUES (?, ?, ?, ?, ?, ?)";
//    private static final String SQL_UPDATE = "UPDATE historiaclinica SET nroHistoria = ?, grupoSanguineo = ?, antecedentes = ?, medicacionActual = ?, observaciones = ? WHERE id = ?";
//    private static final String SQL_DELETE = "DELETE FROM historiaclinica WHERE id = ?"; // Eliminación física 
//    private static final String SQL_SELECT_ID = "SELECT * FROM historiaclinica WHERE id = ?";
//    private static final String SQL_SELECT_ALL = "SELECT * FROM historiaclinica";
//    private static final String SQL_SELECT_BY_NRO = "SELECT * FROM historiaclinica WHERE nroHistoria = ?";
//
//
//    @Override
//    public void insertar(HistoriaClinica historia, Connection conn) throws SQLException {
//
//        try (PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
//            stmt.setInt(1, historia.getId());
//            stmt.setInt(2, historia.getNroHistoria()); 
//            stmt.setString(3, historia.getGrupoSanguineo().getValor());
//            stmt.setString(4, historia.getAntecedentes());
//            stmt.setString(5, historia.getMedicacionActual());
//            stmt.setString(6, historia.getObservaciones());
//
//            if (stmt.executeUpdate() == 0) {
//                throw new SQLException("Error al insertar Historia Clinica. Ninguna fila afectada.");
//            }
//            
//            // Recuperar ID generado
//            try (ResultSet rs = stmt.getGeneratedKeys()) {
//                if (rs.next()) {
//                    historia.setId(rs.getInt(1)); 
//                }
//            }
//        }
//
//    }
//
//    @Override
//    public void actualizar(HistoriaClinica historia, Connection conn) throws SQLException {
//        try (PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {
//            stmt.setInt(1, historia.getNroHistoria());
//            stmt.setString(2, historia.getGrupoSanguineo().getValor());
//            stmt.setString(3, historia.getAntecedentes());
//            stmt.setString(4, historia.getMedicacionActual());
//            stmt.setString(5, historia.getObservaciones());
//            stmt.setInt(6, historia.getId());
//
//            if (stmt.executeUpdate() == 0) {
//                 throw new SQLException("Error al actualizar Historia Clinica. ID no encontrado.");
//            }
//        }
//    }
//
//    //Eliminación física
//    @Override
//    public void eliminar(int id, Connection conn) throws SQLException {
//        try (PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {
//            stmt.setInt(1, id);
//
//            if (stmt.executeUpdate() == 0) {
//                throw new SQLException("Error al ELIMINAR FÍSICAMENTE Historia Clinica. ID no encontrado.");
//            }
//        }
//    }
//
//    @Override
//    public HistoriaClinica getById(int id, Connection conn) throws SQLException {
//        HistoriaClinica hc = null;
//        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ID)) {
//            stmt.setInt(1, id);
//            try (ResultSet result = stmt.executeQuery()) {
//                if (result.next()) {
//                    hc = mapHistoriaResult(result);
//                }
//            }
//        } 
//        return hc;
//    }
//
//    @Override
//    public List<HistoriaClinica> getAll(Connection conn) throws SQLException {
//        List<HistoriaClinica> listaHistorias = new ArrayList<>();
//        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
//             ResultSet result = stmt.executeQuery()) {
//            while (result.next()) {
//                listaHistorias.add(mapHistoriaResult(result));
//            }
//        }
//        return listaHistorias;
//    }
//    
//    @Override
//    public HistoriaClinica buscarPorCampoUnicoInt(int nroHistoria, Connection conn) throws SQLException {
//        HistoriaClinica hc = null;
//        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_NRO)) {
//            stmt.setInt(1, nroHistoria);
//            try (ResultSet result = stmt.executeQuery()) {
//                if (result.next()) {
//                    hc = mapHistoriaResult(result);
//                }
//            }
//        }
//        return hc; 
//
//        // Debes asegurarte de que este constructor exista en la entidad HistoriaClinica
//        return new HistoriaClinica(idHistoria, nroHistoria, gs, antecedentes, medicacion, observaciones);
//    }
//}
package dao;

import entities.GrupoSanguineo;
import entities.HistoriaClinica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriaClinicaDAO implements GenericDAO<HistoriaClinica> {

    private static final String SQL_INSERT =
            "INSERT INTO historiaclinica (grupoSanguineo, antecedentes, medicacionActual, observaciones) " +
            "VALUES (?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE historiaclinica SET grupoSanguineo = ?, antecedentes = ?, " +
            "medicacionActual = ?, observaciones = ? WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM historiaclinica WHERE id = ?";

    private static final String SQL_SELECT_ID =
            "SELECT * FROM historiaclinica WHERE id = ?";

    private static final String SQL_SELECT_ALL =
            "SELECT * FROM historiaclinica";

    private static final String SQL_SELECT_BY_NRO =
            "SELECT * FROM historiaclinica WHERE nroHistoria = ?";

    @Override
    public void insertar(HistoriaClinica historia, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, historia.getGrupoSanguineo().getValor());
            stmt.setString(2, historia.getAntecedentes());
            stmt.setString(3, historia.getMedicacionActual());
            stmt.setString(4, historia.getObservaciones());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    historia.setId(rs.getInt(1));
                }
            }

            // Ahora obtenemos el nroHistoria autogenerado
            historia.setNroHistoria(obtenerNroHistoriaGenerado(conn, historia.getId()));
        }
    }

    private int obtenerNroHistoriaGenerado(Connection conn, int idHistoria) throws SQLException {
        String sql = "SELECT nroHistoria FROM historiaclinica WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idHistoria);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("nroHistoria");
                }
            }
        }
        throw new SQLException("No se encontró nroHistoria para el ID: " + idHistoria);
    }

    @Override
    public void actualizar(HistoriaClinica historia, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            stmt.setString(1, historia.getGrupoSanguineo().getValor());
            stmt.setString(2, historia.getAntecedentes());
            stmt.setString(3, historia.getMedicacionActual());
            stmt.setString(4, historia.getObservaciones());
            stmt.setInt(5, historia.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public HistoriaClinica getById(int id, Connection conn) throws SQLException {
        HistoriaClinica hc = null;
        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    hc = mapHistoriaResult(rs);
                }
            }
        }
        return hc;
    }

    @Override
    public List<HistoriaClinica> getAll(Connection conn) throws SQLException {
        List<HistoriaClinica> lista = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapHistoriaResult(rs));
            }
        }
        return lista;
    }

    @Override
    public HistoriaClinica buscarPorCampoUnicoInt(int nroHistoria, Connection conn) throws SQLException {
        HistoriaClinica hc = null;
        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_NRO)) {
            stmt.setInt(1, nroHistoria);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    hc = mapHistoriaResult(rs);
                }
            }
        }
        return hc;
    }

    private HistoriaClinica mapHistoriaResult(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int nro = rs.getInt("nroHistoria");
        GrupoSanguineo gs = GrupoSanguineo.fromValor(rs.getString("grupoSanguineo"));
        String antecedentes = rs.getString("antecedentes");
        String medicacion = rs.getString("medicacionActual");
        String observaciones = rs.getString("observaciones");

        return new HistoriaClinica(id, nro, gs, antecedentes, medicacion, observaciones);
    }
}
