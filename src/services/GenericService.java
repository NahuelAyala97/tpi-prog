// Archivo: src/service/GenericService.java (Interface)
package service;

import java.sql.SQLException;
import java.util.List;

public interface GenericService<T> {
    
    /** Inserta una nueva entidad, orquestando las transacciones si aplica. */
    void insertar(T entity) throws SQLException;
    
    /** Actualiza una entidad existente. */
    void actualizar(T entity) throws SQLException;
    
    /** Realiza la baja lógica por ID. */
    void eliminar(Long id) throws SQLException;
    
    /** Obtiene una entidad por su clave primaria. */
    T getById(Long id) throws SQLException; 
    
    /** Lista todas las entidades activas (eliminado=FALSE). */
    List<T> getAll() throws SQLException;
    
    // Podrías añadir también la firma para buscar por campo único si fuera un requisito
    // T buscarPorCampoUnico(String valor) throws SQLException;
}