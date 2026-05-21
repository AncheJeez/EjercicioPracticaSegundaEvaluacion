package Modelo;

import Conectividad.ConectarseBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EstadisticaDAO {

    public static List<Estadistica> alumnosPorEmpresa() throws Exception {
        List<Estadistica> res = new ArrayList<>();
        String sql = "SELECT e.nombre AS empresa, COUNT(p.id_practica) AS num_alumnos FROM Empresa e LEFT JOIN Practica p ON p.empresa_id = e.id_empresa GROUP BY e.nombre";
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                res.add(new Estadistica(rs.getString("empresa"), rs.getInt("num_alumnos")));
            }
        }
        return res;
    }

    public static List<Estadistica> alumnosPorCurso() throws Exception {
        List<Estadistica> res = new ArrayList<>();
        String sql = "SELECT a.curso_matriculado AS curso, COUNT(*) AS num_alumnos FROM Alumno a LEFT JOIN Practica p ON p.alumno_id = a.id_alumno GROUP BY a.curso_matriculado";
        try (Connection con = ConectarseBD.conectarse(null);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                res.add(new Estadistica(rs.getString("curso"), rs.getInt("num_alumnos")));
            }
        }
        return res;
    }
}
