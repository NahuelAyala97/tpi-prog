package services;

import config.DatabaseConnection;
import dao.HistoriaClinicaDAO;
import dao.PacienteDAO;
import entities.HistoriaClinica;
import entities.Paciente;
import entities.GrupoSanguineo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteService {

    private final PacienteDAO pacienteDAO;
    private final HistoriaClinicaDAO historiaDAO;

    public PacienteService() {
        this.historiaDAO = new HistoriaClinicaDAO();
        this.pacienteDAO = new PacienteDAO(historiaDAO);
    }

    // CREAR PACIENTE AUTOMÁTICAMENTE ASIGNANDO HISTORIA CLÍNICA
    public void crearPacienteAsignandoHistoria(Paciente p) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {

            try {
                conn.setAutoCommit(false);

                // 1) Obtener la primera historia clínica disponible
                HistoriaClinica h = obtenerHistoriaClinicaAutomatica(conn);

                if (h == null) {
                    throw new Exception("No hay historias clínicas disponibles para asignar.");
                }

                // 2) Asignar historia clínica al paciente
                p.setHistoriaClinica(h);

                // 3) Insertar paciente (sin crear historia clínica)
                pacienteDAO.insertar(p, conn);

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // OBTENER LA PRIMERA HISTORIA CLÍNICA DISPONIBLE (AUTO)=
    private HistoriaClinica obtenerHistoriaClinicaAutomatica(Connection conn) throws SQLException {

        String sql = "SELECT * FROM historiaclinica ORDER BY id LIMIT 1";

        try (var stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {

            if (rs.next()) {
                return new HistoriaClinica(
                        rs.getInt("id"),
                        rs.getInt("nroHistoria"),
                        GrupoSanguineo.fromValor(rs.getString("grupoSanguineo")),
                        rs.getString("antecedentes"),
                        rs.getString("medicacionActual"),
                        rs.getString("observaciones")
                );
            }
        }
        return null;
    }

    // CRUD BÁSICO

    public List<Paciente> obtenerTodos() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return pacienteDAO.getAll(conn);
        }
    }

    public Paciente buscarPorDni(String dniStr) throws Exception {
        int dni = Integer.parseInt(dniStr);
        try (Connection conn = DatabaseConnection.getConnection()) {
            return pacienteDAO.buscarPorCampoUnicoInt(dni, conn);
        }
    }

    public Paciente obtenerPorId(Long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return pacienteDAO.getById(id.intValue(), conn);
        }
    }

    public void actualizar(Paciente p) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            pacienteDAO.actualizar(p, conn);
        }
    }

    public void eliminarPaciente(Long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            pacienteDAO.eliminar(id.intValue(), conn);
        }
    }

    // MÉTODOS OPCIONALES (YA NO SE USAN EN ESTE FLUJO)
   
    public List<HistoriaClinica> obtenerPrimerasHistorias(int cantidad) throws SQLException {
        List<HistoriaClinica> lista = new ArrayList<>();

        String sql = "SELECT * FROM historiaclinica ORDER BY id LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cantidad);
            var rs = stmt.executeQuery();

            while (rs.next()) {
                HistoriaClinica hc = new HistoriaClinica(
                        rs.getInt("id"),
                        rs.getInt("nroHistoria"),
                        GrupoSanguineo.fromValor(rs.getString("grupoSanguineo")),
                        rs.getString("antecedentes"),
                        rs.getString("medicacionActual"),
                        rs.getString("observaciones")
                );
                lista.add(hc);
            }
        }

        return lista;
    }

    public HistoriaClinica obtenerHistoriaPorId(int idHistoria) throws SQLException {
        String sql = "SELECT * FROM historiaclinica WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idHistoria);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                return new HistoriaClinica(
                        rs.getInt("id"),
                        rs.getInt("nroHistoria"),
                        GrupoSanguineo.fromValor(rs.getString("grupoSanguineo")),
                        rs.getString("antecedentes"),
                        rs.getString("medicacionActual"),
                        rs.getString("observaciones")
                );
            }
        }
        return null;
    }
}
