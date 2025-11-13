//package dao;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//
//public interface GenericDAO<T> {
//    
//    void insertar(T entidad, Connection conn) throws SQLException;
//    void actualizar(T entidad, Connection conn) throws SQLException;
//    void eliminar(int id, Connection conn) throws SQLException;
//    T getById(int id, Connection conn) throws SQLException;
//    List<T> getAll(Connection conn) throws SQLException;
//    
//    T buscarPorCampoUnicoInt(int valor, Connection conn) throws SQLException; 
//}

package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {
    void insertar(T entidad, Connection conn) throws SQLException;
    void actualizar(T entidad, Connection conn) throws SQLException;
    void eliminar(int id, Connection conn) throws SQLException;
    T getById(int id, Connection conn) throws SQLException;
    List<T> getAll(Connection conn) throws SQLException;
    T buscarPorCampoUnicoInt(int valor, Connection conn) throws SQLException;
}
