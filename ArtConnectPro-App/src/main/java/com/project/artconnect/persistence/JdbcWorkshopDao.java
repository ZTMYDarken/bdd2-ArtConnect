package com.project.artconnect.persistence;

import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.util.ConnectionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcWorkshopDao implements WorkshopDao {

    @Override
    public List<Workshop> findAll() {
        List<Workshop> workshops = new ArrayList<>();
        String sql = """
        SELECT ws.*, ar.name as instructor_name 
        FROM Workshop ws 
        LEFT JOIN Artist ar ON ws.id_artist = ar.id_artist 
        ORDER BY ws.dateWorkshop
        """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                workshops.add(mapResultSetToWorkshop(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workshops;
    }

    @Override
    public Optional<Workshop> findById(Long id) {
        String sql = "SELECT * FROM Workshop WHERE id_workshop = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToWorkshop(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Workshop mapResultSetToWorkshop(ResultSet rs) throws SQLException {
        Workshop w = new Workshop();
        w.setTitle(rs.getString("title"));
        w.setDescription(rs.getString("description"));
        w.setPrice(rs.getDouble("price"));
        w.setMaxParticipants(rs.getInt("maxParticipants"));
        w.setLocation(rs.getString("location"));
        w.setLevel(rs.getString("level"));
        w.setDurationMinutes(rs.getInt("durationMinutes"));

        // Conversion de la date SQL vers LocalDateTime
        Timestamp ts = rs.getTimestamp("dateWorkshop");
        if (ts != null) {
            w.setDate(ts.toLocalDateTime());
        }

        String instructorName = rs.getString("instructor_name");
        if (instructorName != null) {
            Artist instructor = new Artist();
            instructor.setName(instructorName);
            w.setInstructor(instructor);
        }

        return w;
    }
}