package service;

import config.DatabaseConnection;
import dao.HistoriaClinicaDAO;
import entities.HistoriaClinica;

import java.sql.Connection;
import java.util.List;

public class HistoriaClinicaService {

    private final HistoriaClinicaDAO historiaDAO;

    public HistoriaClinicaService() {
        this.historiaDAO = new HistoriaClinicaDAO();
    }

    public HistoriaClinica crear(HistoriaClinica h) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            historiaDAO.insertar(h, conn);
            return h;
        }
    }

    public HistoriaClinica obtenerPorId(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return historiaDAO.getById(id, conn);
        }
    }

    public List<HistoriaClinica> obtenerTodas() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return historiaDAO.getAll(conn);
        }
    }

    public HistoriaClinica buscarPorNroHistoria(int nro) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return historiaDAO.buscarPorCampoUnicoInt(nro, conn);
        }
    }
}
